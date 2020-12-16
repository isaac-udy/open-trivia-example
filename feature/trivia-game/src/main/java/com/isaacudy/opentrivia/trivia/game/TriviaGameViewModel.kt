package com.isaacudy.opentrivia.trivia.game

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.asListener
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.trivia.game.data.TriviaGameRepository
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import nav.enro.viewmodel.navigationHandle

sealed class TriviaGameState {
    object None : TriviaGameState()
    object Loading: TriviaGameState()
    object Error: TriviaGameState()

    data class Loaded(
        val questions: List<TriviaQuestionEntity>
    ): TriviaGameState()
}

class TriviaGameViewModel @ViewModelInject constructor(
    val repository: TriviaGameRepository
) : SingleStateViewModel<TriviaGameState>() {
    override val initialState = TriviaGameState.None

    private val navigation by navigationHandle<TriviaGameScreen>()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        if(state.value is TriviaGameState.Loading) return

        viewModelScope
            .asListener {
                repository.getQuestions(navigation.key.categoryId)
            }
            .updateStateOnLaunch {
                TriviaGameState.Loading
            }
            .updateStateOnComplete {
                TriviaGameState.Loaded(it)
            }
            .updateStateOnError {
                TriviaGameState.Error
            }
            .launch()
    }
}