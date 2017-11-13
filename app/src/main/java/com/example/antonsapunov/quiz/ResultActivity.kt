package com.example.antonsapunov.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    private lateinit var resultView: TextView
    private lateinit var tryAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultView = findViewById(R.id.resultView)
        tryAgainButton = findViewById(R.id.try_again_button)

        tryAgainButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        resultView.text = "Result: ${intent.getIntExtra(RESULT, 0) * 10}%"
    }

    companion object {
        fun newIntent(context: Context, result: Int): Intent {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(RESULT, result)
            return intent
        }

        private const val RESULT = "result"
    }
}
