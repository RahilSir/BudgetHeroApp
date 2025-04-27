package com.example.myapp.com.example.myapp.data.com.example.myapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "expenses")
@Parcelize
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val amount: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val category: String,
    val username: String,
    val imageUri: String? = null
) : Parcelable

