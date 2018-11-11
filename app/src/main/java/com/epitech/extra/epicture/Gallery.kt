package com.epitech.extra.epicture
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.epitech.extra.epicture.R.layout.gallery
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_main.hello_world_id
import kotlinx.android.synthetic.main.content_main.*
import java.io.InputStream

class Gallery : PageTemp("Gallery") {


    internal class DownloadImageTask(var pic: ImageView) : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            pic.setImageBitmap(result)
        }
    }

    class ProgrammingAdapter(private val data: Array<String>) :
        RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.gallery, parent, false)
            return ProgrammingViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProgrammingViewHolder, position: Int) {
            val title = data[position]
            holder.txttitle.text = title
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ProgrammingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            internal var imgIcon: ImageView
            internal var txttitle: TextView

            init {
                imgIcon = itemView.findViewById(R.id.picture) as ImageView
                Picasso.get().load("https://i.imgur.com/H981AN7.jpg").into(imgIcon)
                txttitle = itemView.findViewById(R.id.txttitle) as TextView
            }
        }

    }

}