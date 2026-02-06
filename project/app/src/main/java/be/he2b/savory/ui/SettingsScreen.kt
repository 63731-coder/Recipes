package be.he2b.savory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import be.he2b.savory.R
import be.he2b.savory.ui.theme.ColorBlindMode
import be.he2b.savory.ui.theme.ThemeManager

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(primaryColor)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 40.dp, start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }

                Text(
                    text = stringResource(R.string.settings),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 24.dp)
                )
            }

            // CONTENT
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.accessibility_display),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.choose_a_color_profile_adapted_to_your_vision),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                // Options for color
                ThemeOptionItem(
                    title = stringResource(R.string.standard),
                    description = stringResource(R.string.default_colors),
                    isSelected = ThemeManager.currentMode == ColorBlindMode.Normal,
                    onClick = {
                        ThemeManager.saveTheme(context, ColorBlindMode.Normal)
                    })

                ThemeOptionItem(
                    title = stringResource(R.string.protanopia),
                    description = stringResource(R.string.for_red_blind_vision),
                    isSelected = ThemeManager.currentMode == ColorBlindMode.Protanopia,
                    onClick = {
                        ThemeManager.saveTheme(context, ColorBlindMode.Protanopia)
                    })

                ThemeOptionItem(
                    title = stringResource(R.string.deuteranopia),
                    description = stringResource(R.string.for_green_blind_vision),
                    isSelected = ThemeManager.currentMode == ColorBlindMode.Deuteranopia,
                    onClick = {
                        ThemeManager.saveTheme(context, ColorBlindMode.Deuteranopia)
                    })

                ThemeOptionItem(
                    title = stringResource(R.string.tritanopia),
                    description = stringResource(R.string.for_blue_blind_vision),
                    isSelected = ThemeManager.currentMode == ColorBlindMode.Tritanopia,
                    onClick = {
                        ThemeManager.saveTheme(context, ColorBlindMode.Tritanopia)
                    })
            }
        }
    }
}

@Composable
fun ThemeOptionItem(
    title: String, description: String, isSelected: Boolean, onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description, fontSize = 12.sp, color = Color.Gray
            )
        }
    }
}