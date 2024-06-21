package com.dicoding.capstone.saiko.view.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        val usernameField = findViewById<EditText>(R.id.username)
        val phoneNumberField = findViewById<EditText>(R.id.phone_number)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val confirmPasswordField = findViewById<EditText>(R.id.confirm_password)
        val createAccountButton = findViewById<Button>(R.id.create_account_button)
        val loginLink = findViewById<TextView>(R.id.login)

        createAccountButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val phoneNumber = phoneNumberField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(username, phoneNumber, email, password)
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(username: String, phoneNumber: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("USER_NAME", username)
                        putString("USER_PHONE", phoneNumber)
                        apply()
                    }

                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                    // Redirect to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
