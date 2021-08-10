package com.example.testapp01

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fm: FragmentManager,
                             private val mFragList: MutableList<Fragment> = mutableListOf(),
                             private val mFragTitles:MutableList<String> = mutableListOf()) : FragmentPagerAdapter(fm){

    fun addFrag(frag:Fragment, title:String){
        mFragList.add(frag);
        mFragTitles.add(title)
    }
    override fun getCount()=mFragList.size

    override fun getItem(position: Int)=mFragList[position]

    override fun getPageTitle(position: Int)=mFragTitles[position]

}