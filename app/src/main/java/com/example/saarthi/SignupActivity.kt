package com.example.saarthi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.saarthi.Models.User
import com.example.saarthi.databinding.ActivitySignupBinding
import com.example.saarthi.utils.USER_MODE
import com.example.saarthi.utils.USER_PROFILE_FOLDER
import com.example.saarthi.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class SignupActivity : AppCompatActivity() {
    val binding by lazy {
         ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user: User

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it == null) {

                } else {
                    user.image = it
                    binding.profileImage.setImageURI(uri)
                }

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        user = User()
        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){
                binding.signUpBtn.text="Update Profile"
                Firebase.firestore.collection(USER_MODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user = it.toObject<com.example.saarthi.Models.User>()!!
                        if(!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImage)

                        }
                        binding.name?.setText(user.name ?: "")
                        binding.email?.setText(user.email ?: "")
                        binding.password?.setText(user.password ?: "")

                    }
            }
        }
//       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//          val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//        insets
//     }
        //ADD  on image
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }




        binding.signUpBtn.setOnClickListener {
            if(intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1) {
                    Firebase.firestore.collection("User")
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignupActivity,HomeActivity::class.java))
                            finish()
                        }

                }
            }
            else{

            }



            if (binding.name.text?.toString().equals("") ||
                binding.email.text?.toString().equals("") ||
                binding.password.text?.toString().equals("")
            ) {
                Toast.makeText(
                    this@SignupActivity,
                    "Please fill all information",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener { result ->

                    if (result.isSuccessful) {
                        user.name = binding.name.text?.toString()
                        user.password = binding.password.text?.toString()
                        user.email =  binding.email.text?.toString()
                        Firebase.firestore.collection("User")
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                startActivity(Intent(this@SignupActivity,HomeActivity::class.java))
                                finish()
                            }

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            result.exception?.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }

        binding.login.setOnClickListener {
            startActivity(Intent(this@SignupActivity,loginActivity::class.java))
            finish()
        }

    }
}
