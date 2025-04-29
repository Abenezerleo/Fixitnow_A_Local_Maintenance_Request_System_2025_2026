package com.example.assigment.ui.theme.others

import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.animation.Easing
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PieChartScreen(
    pendingCount: Int,
    completedCount: Int,
    rejectedCount: Int
) {
    val amber = Color(0xFFFFC107)
    val red = Color(0xFFF44336)
    val green = Color(0xFF4CAF50)
    val chartColors = listOf(amber, red, green)
    val chartColorInts = listOf(
        android.graphics.Color.parseColor("#FFC107"),
        android.graphics.Color.parseColor("#F44336"),
        android.graphics.Color.parseColor("#4CAF50")
    )

    val total = pendingCount + completedCount + rejectedCount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(800.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { context ->
                    PieChart(context).apply {
                        setUsePercentValues(true)
                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        holeRadius = 75f
                        transparentCircleRadius = 80f
                        setHoleColor(android.graphics.Color.WHITE)
                        setTransparentCircleColor(android.graphics.Color.WHITE)
                        setDrawCenterText(false)
                        legend.isEnabled = false
                        setDrawEntryLabels(true)
                        setEntryLabelColor(android.graphics.Color.BLACK)
                        setEntryLabelTextSize(14f)
                        setEntryLabelTypeface(android.graphics.Typeface.DEFAULT_BOLD)
                        isRotationEnabled = false
                        setExtraOffsets(15f, 15f, 15f, 15f)
                        minOffset = 5f
                    }
                },
                update = { chart ->
                    val entries = if (total == 0) {
                        listOf(PieEntry(1f, ""))
                    } else {
                        listOf(
                            PieEntry(pendingCount.toFloat(), "Pending"),
                            PieEntry(rejectedCount.toFloat(), "Rejected"),
                            PieEntry(completedCount.toFloat(), "Completed")
                        )
                    }

                    val dataSet = PieDataSet(entries, "").apply {
                        colors = if (total == 0) {
                            listOf(android.graphics.Color.LTGRAY)
                        } else {
                            chartColorInts
                        }
                        setDrawValues(true)
                        valueTextSize = 18f
                        valueTextColor = android.graphics.Color.BLACK
                        valueTypeface = android.graphics.Typeface.DEFAULT_BOLD
                        valueLinePart1Length = 0.3f
                        valueLinePart2Length = 0.2f
                        valueLineColor = android.graphics.Color.DKGRAY
                        yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                        xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                        setValueLineWidth(1f)
                        sliceSpace = 2f

                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return if (total == 0) "" else "%.1f%%".format(value)
                            }
                        }
                    }

                    chart.data = PieData(dataSet)
                    chart.animateY(500, Easing.EaseInOutQuad)
                    chart.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(800.dp)
                    .padding(0.dp)
            )
        }
    }}