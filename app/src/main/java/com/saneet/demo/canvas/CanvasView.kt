package com.saneet.demo.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    lateinit var viewModel: PointGame

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel =
            ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[PointGame::class.java]
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            viewModel.setPointer(PointF(event.x, event.y))
            invalidate()
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { viewModel.draw(it) }
    }
}