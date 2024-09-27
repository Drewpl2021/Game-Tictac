package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.game.ui.theme.GameTheme
import com.example.game.ui.theme.ThemeType
import com.example.game.ui.theme.darkBlueScheme
import com.example.game.ui.theme.darkMoradoScheme
import com.example.game.ui.theme.lightBlueScheme
import com.example.game.ui.theme.lightMoradoScheme
import com.example.game.utils.conttexto
import com.example.game.utils.isNight
import com.example.navigationbrunelajpc.ui.presentation.component.MyAppDrawer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeType= remember{
                mutableStateOf(ThemeType.RED) }
            val darkThemex= isNight()
            val darkTheme = remember { mutableStateOf(darkThemex)
            }
            val colorScheme=when(themeType.value){
                ThemeType.RED->{if (darkTheme.value)
                    darkMoradoScheme else
                    lightMoradoScheme}
                ThemeType.GREEN->{if (darkTheme.value)
                    darkBlueScheme else
                    lightBlueScheme}
                else->{lightBlueScheme}
            }
            conttexto.CONTEXTO_APPX=this

            GameTheme(colorScheme = colorScheme) {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
                MyAppDrawer(darkMode = darkTheme , themeType = themeType)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GameTheme(colorScheme = lightBlueScheme) {
        Greeting("Android")
    }
}