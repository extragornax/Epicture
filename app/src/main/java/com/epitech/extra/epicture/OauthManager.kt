package com.epitech.extra.epicture

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.Console
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class OauthManager : AppCompatActivity() {

    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var accountUsername: String? = null

    fun getHeaderGet(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        if (accessToken != null)
            connection.setRequestProperty("Authorization", "Bearer $accessToken");
        else {
            login()
            //connection.setRequestProperty("Authorization", "Client-ID ${R.string.com_oauth_client_id}");
        }

        try {
            val toto: String = connection.inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            println("CRASH REPORT ${e.localizedMessage} | ${e.cause}")
        }
        return "toto"

    }

    fun login() {
        println("Accessing login OauthWindow")
        val log = Intent(this, OauthWindow::class.java)
        startActivity(log)
        println("Window should be opened now")
    }

    fun getUserFavorites(user: String, page: Int = 0, sort: String = "newest")
    {
        val url : String = "https://api.imgur.com/3/account/$user/gallery_favorites/$page/$sort"
        val favs: String = getHeaderGet(url)
        //val text:List<String> = favs.readLines()
        //var all: String = ""
        //for(line in text){
          //  all += line
           // println(all)
        //}
    }
}