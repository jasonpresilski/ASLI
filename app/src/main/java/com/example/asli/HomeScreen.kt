package com.example.asli

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.asli.databinding.HomeScreenBinding

class HomeScreen : ComponentActivity() {

    private lateinit var binding: HomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle camera button click to navigate to CameraScreenActivity
        binding.cameraButton.setOnClickListener {
            val intent = Intent(this, CameraScreenActivity::class.java)
            startActivity(intent)
        }
    }
}
