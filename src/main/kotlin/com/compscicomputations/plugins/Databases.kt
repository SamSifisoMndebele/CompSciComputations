package com.compscicomputations.plugins

import java.sql.Connection
import java.sql.DriverManager

private var connection: Connection? = null
fun databaseConnection(): Connection {

    if (connection == null || connection!!.isValid(0).not()) {
        Class.forName("org.postgresql.Driver")

        val dbName =    System.getenv("DB_NAME")
        val host =      System.getenv("DB_HOST")
        val port =      System.getenv("DB_PORT")
        val user =      System.getenv("DB_USER")
        val password =  System.getenv("DB_PASS")

        connection = DriverManager.getConnection("jdbc:postgresql://$host:$port/$dbName?ssl=require", user, password)
    }
    return connection!!
}
