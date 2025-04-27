package com.example.myapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    val minGoal: Float,
    val maxGoal: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

