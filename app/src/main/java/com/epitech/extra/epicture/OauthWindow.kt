package com.epitech.extra.epicture

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient


class OauthWindow : AppCompatActivity() {

   private fun getUserCreationDate(){
        var dateString = Imgur.getUserInfo()!!.getString("created")
        Imgur.creationDate = "Since " + Imgur.convertEpochToDate(dateString)
   }

    private fun splitUrl(url: String) {
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
        ToastPrinter().print("You are now connected ${Imgur.username}!", this)
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
                if (url.contains("http://epicture.extragornax.fr"))
                    splitUrl(url)
                else
                    view.loadUrl(url)
                return true
            }
        }
    }
}