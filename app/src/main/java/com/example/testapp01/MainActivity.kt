package com.example.testapp01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.testapp01.db.utils.*
import android.view.*
import android.widget.Toast
import com.example.testapp01.notification_and_bcast_rcvrs.AlertRcvr
import java.util.*
import com.example.testapp01.notification_and_bcast_rcvrs.SnackbarUtility


class MainActivity : BaseActivity() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var myFragmentPagerAdapter: MyFragmentPagerAdapter     // fragment+titles list
    lateinit var myViewPager: ViewPager                                     // "eagerly" create+manage "pgs"
    private lateinit var tabLayout: TabLayout                               // horizontal layout+tabs/tabing
    lateinit var addBtn: ImageButton                                        // ...img btn ._.
    lateinit var drinkViewModel: DrinkViewModel                             // explained in class
    lateinit var tvBuffer: TextView                                         // info text view


    //-----------------------------------------------





    /*###############################################
    * -----      I N I T   &   S E T U P       -----*
    * =============================================*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main1)
//        supportActionBar?.
//        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.app_bg))
        setSupportActionBar(findViewById(R.id.my_toolbar))
        //----------------------
        handleSetup()

//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        if (cm.activeNetworkInfo?.isConnected == true)
//            return

//        val mySnack = Snackbar.make(findViewById(android.R.id.content),
//            "You're not connected to the internet." +
//                    " Please check your connection to get remote data.", Snackbar.LENGTH_INDEFINITE)
//            .setTextColor(Color.parseColor("#E4B363"))
//            .setBackgroundTint(Color.parseColor("#313638"))
//            .setAction("Close") { }
//            .setActionTextColor(Color.parseColor("#E8E9EB"))
//
//        mySnack.view.translationY= -75F
//        mySnack.show()



//        handleSnackbar(this, findViewById(android.R.id.content))
    }


//    private fun handleSnackbar(ctxt:Context,v: View ){
//        SnackbarUtility(ctxt,v).showSnackbar()
//    }

    //-----------------------------------------------



    private fun handleSetup() {

        handleAlarmMgr()

        handleViewModelAndTvBuffer()

        handleFrags()       // setUp frag pager ada as well as view pager

        handleTabs()        // get view, add tabs via viewPager, set listener, handle visibility

        handleAddBtn()      // get view, add listener
    }

    private fun handleAlarmMgr() {
        val alarMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertRcvr::class.java)
        val pendIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarMgr?.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis, pendIntent)
        }
    }


    private fun handleViewModelAndTvBuffer() {
        tvBuffer = findViewById(R.id.tvBuffer)

//        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
        drinkViewModel = myDrinkViewModel!!

        if (drinkViewModel.mAllDrinks?.value?.isEmpty() == true){
//            Toast.makeText(this, "emptyyyy", Toast.LENGTH_SHORT).show()
            tvBuffer.text = "You have no drinks to list here. " +
                    "\nTap the button on the top right to add a new drink !"
            tvBuffer.visibility=View.VISIBLE
        }

        drinkViewModel.mAllDrinks?.observe(this
        ) {
            if (myViewPager.currentItem==0) {
                if (drinkViewModel.mAllDrinks?.value?.isEmpty() == true)
                    tvBuffer.visibility = View.VISIBLE
                else
                    tvBuffer.visibility = View.INVISIBLE
            }
        }
    }
    //-----------------------------------------------





    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    private fun handleFrags() {
        myViewPager = findViewById(R.id.fragmentContainer)
        setupViewPager(myViewPager)
        myViewPager.currentItem = 0
    }
    //-----------------------------------------------
    private fun setupViewPager(viewPager: ViewPager) {      // simple view pager (ada) setup
        myFragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        myFragmentPagerAdapter.addFrag(FragmentAll(drinkViewModel), "All Drinks")
        myFragmentPagerAdapter.addFrag(FragmentFav(drinkViewModel), "Favourite Drinks")

//        viewPager.setOnTouchListener { p0, p1 -> true }
        viewPager.adapter = myFragmentPagerAdapter

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val tvBuffer = findViewById<TextView>(R.id.tvBuffer)
                if (position==1) {                                       // addBtn
                    addBtn.visibility =
                        View.INVISIBLE                                    // visibility
                    Log.d("tab", "on fav done w TabSelected: $position")

                    if (drinkViewModel.favDrinks?.value?.size == 0) {                 // fav frag opened
                        tvBuffer.text = "You haven't marked any favourite drinks. " +
                                "\nCheck out the all drinks tab to choose some favourites !"
                        tvBuffer.visibility = View.VISIBLE
                    } else
                        tvBuffer.visibility = View.INVISIBLE
                } else {                                                             // all frag opened
                    tvBuffer.text = "You have no drinks to list here. " +
                            "\nTap the button on the top right to add a new drink !"
                    if (drinkViewModel.mAllDrinks?.value?.size == 0) {
                        tvBuffer.visibility = View.VISIBLE
                    } else
                        tvBuffer.visibility = View.INVISIBLE

                    addBtn.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
    //-----------------------------------------------
    private fun handleTabs() {
        tabLayout = findViewById(R.id.myTabLayout)
        tabLayout.setupWithViewPager(myViewPager)                           // internal method
        tabLayout.getTabAt(0)?.setIcon(R.drawable.tab_icon_all)       // adding new tabs
        tabLayout.getTabAt(1)?.setIcon(R.drawable.tab_icon_fav)
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E4B363"))

//        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{     // listener
//            override fun onTabSelected(tab: TabLayout.Tab?) {                           // for
//                val tvBuffer=findViewById<TextView>(R.id.tvBuffer)
//                if (myViewPager.currentItem==0) {                                       // addBtn
//                    addBtn.visibility=View.INVISIBLE                                    // visibility
//                    Log.d("tab", "on fav done w TabSelected: ")
//
//                    if (drinkViewModel.favDrinks?.value?.size==0) {                 // fav frag opened
//                        tvBuffer.text="You haven't marked any favourite drinks. " +
//                                "\nCheck out the all drinks tab to choose some favourites !"
//                        tvBuffer.visibility=View.VISIBLE
//                    }
//                    else
//                        tvBuffer.visibility=View.INVISIBLE
//                }
//                else{                                                             // all frag opened
//                    tvBuffer.text="You have no drinks to list here. " +
//                            "\nTap the button on the top right to add a new drink !"
//                    if (drinkViewModel.mAllDrinks?.value?.size==0) {
//                        tvBuffer.visibility=View.VISIBLE
//                    }
//                    else
//                        tvBuffer.visibility=View.INVISIBLE
//
//                    addBtn.visibility=View.VISIBLE
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
////                if (myViewPager.currentItem==1) {
////                    addBtn.visibility = View.VISIBLE
////                    Log.d("tab", "onTabUnselected: ")
////                }
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                Log.d("tab", "onTabReselected: ")
//            }
//        })
    }
    //-----------------------------------------------
    private fun handleAddBtn() {                                    // simply gets view, attaches
        addBtn = findViewById(R.id.myAddBtn)
        val bSheetHelper = BSheetHelper(this, window.decorView.rootView, drinkViewModel)
    }


    //-----------------------------------------------

    override fun onResume() {
        super.onResume()
//        showSnackbar=true
    }


}