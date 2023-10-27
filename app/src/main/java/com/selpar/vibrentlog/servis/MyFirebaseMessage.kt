package com.selpar.vibrentlog.servis

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessage:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val bildirimTitle:String?=message.notification!!.title
        val bildirimBody:String?=message.notification!!.body
        Log.d("FCM: ",bildirimTitle.toString()+" Body: "+bildirimBody)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}