package com.epitech.extra.epicture
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class Gallery : AppCompatActivity() {


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

    class ProgrammingAdapter(private val item_list: Array<Imgur.Companion.Item>) :
        RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.gallery, parent, false)
            return ProgrammingViewHolder(view, item_list)
        }

        override fun onBindViewHolder(holder: ProgrammingViewHolder, position: Int) {
            Picasso.get().load(item_list[position].link).into(holder.imgIcon)
                holder.txttitle.text = item_list[position].title;
        }

        override fun getItemCount(): Int {
            return item_list.size

        }

        inner class ProgrammingViewHolder(itemView: View, val item_list: Array<Imgur.Companion.Item>) : RecyclerView.ViewHolder(itemView) {

            internal var imgIcon: ImageView
            internal var txttitle: TextView

            init {
                imgIcon = itemView.findViewById(R.id.picture) as ImageView
                txttitle = itemView.findViewById(R.id.txttitle) as TextView
            }
        }

    }

}