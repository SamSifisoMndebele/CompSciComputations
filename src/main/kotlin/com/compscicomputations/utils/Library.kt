package com.compscicomputations.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

suspend inline fun <T> dbQuery(
    connection: Connection,
    crossinline block: suspend Connection.() -> T
): T = withContext(Dispatchers.IO) {
    connection.block()
}

inline fun <T : Any> Connection.executeQuery(
    @Language("postgresql") sql: String,
    args: PreparedStatement.() -> Unit = {},
    transform: Connection.(ResultSet) -> T
): List<T>? {
    val result = arrayListOf<T>()
    prepareStatement(sql).apply {
        args()
        val resultSet = executeQuery()
        while (resultSet.next()) result += transform(resultSet)
    }
    return result.ifEmpty { null }
}

inline fun Connection.executeUpdate(
    @Language("postgresql") sql: String,
    argsFunc: PreparedStatement.() -> Unit = {},
) {
    prepareStatement(sql).apply {
        argsFunc()
        executeUpdate()
    }
}