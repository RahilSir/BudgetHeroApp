//package com.example.myapp
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import kotlin.math.roundToInt
//
//@Composable
//fun CategoryPieChart(categoryTotals: Map<String, Float>) {
//    if (categoryTotals.isEmpty()) {
//        Text(text = "Pie chart placeholder, categories: ${categoryTotals.size}")
//        return
//    }
//
//    val colors = listOf(
//        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan, Color.Gray
//    )
//
//    val total = categoryTotals.values.sum()
//    val sweepAngles = categoryTotals.mapValues { (it.value / total) * 360f }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Canvas(modifier = Modifier
//            .height(250.dp)
//
//            .fillMaxWidth()) {
//
//            var startAngle = 0f
//            var colorIndex = 0
//
//            for ((_, sweep) in sweepAngles) {
//                drawArc(
//                    color = colors[colorIndex % colors.size],
//                    startAngle = startAngle,
//                    sweepAngle = sweep,
//                    useCenter = true,
//                    size = Size(size.width, size.height)
//                )
//                startAngle += sweep
//                colorIndex++
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        categoryTotals.entries.forEachIndexed { index, entry ->
//            val color = colors[index % colors.size]
//            val percent = (entry.value / total * 100).roundToInt()
//            Text("${entry.key}: R${"%.2f".format(entry.value)} ($percent%)", color = color)
//        }
//    }
//}
