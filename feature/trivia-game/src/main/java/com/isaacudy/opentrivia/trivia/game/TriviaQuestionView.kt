package com.isaacudy.opentrivia.trivia.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.isaacudy.opentrivia.trivia.game.data.TriviaQuestionEntity
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaAnswerViewBinding
import com.isaacudy.opentrivia.trivia.game.databinding.TriviaQuestionViewBinding

class TriviaQuestionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = TriviaQuestionViewBinding.inflate(LayoutInflater.from(context))
        .also { addView(it.root) }

    private val answers = List(4) {
        TriviaAnswerView(context)
    }

    fun setQuestion(question: TriviaQuestionEntity, onAnswerSelected: (TriviaQuestionEntity.Answer) -> Unit) {
        view.questionText.text = question.question
        view.questionAnswers.removeAllViews()

        question.answers
            .zip(answers)
            .forEach { (answer, answerView) ->
                answerView.setAnswer(answer) {
                    onAnswerSelected(answer)
                }
                view.questionAnswers.addView(answerView)
            }
    }

}