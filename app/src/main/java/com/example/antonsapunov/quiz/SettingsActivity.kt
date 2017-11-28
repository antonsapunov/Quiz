package com.example.antonsapunov.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class SettingsActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = FirebaseAuth.getInstance().currentUser?.displayName ?: "Quiz"
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

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.logout) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                    finish()
                }
    }
}
