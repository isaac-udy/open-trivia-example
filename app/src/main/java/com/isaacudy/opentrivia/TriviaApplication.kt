package com.isaacudy.opentrivia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import nav.enro.annotations.NavigationComponent
import nav.enro.core.controller.NavigationApplication
import nav.enro.core.controller.NavigationController
import nav.enro.core.controller.navigationController
import nav.enro.core.plugins.EnroHilt
import nav.enro.core.plugins.EnroLogger
import nav.enro.result.EnroResult

@NavigationComponent
@HiltAndroidApp
class TriviaApplication : Application(), NavigationApplication {
    override val navigationController = navigationController {
        withPlugin(EnroHilt())
        withPlugin(EnroResult())
        withPlugin(EnroLogger())
    }
}