package com.example.myapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class MonthlyReportActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE = 100
    private val TAG = "PDF_SAVE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_report)

        val tvMonthlyReportContent = findViewById<TextView>(R.id.tvMonthlyReportContent)
        val btnDownloadPdf = findViewById<Button>(R.id.btnDownloadPdf)

        val reportText = intent.getStringExtra("MONTHLY_REPORT_TEXT") ?: "No data available"
        tvMonthlyReportContent.text = reportText

        btnDownloadPdf.setOnClickListener {
            if (checkPermission()) {
                generatePdf(reportText)
            } else {
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            true
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val reportText = intent.getStringExtra("MONTHLY_REPORT_TEXT") ?: "No data available"
            generatePdf(reportText)
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generatePdf(content: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint()
        paint.textSize = 14f

        // Draw multiline text with line breaks
        val lines = content.split("\n")
        var y = 25f
        for (line in lines) {
            canvas.drawText(line, 10f, y, paint)
            y += paint.textSize + 10 // 10 pixels spacing between lines
        }

        pdfDocument.finishPage(page)

        val fileName = "MonthlyReport_${System.currentTimeMillis()}.pdf"
        val resolver = contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri).use { outputStream -> pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "File saved at URI: $uri")
                openPdf(uri)

            } else {
                Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show()
            }
        } else {

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            try {
                FileOutputStream(file).use { output ->
                    pdfDocument.writeTo(output)
                }

                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
                Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "File saved at path: ${file.absolutePath}")
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to save PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        pdfDocument.close()
    }

    private fun openPdf(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }

}
