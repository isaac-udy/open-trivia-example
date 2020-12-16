package com.isaacudy.opentrivia.trivia.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.navigation.TriviaResultScreen
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaGameFragmentBinding
import com.isaacudy.opentrivia.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import nav.enro.annotations.NavigationDestination
import nav.enro.core.NavigationKey
import nav.enro.core.getNavigationHandle
import nav.enro.core.replace

@AndroidEntryPoint
@NavigationDestination(TriviaGameScreen::class)
class TriviaGameFragment : Fragment(R.layout.trivia_game_fragment) {

    private val binding by viewBinding<TriviaGameFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.finish.setOnClickListener {
            getNavigationHandle<NavigationKey>().replace(TriviaResultScreen())
        }
    }
}