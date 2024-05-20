package com.ssmnd.pdf_viewer

import android.content.Context
import android.os.Environment
import android.util.Log
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import java.io.File
import java.io.IOException

fun Context.createEncryptedPdf(fileName: String = "my_crypt",
                               inputFile: File = Environment.getExternalStoragePublicDirectory(
                                   Environment.DIRECTORY_DOCUMENTS),
                               ownerPass: String = "owner_pass",
                               userPass: String = "user_pass") : File?
{
    try {
        val document = PDDocument.load(inputFile)

        val ap = AccessPermission().apply { setReadOnly() }
        val spp = StandardProtectionPolicy(ownerPass, userPass, ap)
        spp.setEncryptionKeyLength(256)

        val tempFile = File.createTempFile(fileName, ".pdf", externalCacheDir)
        document.protect(spp)
        document.save(tempFile)
        document.close()

        return tempFile
    } catch (e: IOException) {
        Log.e("PdfBox-Android", "Exception thrown while creating PDF for encryption", e)
    }
    return null
}
