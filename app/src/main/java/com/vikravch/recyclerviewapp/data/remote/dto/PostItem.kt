package com.vikravch.recyclerviewapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("id") val id: Long,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_id") val userId: String = "",
    @SerializedName("user_pic") val userPic: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("photo") val photo: String = "",
    @SerializedName("date") val date: String = ""
)