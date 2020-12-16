package com.isaacudy.opentrivia.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isaacudy.opentrivia.navigation.SplashScreen
import nav.enro.annotations.NavigationDestination
import nav.enro.core.navigationHandle
import nav.enro.viewmodel.enroViewModels

@NavigationDestination(SplashScreen::class, allowDefault = true)
class SplashActivity : AppCompatActivity() {

    private val viewModel by enroViewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}