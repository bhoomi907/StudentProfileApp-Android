
package com.example.studentprofileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Student(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val course: String,
    val interests: String,
    val hours: Int,
    val rating: Float
)

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "StudentDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT,
                gender TEXT,
                course TEXT,
                interests TEXT,
                hours INTEGER,
                rating REAL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    fun insertStudent(
        name: String,
        email: String,
        gender: String,
        course: String,
        interests: String,
        hours: Int,
        rating: Float
    ): Long {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", name)
        values.put("email", email)
        values.put("gender", gender)
        values.put("course", course)
        values.put("interests", interests)
        values.put("hours", hours)
        values.put("rating", rating)

        return db.insert("students", null, values)
    }

    fun getAllStudents(): ArrayList<Student> {
        val students = ArrayList<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM students ORDER BY id DESC",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                students.add(
                    Student(
                        it.getInt(it.getColumnIndexOrThrow("id")),
                        it.getString(it.getColumnIndexOrThrow("name")),
                        it.getString(it.getColumnIndexOrThrow("email")),
                        it.getString(it.getColumnIndexOrThrow("gender")),
                        it.getString(it.getColumnIndexOrThrow("course")),
                        it.getString(it.getColumnIndexOrThrow("interests")),
                        it.getInt(it.getColumnIndexOrThrow("hours")),
                        it.getFloat(it.getColumnIndexOrThrow("rating"))
                    )
                )
            }
        }

        return students
    }

    fun deleteStudent(id: Int): Int {
        return writableDatabase.delete(
            "students",
            "id = ?",
            arrayOf(id.toString())
        )
    }
}
