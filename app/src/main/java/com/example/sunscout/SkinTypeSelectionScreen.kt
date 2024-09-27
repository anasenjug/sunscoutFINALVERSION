package com.example.sunscout

import SkinType
import UserViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sunscout.ui.theme.ButtonColor
import com.example.sunscout.ui.theme.LightRed
import com.example.sunscout.ui.theme.SkinDark
import com.example.sunscout.ui.theme.SkinLight
import com.example.sunscout.ui.theme.SkinMedium
import com.example.sunscout.ui.theme.Wine

@Composable
fun SkinTypeSelectionScreen(userViewModel: UserViewModel, navController: NavController) {
    val selectedSkinType = remember { mutableStateOf<SkinType?>(null) }

    // Display success message and navigate back
    LaunchedEffect(userViewModel.successMessage.value) {
        userViewModel.successMessage.value?.let {
            navController.popBackStack("merged_home", false)
            userViewModel.clearSuccessMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightRed, Wine)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center the items vertically in the column
    ) {
        Text("Select your skin type:")
        Spacer(modifier = Modifier.height(20.dp))

        // Clickable Circle Composable
        @Composable
        fun ClickableCircle(
            onClick: () -> Unit,
            color: Color,
            label: String,
            radius: Dp = 50.dp
        ) {
            Row(
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(
                    modifier = Modifier.size(radius * 2)
                ) {
                    drawCircle(
                        color = color,
                        radius = radius.toPx(),
                        center = Offset(size.width / 2, size.height / 2)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, color = ButtonColor) // Set the label color to dark gray
            }
        }

        // Create two rows with two clickable circles in each row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = LightRed),
            horizontalAlignment = Alignment.CenterHorizontally // Center circles horizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Space the circles evenly in the row
            ) {
                ClickableCircle(
                    onClick = {
                        selectedSkinType.value = SkinType.Light
                        userViewModel.updateSkinTypeInFirestore(SkinType.Light)
                    },
                    color = SkinLight,
                    label = "Light"
                )

                ClickableCircle(
                    onClick = {
                        selectedSkinType.value = SkinType.Medium
                        userViewModel.updateSkinTypeInFirestore(SkinType.Medium)
                    },
                    color = SkinMedium,
                    label = "Medium"
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add spacing between rows

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Space the circles evenly in the row
            ) {
                ClickableCircle(
                    onClick = {
                        selectedSkinType.value = SkinType.Dark
                        userViewModel.updateSkinTypeInFirestore(SkinType.Dark)
                    },
                    color = SkinDark,
                    label = "Dark"
                )

                ClickableCircle(
                    onClick = {
                        selectedSkinType.value = null
                        userViewModel.updateSkinTypeInFirestore(null)
                    },
                    color = Color.Gray,
                    label = "None"
                )
            }
        }
    }
}