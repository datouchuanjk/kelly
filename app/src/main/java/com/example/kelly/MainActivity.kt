package com.example.kelly

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.SavedStateHandle
import cafe.adriel.voyager.navigator.Navigator
import io.kelly.util.delegatedState
import io.kelly.util.initKelly
import io.kelly.util.withMutableSnapshot

@SuppressLint("VisibleForTests")
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
