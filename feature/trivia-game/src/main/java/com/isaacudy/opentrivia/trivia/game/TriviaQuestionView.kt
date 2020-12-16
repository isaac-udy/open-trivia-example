package com.isaacudy.opentrivia.trivia.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaQuestionViewBinding

class TriviaQuestionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)  {

    val view = TriviaQuestionViewBinding.inflate(LayoutInflater.from(context))
        .also { addView(it.root) }

    fun setQuestion(question: TriviaQuestionEntity) {
        view.questionText.text = question.question
    }

}