package com.saneet.demo.canvas

import android.animation.AnimatorSet
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saneet.demo.models.Pointer
import com.saneet.demo.models.Shape
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PointGameViewModel : ViewModel() {
    var updateCallback: (() -> Unit)? = null
    private val pointGameRunner = PointGameRunner()
    private val pointerState = MutableStateFlow<Pointer?>(null)

    init {
        viewModelScope.launch {
            pointGameRunner.pointerFlow.collect { pointer ->
                pointerState.value = pointer
                updateCallback?.invoke()
            }
        }
    }

    private var animations = AnimatorSet()
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

    fun draw(canvas: Canvas) {
        canvas.drawRGB(255, 255, 255)
        pointGameRunner.shapes.map {
            it.draw(canvas, paintRect)
        }
        pointerState.value?.run {
            canvas.drawCircle(
                point.x,
                point.y,
                10F,
                paintPoint.apply { color = pointColor })
        }
        if (pointGameRunner.shapes.isNotEmpty()) {
            updateCallback?.invoke()
        }
    }

    fun clearShapes() {
        pointGameRunner.clearShapes()
        animations.end()
        animations.removeAllListeners()
        animations = AnimatorSet()
    }

    fun startAnimations() {
        animations.start()
        updateCallback?.invoke()
    }

    fun stopAnimations() {
        animations.pause()
        updateCallback?.invoke()
    }

    fun handleTouchEvent(event: MotionEvent?) {
        when (event?.action) {
            ACTION_DOWN, ACTION_MOVE -> pointGameRunner.touchEventState.tryEmit(
                PointF(
                    event.x,
                    event.y
                )
            )
        }
    }

    fun addShape(shape: Shape) {
        pointGameRunner.addShape(shape)
        animations.playTogether(shape.animators)

        startAnimations()
    }

    fun getShapeCount(): Int = pointGameRunner.shapes.size
}