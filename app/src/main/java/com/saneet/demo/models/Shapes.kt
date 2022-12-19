package com.saneet.demo.models

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.animation.LinearInterpolator

abstract class Shape protected constructor(
    rect: RectF,
    parentBounds: RectF
) {
    val bounds =
        MutableRect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top)
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

    abstract fun contains(point: PointF): Boolean
    abstract fun draw(canvas: Canvas, paint: Paint)
}

class Rectangle(bounds: RectF, parentBounds: RectF) : Shape(bounds, parentBounds) {
    override fun contains(point: PointF): Boolean {
        return withinBounds(point)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(bounds.toRect(), paint)
    }
}

class Circle(private val center: PointF, private val radius: Float, bounds: RectF) :
    Shape(
        RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius),
        bounds
    ) {
    override fun contains(point: PointF): Boolean {
        return withinBounds(point) && PointF.length(
            center.x - point.x,
            center.y - point.y
        ) <= radius
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(center.x, center.y, radius, paint)
    }
}

class MutableRect(var left: Float, var top: Float, val width: Float, val height: Float) {
    fun toRect() = RectF(left, top, width + left, height + top)
}