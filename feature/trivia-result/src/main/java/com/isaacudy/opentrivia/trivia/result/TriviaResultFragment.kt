package com.isaacudy.opentrivia.trivia.result

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
import nav.enro.core.replaceRoot

@AndroidEntryPoint
@NavigationDestination(TriviaResultScreen::class)
class TriviaResultFragment : Fragment(R.layout.trivia_result_fragment) {

    private val binding by viewBinding<TriviaResultFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.goTolauncher.setOnClickListener {
            getNavigationHandle<TriviaResultScreen>().replaceRoot(TriviaLauncherScreen())
        }
    }
}