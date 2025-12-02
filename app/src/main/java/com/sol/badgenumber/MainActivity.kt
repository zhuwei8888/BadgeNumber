package com.sol.badgenumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sol.badgenumber.ui.theme.BadgeNumberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BadgeNumberTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Row {
                        Greeting(
                            name = "Android",
                            modifier = Modifier
                                .padding(innerPadding)
                                .clickable {
                                    BadgeNumberUtils.setBadgeNumber(1)
                                }
                        )
                        Greeting(
                            name = "Android1",
                            modifier = Modifier
                                .padding(innerPadding)
                                .clickable {
                                    BadgeNumberUtils.clearBadgeNumber()
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BadgeNumberTheme {
        Greeting("Android")
    }
}