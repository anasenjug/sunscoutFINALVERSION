package com.example.sunscout

import LocationWorker
import ShakeDetector
import UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sunscout.ui.theme.Bakbak
import com.example.sunscout.ui.theme.ButtonColor
import com.example.sunscout.ui.theme.LightRed
import com.example.sunscout.ui.theme.Sun
import com.example.sunscout.ui.theme.Wine
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun MergedUvHomeScreen(navController: NavController, locationWorker: LocationWorker, userViewModel: UserViewModel) {
    val scope = rememberCoroutineScope()
    var uvIndex by remember { mutableStateOf<Double?>(null) }
    var loading by remember { mutableStateOf(false) }
    val skinType by userViewModel.skinType // Retrieve the skin type from the ViewModel

        // Main content of the screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Add vertical scroll capability
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(LightRed, Wine)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Content goes here (e.g., welcome message, buttons, etc.)


            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Welcome to SunScout!", style = MaterialTheme.typography.headlineMedium)


            Spacer(modifier = Modifier.height(20.dp))

            UvIndexDisplay(uvIndex)

            // Automatically fetch UV index on loading
            LaunchedEffect(Unit) {
                loading = true
                uvIndex = locationWorker.fetchUVIndexManual() // Trigger the manual UV index fetch
                loading = false
            }

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Add padding around the text
                    contentAlignment = Alignment.Center // Center the text within the Box
                ) {
                    Text(
                        text = "Loading UV Index...",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp) // Increase font size
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Add padding around the text
                    contentAlignment = Alignment.Center // Center the text within the Box
                ) {
                    uvIndex?.let { index ->
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Tips: ${getSunProtectionTips(index, skinType)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp), // Increase font size
                            textAlign = TextAlign.Center // Center the text
                        )
                    } ?: Text(
                        text = "No UV Index available",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp) // Increase font size
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                scope.launch {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("greeting") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)) {
                Text("Logout")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("select_skin_type") },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text("Select Skin Type")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { userViewModel.updateSkinTypeInFirestore(null) },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            ) {
                Text("Clear Skin Type")
            }
            Spacer(modifier = Modifier.height(20.dp))
            skinType?.let {
                Text("Your skin type is: ${it.name}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

@Composable
fun UvIndexDisplay(uvIndex: Double?) {
    var isAnimating by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Lottie animation setup for shake
    val shakeComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.shake)) // Replace with your shake animation JSON
    val shakeProgress by animateLottieCompositionAsState(shakeComposition, isPlaying = isAnimating)

    // Shake detector setup
    val shakeDetector = remember { ShakeDetector(context) {
        isAnimating = true
    } }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            shakeDetector.unregister()
        }
    }

    // Reset isAnimating to false after shake animation completes
    LaunchedEffect(shakeProgress) {
        if (isAnimating && shakeProgress >= 1f) { // Check if the animation has completed
            isAnimating = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .background(color = ButtonColor)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center // Aligns items in the center
        ) {
            // Background image common to all animations
            Image(
                painter = painterResource(id = R.drawable.bg), // Replace with your background image resource
                contentDescription = "Background Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )

            // Determine which animation to show based on UV index
            when {
                uvIndex == null -> {
                    // Handle the case where uvIndex is null
                }
                uvIndex.toInt() in 0..2 -> {
                    // Static image for UV Index 0-2
                    Image(
                        painter = painterResource(id = R.drawable.indicator), // Replace with your static image resource
                        contentDescription = "Low UV",
                        modifier = Modifier.size(400.dp)
                    )
                }
                uvIndex.toInt() in 3..5 -> {
                    // Lottie animation for UV Index 3-5 (uvlow)
                    MyLottieAnimation(animationResId = R.raw.uvlow) // Replace with actual animation
                }
                uvIndex.toInt() in 6..7 -> {
                    // Lottie animation for UV Index 6-7 (uvmoderate)
                    MyLottieAnimation(animationResId = R.raw.uvmoderate) // Replace with actual animation
                }
                uvIndex.toInt() in 8..10 -> {
                    // Lottie animation for UV Index 8-10 (uvhigh)
                    MyLottieAnimation(animationResId = R.raw.uvhigh) // Replace with actual animation
                }
                else -> {
                    // Lottie animation for UV Index 11+ (uvextreme)
                    MyLottieAnimation(animationResId = R.raw.uvextreme) // Replace with actual animation
                }
            }

            // Show the shake animation when a shake is detected
            if (isAnimating) {
                MyLottieAnimation(animationResId = R.raw.shake) // Ensure shake animation is shown
            }
        }

        // Display the UV Index number
        Text(
            text = uvIndex?.toInt()?.toString() ?: "", // Display empty string if null
            fontFamily = Bakbak,
            color = Sun,
            fontSize = 64.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
