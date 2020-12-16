package com.isaacudy.opentrivia.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.isaacudy.opentrivia.ui.databinding.ErrorViewBinding

class ErrorViewParameters(
    val title: String,
    val message: String,
    val button: String,
    val onButtonClicked: () -> Unit
)

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = ErrorViewBinding.inflate(LayoutInflater.from(context))
        .also { addView(it.root) }

    fun set(parameters: ErrorViewParameters) {
        view.title.text = parameters.title
        view.message.text = parameters.message
        view.retryButton.text = parameters.button
        view.retryButton.setOnClickListener { parameters.onButtonClicked() }
    }
}