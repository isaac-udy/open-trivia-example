package com.isaacudy.opentrivia.trivia.game.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaGameApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("category") categoryId: Int,
        @Query("amount") count: Int,
        @Query("encode") encode: String = "url3986"
    ): TriviaQuestionsResponse
}

@Serializable
data class TriviaQuestionsResponse(
    @SerialName("response_code") val responseCode: Int,
    val results: List<TriviaQuestionResponse>
)

@Serializable
data class TriviaQuestionResponse(
    @SerialName("category") val categoryName: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("incorrect_answers") val incorrectAnswers: List<String>
)