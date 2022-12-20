package com.saneet.demo.models

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.animation.LinearInterpolator
import kotlin.math.pow


abstract class Shape protected constructor(
    protected val bounds: MutableRect, parentBounds: RectF
) {
    val animators: List<ValueAnimator>
    private val xDuration = (1000..3000).random().toLong()
    private val yDuration = (1000..3000).random().toLong()

    init {
        val xAnimator = ObjectAnimator.ofFloat(
            bounds,
            "left",
            0F, parentBounds.width() - bounds.width
        ).apply {
            duration = xDuration
        }

        val yAnimator = ObjectAnimator.ofFloat(
            bounds,
            "top",
            0F, parentBounds.height() - bounds.height
        ).apply {
            duration = yDuration
        }

        animators = listOf(
            xAnimator,
            yAnimator
        ).map {
            it.apply {
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
            }
        }
    }

    protected fun withinBounds(point: PointF): Boolean {
        return bounds.toRect().contains(point.x, point.y)
    }

    fun setUpdateListener(animationUpdateListener: ValueAnimator.AnimatorUpdateListener) {
        animators.forEach {
            it.addUpdateListener(animationUpdateListener)
        }
    }

    fun clearUpdateListener() {
        animators.forEach {
            it.removeAllUpdateListeners()
        }
    }

    abstract fun contains(point: PointF): Boolean
    abstract fun draw(canvas: Canvas, paint: Paint)
}

class Rectangle(bounds: MutableRect, parentBounds: RectF) : Shape(bounds, parentBounds) {
    override fun contains(point: PointF): Boolean {
        return withinBounds(point)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(bounds.toRect(), paint)
    }
}

class Ellipse(bounds: MutableRect, parentBounds: RectF) : Shape(bounds, parentBounds) {
    private fun widthAxis() = bounds.width / 2F
    private fun heightAxis() = bounds.height / 2F
    private fun center(): PointF {
        return PointF(bounds.left + widthAxis(), bounds.top + heightAxis())
    }

    override fun contains(point: PointF): Boolean {
        return withinBounds(point) && center().let {
            val xFraction = (point.x - it.x).pow(2) / widthAxis().pow(2)
            val yFraction = (point.y - it.y).pow(2) / heightAxis().pow(2)
            (xFraction + yFraction) <= 1
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawOval(bounds.toRect(), paint)
    }
}

class Circle(bounds: MutableRect, parentBounds: RectF) : Shape(bounds, parentBounds) {
    private fun radius() = bounds.width / 2F
    private fun center(): PointF {
        val radius = radius()
        return PointF(bounds.left + radius, bounds.top + radius)
    }

    override fun contains(point: PointF): Boolean {
        return withinBounds(point) && center().run {
            PointF.length(
                x - point.x, y - point.y
            ) <= radius()
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        center().run { canvas.drawCircle(x, y, radius(), paint) }
    }
}

class MutableRect(var left: Float, var top: Float, val width: Float, val height: Float) {
    fun toRect() = RectF(left, top, width + left, height + top)
}