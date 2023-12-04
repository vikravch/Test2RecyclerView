package com.vikravch.recyclerviewapp.di

import com.google.gson.GsonBuilder
import com.vikravch.recyclerviewapp.data.remote.PostsApi
import com.vikravch.recyclerviewapp.data.repository.PostsRepositoryImplApi
import com.vikravch.recyclerviewapp.domain.repository.PostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun getOkHttpClient(
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            //Log.d("httprequestlog:", message)
        }

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        builder.addInterceptor(logging)
        return builder.build()
    }
    @Singleton
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
            .baseUrl("http://citymani.ezrdv.org/main/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Singleton
    fun providePostsAPI(retrofit: Retrofit):PostsApi {
        return retrofit.create(PostsApi::class.java)
    }
    @Provides
    @Singleton
    fun providePostsRepository(api: PostsApi): PostsRepository {
        return PostsRepositoryImplApi(api)
    }
}