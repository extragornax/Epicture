package com.epitech.extra.epicture

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import android.widget.Toast





class OauthWindow : AppCompatActivity() {

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

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("authorization", "Bearer ${Imgur.accessToken}")
            .addHeader("cache-control", "no-cache")
            .build()
        val response = client.newCall(request).execute()

        var json = response.body()?.string()
        println("GOT JSON $json")

        val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
        val data = jsonObj.getJSONObject("data")
        Imgur.creationDate = data.getString("created")
        println("CREATION DATE IS ${Imgur.creationDate}")
    }

    private fun splitUrl(url: String, view: WebView) {
        val outerSplit =
            url.split("\\#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("\\&".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((index, s) in outerSplit.withIndex()) {
            val innerSplit = s.split("\\=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (index) {
                0 -> Imgur.accessToken = innerSplit[1]
                3 -> Imgur.refreshToken = innerSplit[1]
                4 -> Imgur.username = innerSplit[1]
            }
        }
        if (Imgur.accessToken != null && Imgur.username != null)
            Imgur.loggedIn = true
        getUserCreationDate()
        finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val imgurWebView = findViewById<WebView>(R.id.loginwebview)
        imgurWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=5c7c13bd1c6d930&response_type=token&state=login")
        imgurWebView.settings.javaScriptEnabled = true

        imgurWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //val url = request.url.toString()
                println(url)

                if (url.contains("http://epicture.extragornax.fr")) {
                    splitUrl(url, view)
                } else {
                    view.loadUrl(url)
                }
                return true
            }
        }
    }
}