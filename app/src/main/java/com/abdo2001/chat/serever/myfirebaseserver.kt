package com.abdo2001.chat.serever

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class myfirebaseserver: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("token",token)
    }
}