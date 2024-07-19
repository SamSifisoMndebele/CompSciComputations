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


inline fun <T> Connection.executeQuery(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): List<T> {
    val result = mutableListOf<T>()
    val stmt = prepareStatement(sql)
    stmt.args()
    val resultSet = stmt.executeQuery()
    while (resultSet.next()) result += transform(resultSet)
    return result
}

inline fun <T> Connection.executeQueryOrNull(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): List<T>? = executeQuery(sql, transform, args).ifEmpty { null }

inline fun <T> Connection.executeQuerySingle(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): T {
    val stmt = prepareStatement(sql)
    stmt.args()
    val resultSet = stmt.executeQuery()
    if (!resultSet.first()) throw NoSuchElementException("ResultSet is empty.")
    if (resultSet.next()) throw IllegalArgumentException("ResultSet has more than one row.")
    resultSet.first()
    return transform(resultSet)
}
inline fun <T> Connection.executeQuerySingleOrNull(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): T? = try { executeQuerySingle(sql, transform, args) } catch (e: NoSuchElementException) { null }




inline fun Connection.executeUpdate(
    @Language("postgresql") sql: String,
    args: PreparedStatement.() -> Unit = {},
): Int {
    val stmt = prepareStatement(sql)
    stmt.args()
    return stmt.executeUpdate()
}