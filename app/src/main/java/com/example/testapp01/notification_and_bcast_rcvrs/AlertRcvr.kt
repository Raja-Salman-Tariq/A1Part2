package com.example.testapp01.notification_and_bcast_rcvrs

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlertRcvr : BroadcastReceiver() {
    override fun onReceive(ctxt: Context?, intent: Intent?) {
        val notifHelper = NotificationHelper(ctxt!!, "0", "Reminder Channel",
            NotificationManager.IMPORTANCE_HIGH)
        val notifBilder : NotificationCompat.Builder = notifHelper.getChannelNotif()

        notifHelper.getMgr()?.notify(0, notifBilder.build())
    }
}