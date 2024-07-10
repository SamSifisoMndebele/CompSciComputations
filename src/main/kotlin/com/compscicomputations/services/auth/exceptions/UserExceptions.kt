package com.compscicomputations.services.auth.exceptions

import com.compscicomputations.services.auth.models.User

class NoSuchUserException(
    message: String = "A user associated with the uid does not exists, or it was deleted."
): NoSuchElementException(message)

private class IllegalUserState : IllegalArgumentException("There are more than one users associated to the uid.")

/**
 * Returns the single user.
 * @return [User]
 * @throws NoSuchUserException if the user isn't found.
 * @throws IllegalUserState if it has more than one user.
 */
internal fun Iterable<User>?.singleOrNoSuchUserException(
    message: String = "A user associated with the uid does not exists, or it was deleted."
): User {
    if (this == null) throw NoSuchUserException(message)
    when (this) {
        is List -> when (size) {
            0 -> throw NoSuchUserException(message)
            1 -> return this[0]
            else -> throw IllegalUserState()
        }
        else -> {
            val iterator = iterator()
            if (!iterator.hasNext()) throw NoSuchUserException(message)
            val single = iterator.next()
            if (iterator.hasNext()) throw IllegalUserState()
            return single
        }
    }
}