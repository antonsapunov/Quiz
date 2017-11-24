package com.example.antonsapunov.quiz

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.io.Serializable
import java.util.*


class QuestionFragment : Fragment() {

    private var pageNumber: Int? = null
    private var question: Serializable? = null
    private var backColor: Int? = null
    private lateinit var firstAnswer: Button
    private lateinit var secondAnswer: Button
    private lateinit var thirdAnswer: Button
    private lateinit var fourthAnswer:Button
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments.getInt(ARGUMENT_PAGE_NUMBER)
        question = arguments.getSerializable(QUESTION) as Question

        val rnd = Random()
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_question, null)

        val questionPage = view?.findViewById<View>(R.id.questionPage) as TextView
        questionPage.text = Html.fromHtml((question as Question).question)
        Log.d("Tag", questionPage.text.toString())
        firstAnswer = view.findViewById<View>(R.id.first_answer) as Button
        secondAnswer = view.findViewById<View>(R.id.second_answer) as Button
        thirdAnswer = view.findViewById<View>(R.id.third_answer) as Button
        fourthAnswer = view.findViewById<View>(R.id.fourth_answer) as Button

        firstAnswer.text = Html.fromHtml((question as Question).answers[0])
        secondAnswer.text = Html.fromHtml((question as Question).answers[1])
        thirdAnswer.text = Html.fromHtml((question as Question).answers[2])
        fourthAnswer.text = Html.fromHtml((question as Question).answers[3])

        firstAnswer.setOnClickListener {
            check(firstAnswer)
        }
        secondAnswer.setOnClickListener {
            check(secondAnswer)
        }
        thirdAnswer.setOnClickListener {
            check(thirdAnswer)
        }
        fourthAnswer.setOnClickListener {
            check(fourthAnswer)
        }

        val questionLayout = view.findViewById<View>(R.id.questionLayout) as LinearLayout
        questionLayout.setBackgroundColor(backColor?.let { it } ?: return view)

        return view
    }

    private fun check(button: Button) {
        disableButtons()
        val green = Color.argb(100, 0, 255, 0)
        val red = Color.argb(100, 255, 0, 0)
        if (button.text == (Html.fromHtml((question as Question).correct_answer))) {
            button.setBackgroundColor(green)
            (activity as MainActivity).counter()
        } else {
            button.setBackgroundColor(red)
            when {
                firstAnswer.text == (Html.fromHtml((question as Question).correct_answer)) -> firstAnswer.setBackgroundColor(green)
                secondAnswer.text == (Html.fromHtml((question as Question).correct_answer)) -> secondAnswer.setBackgroundColor(green)
                thirdAnswer.text == (Html.fromHtml((question as Question).correct_answer)) -> thirdAnswer.setBackgroundColor(green)
                fourthAnswer.text == (Html.fromHtml((question as Question).correct_answer)) -> fourthAnswer.setBackgroundColor(green)
            }
        }
        handler = Handler()
        handler?.postDelayed({
            if (pageNumber!! < 9) {
                (activity as MainActivity).slide()
            }
        }, 500)
        (activity as MainActivity).actionCounter()
        (activity as MainActivity).end()
    }

    private fun disableButtons() {
        firstAnswer.isClickable = false
        secondAnswer.isClickable = false
        thirdAnswer.isClickable = false
        fourthAnswer.isClickable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacksAndMessages(null)
    }

    companion object {
        private var ARGUMENT_PAGE_NUMBER: String = "arg_page_number"
        private var QUESTION: String = "question"

        fun newInstance(page: Int, question: Question): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putInt(ARGUMENT_PAGE_NUMBER, page)
            args.putSerializable(QUESTION, question)
            fragment.arguments = args
            return fragment
        }
    }
}
