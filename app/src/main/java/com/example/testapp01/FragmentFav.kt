package com.example.testapp01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentFav(var data:MutableList<Drink>, val drinkViewModel: DrinkViewModel): Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var ada : MyRVAdapter
//    lateinit var drinkViewModel:DrinkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        var view=inflater.inflate(R.layout.fragment_favs, container, false)
        recyclerView=view.findViewById(R.id.favsRv)
        recyclerView.layoutManager= LinearLayoutManager(this.context)
        ada = MyRVAdapter(this, data)
        recyclerView.adapter=ada

//        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
        drinkViewModel.favDrinks?.observe(viewLifecycleOwner,
            { drinks -> // Update the cached copy of the words in the adapter.
                ada.setDrinksData(drinks)
            })
        return view;
    }
}