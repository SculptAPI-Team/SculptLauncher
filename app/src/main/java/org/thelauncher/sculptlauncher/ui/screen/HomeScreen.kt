package org.thelauncher.sculptlauncher.ui.screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppsOutage
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.thelauncher.sculptlauncher.GamePlayActivity
import org.thelauncher.sculptlauncher.LauncherApp
import org.thelauncher.sculptlauncher.MainActivity
import org.thelauncher.sculptlauncher.R
import org.thelauncher.sculptlauncher.backend.launcher.GamePreloader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var openMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gameNotInstalled = LauncherApp.mAbstractMCPE.minecraftInfo
        .minecraftPackageContext.packageName == context.packageName
    val gameVersion = LauncherApp.mAbstractMCPE.minecraftInfo.minecraftVersionName
    val handler = PreloadHandler()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton({ openMenu = true }) {
                        Icon(Icons.Default.MoreVert, "")
                    }
                    DropdownMenu(expanded = openMenu, onDismissRequest = { openMenu = !openMenu}) {
                        DropdownMenuItem(
                            text = { Text("NMod管理") },
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = { Text("Preference") },
                            onClick = {}
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!gameNotInstalled && (gameVersion == "1.21.0.03")) {
                FloatingActionButton({
                    scope.launch(Dispatchers.IO) {
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
                    Icon(Icons.Default.PlayCircleFilled, "")
                }
            }
        }
    ) { pd ->
        Column(Modifier.padding(pd).padding(horizontal = 16.dp)) {
            // 游戏未安装时的提示条
            if (gameNotInstalled) {
                ListItem(
                    headlineContent = { Text("游戏未安装") },
                    leadingContent = {
                        Icon(Icons.Default.Warning, "")
                    },
                    supportingContent = {
                        Text("你需要先安装游戏才能使用本软件。")
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                )
            }
            // 版本不匹配时提出
            if (gameVersion != "1.21.0.03") {
                Card {
                    ListItem(
                        headlineContent = { Text("当前安装的版本") },
                        supportingContent = {
                            Text("你当前已经安装了：${gameVersion}，但它是不符合要求的，请安装1.21.0.03后继续。")
                        },
                        leadingContent = {
                            Icon(Icons.Default.AppsOutage, "")
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
            Image(
                painterResource(R.drawable.wallpaper), "",
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .height(200.dp)
            )
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
                    GamePlayActivity::class.java
                )
                intent.putExtras(msg.data)
                MainActivity.activity.startActivity(intent)
                MainActivity.activity.finish()
            }
        }
    }
}