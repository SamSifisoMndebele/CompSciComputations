package com.compscicomputations.client.utils

import com.compscicomputations.client.auth.models.User


inline val User.displayName: String
    get() = "$names $lastname".trim()