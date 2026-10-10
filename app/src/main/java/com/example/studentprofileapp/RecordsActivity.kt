
package com.example.studentprofileapp

import android.os.Bundle
import android.content.Intent
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RecordsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recordsContainer: LinearLayout
    private lateinit var tvRecordCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        dbHelper = DBHelper(this)

        recordsContainer =
            findViewById(R.id.recordsContainer)

        tvRecordCount =
            findViewById(R.id.tvRecordCount)

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        displayRecords()
    }

    private fun displayRecords() {
        recordsContainer.removeAllViews()

        val students = dbHelper.getAllStudents()

        tvRecordCount.text = "Total records: ${students.size}"

        if (students.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = "No student records saved yet."
            emptyText.textSize = 16f
            emptyText.gravity = Gravity.CENTER
            emptyText.setPadding(8, 24, 8, 24)
            recordsContainer.addView(emptyText)
            return
        }

        for (student in students) {
            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(16, 12, 16, 12)

            val details = TextView(this)
            details.text = """
                ID: ${student.id}
                Name: ${student.name}
                Email: ${student.email}
                Gender: ${student.gender}
                Course: ${student.course}
                Interests: ${student.interests}
                Study hours: ${student.hours}
                Rating: ${student.rating}/5
            """.trimIndent()
            details.textSize = 16f

            val btnDelete = Button(this)
            btnDelete.text = "Delete Record"

            btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete Record")
                    .setMessage(
                        "Delete the record of ${student.name}?"
                    )
                    .setPositiveButton("Yes") { _, _ ->
                        val result =
                            dbHelper.deleteStudent(student.id)

                        if (result > 0) {
                            Toast.makeText(
                                this,
                                "Record deleted",
                                Toast.LENGTH_SHORT
                            ).show()

                            displayRecords()
                        } else {
                            Toast.makeText(
                                this,
                                "Could not delete record",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }

            card.addView(details)
            card.addView(btnDelete)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 16

            recordsContainer.addView(card, params)
        }
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized &&
            ::recordsContainer.isInitialized
        ) {
            displayRecords()
        }
    }
}
