package com.example.instaclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import com.example.instaclone.databinding.ActivitySignupBinding
import com.example.instaclone.models.User
import com.example.instaclone.utils.USER
import com.example.instaclone.utils.USER_PROFILE_FOLDER
import com.example.instaclone.utils.Uploadimage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val bind by lazy {
            ActivitySignupBinding.inflate(layoutInflater)
        }

        // Initialize the user object
        val user = User()
        val launcher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    Uploadimage(uri, USER_PROFILE_FOLDER) {
                        if (it == null) {

                        } else {
                            user.image = it.toString()
                            bind.userproile.setImageURI(uri)
                        }
                    }
                }
            }

        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.signup.setOnClickListener() {
            if (bind.name.editText?.text.toString().isEmpty()
                || bind.email.editText?.text.toString().isEmpty()
                || bind.password.editText?.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this@SignupActivity,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    bind.email.editText?.text.toString(),
                    bind.password.editText?.text.toString(),
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            user.name = bind.name.editText?.text.toString()
                            user.password = bind.password.editText?.text.toString()
                            user.email = bind.email.editText?.text.toString()

                            // Set user data in Firestore
                            FirebaseFirestore.getInstance().collection(USER)
                                .document(currentUser.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@SignupActivity,
                                        "Registration successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // You can add further actions after a successful registration
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@SignupActivity,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            "Registration unsuccessful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        bind.userproile.setOnClickListener{
            launcher.launch("image/*")
        }
    }
}
