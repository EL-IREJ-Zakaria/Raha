package com.example.raha.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Raha.db"
        private const val TABLE_USERS = "users"

        private const val KEY_ID = "id"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NOM = "nom"
        private const val KEY_AGE = "age"
        private const val KEY_POIDS = "poids"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_NOM + " TEXT,"
                + KEY_AGE + " INTEGER,"
                + KEY_POIDS + " REAL" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_EMAIL, user.email)
            put(KEY_PASSWORD, user.motDePasse)
            put(KEY_NOM, user.nom)
            put(KEY_AGE, user.age)
            put(KEY_POIDS, user.poids)
        }
        val success = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return success
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(KEY_ID, KEY_EMAIL, KEY_PASSWORD, KEY_NOM, KEY_AGE, KEY_POIDS),
            "$KEY_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                motDePasse = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)),
                nom = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_AGE)),
                poids = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_POIDS))
            )
        }
        cursor.close()
        db.close()
        return user
    }
}