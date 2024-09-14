package com.example.asli

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.asli.databinding.MainlayoutBinding

class CameraScreen(private val activity: ComponentActivity, private val context: Context) {
    private val cameraPermissionRequest = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Implement camera related code here
        } else {
            // Camera permission denied (Handle denied operation)
        }
    }

    private lateinit var binding: MainlayoutBinding

    fun setupCamera() {
        // Check permissions
        checkAndRequestPermissions()

        val viewBinding = MainlayoutBinding.inflate(activity.layoutInflater)

        // UI setup (dummy TextView for testing)
        viewBinding.videoCaptureButton.setOnClickListener { _ -> VideoCaptureButtonOnClick() }
        addDummyTextViews(viewBinding)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    activity, cameraSelector, preview
                )
            } catch (exc: Exception) {
                // Handle error
            }

        }, ContextCompat.getMainExecutor(context))

        activity.setContentView(viewBinding.root)
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private fun addDummyTextViews(viewBinding: MainlayoutBinding) {
        val tex1 = TextView(context)
        tex1.text = "Hello"
        tex1.textSize = 16f
        viewBinding.scrolllayout.addView(tex1)

        val tex2 = TextView(context)
        tex2.text = "Goodbye"
        tex2.textSize = 18f
        viewBinding.scrolllayout.addView(tex2)

        for (x in 1..5) {
            val texx = TextView(context)
            texx.text = x.toString()
            val size = 20 + x
            texx.textSize = size.toFloat()
            viewBinding.scrolllayout.addView(texx)
        }
    }

    private fun VideoCaptureButtonOnClick() {
        val toasty = Toast.makeText(context, "Not recorded", Toast.LENGTH_SHORT)
        toasty.show()
    }
}
