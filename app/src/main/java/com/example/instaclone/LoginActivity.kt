package com.example.instaclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instaclone.databinding.ActivityLoginBinding
import com.example.instaclone.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val bind by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        val auth = FirebaseAuth.getInstance()

        bind.login.setOnClickListener {
            val username = bind.usernameEditText.text.toString()
            val password = bind.passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Enter details",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val user = User(username, password)

                if (user.email.isNullOrBlank() || user.password.isNullOrBlank()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Authentication failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}
