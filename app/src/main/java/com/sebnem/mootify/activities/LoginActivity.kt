package com.sebnem.mootify.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.sebnem.mootify.databinding.ActivityLoginBinding
import com.sebnem.mootify.db.AppDatabase
import com.sebnem.mootify.db.LoginHistory
import com.sebnem.mootify.db.LoginHistory_Table
import com.sebnem.mootify.db.User
import com.sebnem.mootify.db.User_Table
import com.sebnem.mootify.util.DateUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDatabase()

        binding.loginButton.setOnClickListener {
            val inputUsername = binding.editTextUsername.text.toString()
            val inputPassword = binding.editTextPassword.text.toString()

            if (inputUsername.isEmpty()) {
                Toast.makeText(this, "Lütfen kullanıcı adı giriniz.", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.isEmpty()) {
                Toast.makeText(this, "Lütfen şifre giriniz.", Toast.LENGTH_SHORT).show()
            } else {
                val foundUser = SQLite.select()
                    .from(User::class.java)
                    .where(User_Table.username.eq(inputUsername))
                    .querySingle()
                if (foundUser != null) {
                    if (inputPassword == foundUser.password) {
                        Toast.makeText(this, "Kullanıcı Doğru! Bekleyin...", Toast.LENGTH_SHORT).show()
                        updateRemainingTime(foundUser)
                        val loginAction = LoginHistory(
                            username = foundUser.username,
                            password = foundUser.password,
                            date = DateUtil.getCurrentDateTime()
                        )
                        loginAction.save()
                        Log.i("LoginHistory", loginAction.toString())
                        Log.i("User", foundUser.toString())
                        MainActivity.currentUser = foundUser
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Yanlış şifre! Tekrar deneyin.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Böyle bir kullanıcı adı buluanamadı.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.textViewSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun updateRemainingTime(foundUser: User) {
        val loginHistory = SQLite.select().from(LoginHistory::class.java)
            .where(LoginHistory_Table.username.eq(binding.editTextUsername.text.toString()))
            .queryList()
        try {
            loginHistory.last()?.date?.let { lastLoginDate ->
                val dateFormat = DateUtil.getDate(lastLoginDate)
                dateFormat?.let { dateFormatted ->
                    val year = DateUtil.getYear(dateFormatted)
                    val month = DateUtil.getMonthNumber(dateFormatted)
                    val day = DateUtil.getDay(dateFormatted)

                    val currentDate = Date()
                    val currentYear = DateUtil.getYear(currentDate)
                    val currentMonth = DateUtil.getMonthNumber(currentDate)
                    val currentDay = DateUtil.getDay(currentDate)

                    if (currentYear != year) {
                        updateRemainingColumn(foundUser)
                        return
                    }
                    if (currentMonth != month) {
                        updateRemainingColumn(foundUser)
                        return
                    }
                    if (currentDay > day) {
                        updateRemainingColumn(foundUser)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateRemainingColumn(foundUser: User) {
        CoroutineScope(Dispatchers.Main).launch {
            SQLite.update(User::class.java)
                .set(User_Table.remainingTime.eq(foundUser.time))
                .where(User_Table.username.eq(foundUser.username))
                .async()
                .execute()
        }
    }

    private fun initDatabase() {
        FlowManager.init(
            FlowConfig.Builder(this)
                .addDatabaseConfig(
                    DatabaseConfig.Builder(AppDatabase::class.java)
                        .databaseName("MyDatabase")
                        .build()
                )
                .openDatabasesOnInit(true)
                .build()
        )
    }

}