package com.example.elderwatch.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("RECEIVE", "From: ${message.from}")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            Log.d("RECEIVE", "Message data payload: ${message.data}")

            /*
            // Check if data needs to be processed by long running job
            if (needsToBeScheduled()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }*/
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            Log.d("RECEIVE", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val db = FirebaseFirestore.getInstance()

        val tokenField = hashMapOf(
            "token" to token
        )

        UserManager.uid?.let {
            db.collection("users")
                .document(it)
                .update(tokenField as Map<String, Any>)
        }
    }

}