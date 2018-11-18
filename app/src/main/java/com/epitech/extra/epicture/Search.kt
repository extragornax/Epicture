/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.epitech.extra.epicture

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_search.*

class Search : AppCompatActivity() {

        fun display_pic() {
            var search_button = findViewById<Button>(R.id.search)
            search_button.setOnClickListener {
                var search_entry = findViewById<TextInputEditText>(R.id.search_input)
                val st = search_entry.text
                var search = st.toString()
                if (search.isNullOrBlank()) {
                    ToastPrinter().print("Enter a key word :)", this)
                } else {
                    search_button.visibility = View.INVISIBLE
                    search_entry.visibility = View.INVISIBLE
                    findViewById<TextInputLayout>(R.id.search_input_layout).visibility = View.INVISIBLE
                    val a = findViewById<RecyclerView>(R.id.search_view)
                    a.visibility = View.VISIBLE
                    a.setLayoutManager(LinearLayoutManager(this))
                    val img_list =  Imgur.searchImage(search)
                    val arrayOfItems = img_list.toTypedArray()
                    val gallery = Gallery.ProgrammingAdapter(arrayOfItems)
                    a.setAdapter(gallery)
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
            display_pic()
        }

    }