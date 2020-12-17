package com.isaacudy.opentrivia.trivia.game

import android.util.SparseArray
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.asListener
import com.isaacudy.opentrivia.core.ui.ConfirmationScreen
import com.isaacudy.opentrivia.core.ui.TransientMessageScreen
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.navigation.TriviaResultScreen
import com.isaacudy.opentrivia.trivia.game.data.TriviaGameRepository
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nav.enro.core.close
import nav.enro.core.forward
import nav.enro.core.replace
import nav.enro.result.registerForNavigationResult
import nav.enro.viewmodel.navigationHandle

sealed class TriviaGameState {
    object None : TriviaGameState()
    object Loading : TriviaGameState()
    object Error : TriviaGameState()

    data class Loaded(
        val questions: List<TriviaQuestionEntity>,
        val selectedQuestion: TriviaQuestionEntity,
        val results: List<AnswerResult> = List(questions.size) { AnswerResult.None },
    ) : TriviaGameState() {
        val selectedResult get() = results[questions.indexOf(selectedQuestion)]
    }
}

sealed class AnswerResult {
    object None : AnswerResult()
    data class Correct(val answer: String) : AnswerResult()
    data class Incorrect(val answer: String) : AnswerResult()
}

class TriviaGameViewModel @ViewModelInject constructor(
    val repository: TriviaGameRepository
) : SingleStateViewModel<TriviaGameState>() {
    override val initialState = TriviaGameState.None

    private val navigation by navigationHandle<TriviaGameScreen>()

    private val confirmCancelled by registerForNavigationResult<Boolean>(navigation) {
        if (it) {
            navigation.close()
        }
    }

    init {
        loadQuestions()
        navigation.onCloseRequested {
            confirmCancelled.open(
                ConfirmationScreen(
                    title = "Are you sure?",
                    message = "If you exit the quiz, you will lose your progress.",
                    negativeButton = "Cancel",
                    positiveButton = "Exit"
                )
            )
        }
    }

    fun loadQuestions() {
        if (state.value is TriviaGameState.Loading) return

        viewModelScope
            .asListener {
                repository.getQuestions(navigation.key.categoryId)
            }
            .updateStateOnLaunch {
                TriviaGameState.Loading
            }
            .updateStateOnComplete {
                TriviaGameState.Loaded(it, it.first())
            }
            .updateStateOnError {
                TriviaGameState.Error
            }
            .launch()
    }

    fun onAnswerSelected(question: TriviaQuestionEntity, answer: TriviaQuestionEntity.Answer) {
        val state = state.value as TriviaGameState.Loaded
        val index = state.questions.indexOf(question)
        if (index < 0) return
        if (state.results[index] !is AnswerResult.None) return
        if (state.selectedQuestion != question) return

        val result =
            if (answer.isCorrect) AnswerResult.Correct(answer.answer) else AnswerResult.Incorrect(
                answer.answer
            )

        val answerMessage = TransientMessageScreen(
            text = if (answer.isCorrect) "Correct!" else "Incorrect",
            color = if (answer.isCorrect) R.color.primary else R.color.red,
            duration = 1500
        )
        navigation.forward(answerMessage)

        viewModelScope
            .asListener {
                delay(1500)
            }
            .updateStateOnLaunch {
                this as TriviaGameState.Loaded
                copy(
                    results = results.mapIndexed { resultIndex, it ->
                        if (resultIndex == index) result else it
                    }
                )
            }
            .updateStateOnComplete {
                this as TriviaGameState.Loaded
                checkCompletion()

                val unansweredIndex = results.indexOfFirst { it is AnswerResult.None }
                copy(
                    selectedQuestion = questions.getOrNull(unansweredIndex) ?: selectedQuestion
                )
            }
            .updateStateOnError {
                TriviaGameState.Error
            }
            .launch()
    }

    private fun checkCompletion() {
        val state = state.value as TriviaGameState.Loaded
        val remainingQuestions = state.results.count { it is AnswerResult.None }
        if (remainingQuestions == 0) {
            navigation.replace(TriviaResultScreen(
                answers = state.results.map { it is AnswerResult.Correct }
            ))
        }
    }
}