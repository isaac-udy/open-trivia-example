package com.isaacudy.opentrivia.trivia.game

import com.isaacudy.opentrivia.trivia.game.data.TriviaGameApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TriviaLauncherModule {
    @Provides
    @Singleton
    fun provideTriviaCategoryApi(): TriviaGameApi {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .baseUrl("https://opentdb.com")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }
}