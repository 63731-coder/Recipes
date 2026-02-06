package be.he2b.savory.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ColorBlindMode {
    Normal,
    Protanopia,
    Deuteranopia,
    Tritanopia
}

object ThemeManager {
    var currentMode by mutableStateOf(ColorBlindMode.Normal) // A variable that updates the UI when it changes

    private const val PREFS_NAME = "savory_settings"
    private const val KEY_THEME = "saved_theme"

    fun loadTheme(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedName = prefs.getString(
            KEY_THEME,
            "Normal"
        ) // Get the saved theme or use the default theme.

        currentMode = try {
            ColorBlindMode.valueOf(savedName ?: "Normal")
        } catch (_: Exception) {
            ColorBlindMode.Normal
        }
    }

    fun saveTheme(
        context: Context,
        mode: ColorBlindMode
    ) { // save when user changes the theme
        currentMode = mode // reload screen directly

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_THEME, mode.name) // save the name of the theme (ex: Protanopia)
        editor.apply() // validate the saving
    }
}