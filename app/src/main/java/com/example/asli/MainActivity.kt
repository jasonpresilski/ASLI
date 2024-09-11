package com.example.asli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.asli.composables.CameraPreviewScreen
import com.example.asli.ui.theme.ASLITheme
import android.Manifest
import android.content.pm.PackageManager
import android.renderscript.ScriptGroup.Binding
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import com.example.asli.composables.CameraPreviewScreen
import com.example.asli.databinding.MainlayoutBinding

class MainActivity : ComponentActivity() {
    private val cameraPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Implement camera related code here
        } else {
            // Camera permission denied (Handle denied operation)
        }
    }

    private lateinit var binding: MainlayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        //binding = MainlayoutBinding.inflate(layoutInflater)
        //val view = binding.root
        //setContentView(view)



        // Check permissions
        checkAndRequestPermissions()

        val viewBinding = MainlayoutBinding.inflate(layoutInflater)
        //BindingInit()
        //val ScrollBox = MainlayoutBinding.bind.scrolllayout

        //ui test stuff, needs to be replaced with integration
        viewBinding.videoCaptureButton.setOnClickListener{ _ -> VideoCaptureButtonOnClick()}
        val Tex1 = TextView(this)
        Tex1.setText("Hello")
        Tex1.setTextSize(16f)
        viewBinding.scrolllayout.addView(Tex1)

        val Tex2 = TextView(this)
        Tex2.setText("Goodbye")
        Tex2.setTextSize(18f)
        viewBinding.scrolllayout.addView(Tex2)

        for (x in 1..5) {
            val Texx = TextView(this)
            Texx.setText(x.toString())
            val size = 20 + x
            Texx.setTextSize(size.toFloat())
            viewBinding.scrolllayout.addView(Texx)
        }
        //viewBinding.scrolllayout.addView()


        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                //Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

        setContentView(viewBinding.root)
    }

    override fun onStart() {
        super.onStart()
        //BindingInit()
    }

    override fun onResume() {
        super.onResume()
    }
    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }
    private fun BindingInit()
    {
        //todo:figure out why it wasn't properly binding from this scope and fix
        //binding.videoCaptureButton.setOnClickListener { _ -> VideoCaptureButtonOnClick()}
        //MainlayoutBinding.inflate(layoutInflater)
    }
    companion object {
        private const val TAG = "CameraXBasic"
    }

    //private var counter = 0
    private fun VideoCaptureButtonOnClick()
    {
        //todo:replace with proper camx recording
        //StartCapture()
        val toasty = Toast.makeText(this, "Not recorded", Toast.LENGTH_SHORT)
        toasty.show()

    }

}