package com.keremktas.socialhabit.view


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.keremktas.socialhabit.databinding.ActivityMainBinding
import com.keremktas.socialhabit.utils.startAct
import com.keremktas.socialhabit.viewmodel.MainActivityVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val currentUser = Firebase.auth.currentUser
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null
    private var signInRequest: BeginSignInRequest? = null
    private val viewmodel: MainActivityVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (currentUser!=null){
            Toast.makeText(applicationContext,currentUser.displayName, Toast.LENGTH_SHORT).show()
        }else{
            Intent().startAct(applicationContext, LoginActivity::class.java)
        }


    }


}