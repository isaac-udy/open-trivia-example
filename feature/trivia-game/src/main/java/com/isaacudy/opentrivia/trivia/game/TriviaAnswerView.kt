package com.isaacudy.opentrivia.trivia.game

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaAnswerViewBinding
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaQuestionViewBinding

class TriviaAnswerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)  {

    private val selectableItemBackground by lazy {
        val out = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, out, true);
        out.resourceId
    }


    private val view = TriviaAnswerViewBinding.inflate(LayoutInflater.from(context))
        .also { addView(it.root) }

    fun setAnswer(answer: TriviaQuestionEntity.Answer, state: State, onSelected: () -> Unit) {
        view.answerText.text = answer.answer

        when(state) {
            State.NONE -> {
                view.root.setOnClickListener { onSelected() }
                view.root.setBackgroundResource(selectableItemBackground)
            }
            State.CORRECT -> {
                view.root.setOnClickListener(null)
                view.root.setBackgroundResource(R.color.primary)
            }
            State.INCORRECT -> {
                view.root.setOnClickListener(null)
                view.root.setBackgroundResource(R.color.red)
            }
        }
    }

    enum class State {
        NONE,
        CORRECT,
        INCORRECT
    }
}