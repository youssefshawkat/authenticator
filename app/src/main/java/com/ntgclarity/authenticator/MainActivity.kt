package com.ntgclarity.authenticator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.ntgclarity.authenticator.database.UsersDatabase
import com.ntgclarity.authenticator.words.WordsActivity

class MainActivity : AppCompatActivity() {
    val kEmail = "signature"
    var database: UsersDatabase? = null
    var etEmail: EditText? = null
    var etPassword : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        database = Room.databaseBuilder(this, UsersDatabase::class.java, "users.db")
            .allowMainThreadQueries()
            .build()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById<EditText>(R.id.et_email)
        etPassword = findViewById<EditText>(R.id.et_password)

        loadUserEmail()

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegistration = findViewById<Button>(R.id.btn_register)
        val btnSettings = findViewById<Button>(R.id.btn_settings)

        btnRegistration.setOnClickListener {
            startRegistration()
        }

        btnLogin.setOnClickListener {
            val email = etEmail?.text.toString()
            val password = etPassword?.text.toString()

            verifyUser(email, password)

        }


        btnSettings.setOnClickListener {
            startSettings()
        }
    }

    private fun loadUserEmail() {
        //val shared = getSharedPreferences("user.prf", MODE_PRIVATE)
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val email = shared.getString(kEmail, null)

        etEmail?.setText(email)
    }

    private fun updateSignature(text: String) {
        val defaultPref = PreferenceManager.getDefaultSharedPreferences(this)

        defaultPref.edit()
            .putString(kEmail, text)
            .apply()
    }
    private fun verifyUser(email: String, password : String){

        val users = database?.userDao()?.getUser(email,password)


            if(users?.isNotEmpty() == true){
                Log.d("###", "Welcome Back!")
                startWords()

            }else{
                Log.d("###", "Wrong Email or Password!")

            }


    }

    fun tryFiles() {
        val filename = "hello.txt"
        val output = openFileOutput(filename, MODE_PRIVATE)

        output.write("Hello files!".toByteArray())

        val input = openFileInput(filename)
        val lines = input.bufferedReader().lineSequence()

        Log.d("###", lines.joinToString())

        val files = fileList()

        Log.d("###", files.joinToString())
    }

    private fun startRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)

        startActivity(intent)
    }

    private fun startWords() {
        val intent = Intent(this, WordsActivity::class.java)

        startActivity(intent)
    }

    private fun startSettings() {
        val intent = Intent(this, SettingsActivity::class.java)

        startActivity(intent)
    }
}