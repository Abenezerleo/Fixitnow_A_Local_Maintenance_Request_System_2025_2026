package com.example.allapp

/**
 * AppRouter contains all the navigation routes used in the application.
 * All route names should be defined here to maintain consistency.
 */
object AppRouter {
    // Main screens
    const val HOME = "home"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val MAIN_MENU = "main_menu"
    const val CONTACT_SUPPORT = "contact"

    // Password reset flow
    const val REQUEST_PASSWORD_RESET = "ResetRequestScreen"
    const val VERIFY_RESET_CODE = "reset_verify_screen"
    const val PASSWORD_RESET = "password_reset_screen"

    // Navigation patterns with parameters
    const val PASSWORD_RESET_PATTERN = "password_reset_screen/{email}"
    const val VERIFY_RESET_CODE_PATTERN = "reset_verify_screen/{email}/{code}"
} 