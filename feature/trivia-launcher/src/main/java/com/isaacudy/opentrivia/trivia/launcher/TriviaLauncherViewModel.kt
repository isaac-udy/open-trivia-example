package com.isaacudy.opentrivia.trivia.launcher

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.launchCatching
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaCategoryEntity
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaLauncherRepository
import kotlinx.coroutines.launch

sealed class TriviaLauncherState {
    object Loading : TriviaLauncherState()
    object Error : TriviaLauncherState()

    class Loaded(
        val categories: List<TriviaCategoryEntity>
    ) : TriviaLauncherState()
}

class TriviaLauncherViewModel @ViewModelInject constructor(
    private val triviaLauncherRepository: TriviaLauncherRepository
) : SingleStateViewModel<TriviaLauncherState>() {
    override val initialState = TriviaLauncherState.Loading

    init {
        viewModelScope
            .launchCatching {
                triviaLauncherRepository.getAllTriviaCategories()
            }
            .onError {
                updateState { TriviaLauncherState.Error }
            }
    }
}