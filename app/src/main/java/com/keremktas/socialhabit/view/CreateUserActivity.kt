package com.keremktas.socialhabit.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.keremktas.socialhabit.databinding.ActivityCreateUserBinding
import com.keremktas.socialhabit.model.User
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    val currentUser = Firebase.auth.currentUser
    private var _binding: ActivityCreateUserBinding? = null
    private val binding get() = _binding!!

    var db = Firebase.firestore
    var isSameUsernameFind: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {

        _binding = ActivityCreateUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var userId = UUID.randomUUID()

        if (currentUser == null) {
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btSave.setOnClickListener {
            val etUsername = binding.etUsername.text.toString().lowercase()
            val user = User(
                userId.toString(),
                binding.etName.text.toString(),
                etUsername,
                currentUser!!.email.toString(),
                ""
            )
            //Yeni Kod
            db.collection("usernames").whereEqualTo("username", etUsername).get()
                .addOnSuccessListener { task ->
                    Log.e("109", task.size().toString())
                    if (task.size() == 0) {
                        val usernameMap = HashMap<String, String>()
                        usernameMap["username"] = etUsername
                        db.collection("usernames").document(UUID.randomUUID().toString())
                            .set(usernameMap)
                        Log.e("***111", "User saved")
                        db.collection("users").document(UUID.randomUUID().toString()).set(user)
                            .addOnSuccessListener {
                                val intent2 = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent2)
                            }.addOnFailureListener { e ->
                                e.printStackTrace()
                            }

                    } else {
                        Log.e("***116", "User kayıtlı")
                        Toast.makeText(applicationContext, "User kayıtlı", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }
}