package com.saneet.demo.canvas

import android.graphics.*
import androidx.core.graphics.contains
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PointGame @Inject constructor() : ViewModel() {
    lateinit var rect: RectF
    private val paintRect = Paint().apply {
        strokeWidth = 3F
        color = Color.BLACK
        style = Paint.Style.STROKE
    }
    private val paintPoint = Paint().apply {
        strokeWidth = 5F
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
    }
    private var point: PointF? = null

    fun setPointer(point: PointF) {
        this.point = point
        paintPoint.color = if (rect.contains(point)) Color.GREEN else Color.RED
    }

    fun draw(canvas: Canvas) {
        canvas.drawRGB(255, 255, 255)
        canvas.drawRect(rect, paintRect)
        point?.run { canvas.drawCircle(x, y, 10F, paintPoint) }
    }
}