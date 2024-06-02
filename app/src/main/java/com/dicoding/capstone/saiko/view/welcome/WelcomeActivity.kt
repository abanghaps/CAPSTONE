package com.dicoding.capstone.saiko.view.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.view.login.LoginActivity
import com.dicoding.capstone.saiko.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        enableEdgeToEdge()
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonCreateAccount = findViewById<Button>(R.id.buttonCreateAccount)
        buttonLogin.setOnClickListener {
            val loginIntent = Intent(
                this@WelcomeActivity,
                LoginActivity::class.java
            )
            startActivity(loginIntent)
        }
        buttonCreateAccount.setOnClickListener {
            val registerIntent = Intent(
                this@WelcomeActivity,
                RegisterActivity::class.java
            )
            startActivity(registerIntent)
        }
    }
}
