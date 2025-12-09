package com.example.kelly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import io.kelly.util.initKelly
import io.kelly.util.withMutableSnapshot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKelly()
        enableEdgeToEdge()
        setContent {
            Navigator(AScreen())
            mutableListOf<Int>().withMutableSnapshot {
                asIterable().forEachIndexed { index, item ->

                }
            }
        }
    }
}
