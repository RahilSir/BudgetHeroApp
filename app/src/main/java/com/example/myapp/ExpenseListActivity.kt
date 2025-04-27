package com.example.myapp

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.myapp.com.example.myapp.data.com.example.myapp.data.Expense

import androidx.appcompat.app.AppCompatActivity


class ExpenseListActivity : AppCompatActivity() {

    private lateinit var llExpenseContainer: LinearLayout
    private lateinit var tvCategorySummary: TextView
    private val PERMISSION_REQUEST_CODE = 100
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list_activity)

        val etStartDate = findViewById<EditText>(R.id.etStartDate)
        val etEndDate = findViewById<EditText>(R.id.etEndDate)
        val btnFilter = findViewById<Button>(R.id.btnFilter)
        llExpenseContainer = findViewById(R.id.expense_container)
        tvCategorySummary = findViewById(R.id.tvCategorySummary)

        val expenseList: ArrayList<Expense>? = intent.getParcelableArrayListExtra("EXPENSE_LIST")



        fun displayExpenses(expenses: List<Expense>) {
            llExpenseContainer.removeAllViews()
            for (expense in expenses) {
                val itemView = layoutInflater.inflate(R.layout.expense_item, null)

                val tvInfo = itemView.findViewById<TextView>(R.id.tvItemDetails)
                val ivImage = itemView.findViewById<ImageView>(R.id.ivItemImage)

                val info = """
                    Name: ${expense.name}
                    Amount: ${expense.amount}
                    Date: ${expense.date}
                    Start Time: ${expense.startTime}
                    End Time: ${expense.endTime}
                    Description: ${expense.description}
                    Category: ${expense.category}
                """.trimIndent()


                tvInfo.text = info


                if (permissionGranted && !expense.imageUri.isNullOrEmpty()) {
                    val uri = Uri.parse(expense.imageUri)
                    ivImage.setImageURI(uri)
                } else {
                    ivImage.setImageResource(android.R.drawable.ic_menu_report_image)
                }

                llExpenseContainer.addView(itemView)
            }
        }

        btnFilter.setOnClickListener {
            val startDate = etStartDate.text.toString()
            val endDate = etEndDate.text.toString()

            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                val filtered = expenseList?.filter {
                    it.date >= startDate && it.date <= endDate
                } ?: emptyList()

                displayExpenses(filtered)

                val categoryTotals = mutableMapOf<String, Float>()
                for (expense in filtered) {
                    val category = expense.category
                    val amount = expense.amount.toFloatOrNull() ?: 0f
                    categoryTotals[category] = categoryTotals.getOrDefault(category, 0f) + amount
                }

                val summaryText = if (categoryTotals.isNotEmpty()) {
                    categoryTotals.entries.joinToString("\n") {
                        "${it.key}: R${"%.2f".format(it.value)}"
                    }
                } else {
                    "No category totals to display."
                }

                tvCategorySummary.text = summaryText
            }
        }

        // Initial display
        expenseList?.let { displayExpenses(it) }

        val initialCategoryTotals = mutableMapOf<String, Float>()
        expenseList?.forEach { expense ->
            val category = expense.category
            val amount = expense.amount.toFloatOrNull() ?: 0f
            initialCategoryTotals[category] = initialCategoryTotals.getOrDefault(category, 0f) + amount
        }

        val initialSummaryText = if (initialCategoryTotals.isNotEmpty()) {
            initialCategoryTotals.entries.joinToString("\n") {
                "${it.key}: R${"%.2f".format(it.value)}"
            }
        } else {
            "No category totals to display."
        }

        tvCategorySummary.text = initialSummaryText
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, update the status and reload expenses
                permissionGranted = true
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                // You can optionally re-trigger displayExpenses here to refresh UI with images
            } else {

                Toast.makeText(this, "Permission denied to read images", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goBack(view: View) {
        finish()
    }
}

