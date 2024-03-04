package com.example.barapp.data.remote.di

import com.example.barapp.data.remote.api.CocktailTypeApi
import com.example.barapp.data.remote.api.LearningMaterialApi
import com.example.barapp.data.remote.api.PassingStatusApi
import com.example.barapp.data.remote.api.PhotoApi
import com.example.barapp.data.remote.api.RoleApi
import com.example.barapp.data.remote.api.TechCardApi
import com.example.barapp.data.remote.api.TechCardRecipeApi
import com.example.barapp.data.remote.api.TestApi
import com.example.barapp.data.remote.api.UserApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Singleton
    @Provides
    fun provideUserApi(): UserApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideRoleApi(): RoleApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideTestApi(): TestApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideLearningMaterialApi(): LearningMaterialApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideTechCardApi(): TechCardApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideTechCardRecipeApi(): TechCardRecipeApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideCocktailTypeApi(): CocktailTypeApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }


    @Singleton
    @Provides
    fun providePassingStatusApi(): PassingStatusApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun providePhotoApi(): PhotoApi {
        return Retrofit.Builder()
            .baseUrl(PhotoApi.PHOTO_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create(),
                ),
            )
            .build()
            .create()
    }
    companion object{
        const val BASE_URL = "http://192.168.1.8:5000/api/"
    }
}