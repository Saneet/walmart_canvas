package com.saneet.demo.canvas

import android.graphics.*
import com.nhaarman.mockitokotlin2.firstValue
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.secondValue
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PointGameTest {
    private val viewModel = PointGame()

    @Test
    fun setPointer_inside_green() {
//        //when
//        viewModel.rect = RectF(10F, 10F, 100F, 100F)
//        //action
//        viewModel.setPointer(PointF(15F, 15F))
//        //then
//        assertEquals(Color.GREEN, viewModel.paintPoint.color)
    }

    @Test
    fun setPointer_outside_blue() {
//        //when
//        viewModel.rect = RectF(10F, 10F, 100F, 100F)
//        //action
//        viewModel.setPointer(PointF(1F, 1F))
//        //then
//        assertEquals(Color.BLUE, viewModel.paintPoint.color)
    }

    @Test
    fun draw() {
//        //when
//        viewModel.rect = RectF(10F, 10F, 100F, 100F)
//        viewModel.setPointer(PointF(1F, 1F))
//        val canvas: Canvas = mock()
//        val paintCaptor = ArgumentCaptor.forClass(Paint::class.java)
//
//        //action
//        viewModel.draw(canvas)
//
//        //then
//        verify(canvas).drawRGB(255, 255, 255)
//        verify(canvas).drawRect(eq(RectF(10F, 10F, 100F, 100F)), paintCaptor.capture())
//        assertEquals(3F, paintCaptor.firstValue.strokeWidth)
//        assertEquals(Color.BLACK, paintCaptor.firstValue.color)
//        assertEquals(Paint.Style.STROKE, paintCaptor.firstValue.style)
//
//        verify(canvas).drawCircle(eq(1F), eq(1F), eq(10F), paintCaptor.capture())
//        assertEquals(5F, paintCaptor.secondValue.strokeWidth)
//        assertEquals(Color.BLUE, paintCaptor.secondValue.color)
//        assertEquals(Paint.Style.FILL_AND_STROKE, paintCaptor.secondValue.style)
    }

}
