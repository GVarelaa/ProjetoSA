package com.example.elderwatch.utils

import android.location.Location
import com.google.firebase.Timestamp

object UserManager {
    var uid: String? = null
    var email: String? = null
    var contacts: MutableList<Contact>? = null
    var falls: MutableList<Fall>? = null
    var location: Location? = null
}