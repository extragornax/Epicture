package com.epitech.extra.epicture

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.Console
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class OauthManager : AppCompatActivity() {

    private val accessToken = null
    private val refreshToken = null
    private val expiresIn = 0
    private val tokenType = null
    private val accountUsername = null

    fun getHeaderGet(url: String): String {
        println("======================================================================")
        println("In getHeaderGet")
        println("======================================================================")

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestMethod("GET")

        if (accessToken != null)
            connection.setRequestProperty("Authorization", "Bearer $accessToken");
        else {
        //    login()
            println("======================================================================")
            println("setRequestProperty OK")
            println("======================================================================")
            connection.setRequestProperty("Authorization", "Client-ID ${R.string.com_oauth_client_id}");
        }
        println("======================================================================")
        println("going to return")
        println("======================================================================")
        println("======================================================================")
        println("getting data")
        println("======================================================================")

        try {
            val toto: String = connection.inputStream.bufferedReader().readText()
            println("======================================================================")
            println("GOT $toto")
            println("======================================================================")
        } catch (e: Exception) {
            println("CRASH REPORT ${e.localizedMessage} | ${e.cause}")
        }

        return "toto"

    }

    fun login() {
        val url: String = "https://api.imgur.com/oauth2/authorize"
        val params = "&client_id=5c7c13bd1c6d930&response_type=token&state=toto"
        val tmpUrl: String
        tmpUrl = url + params
        println("URL is [$url] with params [$params] -> [$tmpUrl]")
        webViewOauth.loadUrl(tmpUrl)

    }

    fun getUserFavorites(user: String, page: Int = 0, sort: String = "newest")
    {
        println("======================================================================")
        println("IN getUserFav")
        println("======================================================================")
        val url : String = "https://api.imgur.com/3/account/$user/gallery_favorites/$page/$sort"
        println("======================================================================")
        println("string ready, sending to getHeader")
        println("======================================================================")
        val favs: String = getHeaderGet(url)

        println("======================================================================")
        println("After getHeader")
        println("======================================================================")
        //val text:List<String> = favs.readLines()
        //var all: String = ""
        //for(line in text){
          //  all += line
           // println(all)
        //}
        println("======================================================================")
        println("got list : [$favs]")
        println("======================================================================")
    }
}