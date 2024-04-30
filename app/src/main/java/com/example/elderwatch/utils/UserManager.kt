package com.example.elderwatch.utils

import com.google.firebase.Timestamp

object UserManager {
    var uid: String? = null
    var email: String? = null
    var contacts: MutableList<Contact>? = null
    var falls: MutableList<Timestamp>? = null
}