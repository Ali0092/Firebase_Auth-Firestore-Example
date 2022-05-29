package com.example.firebase_practice.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebase_practice.R
import com.example.firebase_practice.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        auth = FireBaseInstance.Auth

        if(auth.currentUser!=null){
        Toast.makeText(context, auth.currentUser!!.displayName.toString(),Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener {
            Login()
        }
        binding.signOut.setOnClickListener {
            auth.signOut()
        }

        binding.signUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        return binding.root
    }

    private fun Login() {
        val email = binding.Email.text.toString()
        val password = binding.password.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        } else {
            Toast.makeText(context, "Fill the Credentials First..", Toast.LENGTH_LONG).show()
        }

    }


    private fun checkLoggedInState() {
        if (auth.currentUser != null) {
            Toast.makeText(
                context,
                "Login Successful",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Login not done yet !!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

