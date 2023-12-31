package com.sebnem.mootify.activities

import android.app.TimePickerDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.sebnem.mootify.R
import com.sebnem.mootify.databinding.ActivityTimerBinding
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var countDownTimer: CountDownTimer
    private var selectedMillis: Long = 0

    private var currentIndex: Int = 0
    private lateinit var handler: Handler
    private var isTimerRunning = false
    private var elapsedTime = 0L
    private var timerHandler: Handler = Handler()
    private var animationHandler: Handler = Handler()

    private val images = intArrayOf(
        R.drawable.img_59, R.drawable.img_60, R.drawable.img_61,
        R.drawable.img_62, R.drawable.img_63
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeMediaPlayer()
        startMusic()
        binding.apply {

            imageViewOpenMusic.setOnClickListener {
                pauseMusic()
            }

            imageViewCloseMusic.setOnClickListener {
                startMusic()
            }

            imageViewStartTimer.setOnClickListener {
                startAnimation()
                showTimePickerDialog()
            }

            imageViewPauseTimer.setOnClickListener {
                stopAnimation()
                pauseTimer()
            }

            imageViewResetTimer.setOnClickListener {
                resetTimer()
            }

            imageViewClose.setOnClickListener {
                finish()
            }
        }
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker?, hourOfDay: Int, minute: Int ->
                val selectedMillisInFuture =
                    TimeUnit.HOURS.toMillis(hourOfDay.toLong()) +
                            TimeUnit.MINUTES.toMillis(minute.toLong())
                setTimer(selectedMillisInFuture)
            },
            0,
            0,
            true
        )

        timePickerDialog.show()
    }

    private fun setTimer(millisInFuture: Long) {
        selectedMillis = millisInFuture
        updateTimerText(selectedMillis)
        startCountDownTimer(selectedMillis)
    }

    private fun startCountDownTimer(millisInFuture: Long) {
        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                resetTimer()
            }
        }

        countDownTimer.start()
        startAnimation()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.odak)
        mediaPlayer.isLooping = true
    }

    private fun startMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            binding.imageViewOpenMusic.visibility = View.VISIBLE
            binding.imageViewCloseMusic.visibility = View.INVISIBLE
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.imageViewOpenMusic.visibility = View.INVISIBLE
            binding.imageViewCloseMusic.visibility = View.VISIBLE
        }
    }

    private fun startAnimation() {
        handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (currentIndex < images.size) {
                    binding.imageViewAnimate.setImageResource(images[currentIndex])
                    currentIndex++
                    handler.postDelayed(this, 300)
                } else {
                    currentIndex = 0
                    handler.postDelayed(this, 300)
                }
            }
        })
    }

    private fun stopAnimation() {
        handler.removeCallbacksAndMessages(null)
        currentIndex = 0
    }

    private fun pauseTimer() {
        isTimerRunning = false
        countDownTimer.cancel()
        timerHandler.removeCallbacksAndMessages(null)
    }

    private fun resetTimer() {
        isTimerRunning = false
        elapsedTime = 0
        countDownTimer.cancel()
        timerHandler.removeCallbacksAndMessages(null)
        updateTimerText(0)
        stopAnimation()
    }

    private fun updateTimerText(millisUntilFinished: Long) {
        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

        val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        binding.textViewTimer.text = timeText
    }

    override fun onDestroy() {
        timerHandler.removeCallbacksAndMessages(null)
        animationHandler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
        super.onDestroy()
    }
}