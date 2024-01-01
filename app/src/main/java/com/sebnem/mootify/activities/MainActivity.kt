package com.sebnem.mootify.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.sebnem.mootify.R
import com.sebnem.mootify.databinding.ActivityMainBinding
import com.sebnem.mootify.db.ScoreTable
import com.sebnem.mootify.db.ScoreTable_Table
import com.sebnem.mootify.db.User
import com.sebnem.mootify.util.DateUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var skor = -1
    private var countDownTimer: CountDownTimer? = null
    private var scoreTable: ScoreTable? = null

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
        setupScoreTable()
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

        binding.apply {
            imageViewMenu.setOnClickListener {
                val visibility = if (isMenuOpen) View.INVISIBLE else View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100)
                    binding.imageViewTick.visibility = visibility
                    delay(100)
                    binding.imageViewMusic.visibility = visibility
                    delay(100)
                    binding.imageViewTimer.visibility = visibility
                    delay(100)
                    binding.imageViewEffort.visibility = visibility
                    delay(100)
                    binding.imageViewSettings.visibility = if (isMenuOpen) {
                        isMenuOpen = false
                        View.INVISIBLE
                    } else {
                        isMenuOpen = true
                        View.VISIBLE
                    }
                }
            }

            imageViewTimer.setOnClickListener {
                startActivity(Intent(this@MainActivity, TimerActivity::class.java))
            }
            imageViewTick.setOnClickListener {
                startActivity(Intent(this@MainActivity, CheckListActivity::class.java))
            }
            imageViewEffort.setOnClickListener {
                startActivity(Intent(this@MainActivity, ScoreTableActivity::class.java))
            }
        }
    }

    private fun setupScoreTable() {
        val scoreTableList = SQLite.select().from(ScoreTable::class.java)
            .where(ScoreTable_Table.username.eq(currentUser.username.toString()))
            .queryList()

        if (scoreTableList.isEmpty()) {
            scoreTable = ScoreTable(
                username = currentUser.username,
                date = DateUtil.getCurrentDateTime(),
                score = 0
            )
            scoreTable?.save()
        }

        val lastScoreTable = scoreTableList.last()
        lastScoreTable.date?.let { lastScoreDate ->
            val dateFormat = DateUtil.getDate(lastScoreDate)
            dateFormat?.let { dateFormatted ->
                val year = DateUtil.getYear(dateFormatted)
                val month = DateUtil.getMonthNumber(dateFormatted)
                val day = DateUtil.getDay(dateFormatted)

                val currentDate = Date()
                val currentYear = DateUtil.getYear(currentDate)
                val currentMonth = DateUtil.getMonthNumber(currentDate)
                val currentDay = DateUtil.getDay(currentDate)

                if (currentYear == year && currentMonth == month && currentDay == day) {
                    scoreTable = lastScoreTable
                    return
                }

                scoreTable = ScoreTable(
                    username = currentUser.username,
                    date = DateUtil.getCurrentDateTime(),
                    score = 0
                )
                scoreTable?.save()
            }
        }

        Log.i("Skor Tablosu", scoreTable.toString())
    }

    private fun setupCountdown() {
        val time = currentUser.time
        val timerValue = currentUser.remainingTime
        scoreTable?.score?.let {
            skor = it * 60 * 60
            binding.textViewScore.text = it.toString()
        }

        time?.let {
            binding.progressBar.max = it * 60 // Saati dakikaya çeviriyoruz
        }

        timerValue?.let {
            countDownTimer = object : CountDownTimer((it * 60 * 60 * 1000).toLong(), 1000) {
                // 60000 milisaniye (1 dakika) aralıklarla çağrılacak
                override fun onTick(millisUntilFinished: Long) {
                    val hour = 60 * 60 * 1000
                    val hourLeft = millisUntilFinished / hour + 1
                    val progress = (millisUntilFinished / 60 / 1000).toInt()
                    skor++

                    binding.progressBar.progress = progress
                    binding.textViewTimer.text = "$hourLeft Hours Left"
                    if (currentUser.remainingTime != hourLeft.toInt()) {
                        val scored = skor / 60 / 60
                        binding.textViewScore.text = "$scored"
                        currentUser.remainingTime = hourLeft.toInt()
                        currentUser.update()
                        scoreTable?.score = scored
                        scoreTable?.update()

                        Log.i("SKOR", scored.toString())
                    }
                    if (hourLeft == 0L) {
                        countDownTimer?.cancel()
                    }
                }

                override fun onFinish() {
                    // Progress tamamlandığında yapılacak işlemler
                    handler.postDelayed(progressBarRunnable, PROGRESS_ANIMATION_DELAY)
                }
            }

            if (it != 0) {
                countDownTimer?.start()
            }
        }
    }

}