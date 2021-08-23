package com.example.testapp01

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.db.utils.Drink
import com.example.testapp01.db.utils.DrinkViewModel
import com.example.testapp01.retrofit.Comment
import com.example.testapp01.rv_adapters.CommentsRVAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsActivity : BaseActivity() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/

    private val drinkViewModel :DrinkViewModel= myDrinkViewModel!!
    lateinit var myDrink:List<Drink>
    lateinit var comments:MutableLiveData<ArrayList<Comment>>
    lateinit var commentsRv : RecyclerView
    private lateinit var ada: CommentsRVAdapter
    var fresh = true                    // Ignores the first connectivity detecting snackbar


    //-----------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //--------------------------------------------

        handleSetup()
        populateRv()
    }

    //-----------------------------------------------

    private fun populateRv() {
        commentsRv= findViewById(R.id.commentsRv)
        commentsRv.layoutManager=LinearLayoutManager(this)
        ada = CommentsRVAdapter(this, mutableListOf<Comment>())
        commentsRv.adapter= ada
    }

    //-----------------------------------------------

    private fun populateDrink(drink:List<Drink>) {

        if (drink.isEmpty())
            return

        myDrink = drink

        val drinkName = findViewById<TextView>(R.id.detailName)
        val favIcon   = findViewById<ImageView>(R.id.detailFavIcon)
        val drinkDesc = findViewById<TextView>(R.id.detailDescription)
        val favTxt    = findViewById<TextView>(R.id.detailFavTxt)
        val backDrop  = findViewById<ImageView>(R.id.detailImg)

        drinkName.setText(drink?.get(0)?.name)
        drinkDesc.setText(drink?.get(0)?.desc)

        when (drink?.get(0)?.id%4){
            0   ->  backDrop.setImageResource(R.drawable.one)
            1   ->  backDrop.setImageResource(R.drawable.two)
            2   ->  backDrop.setImageResource(R.drawable.three)
            3   ->  backDrop.setImageResource(R.drawable.four)
        }

        when (drink?.get(0)?.fav){
            true    -> {
                favIcon.setImageResource(R.drawable.favourite_icon)
                favTxt.setText("Mark\nUnfavourite")
            }
            false   -> {
                favIcon.setImageResource(R.drawable.unfavourite_icon)
                favTxt.setText("Mark\nFavourite")
            }
        }

        favIcon.setOnClickListener(View.OnClickListener {
            when (favTxt.text.equals("Mark\nFavourite")) {
                true -> {
                    favIcon.setImageResource(R.drawable.favourite_icon)
                    favTxt.setText("Mark\nUnfavourite")
                }
                false -> {
                    favIcon.setImageResource(R.drawable.unfavourite_icon)
                    favTxt.setText("Mark\nFavourite")
                }
            }

            GlobalScope.launch {
                val drink = drink?.get(0)
                drink?.fav= !drink?.fav!!
                drinkViewModel.upd(drink)
            }
        })
    }

    //-----------------------------------------------

    private fun handleSetup() {
//        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
//        val int = intent
////        Toast.makeText(this, " "+int.hasExtra("id"), Toast.LENGTH_SHORT).show()
//        Log.d("intented", "handleSetup: "+" "+int.getIntExtra("id", 0))
//        val id = intent.getIntExtra("id", 0)

        if (drinkViewModel.drink==null)
            finish()

        drinkViewModel.drink?.observe(this){
            drink -> populateDrink(drink)
        }
        drinkViewModel.comments?.observe(this){
            comments -> ada.setCmntData(comments)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDrink[0].toGet=false
        GlobalScope.launch {
            drinkViewModel.upd(myDrink[0])
        }
    }
}