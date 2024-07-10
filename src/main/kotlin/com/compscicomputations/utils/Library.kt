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
    argsFunc: PreparedStatement.() -> Unit = {},
    transform: Connection.(ResultSet) -> T
): List<T>? {
    val result = arrayListOf<T>()
    prepareStatement(sql).apply {
        argsFunc()
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

//inline fun Connection.executeNested(
//    @Language("postgresql") sql: String,
//    argsFunc: PreparedStatement.() -> Unit = {},
//    execute: Connection.(ResultSet) -> Unit
//) {
//    prepareStatement(sql).apply {
//        argsFunc()
//        val resultSet = executeQuery()
//        while (resultSet.next()) execute(resultSet)
//    }
//}

//class PGEnum<T : Enum<T>>(val enumTypeName: String, enumValue: T?) : PGobject(), SQLType {
//    init {
//        value = enumValue?.name
//        type = enumTypeName
//    }
//
//    override fun getName(): String = enumTypeName
//
//    override fun getVendor(): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun getVendorTypeNumber(): Int {
//        return Types.VARCHAR
//    }
//}


//fun Transaction.executeNested(
//    @Language("postgresql") sql: String,
//    args: Iterable<Pair<IColumnType, Any?>> = emptyList(),
//    execute: Transaction.(ResultSet) -> Unit
//) {
//    exec(sql, args) {
//        execute(it)
//    }
//}
//
//fun <T : Any> Transaction.execute(
//    @Language("postgresql") sql: String,
//    args: Iterable<Pair<IColumnType, Any?>> = emptyList(),
//    transform: (ResultSet) -> T
//): List<T> {
//    val result = arrayListOf<T>()
//    exec(sql, args) { rs ->
//        while (rs.next()) result += transform(rs)
//    }
//    return result
//}
//
//fun Transaction.execute(
//    @Language("postgresql") sql: String,
//    args: Iterable<Pair<IColumnType, Any?>> = emptyList()
//) {
//    exec(sql, args)
//}