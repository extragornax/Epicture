package com.epitech.extra.epicture

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class ToastPrinter : AppCompatActivity() {

    fun print(message : String, where : Context) {
        Toast.makeText(where, message, Toast.LENGTH_SHORT).show()
    }
}