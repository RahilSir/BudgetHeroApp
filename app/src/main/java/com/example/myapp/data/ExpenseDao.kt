package com.example.myapp.data

import androidx.room.*
import com.example.myapp.com.example.myapp.data.com.example.myapp.data.Expense
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense) // Insert  into the database



    @Query("SELECT * FROM expenses WHERE username = :username")
    suspend fun getExpensesByUsername(username: String): List<Expense>


    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: Int): Expense

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)






    @Query("SELECT * FROM expenses WHERE username = :username AND date BETWEEN :startDate AND :endDate")
    suspend fun getExpensesByDateRange(username: String, startDate: String, endDate: String): List<Expense>

   // @Query("SELECT category, SUM(CAST(amount AS REAL)) as total FROM expenses WHERE username = :username AND date BETWEEN :startDate AND :endDate GROUP BY category")
    ////suspend fun getCategoryTotalsByDate(username: String, startDate: String, endDate: String): List<CategoryTotal>


    @Query("SELECT SUM(CAST(amount AS REAL)) FROM expenses WHERE username = :username")
    suspend fun getTotalAmountByUsername(username: String): Double?


}
