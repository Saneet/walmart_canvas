package com.saneet.demo.canvas

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    var gameRunner: PointGame? = null
        set(value) {
            field?.updateCallback = null
            field = value
        }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gameRunner?.handleTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { gameRunner?.draw(canvas) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        gameRunner?.updateCallback = {
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        gameRunner?.updateCallback = null
        super.onDetachedFromWindow()
    }
}