package com.isaacudy.opentrivia.trivia.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaAnswerViewBinding
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaQuestionViewBinding

class TriviaAnswerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)  {

    private val view = TriviaAnswerViewBinding.inflate(LayoutInflater.from(context))
        .also { addView(it.root) }

    fun setAnswer(answer: TriviaQuestionEntity.Answer, onSelected: () -> Unit) {
        view.answerText.text = answer.answer
        view.root.setOnClickListener { onSelected() }
    }
}