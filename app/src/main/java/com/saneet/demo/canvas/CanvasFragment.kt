package com.saneet.demo.canvas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.saneet.demo.R

class CanvasFragment : Fragment() {

    companion object {
        fun newInstance() = CanvasFragment()
    }

    var pointGame: PointGame? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_canvas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pointGame = PointGame()

        requireView().findViewById<CanvasView>(R.id.canvas).let {
            it.post {
                it.gameRunner = pointGame
                pointGame?.setParentViewBounds(it.width, it.height)
            }
        }

        requireView().findViewById<Button>(R.id.add_rect)
            .setOnClickListener { pointGame?.addRect() }

        requireView().findViewById<Button>(R.id.clear)
            .setOnClickListener { pointGame?.clearShapes() }
    }

    override fun onResume() {
        super.onResume()
        pointGame?.startAnimations()
    }

    override fun onPause() {
        super.onPause()
        pointGame?.stopAnimations()
    }

    override fun onDestroyView() {
        requireView().findViewById<CanvasView>(R.id.canvas).apply {
            pointGame = null
        }
        pointGame = null
        super.onDestroyView()
    }
}