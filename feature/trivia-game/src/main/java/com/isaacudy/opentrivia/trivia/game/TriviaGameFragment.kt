package com.isaacudy.opentrivia.trivia.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        viewModel.observe(this) {
            if(it is TriviaGameState.Loaded) {
                binding.questionFrame.removeAllViews()
                binding.questionFrame.addView(TriviaQuestionView(requireContext()).apply {
                    setQuestion(it.questions.first())
                })
            }
        }
    }
}