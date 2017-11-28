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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.*


class QuestionFragment : Fragment() {

    private var pageNumber: Int? = null
    private var question: Question? = null
    private var backColor: Int? = null
    private lateinit var firstAnswer: Button
    private lateinit var secondAnswer: Button
    private lateinit var thirdAnswer: Button
    private lateinit var fourthAnswer: Button
    private var handler: Handler? = null

    private lateinit var mAdView : AdView

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

        mAdView = view!!.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d("tag", "load")// Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                Log.d("tag", "failToLoad $errorCode")// Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {

                Log.d("tag", "open")// Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                Log.d("tag", "load1")
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                Log.d("tag", "close")
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        val questionPage = view?.findViewById<View>(R.id.questionPage) as TextView
        questionPage.text = Html.fromHtml((question as Question).question)
        Log.d("Tag", questionPage.text.toString())
        firstAnswer = view.findViewById<View>(R.id.first_answer) as Button
        secondAnswer = view.findViewById<View>(R.id.second_answer) as Button
        thirdAnswer = view.findViewById<View>(R.id.third_answer) as Button
        fourthAnswer = view.findViewById<View>(R.id.fourth_answer) as Button

        question?.let { firstAnswer.text = Html.fromHtml(it.answers[0]) }
        question?.let { secondAnswer.text = Html.fromHtml(it.answers[1]) }
        question?.let { thirdAnswer.text = Html.fromHtml(it.answers[2]) }
        question?.let { fourthAnswer.text = Html.fromHtml(it.answers[3]) }

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

        if (button.text == (Html.fromHtml((question?.correct_answer ?: "")))) {
            button.setBackgroundColor(green)
            (activity as MainActivity).counter()
        } else {
            button.setBackgroundColor(red)
            question?.let {
                when {
                    firstAnswer.text == (Html.fromHtml(it.correct_answer)) -> firstAnswer.setBackgroundColor(green)
                    secondAnswer.text == (Html.fromHtml(it.correct_answer)) -> secondAnswer.setBackgroundColor(green)
                    thirdAnswer.text == (Html.fromHtml(it.correct_answer)) -> thirdAnswer.setBackgroundColor(green)
                    fourthAnswer.text == (Html.fromHtml(it.correct_answer)) -> fourthAnswer.setBackgroundColor(green)
                }
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
