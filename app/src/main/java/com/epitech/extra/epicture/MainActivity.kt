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
import android.os.StrictMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.TextureView
import android.view.View
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT > 9)
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->Snackbar.make(view, "Surprise!", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        if (!Imgur.loggedIn) {
            val oauth = Intent(this, OauthWindow::class.java)
            startActivity(oauth)
        } else
            userNameInTab.text = Imgur.username
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val menuNav = navigationView.menu
        menuNav.findItem(R.id.nav_relog).title = "Disconnect"
    }

    private fun getUserProfilePicture(){
        if (Imgur.profileImage == null) {
            var data = Imgur.getUserInfo()
            var urlString = data!!.getString("avatar")
            Imgur.profileImage = urlString
        }
        Picasso.get().load(Imgur.profileImage).into(userPageIcon)
    }

    override fun onResume() {
        if (Imgur.loggedIn) {
            userNameInTab.text = Imgur.username
            creationDataInTab.text = Imgur.creationDate
            getUserProfilePicture()
        }
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_import -> {
                //val importIntent = Intent(this, Import::class.java)
                //startActivity(importIntent)
                val img_list = Imgur.getImagesUser()
                val u = findViewById<TextView>(R.id.hello_world_id)
//                u.text = img_list.size.toString()
        }

            R.id.nav_gallery -> {
                /*
               val newtext = findViewById<TextView>(R.id.hello_world_id) //gallery.retClassName()
            newtext.setText("hello darkness my old friend")
                setContentView(R.layout.gallery)
                val pic = findViewById<ImageView>(R.id.picture)
                Picasso.get().load("https://i.imgur.com/H981AN7.jpg").into(picture)*/
                setContentView(R.layout.gallery_back)
                val langagtes = arrayOf("Don't", "Blame", "Me")
                val a = findViewById<RecyclerView>(R.id.recycler_view)
                a.setLayoutManager(LinearLayoutManager(this))
                val img_list = Imgur.getImagesUser()
                //val img_array = arrayOfNulls<Item>(img_list.size)
                val arrayOfItems = img_list.toTypedArray()
                val gallery = Gallery.ProgrammingAdapter(langagtes, arrayOfItems)
                a.setAdapter(gallery)
            }
            R.id.nav_slideshow -> {

            }

            R.id.nav_relog -> {
                if (!Imgur.loggedIn) {
                    val oauth = Intent(this, OauthWindow::class.java)
                    startActivity(oauth)
                    userNameInTab.text = Imgur.username
                    creationDataInTab.text = "Feature incoming"

                    val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                    val menuNav = navigationView.menu
                    val relogVal = menuNav.findItem(R.id.nav_relog)
                    relogVal.title = "Disconnect"
                } else {
                    userNameInTab.text = "Disconnected"
                    creationDataInTab.text = "Disconnected"
                    Imgur.username = null
                    Imgur.accessToken = null
                    Imgur.creationDate = null
                    Imgur.refreshToken = null
                    Imgur.loggedIn = false
                    Imgur.profileImage = null
                    val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                    val menuNav = navigationView.menu
                    val relogVal = menuNav.findItem(R.id.nav_relog)
                    relogVal.title = "Connect"
                }
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
        // val tmpUrl = "https://api.imgur.com/3/account/" + Imgur.username

        //val urldata: String = URL("http://date.jsontest.com/").readText()

        var url : String? = null
        url = if (Imgur.username == null) {
            "https://api.imgur.com/3/account/Extragornax"
        } else {
            "https://api.imgur.com/3/account/" + Imgur.username
        }
        val urldata: String = URL(url).readText()
        println("I RECEIVED THIS [${urldata}]")

        var createDate: String? = null

        try {
            var jsonobj = parseJson(urldata)
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


