package com.rkzmn.securesecrets.presentation

data class FormState(
    val enteredSecret: String = "",
    val revealedSecret: String = "",
    val isLoading: Boolean = false,
    val showSecret: Boolean = false,
)