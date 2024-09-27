package com.example.sunscout

import LocationWorker
import SkinType
import UserViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sunscout.ui.theme.ButtonColor
import com.example.sunscout.ui.theme.LightRed
import com.example.sunscout.ui.theme.Wine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, locationWorker: LocationWorker, userViewModel: UserViewModel) {
    val scope = rememberCoroutineScope()
    var uvIndex by remember { mutableStateOf<Double?>(null) }
    var loading by remember { mutableStateOf(false) }
    val skinType by userViewModel.skinType // Retrieve the skin type from the ViewModel

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
        Text(text = "Welcome to Home Screen", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            scope.launch {
                loading = true
                uvIndex = locationWorker.fetchUVIndexManual() // Trigger the manual UV index fetch
                loading = false
            }
        },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),) {
            Text(if (loading) "Loading..." else "Check UV Index")
        }

        Spacer(modifier = Modifier.height(20.dp))

        uvIndex?.let { index ->
            Text("Current UV Index: $index", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Sun Protection Tips: ${getSunProtectionTips(index, skinType)}",
                style = MaterialTheme.typography.bodyLarge
            )
        } ?: Text("No UV Index available")

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

        Button(
            onClick = { navController.navigate("merged_home") },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            modifier = Modifier
                .padding(top = 16.dp)

        ) {
            Text("MERGED HOME")
        }

        Spacer(modifier = Modifier.height(20.dp))

        skinType?.let {
            Text("Your skin type is: ${it.name}", style = MaterialTheme.typography.bodyLarge)
        }

        Button(onClick = { userViewModel.updateSkinTypeInFirestore(null) },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),) {
            Text("Clear Skin Type")
        }
        Button(
            onClick = { navController.navigate("view_uv_index/$uvIndex") },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            modifier = Modifier
                .padding(top = 16.dp)

        ) {
            Text("View UV Index")
        }
        Button(
            onClick = { navController.navigate("test_uv") },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            modifier = Modifier
                .padding(top = 16.dp)

        ) {
            Text("View test UV Index")
        }
    }
}
fun getSunProtectionTips(uvIndex: Double?, skinType: SkinType?): String {
    return when {
        uvIndex == null -> "No UV index available; please check your location."
        skinType == null -> {
            // General tips based on UV index ranges without specific skin type
            when {
                uvIndex in 0.0..2.0 -> "Wear sunglasses and protective clothing, and limit sun exposure."
                uvIndex in 3.0..5.0 -> "Apply SPF 30+ sunscreen and limit direct sun exposure, especially during midday."
                uvIndex in 6.0..7.0 -> "Use SPF 50+ sunscreen, and avoid tanning; seek shade during peak hours."
                uvIndex in 8.0..10.0 -> "Stay indoors during peak hours if possible; if outside, use SPF 50+ and wear protective clothing."
                uvIndex >= 11.0 -> "Avoid outdoor activities; if necessary, use SPF 50+, wear long sleeves, and seek shade."
                else -> "No sun protection tips available."
            }
        }

        uvIndex in 0.0..2.0 -> when (skinType) {
            SkinType.Light -> "Extremely low risk, no need for protection."
            SkinType.Medium -> "Extremely low risk, no need for protection."
            SkinType.Dark -> "Extremely low risk, no need for protection."
        }
        uvIndex in 3.0..5.0 -> when (skinType) {
            SkinType.Light -> "High SPF is required; tanning sessions should not exceed 30 minutes."
            SkinType.Medium -> "Use SPF 30+, safe to tan for up to 1 hour."
            SkinType.Dark -> "SPF 15+ suffices, but full protection is encouraged; tanning time can be extended slightly."
        }
        uvIndex in 6.0..7.0 -> when (skinType) {
            SkinType.Light -> "Opt for SPF 50+, avoid tanning or limit to 15-20 minutes."
            SkinType.Medium -> "SPF 30+ is recommended, so keep tanning sessions under 45 minutes."
            SkinType.Dark -> "Use SPF 30+; cautious tanning for up to 1 hour is possible."
        }
        uvIndex in 8.0..10.0 -> when (skinType) {
            SkinType.Light -> "Avoid direct sun; if tanning, use SPF 50+ and limit to 15 minutes."
            SkinType.Medium -> "High SPF is crucial; consider tanning for only 30 minutes."
            SkinType.Dark -> "SPF 30+ advised with limited tanning time to avoid potential damage."
        }
        uvIndex >= 11.0 -> when (skinType) {
            SkinType.Light -> "Definitely prioritize staying indoors; if outside, use SPF 50 and seek shade. Tanning is not recommended."
            SkinType.Medium -> "Very high SPF and minimizing outdoor activities are necessary. Indirect tanning in the shade is possible for short periods."
            SkinType.Dark -> "High SPF essential; direct sun exposure should be avoided even with natural protection."
        }
        else -> "No sun protection tips available."
    }
}