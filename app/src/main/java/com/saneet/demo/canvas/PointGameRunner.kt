package com.saneet.demo.canvas

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PointF
import com.saneet.demo.models.Pointer
import com.saneet.demo.models.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class PointGameRunner {
    internal val touchEventState =
        MutableSharedFlow<PointF>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val animationUpdateState =
        MutableSharedFlow<Unit?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    internal val pointerFlow =
        touchEventState.combine(animationUpdateState.onStart { emit(null) }) { point: PointF, _: Unit? -> point }
            .sample(5)
            .map { point ->
                Pointer(point, calculateColorForPoint(point))
            }.flowOn(Dispatchers.Default)

    internal var shapes = emptyList<Shape>()


    private fun calculateColorForPoint(point: PointF): Int {
        shapes.forEach { shape ->
            if (shape.contains(point))
                return Color.GREEN
        }
        return Color.BLUE
    }

    fun addShape(shape: Shape) {
        shapes = shapes.plus(shape)
        shape.setUpdateListener(animationUpdateListener)
    }

    fun clearShapes() {
        shapes.forEach {
            it.clearUpdateListener()
        }
        shapes = emptyList()
    }

    private val animationUpdateListener = ValueAnimator.AnimatorUpdateListener {
        //We just need a signal and no object here
        animationUpdateState.tryEmit(null)
    }
}