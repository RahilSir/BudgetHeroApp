package com.example.myapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query



@Dao
interface GoalDao {

    @Insert
    suspend fun insertGoal(goal: Goal)

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals(): List<Goal>

    @Query("SELECT * FROM goals ORDER BY id DESC LIMIT 1")
    suspend fun getLatestGoal(): Goal?

}
