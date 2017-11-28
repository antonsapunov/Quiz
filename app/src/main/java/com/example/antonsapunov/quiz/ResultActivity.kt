package com.example.antonsapunov.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ResultActivity : AppCompatActivity() {

    private lateinit var resultView: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ResultAdapter
    private lateinit var tryAgainButton: Button
    private lateinit var mProgressBar: ProgressBar

    private lateinit var rootRef: DatabaseReference
    private lateinit var topRef: DatabaseReference
    private val resultArray: MutableList<Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.title = FirebaseAuth.getInstance().currentUser?.displayName ?: "Quiz"

        mAdapter = ResultAdapter()
        mRecyclerView = findViewById(R.id.results_table)
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = layoutManager

        resultView = findViewById(R.id.resultView)
        tryAgainButton = findViewById(R.id.try_again_button)
        mProgressBar = findViewById(R.id.progress_bar)

        tryAgainButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        val result = intent.getIntExtra(RESULT, 0) * 10
        resultView.text = "Your Result: $result%"

        rootRef = FirebaseDatabase.getInstance().reference
        topRef = rootRef.child("top")

        topRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                resultArray.clear()
                dataSnapshot.children.mapNotNullTo(resultArray) { it.getValue<Result>(Result::class.java) }
                resultArray.sortBy { it.result }
                resultArray.reverse()
                val top = resultArray.take(10)
                mAdapter.setResults(top.toMutableList())
                mRecyclerView.adapter = mAdapter
                mProgressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        val ref = topRef.push()
        val key = ref.key

        val map = HashMap<String, Any>()
        map.put("person", FirebaseAuth.getInstance().currentUser?.displayName ?: "Person")
        map.put("result", result)
        map.put("uuid", key)

        ref.setValue(map)
        mProgressBar.visibility = View.VISIBLE

    }

    companion object {
        fun newIntent(context: Context, result: Int): Intent {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(RESULT, result)
            return intent
        }

        private const val RESULT = "result"
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
                    startActivity(Intent(this@ResultActivity, LoginActivity::class.java))
                    finish()
                }
    }
}
