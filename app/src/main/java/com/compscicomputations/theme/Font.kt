package com.compscicomputations.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.compscicomputations.R

val comicNeueFamily = FontFamily(
    Font(R.font.comic_neue_light, FontWeight.Light),
    Font(R.font.comic_neue_bold_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.comic_neue, FontWeight.Normal),
    Font(R.font.comic_neue_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.comic_neue_bold, FontWeight.Bold),
    Font(R.font.comic_neue_bold_italic, FontWeight.Bold, FontStyle.Italic),
)