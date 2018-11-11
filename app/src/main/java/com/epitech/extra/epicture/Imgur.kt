package com.epitech.extra.epicture

import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.google.gson.GsonBuilder

class Imgur {
    data class Item(val id: String, val title: String, val favorite : Boolean, val link: String)
    val gson = GsonBuilder().setPrettyPrinting().create()

    companion object {
        var loggedIn: Boolean = false
        var username: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null
        var creationDate: String? = null
    }

    fun getImagesUser(): List<Item> {
        var itemsList: List<Item> = try {
            val client = OkHttpClient()
            val url = "https://api.imgur.com/3/account/me/images"

            val request = Request.Builder().url(url).get()
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            val response = client.newCall(request).execute()
            val json = response.body()?.string()
            val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
            val data = jsonObj.getJSONArray("data")
            gson.fromJson(data.toString(), object : TypeToken<List<Item>>() {}.type)
        } catch (e: Exception) {
            listOf()
        }
        return itemsList
    }
}