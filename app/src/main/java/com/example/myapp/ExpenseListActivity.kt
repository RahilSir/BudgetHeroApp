package com.example.myapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.myapp.com.example.myapp.data.com.example.myapp.data.Expense
//import com.example.myapp.CategoryPieChart

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.roundToInt


class ExpenseListActivity : AppCompatActivity() {

    private lateinit var llExpenseContainer: LinearLayout
    private lateinit var tvCategorySummary: TextView
    private val PERMISSION_REQUEST_CODE = 100
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list_activity)
        val pieChartComposeView = findViewById<ComposeView>(R.id.pieChartComposeView)

        val etStartDate = findViewById<EditText>(R.id.etStartDate)
        val etEndDate = findViewById<EditText>(R.id.etEndDate)
        val btnFilter = findViewById<Button>(R.id.btnFilter)
        llExpenseContainer = findViewById(R.id.expense_container)
        tvCategorySummary = findViewById(R.id.tvCategorySummary)

        val expenseList: ArrayList<Expense>? = intent.getParcelableArrayListExtra("EXPENSE_LIST")

        val btnMonthlyReport = findViewById<Button>(R.id.btnMonthlyReport)

        btnMonthlyReport.setOnClickListener {
            val monthSummary = mutableMapOf<String, Float>()
            val month = "2025-06" // make dynamic later

            expenseList?.filter { it.date.startsWith(month) }?.forEach { expense ->
                val category = expense.category
                val amount = expense.amount.toFloatOrNull() ?: 0f
                monthSummary[category] = monthSummary.getOrDefault(category, 0f) + amount
            }

            val report = if (monthSummary.isNotEmpty()) {
                "Monthly Report for $month\n\n" + monthSummary.entries.joinToString("\n") {
                    "${it.key}: R${"%.2f".format(it.value)}"
                }
            } else {
                "No expenses found for $month"
            }

            val intent = Intent(this, MonthlyReportActivity::class.java)
            intent.putExtra("MONTHLY_REPORT_TEXT", report)
            startActivity(intent)
        }


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


                pieChartComposeView.setContent {
                    BarChartWithGoals(
                        categoryTotals = categoryTotals,
                        minGoal = 100f,
                        maxGoal = 500f
                    )
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
                // Permission granted
                permissionGranted = true
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(this, "Permission denied to read images", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goBack(view: View) {
        finish()
    }





    @Composable
    fun BarChartWithGoals(
        categoryTotals: Map<String, Float>,
        minGoal: Float,
        maxGoal: Float
    ) {
        if (categoryTotals.isEmpty()) {
            Text(text = "No data available")
            return
        }

        val barColors = listOf(
            Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan, Color.Gray
        )

        val maxAmount = (categoryTotals.values.maxOrNull() ?: 0f).coerceAtLeast(maxGoal)
        val categories = categoryTotals.keys.toList()
        val barWidth = 60.dp
        val spacing = 20.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val barSpacePx = barWidth.toPx() + spacing.toPx()
                val leftPadding = (canvasWidth - (barSpacePx * categories.size)) / 2

                categoryTotals.entries.forEachIndexed { index, (label, value) ->
                    val barHeightRatio = value / maxAmount
                    val barHeight = barHeightRatio * canvasHeight

                    val barX = leftPadding + index * barSpacePx
                    val barY = canvasHeight - barHeight

                    // Draw bar
                    drawRect(
                        color = barColors[index % barColors.size],
                        topLeft = androidx.compose.ui.geometry.Offset(barX, barY),
                        size = Size(barWidth.toPx(), barHeight)
                    )

                    // Draw amount above bar
                    drawContext.canvas.nativeCanvas.drawText(
                        "R${value.roundToInt()}",
                        barX + barWidth.toPx() / 4,
                        barY - 10,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 32f
                            textAlign = android.graphics.Paint.Align.LEFT
                        }
                    )
                }

                // Min goal line
                val minLineY = canvasHeight - (minGoal / maxAmount) * canvasHeight
                drawLine(
                    color = Color.Red,
                    start = androidx.compose.ui.geometry.Offset(0f, minLineY),
                    end = androidx.compose.ui.geometry.Offset(canvasWidth, minLineY),
                    strokeWidth = 3f
                )

                // Max goal line
                val maxLineY = canvasHeight - (maxGoal / maxAmount) * canvasHeight
                drawLine(
                    color = Color.Green,
                    start = androidx.compose.ui.geometry.Offset(0f, maxLineY),
                    end = androidx.compose.ui.geometry.Offset(canvasWidth, maxLineY),
                    strokeWidth = 3f
                )
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                val totalSpacing = barWidth + spacing
                Spacer(modifier = Modifier.width((totalSpacing / 2)))

                categories.forEachIndexed { index, label ->
                    Text(
                        text = label,
                        modifier = Modifier
                            .width(barWidth)
                            .padding(horizontal = spacing / 2),
                        maxLines = 1
                    )





                }
            }
        }
    }

}

