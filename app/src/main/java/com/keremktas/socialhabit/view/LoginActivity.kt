package com.keremktas.socialhabit.view

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.keremktas.socialhabit.databinding.ActivityLoginBinding
import com.keremktas.socialhabit.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    val auth = Firebase.auth
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null
    private var signInRequest: BeginSignInRequest? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(Utils.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(Utils.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        binding.btnOneTap.setOnClickListener {
            //displaySignIn()
            displaySignUp()
        }

    }

    private fun displaySignIn() {
        oneTapClient?.beginSignIn(signInRequest!!)
            ?.addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.e("btn click", e.localizedMessage!!)
            }
    }

    private fun displaySignUp() {
        oneTapClient?.beginSignIn(signUpRequest!!)
            ?.addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.e("btn click", e.localizedMessage!!)
            }
    }

    private val oneTapResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
                val idToken = credential?.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        val msg = "idToken: $idToken"
                        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).show()

                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                        Firebase.auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(StateSet.TAG, "signInWithCredential:success")
                                    val user = auth.currentUser
                                    Log.e("****111", user!!.email.toString())
                                    // updateUI(user)
                                    val intent = Intent(applicationContext,CreateUserActivity::class.java)
                                    startActivity(intent)
                                    //TODO
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(StateSet.TAG, "signInWithCredential:failure", task.exception)
                                    //updateUI(null)
                                }
                            }
                        Log.e("one tap", msg)
                    }
                    else -> {
                        // Shouldn't happen.
                        Log.e("one tap", "No ID token!")
                        Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE)
                            .show()
                    }
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.e("one tap", "One-tap dialog was closed.")
                        // Don't re-prompt the user.
                        Snackbar.make(
                            binding.root,
                            "One-tap dialog was closed.",
                            Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }
                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.e("one tap", "One-tap encountered a network error.")
                        // Try again or just ignore.
                        Snackbar.make(
                            binding.root,
                            "One-tap encountered a network error.",
                            Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }
                    else -> {
                        Log.e(
                            "one tap", "Couldn't get credential from result." +
                                    " (${e.localizedMessage})"
                        )
                        Snackbar.make(
                            binding.root, "Couldn't get credential from result.\" +\n" +
                                    " (${e.localizedMessage})", Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }
                }
            }
  }
}