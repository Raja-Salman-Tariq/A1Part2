package com.example.testapp01

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.testapp01.db.utils.DrinkViewModel
import com.example.testapp01.notification_and_bcast_rcvrs.NetworkChangeBcastRcvr
import com.example.testapp01.notification_and_bcast_rcvrs.SnackbarUtility

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        myRef = this
        myDrinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
//        showSnackbar=true
    }

    fun getViewModel(): DrinkViewModel? {
        return myDrinkViewModel
    }

    override fun onResume() {
        super.onResume()
        snackbar()
    }

    companion object {

        public var myRef : BaseActivity? = null
        public var myDrinkViewModel : DrinkViewModel? = null
//        public var showSnackbar = true

        public fun handleSnackbar() {
//            if (showSnackbar)
                SnackbarUtility(myRef!!, myRef?.findViewById(android.R.id.content)!!).showSnackbar()
//            else showSnackbar=true
        }

        private fun snackbar() {
            myRef?.registerReceiver(
                NetworkChangeBcastRcvr(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }
}