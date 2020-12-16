package com.isaacudy.opentrivia.trivia.launcher

import com.isaacudy.opentrivia.trivia.launcher.data.TriviaCategoriesResponse
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaCategoryApi
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaCategoryResponse
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaLauncherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class TriviaLauncherViewModelTest {

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun whenViewModelIsInitialised_thenViewModelStateIsLoading() {
        val triviaCategoryApi = mockk<TriviaCategoryApi>()
        coEvery { triviaCategoryApi.getApiCategories() }.coAnswers {
            delay(10_000)
            TriviaCategoriesResponse(emptyList())
        }

        val viewModel = createViewModel(triviaCategoryApi)

        assertEquals(TriviaLauncherState.Loading, viewModel.state.value)
    }


    @Test
    fun whenApiReturnsError_thenViewModelStateIsError() = runBlockingTest {
        val triviaCategoryApi = mockk<TriviaCategoryApi>()
        coEvery { triviaCategoryApi.getApiCategories() }.throws(RuntimeException())

        val viewModel = createViewModel(triviaCategoryApi)

        assertEquals(TriviaLauncherState.Error, viewModel.state.value)
    }

    @Test
    fun whenApiReturnsList_thenViewModelStateIsLoaded_withCorrectElements() = runBlockingTest {
        val trackingCategoriesResponse = TriviaCategoriesResponse(
            listOf(
                TriviaCategoryResponse(1, "test one"),
                TriviaCategoryResponse(2, "test two")
            )
        )
        val triviaCategoryApi = mockk<TriviaCategoryApi>()
        coEvery { triviaCategoryApi.getApiCategories() }.returns(trackingCategoriesResponse)

        val viewModel = createViewModel(triviaCategoryApi)

        val state = viewModel.state.value as TriviaLauncherState.Loaded
        val loadedCategories = state.categories

        trackingCategoriesResponse.triviaCategories.forEach { responseItem ->
            assertTrue(loadedCategories.any { responseItem.id == it.id })
        }
    }

    private fun createViewModel(triviaCategoryApi: TriviaCategoryApi) = TriviaLauncherViewModel(
        TriviaLauncherRepository(triviaCategoryApi)
    )
}