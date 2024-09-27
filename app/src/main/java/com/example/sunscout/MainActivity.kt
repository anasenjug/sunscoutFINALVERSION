package com.example.sunscout

import LocationWorker
import LoginScreen
import UserViewModel
import UvIndexScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sunscout.ui.theme.SunScoutTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val firebaseAuthService by lazy { FirebaseAuthService(FirebaseAuth.getInstance(),firestore) }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var userViewModel: UserViewModel // Declare UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationWorker = LocationWorker(applicationContext)
        FirebaseApp.initializeApp(this)
        userViewModel = UserViewModel()

        setContent {
            SunScoutTheme {
                scheduleNotifications()
                val navController = rememberNavController()
                val auth = FirebaseAuth.getInstance()
                val startDestination = if (auth.currentUser != null) "merged_home" else "greeting"
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("greeting") {
                            GreetingScreen(navController)
                        }
                        composable("login") {
                            LoginScreen(navController, firebaseAuthService)
                        }
                        composable("register") {
                            RegisterScreen(navController, firebaseAuthService)
                        }
                        composable("home") {
                            HomeScreen(navController, locationWorker, userViewModel)
                        }
                        composable("select_skin_type") {
                            SkinTypeSelectionScreen(userViewModel, navController,)
                        }
                        composable("view_uv_index/{uvIndex}") { backStackEntry ->
                            val uvIndex = backStackEntry.arguments?.getString("uvIndex")?.toDoubleOrNull()
                            UvIndexScreen(navController, uvIndex) // Pass uvIndex to UvIndexScreen
                        }
                        composable("test_uv") { backStackEntry ->
                            val uvIndex = backStackEntry.arguments?.getString("uvIndex")?.toDoubleOrNull()
                            UvIndexTestScreen(navController) // Pass uvIndex to UvIndexScreen
                        }
                        composable("merged_home"){
                            MergedUvHomeScreen(navController,
                                locationWorker,
                                userViewModel)
                        }
                    }
                }
            }
        }
    }

    private fun scheduleNotifications() {
        val workManager = WorkManager.getInstance(this)
        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(3, TimeUnit.HOURS) // Trigger immediately for testing
            .build()
        workManager.enqueue(notificationRequest)
    }
}
