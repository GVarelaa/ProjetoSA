package com.example.elderwatch.utils

import android.app.Activity
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object DataSender {
    fun sendToken(token: String) {
        val db = FirebaseFirestore.getInstance()
        val uid = UserManager.uid

        val tokenField = hashMapOf(
            "token" to token
        )

        if (uid != null) {
            db.collection("users")
                .document(uid)
                .update(tokenField as Map<String, Any>)
        }
    }
    fun sendLocation(location: Location) {
        val db = FirebaseFirestore.getInstance()
        val uid = UserManager.uid
        val locationValues = hashMapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "timestamp" to FieldValue.serverTimestamp()
        )
        val location = hashMapOf(
            "location" to locationValues
        )

        if (uid != null) {
            db.collection("users")
                .document(uid)
                .update(location as Map<String, Any>)
        }
    }
    fun sendNotification(endpoint: String, token: Any?, title: String, body: String) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val json = """
            {
                "token": "$token",
                "title": "$title",
                "body": "$body"
            }
        """.trimIndent()

        val body = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(endpoint)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Response: ${response.body?.string()}")
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
            }
        })
    }

    fun notifyContacts() {
        val db = FirebaseFirestore.getInstance()
        val contacts = UserManager.contacts

        if (contacts != null) {
            val tokens = mutableSetOf<String>()

            for (contact in contacts) {
                db.collection("users")
                    .document(contact.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val token = document.get("token")

                            if (token != null && !tokens.contains(token)) {
                                tokens.add(token.toString())

                                val endpoint = "http://10.0.2.2:5000/send"
                                val title = "Alerta de queda!"
                                val body = "O utilizador ${UserManager.email} possivelmente sofreu uma queda."

                                sendNotification(endpoint, token, title, body)
                            }
                        }
                    }
            }
        }
    }
}