package com.example.antonsapunov.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner



class SettingsActivity : AppCompatActivity() {

    lateinit var startButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val categories = findViewById<View>(R.id.categories) as Spinner
        val categoriesAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categories.adapter = categoriesAdapter

        val complexities = findViewById<View>(R.id.complexity) as Spinner
        val complexityAdapter = ArrayAdapter.createFromResource(this, R.array.complexity, android.R.layout.simple_spinner_item)
        complexityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        complexities.adapter = complexityAdapter

        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            val category = categories.selectedItemPosition
            val complexity = complexities.selectedItem.toString()
            startActivity(MainActivity.newIntent(this, category, complexity))
        }
    }


}
