package com.example.kelly

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kelly.util.formattedAdvanced
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


class AScreen : Screen {


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                var a by remember {
                    mutableStateOf(9999999.seconds)
                }
                LaunchedEffect(Unit) {
                    while (a > 0.seconds) {
                        delay(1.seconds)
                        a = a - 1.seconds
                    }
                }
                Text("这是${a.formattedAdvanced()}", fontSize = 20.sp)

                Button(onClick = {
                    navigator?.push(BScreen("xxxx") {
                        Log.e("1234", "it=${it}")
                    })
                }) {
                    Text("去B", fontSize = 20.sp)
                }
            }
        }
    }
}