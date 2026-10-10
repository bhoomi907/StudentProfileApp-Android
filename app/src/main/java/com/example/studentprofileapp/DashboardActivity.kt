
package com.example.studentprofileapp

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val tvDetails = findViewById<TextView>(R.id.tvDetails)
        val btnRecords = findViewById<Button>(R.id.btnRecords)
        val btnRegisterAgain =
            findViewById<Button>(R.id.btnRegisterAgain)

        val preferences = getSharedPreferences(
            "StudentPrefs",
            MODE_PRIVATE
        )

        val name = preferences.getString("name", "") ?: ""
        val email = preferences.getString("email", "") ?: ""
        val gender = preferences.getString("gender", "") ?: ""
        val course = preferences.getString("course", "") ?: ""
        val interests = preferences.getString("interests", "") ?: ""
        val hours = preferences.getInt("hours", 0)
        val rating = preferences.getFloat("rating", 0f)

        val passedName = intent.getStringExtra("studentName") ?: name

        tvWelcome.text = "Welcome, $passedName!"

        tvDetails.text = """
            Name: $name
            Email: $email
            Gender: $gender
            Course: $course
            Interests: $interests
            Daily study hours: $hours
            Course rating: $rating / 5
        """.trimIndent()

        Toast.makeText(
            this,
            "Dashboard opened",
            Toast.LENGTH_SHORT
        ).show()

        btnRecords.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }

        btnRegisterAgain.setOnClickListener {
            Toast.makeText(
                this,
                "Enter the next student's details",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
