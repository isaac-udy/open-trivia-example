package com.isaacudy.opentrivia.trivia.game

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before

class TriviaGameViewModelTest {

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


}