package com.example.saarthi

import android.animation.*
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity: AppCompatActivity() {


    private lateinit var handImage: ImageView
    private lateinit var saarthiText: TextView
    private val handImages = listOf(R.drawable.hand_sign1, R.drawable.hand_sign2, R.drawable.hand_sign3) // Add your images here
    private var currentImageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        handImage = findViewById(R.id.handImage)
        saarthiText = findViewById(R.id.saarthiText)

        startHandAnimation()


        Handler(Looper.getMainLooper()).postDelayed({
           if(FirebaseAuth.getInstance().currentUser==null){
                startActivity(Intent(this,SignupActivity::class.java))
            }
            else{
                startActivity(Intent(this,HomeActivity::class.java))
               finish()
            }
        },3000)
    }

    private fun startHandAnimation() {
        // Animate hand rotation
        val rotateAnimator = ObjectAnimator.ofFloat(handImage, "rotationY", 0f, 360f)
        rotateAnimator.duration = 3000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE

        // Change the hand gestures in sequence
        val imageChangeAnimator = ValueAnimator.ofInt(0, handImages.size - 1)
        imageChangeAnimator.duration = 1000
        imageChangeAnimator.repeatCount = ValueAnimator.INFINITE
        imageChangeAnimator.addUpdateListener {
            currentImageIndex = it.animatedValue as Int
            handImage.setImageResource(handImages[currentImageIndex])
        }

        // Start the text animation after a delay
        val textAnimator = ObjectAnimator.ofFloat(saarthiText, "alpha", 0f, 1f)
        textAnimator.duration = 1000
        textAnimator.startDelay = 1500 // Delay to ensure it's synced with the hand animation

        // Animate the text's translation (moving down smoothly)
        val textTranslation = ObjectAnimator.ofFloat(saarthiText, "translationY", -100f, 0f)
        textTranslation.duration = 1000
        textTranslation.startDelay = 1500

        // Start animations together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rotateAnimator, imageChangeAnimator, textAnimator, textTranslation)
        animatorSet.start()
    }
}
