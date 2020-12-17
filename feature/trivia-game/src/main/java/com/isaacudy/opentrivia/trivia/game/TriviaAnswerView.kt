package com.isaacudy.opentrivia.trivia.game

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaAnswerViewBinding
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaQuestionViewBinding

class TriviaAnswerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)  {
    private val view = TriviaAnswerViewBinding.inflate(LayoutInflater.from(context))
        .also { addView(it.root) }

    fun setAnswer(answer: TriviaQuestionEntity.Answer, state: State, onSelected: () -> Unit) {
        view.answerText.text = answer.answer

        when(state) {
            State.NONE -> {
                view.root.setOnClickListener { onSelected() }
                view.root.setBackgroundResource(android.R.color.transparent)
            }
            else -> {
                view.root.setOnClickListener(null)

                val backgroundColour = if(state == State.CORRECT) R.color.primary else R.color.red
                val transition = TransitionDrawable(arrayOf(
                    ContextCompat.getDrawable(context, android.R.color.transparent),
                    ContextCompat.getDrawable(context, backgroundColour)
                ))

                view.root.background = transition
                transition.startTransition(150)
            }
        }
    }

    enum class State {
        NONE,
        CORRECT,
        INCORRECT
    }
}