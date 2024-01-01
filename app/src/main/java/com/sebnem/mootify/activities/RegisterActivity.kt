package com.sebnem.mootify.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.sebnem.mootify.databinding.ActivityRegisterBinding
import com.sebnem.mootify.db.User
import com.sebnem.mootify.db.User_Table

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.textViewSignIn.setOnClickListener {
            val inputUsername = binding.editTextUsername.text.toString()
            val inputPassword = binding.editTextPassword.text.toString()
            val inputTime = binding.editTextEnterTimer.text.toString()

            if (inputUsername.isEmpty()){
                Toast.makeText(this, "Lütfen kullanıcı adı giriniz.", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.isEmpty()) {
                Toast.makeText(this, "Lütfen şifre giriniz.", Toast.LENGTH_SHORT).show()
            } else if (inputTime.isEmpty()){
                Toast.makeText(this, "Lütfen geçerli bir süre giriniz.", Toast.LENGTH_SHORT).show()
            } else {
                val foundUserList = SQLite.select().from(User::class.java).where(User_Table.username.eq(inputUsername)).queryList()
                foundUserList.forEach {
                    if (inputUsername == it.username){
                        Toast.makeText(this, "Bu kullanıcı adı kullanılıyor.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                val newUser = User(
                    username = inputUsername,
                    password = inputPassword,
                    time = inputTime.toInt(),
                    remainingTime = inputTime.toInt(),
                )
                newUser.save()
                Toast.makeText(this, "Kayıt alındı. Lütfen bekleyin...", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                }, 2000)
            }
        }
    }
}
