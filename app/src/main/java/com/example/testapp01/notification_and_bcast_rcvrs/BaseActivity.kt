package com.example.testapp01.notification_and_bcast_rcvrs

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testapp01.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        myRef = this
        snackbar()
    }

    private fun snackbar() {
        registerReceiver(NetworkChangeBcastRcvr(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    companion object {
        public var myRef : BaseActivity? = null
        public fun handleSnackbar() {
            SnackbarUtility(myRef!!, myRef?.findViewById(android.R.id.content)!!).showSnackbar()
        }
    }
}