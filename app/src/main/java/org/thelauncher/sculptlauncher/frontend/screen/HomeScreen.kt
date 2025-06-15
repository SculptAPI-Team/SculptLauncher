package org.thelauncher.sculptlauncher.frontend.screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.thelauncher.sculptlauncher.LauncherApp
import org.thelauncher.sculptlauncher.MainActivity
import org.thelauncher.sculptlauncher.backend.launcher.GamePreloader
import org.thelauncher.sculptlauncher.frontend.activity.MinecraftActivity

@Composable
fun HomeScreen() {
    var handler = PreloadHandler()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column {
        Button({
            scope.launch(Dispatchers.Default) {
                GamePreloader(
                    LauncherApp.mAbstractMCPE,
                    preloadListener = object : GamePreloader.GamePreloadListener() {
                        override fun onFinish(bundle: Bundle) {
                            val message = Message()
                            message.what = 1
                            message.data = bundle
                            handler.sendMessage(message)
                        }
                    }
                ).preload(context)
            }
        }) {
            Text("Open the game")
        }
    }
}

class PreloadHandler: Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what) {
            1 -> {
                val intent = Intent(
                    MainActivity.activity.applicationContext,
                    MinecraftActivity::class.java
                )
                intent.putExtras(msg.data)
                MainActivity.activity.startActivity(intent)
                MainActivity.activity.finish()
            }
        }
    }
}