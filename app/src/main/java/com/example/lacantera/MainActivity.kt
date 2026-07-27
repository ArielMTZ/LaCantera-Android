package com.example.lacantera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lacantera.navigation.AppNavGraph
import com.example.lacantera.ui.theme.LaCanteraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LaCanteraTheme {
                AppNavGraph()
            }
        }
    }
}