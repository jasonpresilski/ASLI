package com.example.asli

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.asli.ui.theme.ASLITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CameraScreenActivity : ComponentActivity() {

    private var recording: Recording? = null
    private val boxList = mutableStateListOf<String>()
    private val networkScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent{
            ASLITheme {
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply{
                        setEnabledUseCases(
                            CameraController.VIDEO_CAPTURE
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.6f)
                    ) {
                        CamPreview(
                            controller = controller,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        IconButton(modifier = Modifier
                            .align(Alignment.BottomCenter)
                            //.padding(15.dp)
                            .size(40.dp), onClick = { Recordvid(controller) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Adjust,
                                contentDescription = "Recording",
                                modifier = Modifier.size(40.dp),
                                tint = Color.Red
                            )

                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomCenter)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray), // Background for the box area
                            reverseLayout = true, // Start the newest items from the bottom
                            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                        ) {
                            items(boxList) { text ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp,5.dp)
                                        .clip(shape = RoundedCornerShape(15.dp,15.dp,15.dp,15.dp))
                                        .height(50.dp)
                                        .background(Color.Black),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = text,
                                        color = Color.Yellow,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                    )
                                }
                                }
                        }
                    }
                }


            }
        }
    }

    private fun checkAndRequestPermissions() {
        val cameraPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Camera permission granted
            } else {
                // Camera permission denied
                Toast.makeText(this,
                    "Camera permission denied",
                    Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private fun Recordvid(controller: LifecycleCameraController)
    {
        if(recording != null)
        {
            recording?.stop()

            val file = File(filesDir, "Sign.mp4")
            if (file.exists()){
                val mp4data : ByteArray = file.readBytes()

            }
            else{
                Toast.makeText(this,
                    "File Error",
                    Toast.LENGTH_SHORT).show()
            }

            recording = null
            return
        }
        else
            //return
        {
            val outputFile = File(filesDir, "Sign.mp4")
            val qualitySelector = QualitySelector
                .from(Quality.LOWEST)
            val recorder = Recorder.Builder()
                .setQualitySelector(qualitySelector)
                .build()

            recording = controller.startRecording(
                FileOutputOptions.Builder(outputFile).build(),
                AudioConfig.AUDIO_DISABLED,
                ContextCompat.getMainExecutor(applicationContext),
            ) {
                event ->
                when(event) {
                    is VideoRecordEvent.Finalize -> {
                        if(event.hasError()) {
                            recording?.close()
                            recording = null

                            Toast.makeText(applicationContext,
                                "Recording error",
                                Toast.LENGTH_LONG
                            ).show()
                        } else
                        {
                            Toast.makeText(applicationContext,
                                "Recording finished - Sending",
                                Toast.LENGTH_LONG
                            ).show()

                            val file = File(filesDir, "Sign.mp4")
                            if (file.exists()) {
                                //Toast.makeText(this, "Attempting Send", Toast.LENGTH_SHORT).show()
                                val mp4data: ByteArray = file.readBytes()
                                //Toast.makeText(this, "File Read", Toast.LENGTH_SHORT).show()
                                //var msg: String? = null

                                networkScope.launch {
                                    boxList.add(SendVideo(mp4data))
                                }
                                //Toast.makeText(this, "Send Finished", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }
                }
            }
        }
    }
    private suspend fun SendVideo(mp4 : ByteArray) : String
    {
        try
        {

            val url = URL("http://20.11.54.176:8123/api/predict/")

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            val reqdata = mp4
            connection.setRequestProperty("Content-Type", "video/mp4")
            connection.setRequestProperty("Accept", "application/json")

            connection.doOutput = true
            connection.outputStream.write(reqdata)
            //Toast.makeText(this, "Upload Attempt", Toast.LENGTH_SHORT).show()

            val respCode = connection.responseCode
            if (respCode == 200 || respCode == 201)
            {
                //Toast.makeText(this, "Successful upload", Toast.LENGTH_SHORT).show()
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                var response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line).append("\n")
                    }
                    inputStream.close()
                //Toast.makeText(this, "Response: $response", Toast.LENGTH_LONG).show()
                return response.toString()
            //boxList.add(response.toString())
            }
            return "Error code: $respCode"
        }
        catch (e: Exception)
        {
            //Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show()
            //Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show()
            return e.toString()
        }
        return "error"
    }
}
