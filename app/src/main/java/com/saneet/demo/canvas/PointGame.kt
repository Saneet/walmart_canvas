package com.saneet.demo.canvas

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import androidx.annotation.VisibleForTesting
import com.saneet.demo.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class PointGame() {
    private var canvasWidth: Int? = null
    private var canvasHeight: Int? = null
    var updateCallback: (() -> Unit)? = null
    private val animationUpdateListener = ValueAnimator.AnimatorUpdateListener {
        //We just need a signal and no object here
        animationUpdateState.tryEmit(null)
    }
    private val shapes = mutableListOf<Shape>()
    private var animationsRunning: Boolean = false
    private var animations = AnimatorSet()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val touchEventState =
        MutableSharedFlow<PointF>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val animationUpdateState =
        MutableSharedFlow<Unit?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val pointerState = MutableStateFlow<Pointer?>(null)
    private val pointerFlow =
        animationUpdateState.combine(touchEventState) { _: Unit?, point: PointF -> point }
            .sample(5)
            .map { point ->
                Pointer(point, calculateColorForPoint(point))
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
                return Color.GREEN
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
        }
        pointerState.value?.run {
            canvas.drawCircle(
                point.x,
                point.y,
                10F,
                paintPoint.apply { color = pointColor })
        }
        if (shapes.size > 0) {
            updateCallback?.invoke()
        }
    }

    private fun createNewRectForShape(squareBounds: Boolean): MutableRect {
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
        val height = if (squareBounds) width else maxHeight * Random.nextFloat()
        return MutableRect(startPosition.x, startPosition.y, width, height)
    }

    fun addRect() {
        initShape(
            Rectangle(
                bounds = createNewRectForShape(false),
                parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
            )
        )
    }

    fun addCircle() {
        initShape(
            Circle(
                bounds = createNewRectForShape(true),
                parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
            )
        )
    }

    fun addEllipse() {
        initShape(
            Ellipse(
                bounds = createNewRectForShape(false),
                parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
            )
        )
    }

    fun addSquare() {
        initShape(
            Rectangle(
                bounds = createNewRectForShape(true),
                parentBounds = RectF(0F, 0F, canvasWidth!!.toFloat(), canvasHeight!!.toFloat())
            )
        )
    }

    private fun initShape(shape: Shape) {
        shape.setUpdateListener(animationUpdateListener)
        shapes.add(shape)
        shape.animators.forEach { animations.play(it) }
        animations.playTogether(shape.animators)
        animations.start()
        updateCallback?.invoke()
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
            ACTION_DOWN, ACTION_MOVE -> touchEventState.tryEmit(PointF(event.x, event.y))
        }
    }

}