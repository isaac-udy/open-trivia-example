package com.isaacudy.opentrivia.trivia.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
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

    init {
        if(isInEditMode) {
            answers.forEach {
                it.setAnswer(TriviaQuestionEntity.Answer("Answer Here", false), TriviaAnswerView.State.NONE){}
                view.questionAnswers.addView(it)
            }
        }
    }

    fun setQuestion(question: TriviaQuestionEntity, result: AnswerResult, onAnswerSelected: (TriviaQuestionEntity.Answer) -> Unit) {
        if(view.questionText.text != question.question) {
            view.root.animate()
                .alpha(0f)
                .translationY(75f)
                .setDuration(250)
                .withEndAction {
                    view.questionText.text = question.question
                    updateAnswers(question, result, onAnswerSelected)
                    view.root.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(250)
                        .start()
                }
                .start()
        } else updateAnswers(question, result, onAnswerSelected)
    }

    private fun updateAnswers(question: TriviaQuestionEntity, result: AnswerResult, onAnswerSelected: (TriviaQuestionEntity.Answer) -> Unit) {
        val visibleAnswers = question.answers
            .zip(answers)
            .map { (answer, answerView) ->
                val resultState = when {
                    result is AnswerResult.Correct && answer.answer == result.answer -> TriviaAnswerView.State.CORRECT
                    result is AnswerResult.Incorrect && answer.isCorrect -> TriviaAnswerView.State.CORRECT
                    result is AnswerResult.Incorrect && answer.answer == result.answer -> TriviaAnswerView.State.INCORRECT
                    else -> TriviaAnswerView.State.NONE
                }
                answerView.setAnswer(answer, resultState) {
                    onAnswerSelected(answer)
                }

                answerView
            }

        answers.forEach {
            if(visibleAnswers.contains(it)) {
                if(it.parent == null) {
                    view.questionAnswers.addView(it)
                }
            }
            else {
                view.questionAnswers.removeView(it)
            }
        }
    }
}