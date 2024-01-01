package com.sebnem.mootify.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.sebnem.mootify.databinding.ActivityScoreTableBinding
import com.sebnem.mootify.db.ScoreTable
import com.sebnem.mootify.db.ScoreTable_Table
import com.sebnem.mootify.util.DateUtil

class ScoreTableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreTableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scoreTableList = SQLite.select().from(ScoreTable::class.java)
            .where(ScoreTable_Table.username.eq(MainActivity.currentUser.username.toString()))
            .queryList()

        if (scoreTableList.isEmpty()) {
            binding.apply {
                mondayValue.text = "0"
                tuesdayValue.text = "0"
                wednesdayValue.text = "0"
                thursdayValue.text = "0"
                fridayValue.text = "0"
            }
            return
        }

        Log.i("ScoreTable", scoreTableList.toString())

        try {
            val findMondayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Monday" }
            binding.mondayValue.text = findMondayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.mondayValue.text = "0"
        }

        try {
            val findTuesdayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Tuesday" }
            binding.tuesdayValue.text = findTuesdayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.tuesdayValue.text = "0"
        }

        try {
            val findWednesdayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Wednesday" }
            binding.wednesdayValue.text = findWednesdayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.wednesdayValue.text = "0"
        }

        try {
            val findThursdayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Thursday" }
            binding.thursdayValue.text = findThursdayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.thursdayValue.text = "0"
        }

        try {
            val findFridayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Friday" }
            binding.fridayValue.text = findFridayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.fridayValue.text = "0"
        }

        try {
            val findSaturdayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Saturday" }
            binding.saturdayValue.text = findSaturdayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.saturdayValue.text = "0"
        }

        try {
            val findSundayScoreTable = scoreTableList.last { getDayOfWeek(it.date) == "Sunday" }
            binding.sundayValue.text = findSundayScoreTable?.let { it.score.toString() } ?: kotlin.run { "0" }
        } catch (e: Exception) {
            binding.sundayValue.text = "0"
        }

    }

    private fun getDayOfWeek(date: String?) : String {
        return date?.let {
            val dateFormat = DateUtil.getDate(it)
            dateFormat?.let { dateFormatted ->
                DateUtil.getDayOfTheWeek(dateFormatted)
            } ?: kotlin.run {
                "Monday"
            }
        } ?: kotlin.run {
            "Monday"
        }
    }
}