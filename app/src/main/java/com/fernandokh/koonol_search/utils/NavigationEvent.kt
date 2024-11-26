package com.fernandokh.koonol_search.utils

sealed class NavigationEvent() {
    data object Navigate : NavigationEvent()
}