package com.epitech.extra.epicture

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

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
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val menuNav = navigationView.menu
        if (Imgur.loggedIn) {
            userNameInTab.text = Imgur.username
            menuNav.findItem(R.id.nav_relog).title = "Disconnect"
            getUserProfilePicture()
            Toast.makeText(this, "Welcome back ${Imgur.username}", Toast.LENGTH_SHORT).show()
        } else {
            menuNav.findItem(R.id.nav_relog).title = "Connect"
            Toast.makeText(this, "Welcome! Please connect your account", Toast.LENGTH_SHORT).show()
        }

        setGHotPage()
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

    fun setGHotPage() {
        val a = findViewById<RecyclerView>(R.id.recycler_test)
        a.layoutManager = LinearLayoutManager(this)
        val img_list = Imgur.getHotViralImages()
        val arrayOfItems = img_list.toTypedArray()
        val gallery = Gallery.ProgrammingAdapter(arrayOfItems)
        a.adapter = gallery
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_upload -> {
                ToastPrinter().print("Upload soon to be done!", this)
            }

            R.id.nav_hotpage -> {
                setGHotPage()
                ToastPrinter().print("Hot section", this)
            }

            R.id.nav_gallery -> {
//                setContentView(R.layout.gallery_back)
                val a = findViewById<RecyclerView>(R.id.recycler_test)
                a.setLayoutManager(LinearLayoutManager(this))
                val gallery = Gallery.ProgrammingAdapter(Imgur.getImagesUser().toTypedArray())
                a.setAdapter(gallery)
                ToastPrinter().print("User favorites", this)
            }

            R.id.nav_search -> {
                ToastPrinter().print("Search soon to be done!", this)
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
                    ToastPrinter().print("You are now disconnected!", this)
                }
            }

            /*
            R.id.nav_share -> {
                ToastPrinter().print("Thank you for sharing our app!")
            }
            */

            R.id.nav_extragornax -> {
                ToastPrinter().print("https://github.com/Extragornax", this)
            }
            R.id.nav_cheap -> {
                ToastPrinter().print("https://github.com/DotCheap", this)
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}


