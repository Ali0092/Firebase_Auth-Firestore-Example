package com.example.firebase_practice.screens

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firebase_practice.R
import com.example.firebase_practice.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        auth = FireBaseInstance.Auth
        auth.signOut()

        binding.save.setOnClickListener {
            register()
        }
        return binding.root
    }

    private fun register() {
        val email = binding.Email.text.toString()
        val password = binding.password.text.toString()
        val name="${binding.fname.text.toString()} ${binding.Lname.text.toString()}".toString()
        val imageuri= Uri.parse("android.resources://${R.drawable.man5}")


        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    val user=auth.currentUser
                    user!!.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(imageuri)
                            .build()
                    ).await()

                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    private fun checkLoggedInState() {
        if(auth.currentUser!=null){
            Toast.makeText(
                context,
                "Registration Successful",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            Toast.makeText(
                context,
                "Registration not done yet !!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}