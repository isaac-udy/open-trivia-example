package com.isaacudy.opentrivia.trivia.launcher

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.trivia.launcher.databinding.TriviaLauncherFragmentBinding
import com.isaacudy.opentrivia.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import nav.enro.annotations.NavigationDestination
import nav.enro.core.NavigationKey
import nav.enro.core.forward
import nav.enro.core.getNavigationHandle

@AndroidEntryPoint
@NavigationDestination(TriviaLauncherScreen::class)
class TriviaLauncherFragment : Fragment(R.layout.trivia_launcher_fragment) {

    private val binding by viewBinding<TriviaLauncherFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.launch.setOnClickListener {
            getNavigationHandle<NavigationKey>().forward(TriviaGameScreen())
        }
    }
}