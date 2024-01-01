package com.sebnem.mootify.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val dayOfTheWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // Thursday
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault()) // 20
    val monthNumberFormat = SimpleDateFormat("MM", Locale.getDefault()) // 06
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault()) // 2023

    fun getDate(date: String): Date? {
        try {
            return dateFormat.parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getParsedDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun getCurrentDateTime(): String {
        val currentDate = Date()
        return getParsedDate(currentDate)
    }

    fun getDayOfTheWeek(date: Date): String {
        return dayOfTheWeekFormat.format(date)
    }

    fun getDay(date: Date): String {
        return dayFormat.format(date)
    }

    fun getMonthNumber(date: Date): String {
        return monthNumberFormat.format(date)
    }

    fun getYear(date: Date): String {
        return yearFormat.format(date)
    }
}