package com.vikravch.recyclerviewapp.data.repository

import com.vikravch.recyclerviewapp.data.remote.PostsApi
import com.vikravch.recyclerviewapp.data.remote.dto.PostsResponse
import com.vikravch.recyclerviewapp.domain.repository.PostsRepository
import java.lang.Exception

class PostsRepositoryImplApi(private val postsApi: PostsApi): PostsRepository {
    override suspend fun getItems(pageNumber: Int): Result<PostsResponse?> {
        return try{
            val res = postsApi.items(pageNumber)
            if(res.isSuccessful){
                res.body()?.let {
                    Result.success(res.body())
                }?:run{
                    Result.failure(Exception("No data from server"))
                }
            } else {
                Result.failure(Exception("Request failed"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}