package com.vikravch.recyclerviewapp.domain.repository

import com.vikravch.recyclerviewapp.data.remote.dto.PostsResponse

interface PostsRepository {
    suspend fun getItems(pageNumber: Int): Result<PostsResponse?>
}