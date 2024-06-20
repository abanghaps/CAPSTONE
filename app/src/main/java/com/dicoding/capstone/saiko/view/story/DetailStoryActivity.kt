package com.dicoding.capstone.saiko.view.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.saiko.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val name = intent.getStringExtra(EXTRA_NAME)
        val photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        binding.tvDetailName.text = name
        binding.tvDetailDescription.text = description

        Glide.with(this)
            .load(photoUrl)
            .into(binding.ivDetailPhoto)
            .clearOnDetach()
            .also { supportStartPostponedEnterTransition() }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO_URL = "extra_photo_url"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}
