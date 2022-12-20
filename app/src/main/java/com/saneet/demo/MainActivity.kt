package com.saneet.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saneet.demo.canvas.CanvasFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CanvasFragment.newInstance())
                .commitNow()
        }
    }
}