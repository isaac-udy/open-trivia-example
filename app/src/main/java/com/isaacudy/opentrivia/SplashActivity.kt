package com.isaacudy.opentrivia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isaacudy.opentrivia.navigation.SplashScreen
import nav.enro.annotations.NavigationDestination
import nav.enro.core.navigationHandle

@NavigationDestination(SplashScreen::class, allowDefault = true)
class SplashActivity : AppCompatActivity() {

    private val navigation by navigationHandle<SplashScreen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}