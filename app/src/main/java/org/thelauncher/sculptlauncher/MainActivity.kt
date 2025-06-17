package org.thelauncher.sculptlauncher

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.thelauncher.sculptlauncher.backend.AppRouter
import org.thelauncher.sculptlauncher.frontend.screen.HomeScreen
import org.thelauncher.sculptlauncher.frontend.theme.SculptLauncherTheme
import org.thelauncher.sculptlauncher.frontend.viewmodel.SharedViewModel
import org.thelauncher.sculptlauncher.hooker.SCHooker

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setOnExitAnimationListener { splash ->
                val view = splash.view
                val animation = ObjectAnimator.ofFloat(
                    view, View.ALPHA, 1f, 0f
                )
                animation.duration = 400L
                animation.doOnEnd { splash.remove() }
                animation.start()
            }
        }
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)
        activity = this
        val sharedViewModel: SharedViewModel by viewModels()
        setContent {
            val screen by sharedViewModel.shouldShowUI.collectAsState()
            SculptLauncherTheme {
                if (screen) {
                    val navController = rememberNavController()
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(stringResource(R.string.app_name))
                                }
                            )
                        }
                    ) { pd ->
                        NavHost(
                            navController = navController,
                            startDestination = AppRouter.Main,
                            modifier = Modifier.padding(pd)
                        ) {
                            composable<AppRouter.Main> { HomeScreen() }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Block, "",
                            modifier = Modifier
                                .size(90.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            stringResource(R.string.app_blocked_not_installed),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }

    companion object {
        lateinit var activity: MainActivity
        init {
            val hk = SCHooker()
            println(hk.stringFromJNI())
        }
    }
}