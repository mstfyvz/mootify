package com.sebnem.mootify.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.sebnem.mootify.R
import com.sebnem.mootify.databinding.ActivityLoginBinding
import com.sebnem.mootify.db.AppDatabase
import com.sebnem.mootify.db.LoginHistory
import com.sebnem.mootify.db.User
import com.sebnem.mootify.db.User_Table
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDatabase()

        binding.loginButton.setOnClickListener {
            val inputUsername = binding.editTextUsername.text.toString()
            val inputPassword = binding.editTextPassword.text.toString()

            if (inputUsername.isEmpty()){
                Toast.makeText(this, "Lütfen kullanıcı adı giriniz.", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.isEmpty()) {
                Toast.makeText(this, "Lütfen şifre giriniz.", Toast.LENGTH_SHORT).show()
            } else {
                val foundUser = SQLite.select().from(User::class.java).where(User_Table.username.eq(inputUsername)).querySingle()
                if (foundUser != null) {
                    if (inputPassword == foundUser.password) {
                        Toast.makeText(this, "Kullanıcı Doğru! Bekleyin...", Toast.LENGTH_SHORT).show()
                        val loginAction = LoginHistory(
                            username = foundUser.username,
                            password = foundUser.password,
                            date = getCurrentDateTime()
                        )
                        loginAction.save()
                        MainActivity.currentUser = foundUser
                        startActivity(Intent(this,MainActivity::class.java))
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

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}