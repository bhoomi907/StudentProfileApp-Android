
package com.example.studentprofileapp

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        preferences = getSharedPreferences("StudentPrefs", MODE_PRIVATE)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val spCourse = findViewById<Spinner>(R.id.spCourse)
        val cbDrawing = findViewById<CheckBox>(R.id.cbDrawing)
        val cbMusic = findViewById<CheckBox>(R.id.cbMusic)
        val cbSports = findViewById<CheckBox>(R.id.cbSports)
        val seekStudy = findViewById<SeekBar>(R.id.seekStudy)
        val tvHours = findViewById<TextView>(R.id.tvHours)
        val ratingCourse = findViewById<RatingBar>(R.id.ratingCourse)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val courses = arrayOf("BCA", "BBA", "B.Tech", "B.Sc")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            courses
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spCourse.adapter = adapter

        seekStudy.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvHours.text = "$progress hours"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter name and email",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email"
                return@setOnClickListener
            }

            val selectedId = rgGender.checkedRadioButtonId
            val gender = findViewById<RadioButton>(
                selectedId
            ).text.toString()

            val course = spCourse.selectedItem.toString()

            val interestsList = ArrayList<String>()
            if (cbDrawing.isChecked) interestsList.add("Drawing")
            if (cbMusic.isChecked) interestsList.add("Music")
            if (cbSports.isChecked) interestsList.add("Sports")

            val interests = if (interestsList.isEmpty()) {
                "None"
            } else {
                interestsList.joinToString(", ")
            }

            val hours = seekStudy.progress
            val rating = ratingCourse.rating

            val result = dbHelper.insertStudent(
                name, email, gender, course,
                interests, hours, rating
            )

            if (result == -1L) {
                Toast.makeText(
                    this,
                    "Could not save student",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            preferences.edit()
                .putString("name", name)
                .putString("email", email)
                .putString("gender", gender)
                .putString("course", course)
                .putString("interests", interests)
                .putInt("hours", hours)
                .putFloat("rating", rating)
                .putBoolean("registered", true)
                .apply()

            Toast.makeText(
                this,
                "Registration saved successfully!",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("studentName", name)
            startActivity(intent)
        }
    }
}
