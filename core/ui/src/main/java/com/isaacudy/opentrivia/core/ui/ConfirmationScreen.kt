package com.isaacudy.opentrivia.core.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.parcel.Parcelize
import nav.enro.annotations.NavigationDestination
import nav.enro.core.navigationHandle
import nav.enro.result.ResultNavigationKey
import nav.enro.result.closeWithResult

@Parcelize
data class ConfirmationScreen(
    val title: String,
    val message: String,
    val negativeButton: String,
    val positiveButton: String
): ResultNavigationKey<Boolean>

@NavigationDestination(ConfirmationScreen::class)
class ConfirmationDialog : DialogFragment() {

    private val navigation by navigationHandle<ConfirmationScreen>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val key = navigation.key
        return AlertDialog.Builder(requireContext())
            .setTitle(key.title)
            .setMessage(key.message)
            .setCancelable(false)
            .setPositiveButton(key.positiveButton) { _, _ ->
                navigation.closeWithResult(true)
            }
            .setNegativeButton(key.negativeButton) { _, _ ->
                navigation.closeWithResult(false)
            }
            .create()
    }
}