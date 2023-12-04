package com.vikravch.recyclerviewapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("posts") val posts: List<PostItem>
)