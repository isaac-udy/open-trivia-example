package com.isaacudy.opentrivia.trivia.game

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.navigation.TriviaResultScreen
import com.isaacudy.opentrivia.trivia.game.data.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import nav.enro.core.NavigationHandle
import nav.enro.core.NavigationInstruction
import nav.enro.core.NavigationKey
import nav.enro.viewmodel.enroViewModels
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class TriviaGameViewModelTest {

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun whenApiReturnsError_thenStateIsUpdatedToError() = runBlockingTest {
        val environment = createTestEnvironment()

        coEvery { environment.api.getQuestions(any(), any(), any()) } throws IllegalStateException()

        val viewModel = environment.createViewModel()
        assertTrue(viewModel.state.value is TriviaGameState.Error)
    }

    @Test
    fun whenApiReturnsData_thenStateIsUpdatedToLoaded() = runBlockingTest {
        val environment = createValidTestEnvironment(listOf())
        val viewModel = environment.createViewModel()
        assertTrue(viewModel.state.value is TriviaGameState.Loaded)
    }

    @Test
    fun whenApiReturnsData_thenSelectedQuestionIsFirstQuestion() = runBlockingTest {
        val environment = createValidTestEnvironment(
            listOf(
                TriviaQuestionResponse(
                    question = "test",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                ),
                TriviaQuestionResponse(
                    question = "test two",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                )
            )
        )
        val viewModel = environment.createViewModel()

        val state = viewModel.state.value as TriviaGameState.Loaded

        assertEquals("test", state.selectedQuestion?.question)
    }

    @Test
    fun whenFirstQuestionIsAnswered_thenSelectedQuestionIsSecondQuestion() = runBlockingTest {
        val environment = createValidTestEnvironment(
            listOf(
                TriviaQuestionResponse(
                    question = "1",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                ),
                TriviaQuestionResponse(
                    question = "2",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                ),
                TriviaQuestionResponse(
                    question = "3",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                )
            )
        )
        val viewModel = environment.createViewModel()

        val state = viewModel.state.value as TriviaGameState.Loaded
        viewModel.onAnswerSelected(
            state.questions.first(),
            TriviaQuestionEntity.Answer("correct answer", true)
        )

        val finalState = viewModel.state.value as TriviaGameState.Loaded
        assertEquals("2", finalState.selectedQuestion?.question)
    }

    @Test
    fun whenAllQuestionsAreAnswered_thenNavigateToResultScreen() = runBlockingTest {
        val environment = createValidTestEnvironment(
            listOf(
                TriviaQuestionResponse(
                    question = "1",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                ),
                TriviaQuestionResponse(
                    question = "2",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                ),
                TriviaQuestionResponse(
                    question = "3",
                    correctAnswer = "correct answer",
                    incorrectAnswers = listOf("bad")
                )
            )
        )
        val viewModel = environment.createViewModel()

        val state = viewModel.state.value as TriviaGameState.Loaded
        viewModel.onAnswerSelected(
            state.questions[0],
            TriviaQuestionEntity.Answer("correct answer", true)
        )
        viewModel.onAnswerSelected(
            state.questions[1],
            TriviaQuestionEntity.Answer("correct answer", true)
        )
        viewModel.onAnswerSelected(
            state.questions[2],
            TriviaQuestionEntity.Answer("correct answer", true)
        )

        verify(exactly = 1) {
            environment.navigationHandle.executeInstruction(
                withArg {
                    val open = it as? NavigationInstruction.Open<TriviaResultScreen>
                    check(open?.navigationKey == TriviaResultScreen(
                        listOf(true, true, true)
                    ))
                }
            )
        }
    }

    private fun createValidTestEnvironment(questions: List<TriviaQuestionResponse>): TestEnvironment {
        val environment = createTestEnvironment()
        coEvery { environment.api.getQuestions(any(), any(), any()) } coAnswers {
            TriviaQuestionsResponse(
                responseCode = 0,
                results = questions
            )
        }
        return environment
    }

    private fun createTestEnvironment(): TestEnvironment {
        val lifecycleOwner = object : LifecycleOwner {
            val lifecycle = LifecycleRegistry(this)
            override fun getLifecycle(): Lifecycle {
                return lifecycle
            }
        }

        val navigationHandle = mockk<NavigationHandle<TriviaGameScreen>>(relaxed = true) {
            every { lifecycle } returns lifecycleOwner.lifecycle
            every { key } returns TriviaGameScreen(0)
        }
        val triviaGameApi = mockk<TriviaGameApi>()

        // Slightly hacky, because Enro doesn't quite support testing like this properly... I should really fix that.
        val factory = Class.forName("nav.enro.viewmodel.EnroViewModelFactory")
        val factoryInstance = factory
            .getConstructor(NavigationHandle::class.java, ViewModelProvider.Factory::class.java)
            .newInstance(navigationHandle, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return TriviaGameViewModel(
                        TriviaGameRepository(triviaGameApi)
                    ) as T
                }
            }) as ViewModelProvider.Factory

        return TestEnvironment(
            createViewModel = { factoryInstance.create(TriviaGameViewModel::class.java) },
            navigationHandle = navigationHandle,
            api = triviaGameApi,
        )
    }
}

data class TestEnvironment(
    val createViewModel: () -> TriviaGameViewModel,
    val navigationHandle: NavigationHandle<TriviaGameScreen>,
    val api: TriviaGameApi
)