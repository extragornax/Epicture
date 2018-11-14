package com.epitech.extra.epicture

import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*

class Imgur : AppCompatActivity() {
    companion object {

        data class Item(val id : String,
                        val title : String,
                        val description : String,
                        val favorite : Boolean,
                        val link : String,
                        val score : String,
                        val views : String,
                        val type : String,
                        val datetime : String)

        val gson = GsonBuilder().setPrettyPrinting().create()
        var gsonObjectProfile : JSONObject? = null
        var loggedIn: Boolean = false
        var username: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null
        var creationDate: String? = null
        var profileImage: String? = null

        fun convertEpochToDate(dateString : String) : String {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(Long.parseLong(dateString) * 1000)
            return sdf.format(netDate).toString()
        }

        fun getImagesUser(): List<Item> {
            return try {
                val url = "https://api.imgur.com/3/account/me/submissions"
                val request = Request.Builder().url(url).get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
                val response = OkHttpClient().newCall(request).execute()
                val json = response.body()?.string()
                val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
                val data = jsonObj.getJSONArray("data")
                gson.fromJson(data.toString(), object : TypeToken<List<Item>>() {}.type)
            } catch (e: Exception) {
                listOf()
            }
        }

        fun getHotViralImages() : List<Item> {
            return try {
                val url = "https://api.imgur.com/3/gallery/hot/viral/0.json"
                val request = Request.Builder().url(url).get()
                    .addHeader("cache-control", "no-cache")
                    //.addHeader("Authorization", "Bearer $accessToken")
                    .build()
                val response = OkHttpClient().newCall(request).execute()
                val json = response.body()?.string()
                val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
                val data = jsonObj.getJSONArray("data")
                gson.fromJson(data.toString(), object : TypeToken<List<Item>>() {}.type)
            } catch (e: Exception) {
                listOf()
            }
        }

        fun getUserInfo() : JSONObject? {

            if (this.gsonObjectProfile != null)
                return this.gsonObjectProfile
            var url = if (Imgur.username == null)
                "https://api.imgur.com/3/account/Extragornax"
            else
                "https://api.imgur.com/3/account/" + Imgur.username
            var apiCaller = ApiCaller()
            var response = apiCaller.makeACall(url)
            var json = response.body()?.string()
            val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
            this.gsonObjectProfile = jsonObj.getJSONObject("data")
            return this.gsonObjectProfile
        }

        fun changeFavValue(id : String) : String {
            var url = "https://api.imgur.com/3/image/$id/favorite"
            var urlString : String?

            try {
                var response = ApiCaller().makeACallPost(url)
                var json = response.body()?.string()
                val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
                urlString = jsonObj!!.getString("data")
            } catch (e : Exception) {
                return "ERROR"
            }
            return urlString
        }
    }
}