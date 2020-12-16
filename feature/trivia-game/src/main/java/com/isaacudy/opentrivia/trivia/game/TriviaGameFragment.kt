package com.isaacudy.opentrivia.trivia.game

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.isaacudy.opentrivia.core.ui.ErrorViewParameters
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.observe
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaGameFragmentBinding
import com.isaacudy.opentrivia.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import nav.enro.annotations.NavigationDestination
import nav.enro.viewmodel.enroViewModels

@AndroidEntryPoint
@NavigationDestination(TriviaGameScreen::class)
class TriviaGameFragment : Fragment(R.layout.trivia_game_fragment) {

    private val binding by viewBinding<TriviaGameFragmentBinding>()
    private val viewModel by enroViewModels<TriviaGameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialiseErrorView()
        viewModel.observe(this, ::onStateUpdated)
    }

    private fun initialiseErrorView() {
        binding.errorView.set(
            ErrorViewParameters(
                title = "Whoops!",
                message = "It looks like something went wrong, please try again",
                button = "Retry",
                onButtonClicked = { viewModel.loadQuestions() }
            )
        )
    }

    private fun onStateUpdated(state: TriviaGameState) {
        binding.errorView.isVisible = state is TriviaGameState.Error
        binding.progress.isVisible = state !is TriviaGameState.Error

        binding.progress.isIndeterminate = state !is TriviaGameState.Loaded

        when(state) {
            TriviaGameState.None -> {}
            TriviaGameState.Loading -> {}
            TriviaGameState.Error -> {}
            is TriviaGameState.Loaded -> {
                binding.progress.max = state.questions.size * 100
                val progress = state.results.count { it !is AnswerResult.None } * 100
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    binding.progress.setProgress(progress, true)
                } else {
                    binding.progress.progress = progress
                }

                binding.question.setQuestion(state.selectedQuestion, state.selectedResult) {
                    viewModel.onAnswerSelected(state.selectedQuestion, it)
                }
            }
        }
    }
}