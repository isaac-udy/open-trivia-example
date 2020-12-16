package com.isaacudy.opentrivia.trivia.launcher

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.asListener
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaCategoryEntity
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaLauncherRepository
import kotlinx.coroutines.launch

sealed class TriviaLauncherState {
    object None : TriviaLauncherState()
    object Loading : TriviaLauncherState()
    object Error : TriviaLauncherState()

    class Loaded(
        val categories: List<TriviaCategoryEntity>
    ) : TriviaLauncherState()
}

class TriviaLauncherViewModel @ViewModelInject constructor(
    private val triviaLauncherRepository: TriviaLauncherRepository
) : SingleStateViewModel<TriviaLauncherState>() {
    override val initialState = TriviaLauncherState.None

    init {
        onRefreshed()
    }

    fun onRefreshed() {
        if (state.value == TriviaLauncherState.Loading) return
        updateState { TriviaLauncherState.Loading }

        viewModelScope
            .asListener {
                triviaLauncherRepository.getAllTriviaCategories()
            }
            .updateStateOnLaunch {
                TriviaLauncherState.Loading
            }
            .updateStateOnComplete {
                TriviaLauncherState.Loaded(
                    categories = it
                )
            }
            .updateStateOnError {
                TriviaLauncherState.Error
            }
            .launch()
    }
}