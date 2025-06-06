package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.com.example.myapp.data.com.example.myapp.data.Expense

import android.net.Uri

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.myapp.data.Goal


import com.example.myapp.data.ExpenseDao
import com.example.myapp.data.AppDatabase
import com.example.myapp.data.GoalDao
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min


class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var etStartTime: EditText
    private lateinit var etEndTime: EditText
    private lateinit var etDescription: EditText
    private lateinit var etCategory: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnShowExpenses: Button
    private lateinit var tvCategoryTotals: TextView
    private lateinit var tvExpenses: TextView

    private lateinit var etMinGoal: EditText
    private lateinit var etMaxGoal: EditText
    private lateinit var btnSaveGoals: Button

    private lateinit var expenseDao: ExpenseDao
    private lateinit var goalDao: GoalDao
    private lateinit var loggedInUsername: String



    private lateinit var btnSelectImage: Button
    private lateinit var ivSelectedImage: ImageView
    private var selectedImageUri: Uri? = null


    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgressInfo: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loggedInUsername = intent.getStringExtra("USERNAME") ?: ""
        // Initialize the UI elements
        etName = findViewById(R.id.etName)
        etAmount = findViewById(R.id.etAmount)
        etDate = findViewById(R.id.etDate)
        etStartTime = findViewById(R.id.etStartTime)
        etEndTime = findViewById(R.id.etEndTime)
        etDescription = findViewById(R.id.etDescription)
        etCategory = findViewById(R.id.etCategory)
        btnAdd = findViewById(R.id.btnAdd)

        btnShowExpenses = findViewById(R.id.btnShowExpenses)
        tvCategoryTotals = findViewById(R.id.tvCategoryTotals)
        tvExpenses = findViewById(R.id.tvExpenses)

        // Goal UI
        etMinGoal = findViewById(R.id.etMinGoal)
        etMaxGoal = findViewById(R.id.etMaxGoal)
        btnSaveGoals = findViewById(R.id.btnSaveGoals)

// progess bar
        progressBar = findViewById(R.id.progressBar)
        tvProgressInfo = findViewById(R.id.tvProgressInfo)





        //  Room Database
        val db = AppDatabase.getDatabase(applicationContext)
        expenseDao = db.expenseDao()
        goalDao = db.goalDao()

        // Save goals button
        btnSaveGoals.setOnClickListener {
            val minGoal = etMinGoal.text.toString().toFloatOrNull()
            val maxGoal = etMaxGoal.text.toString().toFloatOrNull()

            if (minGoal != null && maxGoal != null && minGoal <= maxGoal) {
                // Save goals to Room database
                lifecycleScope.launch(Dispatchers.IO) {
                    val goal = Goal(minGoal, maxGoal)
                    goalDao.insertGoal(goal) // Save the goal using GoalDao
                }
                Toast.makeText(this, "Goals saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid goals", Toast.LENGTH_SHORT).show()
            }



        }


        // Add expense button
        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val amount = etAmount.text.toString().toDoubleOrNull()
            val date = etDate.text.toString()
            val startTime = etStartTime.text.toString()
            val endTime = etEndTime.text.toString()
            val description = etDescription.text.toString()
            val category = etCategory.text.toString()















            // Validate input
            if (name.isBlank() || amount == null || date.isBlank() || category.isBlank()) {
                Toast.makeText(
                    this,
                    "Please enter valid name, amount, date, and category.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Create and save the expense to Room database
            val expense = Expense(
                name = name,
                amount = amount.toString(),
                date = date,
                startTime = startTime,
                endTime = endTime,
                description = description,
                category = category,
                username = loggedInUsername,
                imageUri = selectedImageUri.toString()
            )
            lifecycleScope.launch(Dispatchers.IO) {
                expenseDao.insertExpense(expense) // Save the expense
            }
            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
            clearFields()




            lifecycleScope.launch(Dispatchers.IO) {
                val totalSpent = expenseDao.getTotalAmountByUsername(loggedInUsername) ?: 0.0
                val goal = goalDao.getLatestGoal()

                if (goal != null) {
                    val min = goal.minGoal
                    val max = goal.maxGoal

                    val progressPercent = when {
                        totalSpent <= min -> 0
                        totalSpent >= max -> 100
                        else -> ((totalSpent - min) / (max - min) * 100).toInt()
                    }

                    withContext(Dispatchers.Main) {
                        progressBar.progress = progressPercent
                        tvProgressInfo.text = "You’ve spent R${String.format("%.2f", totalSpent)} — Goal Range: R$min to R$max"
                    }
                }
            }




        }




        btnShowExpenses.setOnClickListener {
            val intent = Intent(this, ExpenseListActivity::class.java)
            lifecycleScope.launch(Dispatchers.IO) {
                val expenses = expenseDao.getExpensesByUsername(loggedInUsername)
                withContext(Dispatchers.Main) {
                    intent.putParcelableArrayListExtra("EXPENSE_LIST", ArrayList(expenses))
                    startActivity(intent)
                }
            }
        }

        btnSelectImage = findViewById(R.id.btnSelectImage)
        ivSelectedImage = findViewById(R.id.ivSelectedImage)

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1001)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            ivSelectedImage.setImageURI(selectedImageUri)
        }
    }
    private fun clearFields() {
        // Clear all input fields
        etName.text.clear()
        etAmount.text.clear()
        etDate.text.clear()
        etStartTime.text.clear()
        etEndTime.text.clear()
        etDescription.text.clear()
        etCategory.text.clear()
    }

}





