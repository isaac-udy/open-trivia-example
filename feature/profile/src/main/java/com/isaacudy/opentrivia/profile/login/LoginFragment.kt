package com.isaacudy.opentrivia.profile.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.isaacudy.opentrivia.navigation.LoginScreen
import com.isaacudy.opentrivia.profile.R
import nav.enro.annotations.NavigationDestination

@NavigationDestination(LoginScreen::class)
class LoginFragment : Fragment(R.layout.login_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}