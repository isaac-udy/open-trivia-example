package com.isaacudy.opentrivia.profile.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        binding.loginButton.setOnClickListener {
            getNavigationHandle<NavigationKey>().replace(TriviaLauncherScreen())
        }
    }
}