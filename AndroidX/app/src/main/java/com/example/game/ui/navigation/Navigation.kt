package com.example.game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.game.ui.presentation.screens.TicTacToeGame
import com.example.navigationbrunelajpc.ui.presentation.screens.CalcUPeU
import com.example.navigationbrunelajpc.ui.presentation.screens.HomeScreen
import com.example.navigationbrunelajpc.ui.presentation.screens.ProfileScreen
import com.example.navigationbrunelajpc.ui.presentation.screens.ScanSurface
import com.example.navigationbrunelajpc.ui.presentation.screens.SettingsScreen

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("profile") { ProfileScreen() }
        composable("settings") { SettingsScreen() }
        composable("calc"){ CalcUPeU() }
        composable("3enraya"){ TicTacToeGame()}
        composable("QR"){ ScanSurface(navController = navController) }
    }
}
