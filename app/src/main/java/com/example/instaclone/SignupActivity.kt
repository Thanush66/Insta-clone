package com.example.instaclone


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instaclone.databinding.ActivitySignupBinding
import com.example.instaclone.models.User
import com.example.instaclone.utils.USER
import com.example.instaclone.utils.USER_PROFILE_FOLDER
import com.example.instaclone.utils.Uploadimage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val bind by lazy {
            ActivitySignupBinding.inflate(layoutInflater)
        }

        // Initialize the user object
        var user = User()
        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 1) {
                bind.signup.text = "Update profile "
                Firebase.firestore.collection(USER).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {

                       user =  it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(bind.userproile)
                        }
                    }

            }
        }
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
        val text =
            "<font color=#cc0029>Already Have an account? </font> <font color=#1E88E5>Login</font>"
        bind.Login.setText(Html.fromHtml(text))
        bind.signup.setOnClickListener() {
            if( intent.hasExtra("MODE")){
                Firebase.firestore.collection(USER)
                    .document(Firebase.auth.currentUser!!.uid).set(user)
                    .addOnSuccessListener {
                        startActivity(
                            Intent(
                                this@SignupActivity,
                                HomeActivity::class.java
                            )
                        )
                        finish()
                    }
            }
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
                                    startActivity(
                                        Intent(
                                            this@SignupActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
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
        bind.userproile.setOnClickListener {
            launcher.launch("image/*")
        }
        bind.Login.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }
}
