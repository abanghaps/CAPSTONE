package com.dicoding.capstone.saiko.data.remote


data class Story(
    val description: String,
    val photoUrl: String
)
data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)
