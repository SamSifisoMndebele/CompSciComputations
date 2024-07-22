package com.compscicomputations.plugins

import com.compscicomputations.routing.authRouting
import com.compscicomputations.routing.publicRouting
import com.compscicomputations.services.auth.models.Admin
import com.compscicomputations.services.auth.models.Users
import com.compscicomputations.services.auth.models.response.User
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.mail.DefaultAuthenticator
import io.ktor.server.routing.get as getA

fun Application.configureRouting() {

    routing {
        staticResources("/.well-known", "well-known")

        getA {
            call.respondText("Hello CompSci Computations API!")
        }

        getA("/test/email") {
            call.respondRedirect("/test/email/sams.mndebele@gmail.com")
        }

        getA("/test/email/{email}") {
            try {
                val emailAddress = System.getenv("EMAIL_ADDR")
                val emailPassword = System.getenv("EMAIL_PASS")

                val emailTo = call.parameters["email"]!!

                val email = org.apache.commons.mail.HtmlEmail()
                email.hostName = "smtp.gmail.com"
                email.setSmtpPort(465)
                email.setAuthenticator(DefaultAuthenticator(emailAddress, emailPassword))
                email.isSSLOnConnect = true
                email.setFrom(emailAddress)
                email.subject = "Password Reset"
                email.setHtmlMsg(
                    """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Password Reset</title>
                        <style>
                            /* Basic styling, adjust as needed */
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f2f2f2;
                                padding: 20px;
                            }
                            .container {
                                max-width: 600px;
                                margin: 0 auto;
                                background-color: #fff;
                                padding: 20px;
                                border-radius: 5px;
                            }
                            .header {
                                text-align: center;
                                margin-bottom: 20px;
                            }
                            .content {
                                margin-bottom: 20px;
                            }
                            .button {
                                background-color: #4CAF50;
                                color: white;
                                padding: 15px 25px;
                                text-align: center;
                                text-decoration: none;
                                display: inline-block;
                                border-radius: 5px;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Password Reset Request</h1>
                            </div>
                            <div class="content">
                                <p>Hi $emailTo,</p>
                                <p>You have requested a password reset for your account on CompSci Computations App.</p>
                                <p>To reset your password, please click the following link:</p>
                                <a href="{{reset_password_link}}" class="button">Reset Password</a>
                                <p>This link will expire in {{expiration_time}}.</p>
                                <p>If you did not request a password reset, please ignore this email.</p>
                                <p>Best regards,</p>
                                <p>CompSci Computations Team</p>
                            </div>
                        </div>
                    </body>
                    </html>
                """.trimIndent()
                )
                email.addTo(emailTo)
                val send = email.send()
                call.respond(HttpStatusCode.OK, send)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }

        }

        authRouting()
        publicRouting()
    }

    // Redirects
//    val resourcesFormat = plugin(Resources).resourcesFormat
//    routing {
//        href(resourcesFormat, Users.Me())
//        authenticateAdmin {
//        }
//    }
}