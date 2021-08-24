package com.example.testapp01.notification_and_bcast_rcvrs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.testapp01.BaseActivity

class NetworkChangeBcastRcvr : BroadcastReceiver() {
    override fun onReceive(ctxt: Context?, intent: Intent?) {
        Log.d("bcastrcv", "onReceive: ${intent?.action}")
//        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent?.action){
//            SnackbarUtility(ctxt?.applicationContext!!,
//                View)
//                .showSnackbar()
//        }


        BaseActivity.handleSnackbar()
    }
}