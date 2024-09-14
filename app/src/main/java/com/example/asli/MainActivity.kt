package com.example.asli

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the home_screen.xml layout
        setContentView(R.layout.home_screen)

        // Find the camera button by its ID and set a click listener
        val cameraButton = findViewById<ImageButton>(R.id.cameraButton)
        cameraButton.setOnClickListener {
            // Navigate to CameraScreenActivity when the button is clicked
            val intent = Intent(this, CameraScreenActivity::class.java)
            startActivity(intent)
        }
    }
}
