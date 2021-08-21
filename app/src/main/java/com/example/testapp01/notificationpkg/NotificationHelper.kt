package com.example.testapp01.notificationpkg

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import androidx.core.app.NotificationCompat
import com.example.testapp01.DetailsActivity
import com.example.testapp01.MainActivity
import com.example.testapp01.R

class NotificationHelper(val ctxt:Context, val channelId:String, val channelName:String, val imp:Int, var notifMgr:NotificationManager?=null)
    :ContextWrapper(ctxt) {

        init{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel()
            }
        }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val notifChnl = NotificationChannel(channelId, channelName, imp)
        getMgr()?.createNotificationChannel(notifChnl)
    }

    public fun getMgr():NotificationManager? {
        if (notifMgr == null) {
            notifMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return notifMgr
    }

    public fun getChannelNotif():NotificationCompat.Builder{

        val pendIntend1 = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),0)
        val actionIntent= PendingIntent.getBroadcast(this, 0,Intent(this, NotificationReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT)


        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Meal Time !")
            .setContentText("It's almost time for your meal. Don't forget to have a drink !" +
                    "Click to see today's recommendation.")
            .setSmallIcon(R.drawable.drink_icon)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendIntend1)
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, "Go !", actionIntent)
            .setColor(Color.parseColor("#C8B78F"))
    }
}