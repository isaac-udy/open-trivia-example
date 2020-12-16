package com.isaacudy.opentrivia.trivia.game.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URLDecoder
import javax.inject.Inject

class TriviaGameRepository @Inject constructor(
    private val api: TriviaGameApi
) {
    suspend fun getQuestions(
        categoryId: Int,
        count: Int = 10
    ): List<TriviaQuestionEntity> {
        val response = api.getQuestions(categoryId = categoryId, count = count)

        return response.results
            .map {
                TriviaQuestionEntity(it.question.fromUrlEncoding())
            }
    }
}

private fun String.fromUrlEncoding() = URLDecoder.decode(this, "utf-8")

@Parcelize
data class TriviaQuestionEntity(
    val question: String
) : Parcelable