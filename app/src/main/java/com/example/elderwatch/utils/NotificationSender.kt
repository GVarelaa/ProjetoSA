package com.example.elderwatch.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object NotificationSender {
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
            for (contact in contacts) {
                db.collection("users")
                    .document(contact.toString())
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val token = document.get("token")

                            if (token != null) {
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