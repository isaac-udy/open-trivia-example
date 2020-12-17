package com.isaacudy.opentrivia.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.isaacudy.opentrivia.SingleStateViewModel
import com.isaacudy.opentrivia.navigation.LoginScreen
import com.isaacudy.opentrivia.navigation.SplashScreen
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.profile.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nav.enro.core.*
import nav.enro.viewmodel.navigationHandle

class SplashViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : SingleStateViewModel<Unit>() {
    override val initialState = Unit

    private val navigation by navigationHandle<SplashScreen>()

    init {
        viewModelScope.launch {
            delay(225)
            val nextScreen = when(profileRepository.getCurrentUser()) {
                null -> LoginScreen()
                else -> TriviaLauncherScreen()
            }
            navigation.executeInstruction(
                NavigationInstruction.Open(
                    navigationKey = nextScreen,
                    navigationDirection = NavigationDirection.FORWARD,
                    animations = NavigationAnimations.Resource(
                        openEnter = 0,
                        openExit = R.anim.enro_no_op_animation,
                        closeEnter = R.anim.enro_no_op_animation,
                        closeExit = R.anim.enro_no_op_animation,
                    )
                )
            )
        }
    }
}

