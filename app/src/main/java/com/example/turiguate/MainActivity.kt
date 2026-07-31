package com.example.turiguate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.turiguate.ui.theme.TURIGUATETheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TURIGUATETheme {
                PlanScreen()
            }
        }
    }
}