package com.compscicomputations.plugins

import java.sql.Connection
import java.sql.DriverManager

fun connectToPostgres(): Connection {
    val dbName =    System.getenv("DB_NAME")
    val host =      System.getenv("DB_HOST")
    val port =      System.getenv("DB_PORT")
    val user =      System.getenv("DB_USER")
    val password =  System.getenv("DB_PASS")

    Class.forName("org.postgresql.Driver")
    return DriverManager.getConnection("jdbc:postgresql://$host:$port/$dbName?ssl=require", user, password)
}