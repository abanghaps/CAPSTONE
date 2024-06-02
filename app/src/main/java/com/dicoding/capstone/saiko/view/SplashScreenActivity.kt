package com.dicoding.capstone.saiko.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.view.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Handler to start the WelcomeActivity and close this SplashScreenActivity after some seconds.
        Handler().postDelayed({ // Create an Intent that will start the WelcomeActivity.
            val mainIntent = Intent(
                this@SplashScreenActivity,
                WelcomeActivity::class.java
            )
            this@SplashScreenActivity.startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH =
            3000 // Duration for splash screen display in milliseconds (3 seconds)
    }
}
