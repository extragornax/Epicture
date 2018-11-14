package com.epitech.extra.epicture

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class ApiCaller {
    fun makeACall(url: String) : Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("authorization", "Bearer ${Imgur.accessToken}")
            .addHeader("cache-control", "no-cache")
            .build()
        return client.newCall(request).execute()
    }

    fun makeACallPost(url: String) : Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(FormBody.Builder().build())
            .addHeader("authorization", "Bearer ${Imgur.accessToken}")
            .addHeader("cache-control", "no-cache")
            .build()
        FormBody.Builder().build()
        return client.newCall(request).execute()
    }
}