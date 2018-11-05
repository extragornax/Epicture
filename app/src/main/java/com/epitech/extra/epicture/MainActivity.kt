package com.epitech.extra.epicture

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import android.os.StrictMode
import java.io.FileNotFoundException
import java.lang.Exception
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (!Imgur.loggedIn) {
            val oauth = Intent(this, OauthWindow::class.java)
            startActivity(oauth)
        } else {
            userNameInTab.text = Imgur.username
        }
        if (Imgur.creationDate == null) {
            getUserCreationDate()
        }
        //creationDataInTab.text = Imgur.username
        //creationDataInTab.text = Imgur.creationDate
    }

    override fun onResume() {
        if (Imgur.loggedIn) {
            userNameInTab.text = Imgur.username
        }
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_import -> {
                //val importIntent = Intent(this, Import::class.java)
                //startActivity(importIntent)
            }

            R.id.nav_gallery -> {
               // val gallery = Gallery()
                // hello_world_id.text = gallery.retClassName()

            }

            R.id.nav_slideshow -> {

            }

            R.id.nav_toto -> {

            }

            R.id.nav_manage -> {

            }

            R.id.nav_share -> {

            }

            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun parseJson(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }

    private fun getUserCreationDate(){
        var url : String? = null
        url = if (Imgur.username == null) {
            "https://api.imgur.com/3/account/Extragornax"
        } else {
            "https://api.imgur.com/3/account/" + Imgur.username
        }

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        println("============================================")
        if (Imgur.accessToken != null) {
            connection.setRequestProperty("Authorization", "Bearer $Imgur.accessToken")
            println("Authorization Bearer ${Imgur.accessToken}")
        } else {
            connection.setRequestProperty("Authorization", "Client-ID ${R.string.com_oauth_client_id}")
            println("Authorization Client-ID ${R.string.com_oauth_client_id}")
        }

        var apiData: String? = null
        try {
            apiData = connection.inputStream.bufferedReader().readText()
            println("I RECEIVED THIS [${apiData}]")
        } catch (e: FileNotFoundException) {
            println("CRASH FileNotFoundException ${e.stackTrace} -> ${e.message} -> ${e.cause}")
        }


        var createDate: String? = null

        try {
            var jsonobj = parseJson(apiData.toString())
            createDate = jsonobj!!.getString("created")
        } catch (e: Exception) {
            createDate = "0"
        }

        println("CREATED == $createDate")

        Imgur.creationDate = createDate

        /*
        val connection = URL("https://api.imgur.com/3/account/" + Imgur.username).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        if (Imgur.accessToken != null)
            connection.setRequestProperty("Authorization", "Bearer $Imgur.accessToken");
        else
            connection.setRequestProperty("Authorization", "Client-ID ${R.string.com_oauth_client_id}")
        val toto: String = connection.inputStream.bufferedReader().readText()
        */
    }
}


