package com.example.weatherapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val age: Int,
    val postcode: String,
    val city: String,
    val country: String = "Malaysia" // Added the country parameter
)


