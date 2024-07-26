package com.compscicomputations.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

suspend inline fun <T: Any?> dbQuery(
    connection: Connection,
    crossinline block: suspend Connection.() -> T
): T = withContext(Dispatchers.IO) {
    connection.block()
}


inline fun <T: Any> Connection.query(
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

inline fun <T: Any> Connection.executeQueryOrNull(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): List<T>? = query(sql, transform, args).ifEmpty { null }

inline fun <T: Any> Connection.queryFirst(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): T {
    val stmt = prepareStatement(sql)
    stmt.args()
    val resultSet = stmt.executeQuery()
    if (!resultSet.next()) throw NoSuchElementException("ResultSet is empty.")
    return resultSet.transform()
}

inline fun <T: Any> Connection.queryLast(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): T {
    val stmt = prepareStatement(sql)
    stmt.args()
    val resultSet = stmt.executeQuery()
    if (!resultSet.last()) throw NoSuchElementException("ResultSet is empty.")
    return resultSet.transform()
}

inline fun <T: Any> Connection.querySingle(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): T {
    val stmt = prepareStatement(sql)
    stmt.args()
    val resultSet = stmt.executeQuery()
    if (!resultSet.next()) throw NoSuchElementException("ResultSet is empty.")
    val result = resultSet.transform()
    if (resultSet.next()) throw IllegalArgumentException("ResultSet has more than one row.")
    return result
}

inline fun <T: Any> Connection.querySingleOrNull(
    @Language("postgresql") sql: String,
    transform: ResultSet.() -> T,
    args: PreparedStatement.() -> Unit = {},
): T? = try { querySingle(sql, transform, args) } catch (e: NoSuchElementException) { null }


inline fun Connection.update(
    @Language("postgresql") sql: String,
    args: PreparedStatement.() -> Unit = {},
) {
    val stmt = prepareStatement(sql)
    stmt.args()
    stmt.executeUpdate()
}