package com.dicoding.capstone.saiko.data.remote

import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String
)

data class StoryResponse(
    @SerializedName("listStory") val stories: List<Story>
)
