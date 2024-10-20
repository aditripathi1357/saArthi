package com.example.saarthi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.saarthi.Models.User
import com.example.saarthi.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class loginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.signUp.setOnClickListener {
            startActivity(Intent(this@loginActivity,SignupActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            if(binding.email.text?.toString().equals("")or
                binding.pass.text?.toString().equals("")){
                Toast.makeText(this@loginActivity,"Please fill all details", Toast.LENGTH_LONG).show()
            }
            else{
                var user =  User(binding.email.text?.toString(),
                    binding.pass.text?.toString())

                Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            startActivity(Intent(this@loginActivity,HomeActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this@loginActivity,it.exception?.localizedMessage, Toast.LENGTH_LONG).show()

                        }
                    }

            }
        }
    }
}