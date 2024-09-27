package com.example.sunscout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sunscout.ui.theme.Bakbak
import com.example.sunscout.ui.theme.ButtonColor
import com.example.sunscout.ui.theme.LightRed
import com.example.sunscout.ui.theme.Sun
import com.example.sunscout.ui.theme.Wine


@Composable
fun GreetingScreen(navController: NavController) {
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
        Image(
            painter = painterResource(id = R.drawable.sun),
            contentDescription = "image of sunhat",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "SunScout",
            fontFamily = Bakbak,
            color = Sun,
            fontSize = 64.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Your trusty UV index companion!",
            color = ButtonColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("login") },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            modifier = Modifier
                .size(width = 350.dp, height = 70.dp)
                .padding(10.dp)
        ) {
            Text("Login", style = TextStyle(fontSize = 17.sp),fontWeight = FontWeight.Bold,)
        }
        Button(
            onClick = { navController.navigate("register") },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            modifier = Modifier
                .size(width = 350.dp, height = 70.dp)
                .padding(10.dp)
        ) {
            Text("Register", style = TextStyle(fontSize = 17.sp),fontWeight = FontWeight.Bold,)
        }
        // Spacer to push the guest button to the bottom
        Spacer(modifier = Modifier.weight(1f))

    }
}
