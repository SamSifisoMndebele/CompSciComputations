package com.compscicomputations.utils

import org.intellij.lang.annotations.Language

const val EMAIL_REGEX = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

val phoneNumberRegex = Regex("^(0|\\+?27[ -]?)[5-9]\\d[ -]?\\d{3}[ -]?\\d{4}$")

@Language("html")
val RESET_PASSWORD_EMAIL = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset</title>
    <style>
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
        .content {
            margin-bottom: 20px;
        }
        .otp {
            background-color: #EE2737;
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
        <div class="content">
            <p>Hi {{email_to}},</p>
            <p>You have requested a password reset for your account on <b>CompSci Computations</b> app.</p>
            <p>To reset your password, use the following OTP:</p>
            <h1 class="otp"><b>{{otp}}</b></h1>
            <p>This OTP will expire at <b>{{expiration_time}}</b>.</p>
            <p>If you did not ask to reset your password, you can ignore this email.</p>
            <p>Best regards,<br>Your <b>CompSci Computations</b> team</p>
        </div>
    </div>
</body>
</html>
""".trimIndent()

@Language("html")
val EMAIL_VERIFICATION_EMAIL = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset</title>
    <style>
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
        .content {
            margin-bottom: 20px;
        }
        .otp {
            background-color: #EE2737;
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
        <div class="content">
            <p>Hi {{email_to}},</p>
            <p>You have requested an email verification for your email above on <b>CompSci Computations</b> app.</p>
            <p>To verify your email, use the following OTP:</p>
            <h1 class="otp"><b>{{otp}}</b></h1>
            <p>This OTP will expire at <b>{{expiration_time}}</b>.</p>
            <p>If you did not ask for email verification, you can ignore this email.</p>
            <p>Best regards,<br>Your <b>CompSci Computations</b> team</p>
        </div>
    </div>
</body>
</html>
""".trimIndent()

@Language("html")
val FEEDBACK_EMAIL = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset</title>
    <style>
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
        .content {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="content">
            <p><b>From:</b> {{email_from}}</p>
            <p><b>Subject:</b> {{subject}}</p>
            <hr>
            <p><b>Message:</b></p>
            <p>{{message}}</p>
            {{suggestion_html}}
        </div>
    </div>
</body>
</html>
""".trimIndent()