package com.example.testapp01

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.db.utils.*

class FragmentFav(private val drinkViewModel: DrinkViewModel): Fragment() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var recyclerView : RecyclerView
    private lateinit var ada : MyRVAdapter
    private lateinit var data:MutableList<Drink>
    //-----------------------------------------------





    /*###############################################
    * -----      I N I T   &   S E T U P       -----*
    * =============================================*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_favs, container, false)

        handleRv(view)

        drinkViewModel.favDrinks?.observe(viewLifecycleOwner,
            { drinks -> // Update the cached copy of the words in the adapter.
                ada.setDrinksData(drinks)
            })
        Log.d("oncreateview", "onCreateView in fav: ")
        return view
    }
    //-----------------------------------------------





    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    private fun handleRv(view : View) {                 // typical recycler view setup
        data= mutableListOf()
        recyclerView= view.findViewById(R.id.favsRv)
        recyclerView.layoutManager= LinearLayoutManager(this.context)
        ada = MyRVAdapter(this, data)
        recyclerView.adapter=ada

    }
    //-----------------------------------------------
}