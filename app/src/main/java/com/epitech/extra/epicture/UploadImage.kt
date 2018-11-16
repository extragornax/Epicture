/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.epitech.extra.epicture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class UploadImage :  AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageValue : Intent? = null
    private var isImageSelected : Boolean = false
    private var picturePath : String? = null

    fun makeACallPost(url: String, requestBody : RequestBody, authString : String) : Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("authorization", authString)
            .addHeader("cache-control", "no-cache")
            .build()
        FormBody.Builder().build()
        return client.newCall(request).execute()
    }

    fun uploadImage() {
        if (isImageSelected){
            var url = "https://api.imgur.com/3/image"
            var authString : String? = null
            if (Imgur.loggedIn) {
                ToastPrinter().print("Uploading to ${Imgur.username}'s account!", this)
                authString = "Bearer ${Imgur.accessToken}"
            } else {
                ToastPrinter().print("Uploading anonymously!", this)
                authString = "Client-ID ${R.string.com_oauth_client_id}"
            }
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //.addFormDataPart("type", "file")
                .addFormDataPart("image", picturePath)
                .addFormDataPart("name", "TestName")
                .addFormDataPart("title", "TestTitle")
                .addFormDataPart("description", "TestDescription")
                .build()
            var response = makeACallPost(url, requestBody, authString)
            var json = response.body()?.string()
            val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
            var urlString = jsonObj!!.getString("data")
            println("OUTPUT UPLOAD $jsonObj -> $urlString")

        } else {
            ToastPrinter().print("Please choose an image to upload before", this)
            findViewById<Button>(R.id.imageSelect).visibility = View.INVISIBLE
        }

    }

    fun getImage() {
        val intent = Intent()
        // Show only images, no videos or anything else
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        findViewById<Button>(R.id.uploadButton).visibility = View.INVISIBLE
        findViewById<Button>(R.id.imageSelect).setOnClickListener { getImage() }
        findViewById<Button>(R.id.uploadButton).setOnClickListener { uploadImage() }
    }

    private fun getUri(): Uri {
        val state = Environment.getExternalStorageState()
        return if (!state.equals(
                Environment.MEDIA_MOUNTED,
                ignoreCase = true
            )
        ) MediaStore.Images.Media.INTERNAL_CONTENT_URI else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageValue = data
            isImageSelected = true
            val uri = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                // Log.d(TAG, String.valueOf(bitmap));

                val uri = data.data

                var takeFlags = data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                var id = uri.getLastPathSegment().split(":")[1]
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                var imageOrderBy = null;

                var uriNew = getUri();
                var selectedImagePath = "path"

                var imageCursor = managedQuery(uriNew, projection, MediaStore.Images.Media._ID + "="+id, null, imageOrderBy)

                if (imageCursor.moveToFirst()) {
                    picturePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                }

                println("FILE PATH IS $picturePath")
                //Log.d("tag", "image path : $picturePath")

                val imageView = findViewById<ImageView>(R.id.imageViewUpload)
                imageView.setImageBitmap(bitmap)
                findViewById<Button>(R.id.uploadButton).visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            imageValue = null
            isImageSelected = false
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}