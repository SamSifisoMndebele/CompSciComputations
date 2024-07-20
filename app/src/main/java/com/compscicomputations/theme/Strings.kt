package com.compscicomputations.theme

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


val namesRegex = Regex("^[A-Za-z ]{2,}$")
val emailRegex = Regex("^[A-Za-z0-9._+\\-\']+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
val phoneNumberRegex = Regex("^((\\+27|27)|0)[- ]?(\\d{2})[- ]?(\\d{3})[- ]?(\\d{4})$")
val strongPasswordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*_]).{6,}$")
val moduleCodeRegex = Regex("^[A-Z]{4,5}[0-9]{3}$")


val offlineContent = buildAnnotatedString {
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append("Offline:")
    }
    append(" Content may be out of date")
}
val offlineAuthentication = buildAnnotatedString {
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append("Offline:")
    }
    append(" Authentication may not be successful")
}

const val hintUsertype = "Register as _"
const val hintAdminCode = "Admin Code *"
const val hintProfileImage = "Profile image"
const val hintNames = "Names *"
const val hintLastname = "Lastname *"
const val hintEmail = "Email *"
const val hintPhone = "Phone"
const val hintPassword = "Password *"
const val hintPasswordConfirm = "Confirm password *"

const val placeholderEmail = "Enter your email. (example@mail.com)"

//const val welcomeMessage = """
//    Welcome To CompSci Computations,
//    Your Digital Laboratory For Exploring The Depths Of Mathematics and Computer Science!
//"""

const val loremIpsum = """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris
        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus
        dui nec risus. Maecenas non sodales nisi, vel dictum dolor. Class aptent taciti sociosqu ad
        litora torquent per conubia nostra, per inceptos himenaeos. Suspendisse blandit eleifend
        diam, vel rutrum tellus vulputate quis. Aliquam eget libero aliquet, imperdiet nisl a,
        ornare ex. Sed rhoncus est ut libero porta lobortis. Fusce in dictum tellus.
        
        Suspendisse interdum ornare ante. Aliquam nec cursus lorem. Morbi id magna felis. Vivamus
        egestas, est a condimentum egestas, turpis nisl iaculis ipsum, in dictum tellus dolor sed
        neque. Morbi tellus erat, dapibus ut sem a, iaculis tincidunt dui. Interdum et malesuada
        fames ac ante ipsum primis in faucibus. Curabitur et eros porttitor, ultricies urna vitae,
        molestie nibh. Phasellus at commodo eros, non aliquet metus. Sed maximus nisl nec dolor
        bibendum, vel congue leo egestas.
        
        Sed interdum tortor nibh, in sagittis risus mollis quis. Curabitur mi odio, condimentum sit
        amet auctor at, mollis non turpis. Nullam pretium libero vestibulum, finibus orci vel,
        molestie quam. Fusce blandit tincidunt nulla, quis sollicitudin libero facilisis et. Integer
        interdum nunc ligula, et fermentum metus hendrerit id. Vestibulum lectus felis, dictum at
        lacinia sit amet, tristique id quam. Cras eu consequat dui. Suspendisse sodales nunc ligula,
        in lobortis sem porta sed. Integer id ultrices magna, in luctus elit. Sed a pellentesque
        est.
        
        Aenean nunc velit, lacinia sed dolor sed, ultrices viverra nulla. Etiam a venenatis nibh.
        Morbi laoreet, tortor sed facilisis varius, nibh orci rhoncus nulla, id elementum leo dui
        non lorem. Nam mollis ipsum quis auctor varius. Quisque elementum eu libero sed commodo. In
        eros nisl, imperdiet vel imperdiet et, scelerisque a mauris. Pellentesque varius ex nunc,
        quis imperdiet eros placerat ac. Duis finibus orci et est auctor tincidunt. Sed non viverra
        ipsum. Nunc quis augue egestas, cursus lorem at, molestie sem. Morbi a consectetur ipsum, a
        placerat diam. Etiam vulputate dignissim convallis. Integer faucibus mauris sit amet finibus
        convallis.
        
        Phasellus in aliquet mi. Pellentesque habitant morbi tristique senectus et netus et
        malesuada fames ac turpis egestas. In volutpat arcu ut felis sagittis, in finibus massa
        gravida. Pellentesque id tellus orci. Integer dictum, lorem sed efficitur ullamcorper,
        libero justo consectetur ipsum, in mollis nisl ex sed nisl. Donec maximus ullamcorper
        sodales. Praesent bibendum rhoncus tellus nec feugiat. In a ornare nulla. Donec rhoncus
        libero vel nunc consequat, quis tincidunt nisl eleifend. Cras bibendum enim a justo luctus
        vestibulum. Fusce dictum libero quis erat maximus, vitae volutpat diam dignissim."""