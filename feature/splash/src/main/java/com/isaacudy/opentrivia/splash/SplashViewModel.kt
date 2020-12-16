package com.isaacudy.opentrivia.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.navigation.LoginScreen
import com.isaacudy.opentrivia.navigation.SplashScreen
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.profile.ProfileRepository
import kotlinx.coroutines.launch
import nav.enro.core.forward
import nav.enro.core.replace
import nav.enro.viewmodel.navigationHandle

class SplashViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : SingleStateViewModel<Unit>() {
    override val initialState = Unit

    private val navigation by navigationHandle<SplashScreen>()

    init {
        viewModelScope.launch {
            val nextScreen = when(profileRepository.getCurrentUser()) {
                null -> LoginScreen()
                else -> TriviaLauncherScreen()
            }
            navigation.forward(nextScreen)
        }
    }
}

