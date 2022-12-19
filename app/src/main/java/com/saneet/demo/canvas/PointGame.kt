package com.saneet.demo.canvas

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import androidx.annotation.VisibleForTesting
import com.saneet.demo.models.Pointer
import com.saneet.demo.models.Rectangle
import com.saneet.demo.models.Shape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random

class PointGame() {
    private var canvasWidth: Int? = null
    private var canvasHeight: Int? = null
    var updateCallback: (() -> Unit)? = null
    private val animationUpdateListener = ValueAnimator.AnimatorUpdateListener {
        updateCallback?.invoke()
    }
    private val shapes = mutableListOf<Shape>()
    private var animationsRunning: Boolean = false
    private var animations = AnimatorSet()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val touchEventState = MutableStateFlow<PointF?>(null)
    private val pointerState = MutableStateFlow<Pointer?>(null)
    private val pointerFlow = touchEventState.debounce(8).map { point ->
        if (point != null) Pointer(point, calculateColorForPoint(point)) else null
    }.flowOn(Dispatchers.Default)

    init {
        coroutineScope.launch {
            pointerFlow.collect { pointer ->
                pointerState.value = pointer
                updateCallback?.invoke()
            }
        }
    }

    fun setParentViewBounds(canvasWidth: Int, canvasHeight: Int) {
        this.canvasWidth = canvasWidth
        this.canvasHeight = canvasHeight
    }

    private fun calculateColorForPoint(point: PointF): Int {
        shapes.forEach { shape ->
            if (shape.contains(point))
                Color.GREEN
        }
        return Color.BLUE
    }

    @VisibleForTesting
    val paintRect = Paint().apply {
        strokeWidth = 3F
        color = Color.BLACK
        style = Paint.Style.STROKE
    }

    @VisibleForTesting
    val paintPoint = Paint().apply {
        strokeWidth = 5F
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.drawRGB(255, 255, 255)
        shapes.map {
            it.draw(canvas, paintRect)
            Log.i("Saneet", "Left: ${it.bounds.left} | Top: ${it.bounds.top}")
        }
        pointerState.value?.run {
            canvas.drawCircle(
                point.x,
                point.y,
                10F,
                paintPoint.apply { color = pointColor })
        }
    }

    fun addRect() {
        if ((canvasWidth == null) || (canvasHeight == null)) {
            throw IllegalStateException("CanvasWidth or Height were not set before adding Shapes")
        }
        val maxWidth = canvasWidth!! / 2F
        val maxHeight = canvasHeight!! / 2F

        val startPosition = PointF(
            0F,//(canvasWidth - maxWidth) * Random.nextFloat(),
            0F//(canvasHeight - maxHeight) * Random.nextFloat()
        )

        val width = maxWidth * Random.nextFloat()
        val height = maxHeight * Random.nextFloat()
        val shape = Rectangle(
            bounds = RectF(startPosition.x, startPosition.y, width, height),
            parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
        )
        shape.setUpdateListener(animationUpdateListener)
        shapes.add(shape)
        shape.animators.forEach { animations.play(it) }
        animations.playTogether(shape.animators)
    }

    fun clearShapes() {
        animationsRunning = false
        animations.end()
        animations = AnimatorSet()
        shapes.clear()
    }

    fun startAnimations() {
        animations.start()
        animationsRunning = true
    }

    fun stopAnimations() {
        animations.pause()
        animationsRunning = false
    }

    fun handleTouchEvent(event: MotionEvent?) {
        when (event?.action) {
            ACTION_DOWN, ACTION_MOVE -> touchEventState.value = PointF(event.x, event.y)
            ACTION_UP -> touchEventState.value = null
        }
    }
}