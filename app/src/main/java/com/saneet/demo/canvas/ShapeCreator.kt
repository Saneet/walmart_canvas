package com.saneet.demo.canvas

import android.graphics.PointF
import android.graphics.RectF
import com.saneet.demo.models.*
import kotlin.random.Random

class ShapeCreator {
    private var canvasWidth: Int? = null
    private var canvasHeight: Int? = null

    fun setParentViewBounds(canvasWidth: Int, canvasHeight: Int) {
        this.canvasWidth = canvasWidth
        this.canvasHeight = canvasHeight
    }

    fun createRect(): Shape =
        Rectangle(
            bounds = createNewRectForShape(false),
            parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
        )


    fun createCircle(): Shape =
        Circle(
            bounds = createNewRectForShape(true),
            parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
        )


    fun createEllipse(): Shape =
        Ellipse(
            bounds = createNewRectForShape(false),
            parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
        )


    fun createSquare(): Shape =
        Rectangle(
            bounds = createNewRectForShape(true),
            parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
        )


    private fun createNewRectForShape(squareBounds: Boolean): MutableRect {
        if ((canvasWidth == null) || (canvasHeight == null)) {
            throw IllegalStateException("CanvasWidth or Height were not set before adding Shapes")
        }
        val maxWidth = canvasWidth!! / 2F
        val maxHeight = canvasHeight!! / 2F

        val startPosition = PointF(
            0F,
            0F
        )

        val width = maxWidth * Random.nextFloat()
        val height = if (squareBounds) width else maxHeight * Random.nextFloat()
        return MutableRect(startPosition.x, startPosition.y, width, height)
    }
}