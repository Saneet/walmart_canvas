package com.saneet.demo.canvas

import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saneet.demo.DemoApplication
import com.saneet.demo.R
import com.saneet.demo.ViewModelFactory
import javax.inject.Inject

class CanvasFragment : Fragment() {

    companion object {
        fun newInstance() = CanvasFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<PointGame>

    private val viewModel: PointGame by lazy {
        viewModelFactory.get<PointGame>(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.rect = RectF(300F, 400F, 800F, 1200F)
        return inflater.inflate(R.layout.fragment_canvas, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as DemoApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }
}