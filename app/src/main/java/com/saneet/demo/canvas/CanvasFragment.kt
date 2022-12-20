package com.saneet.demo.canvas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.saneet.demo.R

class CanvasFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = CanvasFragment()
    }

    private lateinit var pointGameViewModel: PointGameViewModel
    private val shapeCreator = ShapeCreator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_canvas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pointGameViewModel = ViewModelProvider(this)[PointGameViewModel::class.java]

        val canvasView = requireView().findViewById<CanvasView>(R.id.canvas)
        canvasView.gameRunner = pointGameViewModel
        canvasView.post {
            shapeCreator.setParentViewBounds(canvasView.width, canvasView.height)
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
        pointGameViewModel.startAnimations()
    }

    override fun onPause() {
        pointGameViewModel.stopAnimations()
        super.onPause()
    }

    override fun onDestroyView() {
        requireView().findViewById<CanvasView>(R.id.canvas).apply {
            gameRunner = null
        }
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.clear) {
            pointGameViewModel.clearShapes()
            requireView().findViewById<TextView>(R.id.view_count).text =
                "Shapes: ${pointGameViewModel.getShapeCount()}"
        } else {
            pointGameViewModel.addShape(
                when (v?.id) {
                    R.id.add_rect -> shapeCreator.createRect()
                    R.id.add_circle -> shapeCreator.createCircle()
                    R.id.add_ellipse -> shapeCreator.createEllipse()
                    R.id.add_square -> shapeCreator.createSquare()
                    else -> shapeCreator.createRect()
                }
            )
            requireView().findViewById<TextView>(R.id.view_count).text =
                "Shapes: ${pointGameViewModel.getShapeCount()}"
        }
    }
}