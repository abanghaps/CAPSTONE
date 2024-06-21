package com.dicoding.capstone.saiko.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.data.remote.Story

class StoriesAdapter(private val context: Context, private val stories: List<Story>) : BaseAdapter() {

    override fun getCount(): Int = stories.size

    override fun getItem(position: Int): Any = stories[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)

        val story = stories[position]

        val imageView: ImageView = view.findViewById(R.id.image_fruit)
        val nameTextView: TextView = view.findViewById(R.id.text_fruit_name)
        val descriptionTextView: TextView = view.findViewById(R.id.text_fruit_price)

        val baseUrl = "https://capstone-project13.et.r.appspot.com/"
        val imageUrl = baseUrl + story.photoUrl
        Log.d("StoriesAdapter", "Loading image: $imageUrl")

        Glide.with(imageView.context)
            .load(imageUrl)
            .centerCrop()
            .into(imageView)

        nameTextView.text = story.description
        descriptionTextView.visibility = View.GONE

        return view
    }
}
