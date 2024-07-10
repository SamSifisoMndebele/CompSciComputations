package com.compscicomputations.utils

import android.content.Context
import java.io.File
import java.util.Date


fun Context.createImageFile(): File {
    return File.createTempFile(
        "image_" + Date().time.toString(), ".jpg",
        externalCacheDir
    )
}