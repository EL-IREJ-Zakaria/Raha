package com.example.raha

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.raha.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingPagerAdapter: OnboardingPagerAdapter
    private val layouts = listOf(
        R.layout.onboarding_slide_1,
        R.layout.onboarding_slide_2,
        R.layout.onboarding_slide_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onboardingPagerAdapter = OnboardingPagerAdapter(layouts)
        binding.viewPagerOnboarding.adapter = onboardingPagerAdapter

        setupOnboardingIndicators()
        setCurrentOnboardingIndicator(0)

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position)
                if (position == layouts.lastIndex) {
                    binding.buttonNext.visibility = View.GONE
                    binding.buttonDone.visibility = View.VISIBLE
                } else {
                    binding.buttonNext.visibility = View.VISIBLE
                    binding.buttonDone.visibility = View.GONE
                }
            }
        })

        binding.buttonNext.setOnClickListener { 
            vibratePhone()
            binding.viewPagerOnboarding.currentItem = binding.viewPagerOnboarding.currentItem + 1
        }

        binding.buttonDone.setOnClickListener { 
            vibratePhone()
            launchMainActivity()
        }
    }

    private fun setupOnboardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(onboardingPagerAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = 
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.apply {
                this.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_indicator_inactive
                    )
                )
                this.layoutParams = layoutParams
            }
            binding.onboardingIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentOnboardingIndicator(index: Int) {
        val childCount = binding.onboardingIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.onboardingIndicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_indicator_inactive
                    )
                )
            }
        }
    }

    private fun launchMainActivity() {
        getSharedPreferences("onboarding_pref", Context.MODE_PRIVATE).edit().apply {
            putBoolean("is_onboarding_completed", true)
            apply()
        }
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Deprecated in API 26
            vibrator.vibrate(50)
        }
    }
}