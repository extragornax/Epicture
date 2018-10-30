package com.epitech.extra.epicture

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
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

    fun login() {
        val url: String = "https://api.imgur.com/oauth2/authorize"
        val params = "&client_id=5c7c13bd1c6d930&response_type=token&state=toto"
        val tmpUrl: String
        tmpUrl = url + params
        println("URL is [$url] with params [$params] -> [$tmpUrl]")
        webViewOauth.loadUrl(tmpUrl)

    }


    fun getHeaderGet(url: String): BufferedReader {
        val imgURL = URL(url)
        val conn = imgURL.openConnection() as HttpURLConnection
        conn.setRequestMethod("GET");
        if (accessToken != null) {
            conn.setRequestProperty("Authorization", "Bearer $accessToken");
        } else {
            conn.setRequestProperty("Authorization", "Client-ID ${R.string.com_oauth_client_id}");
        }
        var bin = BufferedReader(InputStreamReader(conn.inputStream))

        return bin

    }
}