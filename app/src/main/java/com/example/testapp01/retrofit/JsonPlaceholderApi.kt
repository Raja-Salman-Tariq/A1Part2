package com.example.testapp01.retrofit

import retrofit2.Call
import retrofit2.http.GET


public interface JsonPlaceholderApi {

    @GET("posts")
    fun getPosts(): Call<List<Post>>
}