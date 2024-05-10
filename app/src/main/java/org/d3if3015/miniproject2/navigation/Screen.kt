package org.d3if3015.miniproject2.navigation

sealed class Screen (val route: String){
    data object Home: Screen("mainScreen")
}