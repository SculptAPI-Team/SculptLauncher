package org.thelauncher.sculptlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.thelauncher.sculptlauncher.ui.router.RouterIndex
import org.thelauncher.sculptlauncher.ui.screen.HomeScreen
import org.thelauncher.sculptlauncher.ui.theme.SculptTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        activity = this
        setContent {
            SculptTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = RouterIndex.HomePage) {
                        composable<RouterIndex.HomePage> { HomeScreen() }
                    }
                }
            }
        }
    }

    companion object {
        lateinit var activity: MainActivity
    }
}