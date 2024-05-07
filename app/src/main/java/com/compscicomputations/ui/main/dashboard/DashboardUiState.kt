package com.compscicomputations.ui.main.dashboard

import android.net.Uri
import com.compscicomputations.ui.auth.FieldError
import com.compscicomputations.ui.auth.UserType

data class DashboardUiState(
    val userType: UserType = UserType.STUDENT,
    val names: String = "CompSci",
    val lastname: String = "Computations",
    val email: String = "computations@compsci.com",
    val imageUri: Uri? =
        Uri.parse("12https://lh3.googleusercontent.com/-AYRB9IzbH-I/AAAAAAAAAAI/AAAAAAAAAAA/ALKGfklcowkjEE1uiyruWJr594ri-91U2g/photo.jpg?sz=46")
    , //null,

    val errors: Set<FieldError> = setOf()
)
