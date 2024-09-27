import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.*
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sunscout.R
import com.example.sunscout.ui.theme.Bakbak
import com.example.sunscout.ui.theme.ButtonColor
import com.example.sunscout.ui.theme.LightRed
import com.example.sunscout.ui.theme.Sun
import com.example.sunscout.ui.theme.Wine



@Composable
fun UvIndexScreen(navController: NavController, uvIndex: Double?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightRed, Wine)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
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

            when (uvIndex?.toInt()) {
                in 0..2 -> {
                    // Static image for UV Index 0-2
                    Image(
                        painter = painterResource(id = R.drawable.indicator), // Replace with your static image resource
                        contentDescription = "Low UV",
                        modifier = Modifier.size(400.dp)
                    )
                }
                in 3..5 -> {
                    // Lottie animation for UV Index 3-5 (uvlow)
                    MyLottieAnimation(animationResId = R.raw.uvlow) // Replace with actual animation
                }
                in 6..7 -> {
                    // Lottie animation for UV Index 6-7 (uvmoderate)
                    MyLottieAnimation(animationResId = R.raw.uvmoderate) // Replace with actual animation
                }
                in 8..10 -> {
                    // Lottie animation for UV Index 8-10 (uvhigh)
                    MyLottieAnimation(animationResId = R.raw.uvhigh) // Replace with actual animation
                }
                else -> {
                    // Lottie animation for UV Index 11+ (uvextreme)
                    MyLottieAnimation(animationResId = R.raw.uvextreme) // Replace with actual animation
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Text(
            text = "${uvIndex?.toInt()}",
            fontFamily = Bakbak,
            color = Sun,
            fontSize = 64.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MyLottieAnimation(animationResId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1 // Optional: Loop animation
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(400.dp)
    )
}

