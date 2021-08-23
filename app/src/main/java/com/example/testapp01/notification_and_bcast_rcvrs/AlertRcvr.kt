package com.example.testapp01.notification_and_bcast_rcvrs

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.testapp01.BaseActivity
import com.example.testapp01.R
import com.example.testapp01.db.utils.Drink
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random.Default.nextInt

class AlertRcvr : BroadcastReceiver() {

    var imgId: Int = R.drawable.drink_icon
    var msg = "Meal Time !"

    override fun onReceive(ctxt: Context?, intent: Intent?) {
        handleData()

        val notifHelper = NotificationHelper(ctxt!!, "0", "Reminder Channel",
            NotificationManager.IMPORTANCE_HIGH, msg, imgId)
        val notifBilder : NotificationCompat.Builder = notifHelper.getChannelNotif()

        notifHelper.getMgr()?.notify(0, notifBilder.build())
    }


    //-------------------------------------------------------------------------------------

    private fun handleData() {
        Log.d("datalertcvr", "onReceive: ")


        val data = BaseActivity.myDrinkViewModel?.mAllDrinks?.value
        val drink : Drink = data?.get(nextInt(data?.size))!!

        drink.toGet=true

        GlobalScope.launch {
            BaseActivity.myDrinkViewModel?.upd(drink)
        }

        Log.d("datalertcvr", "onReceive: $drink")


        drink.let{
            msg= drink?.name!!

            when (drink?.id?.rem(4)){
                0   ->  imgId=R.drawable.one
                1   ->  imgId=R.drawable.two
                2   ->  imgId=R.drawable.three
                3   ->  imgId=R.drawable.four
            }
        }
    }
}