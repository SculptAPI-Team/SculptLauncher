package org.thelauncher.sculptlauncher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.thelauncher.sculptlauncher.backend.launcher.GamePreloader
import org.thelauncher.sculptlauncher.ui.theme.SculptLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            val handler = PreloadHandler()
            SculptLauncherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    companion object {
        lateinit var activity: MainActivity
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
    SculptLauncherTheme {
        Greeting("Android")
    }
}

class PreloadHandler: Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what) {
            1 -> {
                val intent = Intent(
                    MainActivity.activity.applicationContext,
                    GamePlayActivity::class.java
                )
                intent.putExtras(msg.data)
                MainActivity.activity.startActivity(intent)
                MainActivity.activity.finish()
            }
        }
    }
}