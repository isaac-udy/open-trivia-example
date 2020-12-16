package com.isaacudy.opentrivia.trivia.game

import android.util.SparseArray
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.asListener
import com.isaacudy.opentrivia.core.ui.ConfirmationScreen
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.trivia.game.data.TriviaGameRepository
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import nav.enro.core.close
import nav.enro.result.registerForNavigationResult
import nav.enro.viewmodel.navigationHandle

sealed class TriviaGameState {
    object None : TriviaGameState()
    object Loading: TriviaGameState()
    object Error: TriviaGameState()

    data class Loaded(
        val questions: List<TriviaQuestionEntity>,
        val results: List<AnswerResult> = List(questions.size) { AnswerResult.NONE }
    ): TriviaGameState() {
        val selectedQuestion: TriviaQuestionEntity?
            get() {
                val selectedIndex = results.indexOfFirst { it == AnswerResult.NONE }
                if(selectedIndex < 0) return null
                return questions[selectedIndex]
            }
    }
}

enum class AnswerResult {
    NONE,
    CORRECT,
    INCORRECT
}

class TriviaGameViewModel @ViewModelInject constructor(
    val repository: TriviaGameRepository
) : SingleStateViewModel<TriviaGameState>() {
    override val initialState = TriviaGameState.None

    private val navigation by navigationHandle<TriviaGameScreen>()

    private val confirmCancelled by registerForNavigationResult<Boolean>(navigation) {
        if(it) {
            navigation.close()
        }
    }

    init {
        loadQuestions()
        navigation.onCloseRequested {
            confirmCancelled.open(ConfirmationScreen(
                title = "Are you sure?",
                message = "If you exit the quiz, you will lose your progress!",
                negativeButton = "Cancel",
                positiveButton = "Exit"
            ))
        }
    }

    fun loadQuestions() {
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

    fun onAnswerSelected(question: TriviaQuestionEntity, answer: TriviaQuestionEntity.Answer) {
        val state = state.value as TriviaGameState.Loaded
        val index = state.questions.indexOf(question)
        if(index < 0) return

        val result = if(answer.isCorrect) AnswerResult.CORRECT else AnswerResult.INCORRECT

        updateState {
            state.copy(
                results = state.results.mapIndexed { resultIndex, it ->
                    if(resultIndex == index) result else it
                }
            )
        }

        checkCompletion()
    }

    private fun checkCompletion() {
        val state = state.value as TriviaGameState.Loaded

    }
}