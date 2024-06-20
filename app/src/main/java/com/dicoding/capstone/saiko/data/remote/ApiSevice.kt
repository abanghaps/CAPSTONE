package com.dicoding.capstone.saiko.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<UploadResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<StoryResponse>
}
