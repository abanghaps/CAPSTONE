package com.dicoding.capstone.saiko.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.dicoding.capstone.saiko.R

class RecommendationAdapter(private val context: Context, private val recommendations: List<Pair<String, Float>>) : BaseAdapter() {

    override fun getCount(): Int = recommendations.size

    override fun getItem(position: Int): Any = recommendations[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        val recommendationText = view.findViewById<TextView>(R.id.text_fruit_name)
        val recommendationPrice = view.findViewById<TextView>(R.id.text_fruit_price)

        val recommendation = recommendations[position]
        recommendationText.text = recommendation.first
        recommendationPrice.text = String.format("Score: %.2f", recommendation.second)

        // Set a placeholder image or actual image based on recommendation
        val recommendationImage = view.findViewById<ImageView>(R.id.image_fruit)
        // For now, we'll set a placeholder image
        recommendationImage.setImageResource(R.drawable.ic_launcher_background)

        return view
    }
}
