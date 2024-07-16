package com.compscicomputations.utils

import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom

internal interface PasswordEncryptorContrast2 {
    /**
     * Generate a random password
     * @param isWithLetters Boolean value to specify if the password must contain letters
     * @param isWithUppercase Boolean value to specify if the password must contain uppercase letters
     * @param isWithNumbers Boolean value to specify if the password must contain numbers
     * @param isWithSpecial Boolean value to specify if the password must contain special chars
     * @param length Int value with the length of the password
     * @return the new password.
     */
    fun generatePassword(
        isWithLetters: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = false,
        length: Int = 8
    ): String

    /**
     * Validate plain password with the hashed password
     * @return `true` if passwords match, otherwise `false`
     */
    fun validatePassword(password: String, hashedPassword: String): Boolean

    /**
     * Encrypt a password to a hash
     * @return the hash password.
     */
    fun encryptPassword(password: String): String
}

internal object PasswordEncryptor2 : PasswordEncryptorContrast2 {
    private const val LETTERS: String = "_abcdefghijklmnopqrstuvwxyz"
    private const val UPPERCASE_LETTERS: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val NUMBERS: String = "0123456789"
    private const val SPECIAL: String = "`~!@#%^&*+?*-="

    override fun generatePassword(
        isWithLetters: Boolean,
        isWithUppercase: Boolean,
        isWithNumbers: Boolean,
        isWithSpecial: Boolean,
        length: Int
    ): String {
        var contains = ""
        if (isWithLetters) contains += LETTERS
        if (isWithUppercase) contains += UPPERCASE_LETTERS
        if (isWithNumbers) contains += NUMBERS
        if (isWithSpecial) contains += SPECIAL
        val rnd = SecureRandom.getInstance("SHA1PRNG")
        return StringBuilder().apply {
            repeat(length) {
                append(contains[rnd.nextInt(contains.length)])
            }
        }.toString()
    }

    override fun validatePassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }

    override fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

}