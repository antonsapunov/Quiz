package com.example.antonsapunov.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.await
import java.util.*


class MainActivity : FragmentActivity() {

    val PAGE_COUNT = 10

    private lateinit var pager: ViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var progressBar: ProgressBar

    var questionList: List<Question> = ArrayList()
    private var counter: Int = 0
    private var actionCounter: Int = 0
    private val END: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

        counter = 0
        actionCounter = 0

        pager = findViewById(R.id.question_view_pager)
        pagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        pager.setPageTransformer(true, ZoomOutSlideTransformer())
        pager.offscreenPageLimit = 10

        val category = intent.getIntExtra(CATEGORY, 0)
        val complexity = intent.getStringExtra(COMPLEXITY)

        launch(UI) {
            progressBar.visibility = ProgressBar.VISIBLE
            val result = QuestionsRepository.readQuestions(category, complexity).await()
            if (result.responseCode != 0) {
                Toast.makeText(this@MainActivity, "This test is not available now, try later", Toast.LENGTH_LONG).show()
                finish()
                return@launch
            }
            questionList = result.questions
            Log.d("log_tag", result.toString())
            progressBar.visibility = ProgressBar.INVISIBLE
            pager.adapter = pagerAdapter
        }

    }

    private inner class MyFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val answers: ArrayList<String> = ArrayList(4)
            answers.add(questionList[position].correct_answer)
            answers.add(questionList[position].incorrect_answers[0])
            answers.add(questionList[position].incorrect_answers[1])
            answers.add(questionList[position].incorrect_answers[2])
            Collections.shuffle(answers)
            questionList[position].answers = answers
            return QuestionFragment.newInstance(position, questionList[position])
        }

        override fun getCount(): Int = PAGE_COUNT

    }

    fun slide() {
        pager.currentItem += 1
    }

    fun counter() {
        counter++
    }

    fun actionCounter() {
        actionCounter++
        Log.d("action", actionCounter.toString())
    }

    fun end() {
        if (actionCounter == END) {
            startActivity(ResultActivity.newIntent(this, counter))
        }
    }

    companion object {
        fun newIntent(context: Context, category: Int, complexity: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(CATEGORY, category + 9)
            intent.putExtra(COMPLEXITY, complexity)
            return intent
        }

        const val CATEGORY = "CATEGORY"
        const val COMPLEXITY = "COMPLEXITY"
    }
}
