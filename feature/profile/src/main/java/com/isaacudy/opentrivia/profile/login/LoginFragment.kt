package com.isaacudy.opentrivia.profile.login

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.TransitionManager
import com.isaacudy.opentrivia.navigation.LoginScreen
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.profile.R
import com.isaacudy.opentrivia.profile.databinding.LoginFragmentBinding
import com.isaacudy.opentrivia.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import nav.enro.annotations.NavigationDestination
import nav.enro.core.NavigationKey
import nav.enro.core.forward
import nav.enro.core.getNavigationHandle
import nav.enro.core.replace

@AndroidEntryPoint
@NavigationDestination(LoginScreen::class)
class LoginFragment : Fragment(R.layout.login_fragment) {

    private val binding by viewBinding<LoginFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            val defaultConstraints = ConstraintSet().apply { clone(binding.root) }
            val initialConstraints = ConstraintSet().apply {
                clone(requireContext(), R.layout.login_fragment_start)
            }

            initialConstraints.applyTo(binding.root)
            binding.root.animate()
                .setDuration(1)
                .withEndAction {
                    TransitionManager.beginDelayedTransition(binding.root, AutoTransition().apply {
                        interpolator = LinearOutSlowInInterpolator()
                        duration = 500
                    })
                    defaultConstraints.applyTo(binding.root)
                }
                .start()
        }

        binding.loginButton.setOnClickListener {
            getNavigationHandle<NavigationKey>().replace(TriviaLauncherScreen())
        }
    }
}