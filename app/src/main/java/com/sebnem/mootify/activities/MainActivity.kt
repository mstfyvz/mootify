package com.sebnem.mootify.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.sebnem.mootify.R
import com.sebnem.mootify.databinding.ActivityMainBinding
import com.sebnem.mootify.db.AppDatabase
import com.sebnem.mootify.db.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var skor = -1

    companion object {
        lateinit var currentUser: User
    }

    private val images = intArrayOf(
        R.drawable.img_15, R.drawable.img_16, R.drawable.img_17,
        R.drawable.img_17, R.drawable.img_18, R.drawable.img_19,
        R.drawable.img_20, R.drawable.img_21, R.drawable.img_22,
        R.drawable.img_15
    )
    private val images2 = intArrayOf(
        R.drawable.img_47, R.drawable.img_48, R.drawable.img_49,
        R.drawable.img_50, R.drawable.img_51, R.drawable.img_52,
        R.drawable.img_53, R.drawable.img_54, R.drawable.img_55,
        R.drawable.img_56, R.drawable.img_57, R.drawable.img_47
    )

    private val images3 = intArrayOf(
        R.drawable.img_47, R.drawable.img_48, R.drawable.img_49,
        R.drawable.img_50, R.drawable.img_51, R.drawable.img_52,
        R.drawable.img_53, R.drawable.img_54, R.drawable.img_55,
        R.drawable.img_56, R.drawable.img_57, R.drawable.img_55
    )

    private var currentImages: IntArray = images
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private val ANIMATION_DELAY = 170L
    private val PROGRESS_ANIMATION_DELAY = 10L

    private val imageRunnable = object : Runnable {
        override fun run() {
            try {
                binding.imageViewPlay.setImageResource(currentImages[currentIndex])
                currentIndex = (currentIndex + 1) % currentImages.size
                handler.postDelayed(this, ANIMATION_DELAY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var progressBarHalfFilled = false

    private val progressBarRunnable = object : Runnable {
        override fun run() {
            try {
                val progressBar: ProgressBar = findViewById(R.id.progressBar)

                when {
                    progressBar.progress >= progressBar.max / 2 && !progressBarHalfFilled -> {
                        // images'dan images2'ye geçiş
                        handler.removeCallbacks(imageRunnable)
                        currentImages = images2
                        currentIndex = 0
                        progressBarHalfFilled = true
                        handler.postDelayed(imageRunnable, ANIMATION_DELAY)
                    }
                    progressBar.progress == progressBar.max && currentImages == images -> {
                        // images2'den images3'e geçiş
                        handler.removeCallbacks(imageRunnable)
                        currentImages = images3
                        currentIndex = 0
                        progressBarHalfFilled = false
                        handler.postDelayed(imageRunnable, ANIMATION_DELAY)
                    }
                }

                handler.postDelayed(this, PROGRESS_ANIMATION_DELAY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuButton()
        setupCountdown()

        binding.imageViewPlay.setImageResource(images[currentIndex])
        handler.postDelayed(imageRunnable, ANIMATION_DELAY)
        binding.textViewNamed.text = currentUser.username
    }

    override fun onDestroy() {
        handler.removeCallbacks(imageRunnable)
        handler.removeCallbacks(progressBarRunnable)
        super.onDestroy()
    }

    private fun setupMenuButton() {
        var isMenuOpen = false

        binding.imageViewMenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.imageViewTick.visibility = if (isMenuOpen) View.INVISIBLE else View.VISIBLE },
                100
            )
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.imageViewMusic.visibility = if (isMenuOpen) View.INVISIBLE else View.VISIBLE },
                200
            )
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.imageViewTimer.visibility = if (isMenuOpen) View.INVISIBLE else View.VISIBLE },
                300
            )
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.imageViewEffort.visibility = if (isMenuOpen) View.INVISIBLE else View.VISIBLE },
                400
            )
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding.imageViewSettings.visibility = if (isMenuOpen) {
                        isMenuOpen = false
                        View.INVISIBLE
                    } else {
                        isMenuOpen = true
                        View.VISIBLE
                    }
                }, 500
            )
        }


        binding.imageViewTimer.setOnClickListener {
            startActivity(Intent(this@MainActivity, TimerActivity::class.java))
        }
        binding.imageViewTick.setOnClickListener {
            startActivity(Intent(this,CheckListActivity::class.java))
        }
    }

    private fun setupCountdown() {
        val timerValue = currentUser.time

        timerValue?.let {
            binding.progressBar.max = it * 60 // Saati dakikaya çeviriyoruz

            val countDownTimer = object : CountDownTimer((it * 60 * 1000).toLong(), 3600000) {
                // 60000 milisaniye (1 dakika) aralıklarla çağrılacak
                override fun onTick(millisUntilFinished: Long) {
                    val minutesLeft = millisUntilFinished / 60000
                    val progress = (it * 60 - minutesLeft).toInt()

                    binding.progressBar.progress = progress
                    binding.textViewTimer.text = "${minutesLeft+1} Hours Left"
                    skor++
                    binding.textViewScore.text = "$skor"
                }

                override fun onFinish() {
                    // Progress tamamlandığında yapılacak işlemler
                    handler.postDelayed(progressBarRunnable, PROGRESS_ANIMATION_DELAY)
                }
            }

            countDownTimer.start()
        }
    }

}