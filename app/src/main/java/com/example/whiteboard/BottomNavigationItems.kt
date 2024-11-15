package com.example.whiteboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, "home"),
    BottomNavItem("Mars Image", Icons.Default.Search, "search"),
    BottomNavItem("Settings", Icons.Default.Settings, "profile")
)

