package com.isaacudy.opentrivia.core.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.parcel.Parcelize
import nav.enro.annotations.NavigationDestination
import nav.enro.core.NavigationInstruction
import nav.enro.core.NavigationKey
import nav.enro.core.context.NavigationContext
import nav.enro.core.context.activity
import nav.enro.core.navigator.SyntheticDestination
import java.lang.IllegalStateException

@Parcelize
data class TransientMessageScreen(
    val text: String,
    val color: Int,
    val duration: Int
) : NavigationKey

@NavigationDestination(TransientMessageScreen::class)
class SnackbarDestination : SyntheticDestination<TransientMessageScreen> {
    override fun process(
        navigationContext: NavigationContext<out Any, out NavigationKey>,
        instruction: NavigationInstruction.Open<TransientMessageScreen>
    ) {
        val key = instruction.navigationKey
        val root = navigationContext.activity.findViewById<View>(android.R.id.content)

        Snackbar.make(root, key.text, key.duration)
            .setBackgroundTint(ContextCompat.getColor(navigationContext.activity, key.color))
            .show()

    }

    private fun View.dp(dp: Float): Int{
        return (dp * resources.displayMetrics.density).toInt()
    }
}