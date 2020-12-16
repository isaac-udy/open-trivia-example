package com.isaacudy.opentrivia.trivia.launcher.data

import android.util.Log
import javax.inject.Inject

class TriviaLauncherRepository @Inject constructor(
    private val api: TriviaCategoryApi
) {

    suspend fun getAllTriviaCategories(): List<TriviaCategoryEntity> {
        val categories = api.getApiCategories().triviaCategories

        return categories.map {
            TriviaCategoryEntity(
                id = it.id,
                name = it.name
            )
        }
    }

}

data class TriviaCategoryEntity(
    val id: Int,
    val name: String
)