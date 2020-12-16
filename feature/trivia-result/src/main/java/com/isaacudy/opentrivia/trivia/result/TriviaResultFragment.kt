package com.isaacudy.opentrivia.trivia.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.navigation.TriviaResultScreen
import com.isaacudy.opentrivia.trivia.result.databinding.TriviaResultFragmentBinding
import com.isaacudy.opentrivia.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import nav.enro.annotations.NavigationDestination
import nav.enro.core.forward
import nav.enro.core.getNavigationHandle
import nav.enro.core.navigationHandle
import nav.enro.core.replaceRoot

@AndroidEntryPoint
@NavigationDestination(TriviaResultScreen::class)
class TriviaResultFragment : Fragment(R.layout.trivia_result_fragment) {

    private val binding by viewBinding<TriviaResultFragmentBinding>()
    private val navigation by navigationHandle<TriviaResultScreen>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val correctQuestions = navigation.key.answers.count { it }
        val totalQuestions = navigation.key.answers.size
        val percentCorrect =
            correctQuestions.times(100).toDouble() / totalQuestions.times(100).toDouble()

        val questionText = "You scored $correctQuestions out of $totalQuestions\n\n"

        binding.message.text = questionText + when {
            percentCorrect < 33 -> {
                 "Whew, that wasn't so great..."
            }
            percentCorrect < 66 -> {
                "That wasn't too bad, but you can do better!"
            }
            percentCorrect < 99 -> {
                "Well done, that was pretty good!"
            }
            else -> {
                "Wow! I'm impressed!"
            }
        }

        binding.homeButton.setOnClickListener {
            navigation.replaceRoot(TriviaLauncherScreen())
        }
    }
}