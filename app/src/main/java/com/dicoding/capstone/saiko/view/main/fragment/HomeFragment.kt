package com.dicoding.capstone.saiko.view.main.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dicoding.capstone.saiko.BuildConfig
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.data.remote.ApiConfig
import com.dicoding.capstone.saiko.data.remote.StoryResponse
import com.dicoding.capstone.saiko.helper.TFLiteHelper
import com.dicoding.capstone.saiko.view.adapter.RecommendationAdapter
import com.dicoding.capstone.saiko.view.adapter.StoriesAdapter
import com.dicoding.capstone.saiko.view.camera.AddDaganganActivity
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var selectedCategoryTextView: TextView
    private lateinit var gridView: GridView
    private lateinit var categoryFishButton: ImageButton
    private lateinit var categoryVegetablesButton: ImageButton
    private lateinit var addButton: ImageButton
    private lateinit var searchEditText: EditText

    private lateinit var auth: FirebaseAuth
    private var isGuest: Boolean = true
    private lateinit var tfliteHelper: TFLiteHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()
        tfliteHelper = TFLiteHelper(requireContext())

        if (auth.currentUser != null) {
            isGuest = false
        }

        selectedCategoryTextView = view.findViewById(R.id.selected_category)
        gridView = view.findViewById(R.id.gridview_fruits)
        categoryFishButton = view.findViewById(R.id.category_fish)
        categoryVegetablesButton = view.findViewById(R.id.category_vegetables)
        addButton = view.findViewById(R.id.imageButton)
        searchEditText = view.findViewById(R.id.search_bar)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.home_fragment_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryFishButton.setOnClickListener {
            selectedCategoryTextView.text = getString(R.string.ikan_segar)
            categoryFishButton.setBackgroundResource(R.drawable.background_clicked)
            categoryVegetablesButton.setBackgroundResource(R.drawable.background_default)
            fetchStories()
        }

        categoryVegetablesButton.setOnClickListener {
            selectedCategoryTextView.text = getString(R.string.sayur_sayuran)
            categoryVegetablesButton.setBackgroundResource(R.drawable.background_clicked)
            categoryFishButton.setBackgroundResource(R.drawable.background_default)
            fetchStories()
        }

        addButton.setOnClickListener {
            if (isGuest) {
                showGuestWarning()
            } else {
                val intent = Intent(activity, AddDaganganActivity::class.java)
                startActivity(intent)
            }
        }

        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch()
                true
            } else {
                false
            }
        }

        fetchStories()

        return view
    }

    private fun fetchStories() {
        val token = BuildConfig.API_TOKEN
        val client = ApiConfig.getApiService().getStories(token)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory ?: emptyList()
                    gridView.adapter = StoriesAdapter(requireContext(), stories)
                } else {
                    Toast.makeText(context, "Failed to load stories: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performSearch() {
        val query = searchEditText.text.toString()
        if (query.isNotEmpty()) {
            searchRecommendations(query)
        }
    }

    private fun searchRecommendations(query: String) {
        val input = FloatArray(1) { query.length.toFloat() }
        val recommendations = tfliteHelper.predict(input)
        gridView.adapter = RecommendationAdapter(requireContext(), recommendations)
    }

    private fun showGuestWarning() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.guest_warning_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
