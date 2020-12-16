package com.isaacudy.opentrivia.trivia.launcher

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaLauncherRepository
import kotlinx.coroutines.launch

class TriviaLauncherViewModel @ViewModelInject constructor(
    private val triviaLauncherRepository: TriviaLauncherRepository
) : SingleStateViewModel<Unit>() {
    override val initialState: Unit
        get() = Unit

    init {
        viewModelScope.launch {
            triviaLauncherRepository.getAllTriviaCategories()
        }
    }
}