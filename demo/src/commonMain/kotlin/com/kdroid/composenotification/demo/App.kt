package com.kdroid.composenotification.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdroid.composenotification.builder.Notification
import com.kdroid.kmplog.Log
import com.kdroid.kmplog.d
import com.kdroid.kmplog.w
import java.io.File

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Screen1) }
    var notificationMessage by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                Screen.Screen1 -> ScreenOne(
                    onNavigate = { currentScreen = Screen.Screen2 },
                    notificationMessage = notificationMessage,
                    onShowMessage = { message -> notificationMessage = message }
                )
                Screen.Screen2 -> ScreenTwo(
                    onNavigate = { currentScreen = Screen.Screen1 },
                    notificationMessage = notificationMessage,
                    onShowMessage = { message -> notificationMessage = message }
                )
            }
        }
    }
}

private const val RESOURCE_DIR = "src/jvmTest/resources/"
private const val TAG = "ComposeNotification"
val largeImagePath = getAbsolutePath("icon.png")

private fun getAbsolutePath(relativePath: String): String? {
    val file = File(RESOURCE_DIR, relativePath)
    return if (file.exists()) {
        file.absolutePath
    } else {
        Log.w(TAG, "Resource not found: ${file.path}.")
        null
    }
}

@Composable
fun ScreenOne(onNavigate: () -> Unit, notificationMessage: String?, onShowMessage: (String?) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Screen 1",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.primary
        )package com.kdroid.composenotification.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdroid.composenotification.builder.Notification
import com.kdroid.kmplog.Log
import com.kdroid.kmplog.d
import com.kdroid.kmplog.w
import java.io.File

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Screen1) }
    var showAdditionalText by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                Screen.Screen1 -> ScreenOne(
                    onNavigate = { currentScreen = Screen.Screen2 },
                    showAdditionalText = showAdditionalText,
                    onShowText = { showAdditionalText = it }
                )
                Screen.Screen2 -> ScreenTwo(
                    onNavigate = { currentScreen = Screen.Screen1 },
                    showAdditionalText = showAdditionalText,
                    onShowText = { showAdditionalText = it }
                )
            }
        }
    }
}

private const val RESOURCE_DIR = "src/jvmTest/resources/"
private const val TAG = "ComposeNotification"
val largeImagePath = getAbsolutePath("icon.png")

private fun getAbsolutePath(relativePath: String): String? {
    val file = File(RESOURCE_DIR, relativePath)
    return if (file.exists()) {
        file.absolutePath
    } else {
        Log.w(TAG, "Resource not found: ${file.path}.")
        null
    }
}

@Composable
fun ScreenOne(onNavigate: () -> Unit, showAdditionalText: Boolean, onShowText: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Screen 1",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigate,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Go to Screen 2")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Notification(
                    title = "Notification from Screen 1",
                    largeImagePath = largeImagePath,
                    message = "This is a test notification from Screen 1"
                ) {
                    Button("Show Message from Button 1") {
                        Log.d("NotificationLog", "Button 1 from Screen 1 clicked")
                        onShowText(true)
                    }
                    Button("Hide Message from Button 2") {
                        Log.d("NotificationLog", "Button 2 from Screen 1 clicked")
                        onShowText(false)
                    }
                    onActivated {
                        Log.d("NotificationLog", "Notification activated")
                    }
                    onDismissed { reason ->
                        Log.d("NotificationLog", "Notification dismissed: $reason")
                    }
                    onFailed {
                        Log.d("NotificationLog", "Notification failed")
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Send notification from Screen 1")
        }

        if (showAdditionalText) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Message from Button 1 is shown!", fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun ScreenTwo(onNavigate: () -> Unit, showAdditionalText: Boolean, onShowText: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Screen 2",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigate,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Go back to Screen 1")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Notification(
                    largeImagePath = largeImagePath,
                    title = "Notification from Screen 2",
                    message = "This is a test notification from Screen 2"
                ) {
                    Button("Show Message from Button 1") {
                        Log.d("NotificationLog", "Button 1 from Screen 2 clicked")
                        onShowText(true)
                    }
                    Button("Hide Message from Button 2") {
                        Log.d("NotificationLog", "Button 2 from Screen 2 clicked")
                        onShowText(false)
                    }
                    onActivated {
                        Log.d("NotificationLog", "Notification activated")
                    }
                    onDismissed { reason ->
                        Log.d("NotificationLog", "Notification dismissed: $reason")
                    }
                    onFailed {
                        Log.d("NotificationLog", "Notification failed")
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Send notification from Screen 2")
        }

        if (showAdditionalText) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Message from Button 1 is shown!", fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

enum class Screen {
    Screen1,
    Screen2
}

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigate,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Go to Screen 2")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Notification(
                    title = "Notification from Screen 1",
                    largeImagePath = largeImagePath,
                    message = "This is a test notification from Screen 1"
                ) {
                    Button("Show Message from Button 1") {
                        Log.d("NotificationLog", "Button 1 from Screen 1 clicked")
                        onShowMessage("Button 1 clicked from Screen 1's notification")
                    }
                    Button("Hide Message from Button 2") {
                        Log.d("NotificationLog", "Button 2 from Screen 1 clicked")
                        onShowMessage(null)
                    }
                    onActivated {
                        Log.d("NotificationLog", "Notification activated")
                    }
                    onDismissed { reason ->
                        Log.d("NotificationLog", "Notification dismissed: $reason")
                    }
                    onFailed {
                        Log.d("NotificationLog", "Notification failed")
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Send notification from Screen 1")
        }

        notificationMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun ScreenTwo(onNavigate: () -> Unit, notificationMessage: String?, onShowMessage: (String?) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Screen 2",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigate,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Go back to Screen 1")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Notification(
                    largeImagePath = largeImagePath,
                    title = "Notification from Screen 2",
                    message = "This is a test notification from Screen 2"
                ) {
                    Button("Show Message from Button 1") {
                        Log.d("NotificationLog", "Button 1 from Screen 2 clicked")
                        onShowMessage("Button 1 clicked from Screen 2's notification")
                    }
                    Button("Hide Message from Button 2") {
                        Log.d("NotificationLog", "Button 2 from Screen 2 clicked")
                        onShowMessage(null)
                    }
                    onActivated {
                        Log.d("NotificationLog", "Notification activated")
                    }
                    onDismissed { reason ->
                        Log.d("NotificationLog", "Notification dismissed: $reason")
                    }
                    onFailed {
                        Log.d("NotificationLog", "Notification failed")
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Send notification from Screen 2")
        }

        notificationMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

enum class Screen {
    Screen1,
    Screen2
}
