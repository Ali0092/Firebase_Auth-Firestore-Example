package com.example.firebase_practice.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

const val REQUEST_SIGN_IN_CODE = 0

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth
    private lateinit var option: GoogleSignInOptions
    private lateinit var getResults: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        auth = FireBaseInstance.Auth

        if (auth.currentUser != null) {
            Toast.makeText(context, auth.currentUser!!.displayName.toString(), Toast.LENGTH_SHORT)
                .show()
        }

        getResults=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
         try {
             if (it.resultCode == RESULT_OK) {
                 val account = GoogleSignIn.getSignedInAccountFromIntent(it.data).result
                 account?.let { x->
                     googleAuthForFirebase(x)
                 }
             }
         }catch (e: NullPointerException) {
             Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
         }
        }

        binding.btnLogin.setOnClickListener {
            Login()
        }
        binding.signOut.setOnClickListener {
            val signInClient = GoogleSignIn.getClient(requireActivity(), option)
            signInClient.signOut()
            auth.signOut()
            Toast.makeText(context, "Signed Out...", Toast.LENGTH_SHORT).show()
        }

        binding.signUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.siginWithGoogle.setOnClickListener {
            SignInWithGoogle()
        }
        return binding.root
    }


    private fun SignInWithGoogle() {
        val signInClient = GoogleSignIn.getClient(requireActivity(), option)
        getResults.launch(signInClient.signInIntent!!)
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Successfully Signed In with Google..",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
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

