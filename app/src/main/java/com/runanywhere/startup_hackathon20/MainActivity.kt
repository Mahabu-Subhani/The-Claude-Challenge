package com.runanywhere.startup_hackathon20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.runanywhere.startup_hackathon20.ui.theme.Startup_hackathon20Theme

/**
 * MainActivity - Entry point for GridZero application
 *
 * GridZero: Offline Situation Awareness Tool for First Responders
 * Transforms chaotic field reports into structured tactical data
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Startup_hackathon20Theme {
                CrisisScreen()
            }
        }
    }
}