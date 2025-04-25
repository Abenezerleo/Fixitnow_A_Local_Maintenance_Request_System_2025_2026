package com.example.assigment.ui.theme.others

import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
@Composable
fun PieChartScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(800.dp), // Make the container bigger
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    setUsePercentValues(false)
                    description.isEnabled = false

                    isDrawHoleEnabled = true
                    holeRadius = 80f

                    setDrawCenterText(true)
                    centerText = "Requests"
                    setCenterTextSize(22f)
                    setCenterTextColor(Color.DKGRAY)


                    setExtraOffsets(30f, 30f, 30f, 30f)

                    setEntryLabelColor(Color.BLACK)
                    setEntryLabelTextSize(12f)

                    val entries = listOf(
                        PieEntry(75f, "Completed"),
                        PieEntry(10f, "Rejected"),
                        PieEntry(15f, "Pending")
                    )

                    val dataSet = PieDataSet(entries, "Request Status").apply {
                        colors = listOf(
                            android.graphics.Color.rgb(76, 175, 80),
                            android.graphics.Color.rgb(244, 67, 54),
                            android.graphics.Color.rgb(255, 193, 7)
                        )
                        valueTextSize = 12f
                        valueTextColor = Color.BLACK
                        yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                        xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    }

                    dataSet.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }

                    data = PieData(dataSet)
                    animateY(1200)
                    invalidate()
                }
            },
            modifier = Modifier
                .size(475.dp)
        )
    }
}