package com.example.antonsapunov.quiz

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

    override fun onResume() {
        super.onResume()

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in

            startActivity(SettingsActivity.newIntent(this))
        } else {
            // not signed in
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .setIsSmartLockEnabled(false)
                            .build(), RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            startActivity(SettingsActivity.newIntent(this))
            finish()
            return
        }
    }
}
