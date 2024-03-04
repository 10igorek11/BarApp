package com.example.barapp.presentation.screens.home

import com.example.barapp.R

sealed class BottomBar(
    val route: String,
    val title: Int,
    val titleLong: Int,
    val icon: Int,
) {
    object Tests : BottomBar(
        route = "TESTS",
        title = R.string.tests,
        titleLong = R.string.tests,
        icon = R.drawable.ic_tests,
    )

    object TechCards : BottomBar(
        route = "TECHCARDS",
        title = R.string.tech_cards_short,
        titleLong = R.string.tech_cards,
        icon = R.drawable.ic_tech_cards,
    )

    object Learning : BottomBar(
        route = "LEARNING",
        title = R.string.learning,
        titleLong = R.string.learning,
        icon = R.drawable.ic_learning,
    )

    object Users : BottomBar(
        route = "USERS",
        title = R.string.users,
        titleLong = R.string.users,
        icon = R.drawable.ic_users,
    )
}
