package com.example.testapp01.notification_and_bcast_rcvrs

import android.R.attr
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.testapp01.DetailsActivity
import com.example.testapp01.MainActivity
import com.example.testapp01.R
import com.google.android.material.canvas.CanvasCompat
import kotlin.math.round
import android.R.attr.bitmap

import android.graphics.PorterDuffXfermode

import android.graphics.Bitmap




class NotificationHelper(val ctxt:Context, val channelId:String, val channelName:String, val imp:Int, val msg: String, val notifIcon: Int, var notifMgr:NotificationManager?=null)
    :ContextWrapper(ctxt) {

    private val contentText ="It's almost time for your meal. Click to view this drink !"

        init{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel()
            }
        }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val notifChnl = NotificationChannel(channelId, channelName, imp)
        getMgr()?.createNotificationChannel(notifChnl)
        notifChnl.enableLights(true)
        notifChnl.enableVibration(true)

        getMgr()?.createNotificationChannel(notifChnl)
    }

    public fun getMgr():NotificationManager? {
        if (notifMgr == null) {
            notifMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return notifMgr
    }

    public fun getChannelNotif():NotificationCompat.Builder{

        val pendIntend1 = PendingIntent.getActivity(this, 0, Intent(this, DetailsActivity::class.java),0)
//        val actionIntent= PendingIntent.getBroadcast(this, 0,Intent(this, ReminderNotificationReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val myLargeIcon = BitmapFactory.decodeResource(resources, notifIcon)
//
//        val output: Bitmap = if (myLargeIcon.width > myLargeIcon.height) {
//            Bitmap.createBitmap(myLargeIcon.height, myLargeIcon.height, Bitmap.Config.ARGB_8888)
//        } else {
//            Bitmap.createBitmap(myLargeIcon.width, myLargeIcon.width, Bitmap.Config.ARGB_8888)
//        }
//
//        val canvas = Canvas(output)
//
//        val color = Color.TRANSPARENT
//        val paint = Paint()
//        val rect = Rect(0, 0, myLargeIcon.width, myLargeIcon.height)
//
//        var r = 0f
//
//        r = if (myLargeIcon.width > myLargeIcon.height) {
//            myLargeIcon.height.toFloat() / 2
//        } else {
//            myLargeIcon.width.toFloat() / 2
//        }
//
//        paint.isAntiAlias = true
//        canvas.drawARGB(0, 0, 0, 0)
//        paint.color = color
//        canvas.drawCircle(r, r, r, paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawBitmap(myLargeIcon, rect, rect, paint)

//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources,myLargeIcon)
//        roundedBitmapDrawable.cornerRadius=50.0f
//        roundedBitmapDrawable.setAntiAlias(true)
//        roundedBitmapDrawable.isCircular=true

        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(msg)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.drink_icon)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendIntend1)
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, "Go !", pendIntend1)
            .setColor(Color.parseColor("#C8B78F"))
            .setPriority(imp)
            .setLargeIcon(myLargeIcon)
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(contentText)
//                    .setBigContentTitle(msg)
//            )
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(myLargeIcon)
                    .bigLargeIcon(null)
                    .setBigContentTitle(msg)
            )
    }
}