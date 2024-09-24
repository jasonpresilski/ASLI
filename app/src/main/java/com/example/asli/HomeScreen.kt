package com.example.asli

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.example.asli.ui.theme.ASLITheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import com.example.asli.R
import androidx.compose.ui.text.font.FontWeight


@Composable
fun HomeScreenUI() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC8E7F9))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Add the logo image
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(250.dp)
            )

            //Spacer(modifier = Modifier.height(16.dp))

            // Multi-colored "Signify" text
            MultiStyleText(
                text1 = "Sign",
                color1 = Color(0xFF3385C6),
                text2 = "ify",
                color2 = Color(0xFF7F8E9E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle Text
            Text(
                text = "Transform signs into meaningful connections by turning it into seamless communication",
                color = Color.Black,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Instruction Text
            Text(
                text = "To begin, simply tap the camera button and start recording your sign. Your translation will appear shortly after.",
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
        }


        Button(
            onClick = {
                val intent = Intent(context, CameraScreenActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .size(72.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3B3B3))
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,  // Change later
                contentDescription = "Camera Icon",
                tint = Color(0xFF3385C6)
            )
        }
    }
}

@Composable
fun MultiStyleText(text1: String, color1: Color, text2: String, color2: Color) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = color1, fontWeight = FontWeight.Bold)) {
                append(text1)
            }
            withStyle(style = SpanStyle(color = color2, fontWeight = FontWeight.Normal)) {
                append(text2)
            }
        },
        fontSize = 36.sp,
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ASLITheme {
        HomeScreenUI()
    }
}
