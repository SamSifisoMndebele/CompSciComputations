package com.compscicomputations.presentation.main.dashboard

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement


object DB {
    fun test() {
        var conn: Connection? = null
        var st: Statement? = null
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.postgresql.Driver")

            //STEP 3: Open a connection
            println("Connecting to database...")
            conn = DriverManager.getConnection(
                "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:6543/postgres",
                "postgres.noqdpkgtaehqvjqxbojt",
                "CompSciComputations"
            )

            //STEP 4: Execute a query
            println("Creating statement...")
            st = conn.createStatement()
            val sql = "SELECT uid, email FROM firebase_users"
            val rs = st.executeQuery(sql)

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                val uid = rs.getString("uid")
                val email = rs.getString("email")

                //Display values
                print(", uid: $uid")
                println(", email: $email")
            }
            //STEP 6: Cleanup environment
            rs.close()
            st.close()
            conn.close()
        } catch (se: SQLException) {
            //Handle errors for JDBC
            System.err.println("SQLException: $se")
        } catch (e: Exception) {
            //Handle errors for Class.forName
            System.err.println("Exception: $e")
        } finally {
            //finally block used to close resources
            try {
                st?.close()
            } catch (se2: SQLException) {
                System.err.println("SQLException: $se2")
            } // nothing we can do

            try {
                if (conn != null) conn.close()
            } catch (se: SQLException) {
                System.err.println("SQLException: $se")
            } //end finally try
        }
    }
}