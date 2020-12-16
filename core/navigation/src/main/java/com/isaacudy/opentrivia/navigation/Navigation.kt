package com.isaacudy.opentrivia.navigation

import kotlinx.android.parcel.Parcelize
import nav.enro.core.NavigationKey

@Parcelize
class SplashScreen : NavigationKey

@Parcelize
class LoginScreen : NavigationKey

@Parcelize
class TriviaLauncherScreen : NavigationKey

@Parcelize
data class TriviaGameScreen(val categoryId: Int) : NavigationKey

@Parcelize
class TriviaResultScreen : NavigationKey