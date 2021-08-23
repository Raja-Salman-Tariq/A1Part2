package com.example.testapp01.notification_and_bcast_rcvrs

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import com.example.testapp01.R
import com.google.android.material.snackbar.Snackbar

class SnackbarUtility(val ctxt:Context, val view: View) {

    public fun isConnectedToNetwork(context: Context) =
            null != (context.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager).activeNetworkInfo

    public fun showSnackbar(){

        var msg = String()

        msg = when (isConnectedToNetwork(ctxt)){
            true    -> "You've reconnected with a network ! Syncing now... "
            false   -> "You're not connected to the internet. Please check your connection to get remote data."
        }

        val mySnack = Snackbar.make(ctxt,
            view,
            msg, Snackbar.LENGTH_INDEFINITE)
            .setTextColor(Color.parseColor("#E4B363"))
            .setBackgroundTint(Color.parseColor("#313638"))
            .setAction("Close") { }
            .setActionTextColor(Color.parseColor("#E8E9EB"))

        mySnack.view.translationY= -75F
        mySnack.show()
        Log.d("bcastrcv", "showSnackbar: ")
    }

}