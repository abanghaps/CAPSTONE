package com.dicoding.capstone.saiko.view.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dicoding.capstone.saiko.R

class HomeFragment : Fragment() {

    private lateinit var selectedCategoryTextView: TextView
    private lateinit var gridView: GridView
    private lateinit var categoryFishButton: ImageButton
    private lateinit var categoryVegetablesButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        selectedCategoryTextView = view.findViewById(R.id.selected_category)
        gridView = view.findViewById(R.id.gridview_fruits)
        categoryFishButton = view.findViewById(R.id.category_fish)
        categoryVegetablesButton = view.findViewById(R.id.category_vegetables)

        // Apply window insets to the main layout
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.home_fragment_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryFishButton.setOnClickListener {
            selectedCategoryTextView.setText("Ikan Segar")
            categoryFishButton.setBackgroundResource(R.drawable.background_clicked)
            categoryVegetablesButton.setBackgroundResource(R.drawable.background_default)
            // Update GridView with fish data
        }

        categoryVegetablesButton.setOnClickListener {
            selectedCategoryTextView.setText("Sayur-sayuran")
            categoryVegetablesButton.setBackgroundResource(R.drawable.background_clicked)
            categoryFishButton.setBackgroundResource(R.drawable.background_default)
            // Update GridView with vegetable data
        }

        return view
    }
}
