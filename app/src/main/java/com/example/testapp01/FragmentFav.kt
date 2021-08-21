package com.example.testapp01

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.db.utils.*

class FragmentFav(private val drinkViewModel: DrinkViewModel): Fragment() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var recyclerView : RecyclerView
    private lateinit var favAda : MyRVAdapter
    private lateinit var favData:MutableList<Drink>
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

        drinkViewModel.favDrinks?.observe(viewLifecycleOwner)
            { favDrinks -> // Update the cached copy of the words in the adapter.
//                Log.d("frafa", "${favDrinks.size}")//, whereby Item: ${drinks[0].id},\tname=${drinks[0].name}, \tdesc=${drinks[0].desc}, fav=${drinks[0].fav} ")
                favAda.setDrinksData(favDrinks)
            }
        Log.d("frafa", "${favData.size}")//, whereby Item: ${drinks[0].id},\tname=${drinks[0].name}, \tdesc=${drinks[0].desc}, fav=${drinks[0].fav} ")
        return view
    }
    //-----------------------------------------------





    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    private fun handleRv(view : View) {                 // typical recycler view setup
        favData= mutableListOf()
        recyclerView= view.findViewById(R.id.favsRv)
        recyclerView.layoutManager= LinearLayoutManager(this.context)
        favAda = MyRVAdapter(this, favData)
        recyclerView.adapter=favAda

    }
    //-----------------------------------------------
}