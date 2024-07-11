package com.compscicomputations.plugins

import java.sql.Connection
import java.sql.DriverManager

fun connectToPostgres(): Connection {
    // TODO: Put in environment variables only
    val dbName =    System.getenv("DB_NAME")?:"devdb"
    val host =      System.getenv("DB_HOST")?:"postgres-compsci-computations.l.aivencloud.com"
    val port =      System.getenv("DB_PORT")?:"18131"
    val user =      System.getenv("DB_USER")?:"avnadmin"
    val password =  System.getenv("DB_PASS")?:"AVNS_JQOkWN3xz_sowwY4rg1"

    Class.forName("org.postgresql.Driver")
    return DriverManager.getConnection("jdbc:postgresql://$host:$port/$dbName?ssl=require", user, password)
}