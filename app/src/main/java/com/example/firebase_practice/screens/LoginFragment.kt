package com.example.firebase_practice.screens

import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val REQUEST_SIGN_IN_CODE=0

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
            Toast.makeText(context, "Signed Out ", Toast.LENGTH_LONG).show()

        }

        binding.signUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.siginWithGoogle.setOnClickListener {
            val option=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
            val signInClient=GoogleSignIn.getClient(requireActivity(), option)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_SIGN_IN_CODE)
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== REQUEST_SIGN_IN_CODE){
            val account=GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }

        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
      val credentials=GoogleAuthProvider.getCredential(account.idToken,null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Successfully Signed In with Google..", Toast.LENGTH_LONG).show()
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
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

