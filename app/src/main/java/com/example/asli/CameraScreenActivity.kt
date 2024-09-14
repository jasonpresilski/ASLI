package com.example.asli

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.asli.databinding.MainlayoutBinding

class CameraScreenActivity : ComponentActivity() {

    private lateinit var binding: MainlayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up camera
        setupCamera()
    }

    private fun setupCamera() {
        // Check permissions
        checkAndRequestPermissions()

        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI setup (dummy TextView for testing)
        binding.videoCaptureButton.setOnClickListener { _ -> VideoCaptureButtonOnClick() }
        addDummyTextViews()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )
            } catch (exc: Exception) {
                // Handle error
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun checkAndRequestPermissions() {
        val cameraPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Camera permission granted
            } else {
                // Camera permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private fun addDummyTextViews() {
        val tex1 = TextView(this)
        tex1.text = "Hello"
        tex1.textSize = 16f
        binding.scrolllayout.addView(tex1)

        val tex2 = TextView(this)
        tex2.text = "Goodbye"
        tex2.textSize = 18f
        binding.scrolllayout.addView(tex2)

        for (x in 1..5) {
            val texx = TextView(this)
            texx.text = x.toString()
            val size = 20 + x
            texx.textSize = size.toFloat()
            binding.scrolllayout.addView(texx)
        }
    }

    private fun VideoCaptureButtonOnClick() {
        val toasty = Toast.makeText(this, "Not recorded", Toast.LENGTH_SHORT)
        toasty.show()
    }
}
