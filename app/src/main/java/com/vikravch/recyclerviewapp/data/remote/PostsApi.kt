package com.vikravch.recyclerviewapp.data.remote

import com.vikravch.recyclerviewapp.data.remote.dto.PostsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostsApi {
    @GET("test")
    suspend fun items(
        @Query("page") page: Int
    ): Response<PostsResponse?>
}