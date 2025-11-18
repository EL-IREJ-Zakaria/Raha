package com.example.raha

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.raha.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSplashAnimation()
    }

    private fun startSplashAnimation() {
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        // Moon rising animation
        val moonAnimatorY = ObjectAnimator.ofFloat(binding.imageViewMoon, View.TRANSLATION_Y, screenHeight, 0f)
        moonAnimatorY.duration = 1500

        // App name fade-in animation
        val appNameAnimatorAlpha = ObjectAnimator.ofFloat(binding.textViewAppName, View.ALPHA, 0f, 1f)
        appNameAnimatorAlpha.duration = 1000

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(moonAnimatorY, appNameAnimatorAlpha)
        animatorSet.start()

        // Post a delayed action to move to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Delay for 2 seconds (animation duration + a bit more)
    }
}