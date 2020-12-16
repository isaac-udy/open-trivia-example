package com.isaacudy.opentrivia.trivia.game.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URLDecoder
import java.util.*
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
                val answers = it.incorrectAnswers
                    .map { answer ->
                        TriviaQuestionEntity.Answer(answer.decodeUrl(), false)
                    } + TriviaQuestionEntity.Answer(it.correctAnswer.decodeUrl(), true)

                TriviaQuestionEntity(
                    question = it.question.decodeUrl(),
                    difficulty = TriviaQuestionEntity.Difficulty.fromApiString(it.difficulty),
                    answers = answers.shuffled()
                )
            }
    }
}

private fun String.decodeUrl() = URLDecoder.decode(this, "utf-8")

@Parcelize
data class TriviaQuestionEntity(
    val question: String,
    val difficulty: Difficulty,
    val answers: List<Answer>
) : Parcelable {

    @Parcelize
    data class Answer(
        val answer: String,
        val isCorrect: Boolean
    ) : Parcelable

    enum class Difficulty {
        EASY,
        MEDIUM,
        HARD;

        companion object {
            fun fromApiString(string: String) =
                valueOf(string.toUpperCase(Locale.ROOT))
        }
    }
}