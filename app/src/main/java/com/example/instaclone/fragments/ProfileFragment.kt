package com.example.instaclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instaclone.R
import com.example.instaclone.SignupActivity
import com.example.instaclone.databinding.FragmentProfileBinding
import com.example.instaclone.models.User
import com.example.instaclone.utils.USER_PROFILE_FOLDER
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var bind: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentProfileBinding.inflate(inflater, container, false)
         bind.editProfileButton.setOnClickListener {
             val intent = Intent(activity,SignupActivity::class.java)
             intent.putExtra("MODE",1)
             activity?.startActivity(intent)
         }
        return bind.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_PROFILE_FOLDER).document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
              val user: User = it.toObject<User>()!!
                bind.usernameTextView.text = user.name
                bind.emailTextView.text=user.email
                if(!user.image.isNullOrEmpty()){
                Picasso.get().load(user.image).into(bind.userProfileImageView)
                }
        }
    }

}