package com.saneet.demo.canvas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.saneet.demo.R

class CanvasFragment : Fragment(), View.OnClickListener {

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

        val canvasView = requireView().findViewById<CanvasView>(R.id.canvas)
        canvasView.gameRunner = pointGame
        canvasView.post {
            pointGame?.setParentViewBounds(canvasView.width, canvasView.height)
        }

        listOf(
            requireView().findViewById<Button>(R.id.add_rect),
            requireView().findViewById<Button>(R.id.add_circle),
            requireView().findViewById<Button>(R.id.add_ellipse),
            requireView().findViewById<Button>(R.id.add_square),
            requireView().findViewById<Button>(R.id.clear),
        ).forEach {
            it.setOnClickListener(this)
        }
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_rect -> pointGame?.addRect()
            R.id.add_circle -> pointGame?.addCircle()
            R.id.add_ellipse -> pointGame?.addEllipse()
            R.id.add_square -> pointGame?.addSquare()
            R.id.clear -> pointGame?.clearShapes()
        }
    }
}