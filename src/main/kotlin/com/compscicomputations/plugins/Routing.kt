package com.compscicomputations.plugins

import com.compscicomputations.authentication.authenticateAdmin
import com.compscicomputations.authentication.google.GoogleToken
import com.compscicomputations.routing.authRouting
import com.compscicomputations.routing.publicRouting
import com.compscicomputations.services.auth.models.Admins
import com.compscicomputations.services.auth.models.Users
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
        getA {
            call.respondText("Hello CompSci Computations API!")
        }
        staticResources("/.well-known", "well-known")

        authRouting()
        publicRouting()

//        post("upload") {
//            val file = File("uploads/ktor_logo.png")
//            call.receiveChannel().copyAndClose(file.writeChannel())
////        call.respondText("A file is uploaded"+String(file.readBytes()))
//            call.respondFile(file)
//        }
        authenticate {
            getA("/test/auth") {
                val principal = call.principal<GoogleToken>()
                call.respondText("Hello\n$principal")
            }
        }

        getA("/email/send") {
            try {
                val emailAddress = System.getenv("EMAIL_ADDR")
                val emailPassword = System.getenv("EMAIL_PASS")

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
                                <p>Hi {{user_name}},</p>
                                <p>You have requested a password reset for your account on {{your_website_link}}.</p>
                                <p>To reset your password, please click the following link:</p>
                                <a href="{{reset_password_link}}" class="button">Reset Password</a>
                                <p>This link will expire in {{expiration_time}}.</p>
                                <p>If you did not request a password reset, please ignore this email.</p>
                                <p>Best regards,</p>
                                <p>{{your_company_name}}</p>
                            </div>
                        </div>
                    </body>
                    </html>
                """.trimIndent()
                )
                email.addTo("sams.mndebele@gmail.com")
                val send = email.send()
                call.respond(HttpStatusCode.OK, send)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }

        }
    }

    // Redirects
    val resourcesFormat = plugin(Resources).resourcesFormat
    routing {
        authenticateAdmin {
//            get<Admins.Me> { call.respondRedirect(href(resourcesFormat, Users.Me())) }
            get<Admins.Id> { call.respondRedirect(href(resourcesFormat, Users.Id(id = it.id))) }
        }
    }
}