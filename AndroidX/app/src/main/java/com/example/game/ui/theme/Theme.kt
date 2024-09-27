package com.example.game.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
public val lightMoradoScheme = lightColorScheme(
    primary = morado_primaryLight,
    onPrimary = morado_onPrimaryLight,
    primaryContainer = morado_primaryContainerLight,
    onPrimaryContainer = morado_onPrimaryContainerLight,
    secondary = morado_secondaryLight,
    onSecondary = morado_onSecondaryLight,
    secondaryContainer = morado_secondaryContainerLight,
    onSecondaryContainer = morado_onSecondaryContainerLight,
    tertiary = morado_tertiaryLight,
    onTertiary = morado_onTertiaryLight,
    tertiaryContainer = morado_tertiaryContainerLight,
    onTertiaryContainer = morado_onTertiaryContainerLight,
    error = morado_errorLight,
    onError = morado_onErrorLight,
    errorContainer = morado_errorContainerLight,
    onErrorContainer = morado_onErrorContainerLight,
    background = morado_backgroundLight,
    onBackground = morado_onBackgroundLight,
    surface = morado_surfaceLight,
    onSurface = morado_onSurfaceLight,
    surfaceVariant = morado_surfaceVariantLight,
    onSurfaceVariant = morado_onSurfaceVariantLight,
    outline = morado_outlineLight,
    outlineVariant = morado_outlineVariantLight,
    scrim = morado_scrimLight,
    inverseSurface = morado_inverseSurfaceLight,
    inverseOnSurface = morado_inverseOnSurfaceLight,
    inversePrimary = morado_inversePrimaryLight,
    surfaceDim = morado_surfaceDimLight,
    surfaceBright = morado_surfaceBrightLight,
    surfaceContainerLowest = morado_surfaceContainerLowestLight,
    surfaceContainerLow = morado_surfaceContainerLowLight,
    surfaceContainer = morado_surfaceContainerLight,
    surfaceContainerHigh = morado_surfaceContainerHighLight,
    surfaceContainerHighest = morado_surfaceContainerHighestLight,
)

public val darkMoradoScheme = darkColorScheme(
    primary = morado_primaryDark,
    onPrimary = morado_onPrimaryDark,
    primaryContainer = morado_primaryContainerDark,
    onPrimaryContainer = morado_onPrimaryContainerDark,
    secondary = morado_secondaryDark,
    onSecondary = morado_onSecondaryDark,
    secondaryContainer = morado_secondaryContainerDark,
    onSecondaryContainer = morado_onSecondaryContainerDark,
    tertiary = morado_tertiaryDark,
    onTertiary = morado_onTertiaryDark,
    tertiaryContainer = morado_tertiaryContainerDark,
    onTertiaryContainer = morado_onTertiaryContainerDark,
    error = morado_errorDark,
    onError = morado_onErrorDark,
    errorContainer = morado_errorContainerDark,
    onErrorContainer = morado_onErrorContainerDark,
    background = morado_backgroundDark,
    onBackground = morado_onBackgroundDark,
    surface = morado_surfaceDark,
    onSurface = morado_onSurfaceDark,
    surfaceVariant = morado_surfaceVariantDark,
    onSurfaceVariant = morado_onSurfaceVariantDark,
    outline = morado_outlineDark,
    outlineVariant = morado_outlineVariantDark,
    scrim = morado_scrimDark,
    inverseSurface = morado_inverseSurfaceDark,
    inverseOnSurface = morado_inverseOnSurfaceDark,
    inversePrimary = morado_inversePrimaryDark,
    surfaceDim = morado_surfaceDimDark,
    surfaceBright = morado_surfaceBrightDark,
    surfaceContainerLowest = morado_surfaceContainerLowestDark,
    surfaceContainerLow = morado_surfaceContainerLowDark,
    surfaceContainer = morado_surfaceContainerDark,
    surfaceContainerHigh = morado_surfaceContainerHighDark,
    surfaceContainerHighest = morado_surfaceContainerHighestDark,
)

public val lightBlueScheme = lightColorScheme(
    primary = azul_primaryLight,
    onPrimary = azul_onPrimaryLight,
    primaryContainer = azul_primaryContainerLight,
    onPrimaryContainer = azul_onPrimaryContainerLight,
    secondary = azul_secondaryLight,
    onSecondary = azul_onSecondaryLight,
    secondaryContainer = azul_secondaryContainerLight,
    onSecondaryContainer = azul_onSecondaryContainerLight,
    tertiary = azul_tertiaryLight,
    onTertiary = azul_onTertiaryLight,
    tertiaryContainer = azul_tertiaryContainerLight,
    onTertiaryContainer = azul_onTertiaryContainerLight,
    error = azul_errorLight,
    onError = azul_onErrorLight,
    errorContainer = azul_errorContainerLight,
    onErrorContainer = azul_onErrorContainerLight,
    background = azul_backgroundLight,
    onBackground = azul_onBackgroundLight,
    surface = azul_surfaceLight,
    onSurface = azul_onSurfaceLight,
    surfaceVariant = azul_surfaceVariantLight,
    onSurfaceVariant = azul_onSurfaceVariantLight,
    outline = azul_outlineLight,
    outlineVariant = azul_outlineVariantLight,
    scrim = azul_scrimLight,
    inverseSurface = azul_inverseSurfaceLight,
    inverseOnSurface = azul_inverseOnSurfaceLight,
    inversePrimary = azul_inversePrimaryLight,
    surfaceDim = azul_surfaceDimLight,
    surfaceBright = azul_surfaceBrightLight,
    surfaceContainerLowest = azul_surfaceContainerLowestLight,
    surfaceContainerLow = azul_surfaceContainerLowLight,
    surfaceContainer = azul_surfaceContainerLight,
    surfaceContainerHigh = azul_surfaceContainerHighLight,
    surfaceContainerHighest = azul_surfaceContainerHighestLight,
)

public val darkBlueScheme = darkColorScheme(
    primary = azul_primaryDark,
    onPrimary = azul_onPrimaryDark,
    primaryContainer = azul_primaryContainerDark,
    onPrimaryContainer = azul_onPrimaryContainerDark,
    secondary = azul_secondaryDark,
    onSecondary = azul_onSecondaryDark,
    secondaryContainer = azul_secondaryContainerDark,
    onSecondaryContainer = azul_onSecondaryContainerDark,
    tertiary = azul_tertiaryDark,
    onTertiary = azul_onTertiaryDark,
    tertiaryContainer = azul_tertiaryContainerDark,
    onTertiaryContainer = azul_onTertiaryContainerDark,
    error = azul_errorDark,
    onError = azul_onErrorDark,
    errorContainer = azul_errorContainerDark,
    onErrorContainer = azul_onErrorContainerDark,
    background = azul_backgroundDark,
    onBackground = azul_onBackgroundDark,
    surface = azul_surfaceDark,
    onSurface = azul_onSurfaceDark,
    surfaceVariant = azul_surfaceVariantDark,
    onSurfaceVariant = azul_onSurfaceVariantDark,
    outline = azul_outlineDark,
    outlineVariant = azul_outlineVariantDark,
    scrim = azul_scrimDark,
    inverseSurface = azul_inverseSurfaceDark,
    inverseOnSurface = azul_inverseOnSurfaceDark,
    inversePrimary = azul_inversePrimaryDark,
    surfaceDim = azul_surfaceDimDark,
    surfaceBright = azul_surfaceBrightDark,
    surfaceContainerLowest = azul_surfaceContainerLowestDark,
    surfaceContainerLow = azul_surfaceContainerLowDark,
    surfaceContainer = azul_surfaceContainerDark,
    surfaceContainerHigh = azul_surfaceContainerHighDark,
    surfaceContainerHighest = azul_surfaceContainerHighestDark,
)
//aqui agragremos Themetype
enum class ThemeType{RED, GREEN}



@Composable
fun GameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    /*val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }*/

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}