package com.example.testapp01

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.testapp01.db.utils.*


class MainActivity : AppCompatActivity() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    lateinit var myFragmentPagerAdapter: MyFragmentPagerAdapter     // fragment+titles list
    lateinit var myViewPager: ViewPager                             // "eagerly" create+manage "pgs"
    lateinit var tabLayout: TabLayout                               // horizontal layout+tabs/tabing
    lateinit var addBtn: ImageButton                                // ...img btn ._.
    lateinit var drinkViewModel: DrinkViewModel                     // explained in class
    //-----------------------------------------------





    /*###############################################
    * -----      I N I T   &   S E T U P       -----*
    * =============================================*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        //----------------------
        handleSetup()
    }
    //-----------------------------------------------



    private fun handleSetup() {
        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)

        handleFrags()

        handleTabs()

        handleAddBtn()
    }
    //-----------------------------------------------





    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    private fun handleFrags() {
//        myFragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        myViewPager = findViewById(R.id.fragmentContainer)
        setupViewPager(myViewPager)
        setViewPager(0)
    }
    //-----------------------------------------------
    private fun handleTabs() {
        tabLayout = findViewById(R.id.myTabLayout)
        tabLayout.setupWithViewPager(myViewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.tab_icon_all)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.tab_icon_fav)

//        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (myViewPager.currentItem==0) {
//                    Log.d("tab", "on fav TabSelected: ")
//                    (myFragmentPagerAdapter.getItem(1) as FragmentFav)
//                        .acquireData((myFragmentPagerAdapter.getItem(0) as FragmentAll).data)
//                    Log.d("tab", "on fav done w TabSelected: ")
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                Log.d("tab", "onTabUnselected: ")
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                Log.d("tab", "onTabReselected: ")
//            }
//        })
    }
    //-----------------------------------------------
    private fun handleAddBtn() {
        addBtn = findViewById(R.id.myAddBtn)
        addBtn.setOnClickListener(View.OnClickListener {
            val curr:Int =myViewPager.currentItem
//            if (curr != 0 && curr != 1)
//                Toast.makeText(this, "uhh... $curr", Toast.LENGTH_SHORT).show()
//
            when (curr) {
                0 -> Toast.makeText(this, "All acc", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "Fav acc", Toast.LENGTH_SHORT).show()
            }

            val newDrink = Drink(// uses curr
                0,
                "Drink " + curr + " " + (drinkViewModel.mAllDrinks?.value?.size?.plus(1)),
                "this is ay drink...",
                curr == 1
            )
//            Toast.makeText(this, "new drink is fav: ${newDrink.fav}", Toast.LENGTH_SHORT).show()
            GlobalScope.launch {drinkViewModel.insert(newDrink)}
        })
    }
    //-----------------------------------------------
    private fun setupViewPager(viewPager: ViewPager) {
        myFragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        myFragmentPagerAdapter.addFrag(FragmentAll(drinkViewModel), "All Drinks")
        myFragmentPagerAdapter.addFrag(FragmentFav(drinkViewModel), "Favourite Drinks")
        viewPager.adapter = myFragmentPagerAdapter
    }
    //-----------------------------------------------
    private fun setViewPager(fragmentPos: Int) {
        myViewPager.currentItem = fragmentPos
    }
}