package com.example.testapp01

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.testapp01.db.utils.*
import android.graphics.drawable.ColorDrawable

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import android.content.DialogInterface
import android.view.*
import androidx.lifecycle.lifecycleScope


class MainActivity : AppCompatActivity() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var myFragmentPagerAdapter: MyFragmentPagerAdapter     // fragment+titles list
    lateinit var myViewPager: ViewPager                                     // "eagerly" create+manage "pgs"
    private lateinit var tabLayout: TabLayout                               // horizontal layout+tabs/tabing
    lateinit var addBtn: ImageButton                                        // ...img btn ._.
    lateinit var drinkViewModel: DrinkViewModel                             // explained in class
    lateinit var tvBuffer: TextView
    // info text view

    //-----------------------------------------------





    /*###############################################
    * -----      I N I T   &   S E T U P       -----*
    * =============================================*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        //----------------------
        handleSetup()
    }






    //-----------------------------------------------



    private fun handleSetup() {
        tvBuffer = findViewById(R.id.tvBuffer)

        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
        drinkViewModel.mAllDrinks?.observe(this
        ) { if (drinkViewModel.mAllDrinks?.value?.isEmpty() == true)
            tvBuffer.visibility=View.VISIBLE
            else
            tvBuffer.visibility=View.INVISIBLE

        }

        handleFrags()       // setUp frag pager ada as well as view pager

        handleTabs()        // get view, add tabs via viewPager, set listener, handle visibility

        handleAddBtn()      // get view, add listener
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
        addBtn = findViewById(R.id.myAddBtn)                        //  listener; creates new obj
        addBtn.setOnClickListener(View.OnClickListener {            //  & inserts into db
            val curr:Int =myViewPager.currentItem
            val nDrinks=drinkViewModel.mAllDrinks?.value?.size

            DialogueUtility(this, "Add A New Drink",R.drawable.add_btn,nDrinks!!).show();
        })
    }


    //-----------------------------------------------


}