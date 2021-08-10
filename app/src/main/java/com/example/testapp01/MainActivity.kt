package com.example.testapp01

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


class MainActivity : AppCompatActivity() {

    lateinit var myFragmentPagerAdapter: MyFragmentPagerAdapter
    lateinit var myViewPager: ViewPager
    lateinit var tabLayout:TabLayout
    lateinit var data:MutableList<Drink>
    lateinit var dataAll:MutableList<Drink>
    lateinit var dataFav:MutableList<Drink>
    lateinit var addBtn:ImageButton
    lateinit var drinkViewModel:DrinkViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        dataAll=mutableListOf()
        dataFav=mutableListOf()

        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)

//        myFragmentPagerAdapter= MyFragmentPagerAdapter(supportFragmentManager)
//        myViewPager= findViewById(R.id.fragmentContainer)
//        setupViewPager(myViewPager)
//        setViewPager(0)
        handleFrags()
//        tabLayout=findViewById(R.id.myTabLayout)
//        tabLayout.setupWithViewPager(myViewPager)
//        tabLayout.getTabAt(0)?.setIcon(R.drawable.tab_icon_all)
//        tabLayout.getTabAt(1)?.setIcon(R.drawable.tab_icon_fav)
        handleTabs()
        handleAddBtn()
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//        })
    }

    private fun handleFrags() {
        myFragmentPagerAdapter= MyFragmentPagerAdapter(supportFragmentManager)
        myViewPager= findViewById(R.id.fragmentContainer)
        setupViewPager(myViewPager)
        setViewPager(0)
    }

    private fun handleTabs() {
        tabLayout=findViewById(R.id.myTabLayout)
        tabLayout.setupWithViewPager(myViewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.tab_icon_all)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.tab_icon_fav)
    }

    private fun handleAddBtn() {
        addBtn=findViewById(R.id.myAddBtn)
        addBtn.setOnClickListener(View.OnClickListener {
//            val curr=myViewPager.currentItem
//            val newDrink=Drink("Drink "+curr+" "+(dataAll.size+1), "this is ay drink...", curr==1)
//            dataAll.add(newDrink)
//            (myFragmentPagerAdapter.getItem(0) as FragmentAll).recyclerView.adapter?.notifyDataSetChanged()
//            if (curr==1){
//                dataFav.add(newDrink)
//                (myFragmentPagerAdapter.getItem(1) as FragmentFav).recyclerView.adapter?.notifyDataSetChanged()
//            }
            val curr=myViewPager.currentItem

            if (curr!=0 && curr!=1)
                Toast.makeText(this, "uhh... $curr", Toast.LENGTH_SHORT).show()

            when (curr){
                0 -> Toast.makeText(this, "All acc", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "Fav acc", Toast.LENGTH_SHORT).show()
            }
            val newDrink=Drink(0,"Drink "+curr+" "+(dataAll.size+1), "this is ay drink...", curr==1)
            Toast.makeText(this, "new drink is fav: ${newDrink.fav}", Toast.LENGTH_SHORT).show()
            GlobalScope.launch{
                val result = /*(myFragmentPagerAdapter.getItem(0) as FragmentAll).*/drinkViewModel.insert(newDrink)
            }
        })
    }

    public fun selectedFragment()=myViewPager.currentItem

//    public fun (pos:Int) = dataFav.removeAt(pos)

    private fun setupViewPager(viewPager: ViewPager){
        myFragmentPagerAdapter= MyFragmentPagerAdapter(supportFragmentManager)
        myFragmentPagerAdapter.addFrag( FragmentAll(dataAll, drinkViewModel), "All Drinks")
        myFragmentPagerAdapter.addFrag( FragmentFav(dataFav, drinkViewModel), "Favourite Drinks")
        viewPager.adapter=myFragmentPagerAdapter
    }

    private fun setViewPager(fragmentPos: Int){
        myViewPager.currentItem = fragmentPos
    }

    //TODO: - Create public method for rv dataset updation in main
    //      -   using ctxt or get Activity, find out which frag pos is currently set in main.
    //      -   using that info, upd that frag's rv. (Using myViewPager.currentItem ? maybe)

    //TODO:  Logic for updation:
    //      -   data resides with Main
    //      -   rvs hold refs
    //      -   favouriting only updates favourites (pass 1 in the abv to-do public fun)
}