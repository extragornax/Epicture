/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.epitech.extra.epicture

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.content.DialogInterface
import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File


class UploadImage :  AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageValue : Intent? = null
    private var imageBitmap : Bitmap? = null
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
            var url = "https://api.imgur.com/3/upload"
            var authString : String? = null
            if (Imgur.loggedIn) {
                ToastPrinter().print("Uploading to ${Imgur.username}'s account!", this)
                authString = "Bearer ${Imgur.accessToken}"
            } else {
                ToastPrinter().print("Uploading anonymously!", this)
                authString = "Client-ID 5c7c13bd1c6d930"
            }
            val stream = ByteArrayOutputStream()
            imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val fd = File(picturePath)
            val string = fd.readBytes()
            val b64 = Base64.encodeToString(string, 0)
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "file")
                .addFormDataPart("image", b64)
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
        checkPermissionREAD_EXTERNAL_STORAGE(this)
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
        return if (!state.equals(Environment.MEDIA_MOUNTED, ignoreCase = true
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
                imageBitmap = bitmap
                // Log.d(TAG, String.valueOf(bitmap));

                val uri = data.data
                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                    //var takeFlags = data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    //contentResolver.takePersistableUriPermission(uri, takeFlags)

                    var id = uri.getLastPathSegment().split(":")[1]
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    var imageOrderBy = null;

                    var uriNew = getUri();
                    var selectedImagePath = "path"

                    var imageCursor =
                        managedQuery(uriNew, projection, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy)

                    if (imageCursor.moveToFirst()) {
                        picturePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    }
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

    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    fun checkPermissionREAD_EXTERNAL_STORAGE(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    ActivityCompat
                        .requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }
                return false
            } else {
                return true
            }

        } else {
            return true
        }
    }

    fun showDialog(
        msg: String, context: Context,
        permission: String
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            })
        val alert = alertBuilder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do your stuff
            } else {
                Toast.makeText(
                    this@UploadImage, "GET_ACCOUNTS Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }
}