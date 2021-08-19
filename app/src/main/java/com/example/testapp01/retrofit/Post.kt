package com.example.testapp01.retrofit

import com.google.gson.annotations.SerializedName

public data class Post(val userId:Int, val id:Int, val title:String, @SerializedName("body") val text:String)