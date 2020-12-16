package com.isaacudy.opentrivia.trivia.launcher.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import javax.inject.Singleton

interface TriviaCategoryApi {
    @GET("api_category.php")
    suspend fun getApiCategories(): TriviaCategoriesResponse
}

@Serializable
data class TriviaCategoriesResponse(
    @SerialName("trivia_categories") val triviaCategories: List<TriviaCategoryResponse>
)

@Serializable
data class TriviaCategoryResponse(
    val id: Int,
    val name: String
)
