package com.example.firebase_practice.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebase_practice.R
import com.example.firebase_practice.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        auth.signOut()

        binding.btnRegister.setOnClickListener {
            register()
            this.findNavController().navigate(R.id.action_loginFragment_to_apptListFragment)
        }
        binding.btnLogin.setOnClickListener {
            Login()
            this.findNavController().navigate(R.id.action_loginFragment_to_apptListFragment)
        }

        return binding.root
    }

    private fun register() {
        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                    withContext(Dispatchers.Main) {
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

    private fun Login() {
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password)
                    withContext(Dispatchers.Main) {
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

    @SuppressLint("SetTextI18n")
    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "Your are not LoggedIn", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Your are LoggedIn", Toast.LENGTH_LONG).show()
        }
    }
}

