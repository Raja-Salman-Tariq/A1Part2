package com.example.testapp01

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.progressindicator.CircularProgressIndicator
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
    private lateinit var progressBar : CircularProgressIndicator


    //-----------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //--------------------------------------------
//        showSnackbar=false
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

        val d=drink[0]
        handleTextViews(d)

//        Log.d("chqdrink", "populateDrink: name=${d.}")

        val favIcon   = findViewById<ImageView>(R.id.detailFavIcon)
        val favTxt    = findViewById<TextView>(R.id.detailFavTxt)
        val backDrop  = findViewById<ImageView>(R.id.detailImg)


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

    private fun handleTextViews(d: Drink) {
        val nameField = findViewById<TextView>(R.id.detailsdrinkName)
        val roomIdField = findViewById<TextView>(R.id.detailsroomId)
        val userIdField = findViewById<TextView>(R.id.detailsuserId)
        val postId= findViewById<TextView>(R.id.detailsmyPostId)
        val textBody = findViewById<TextView>(R.id.detailsdrinkDesc)

        if (null != d.postId){
            nameField.setText("Post Title: ${d.title}")
            roomIdField.setText("Room ID: ${d.id}")
            userIdField.setText("Post User ID : ${d.userId}")
            postId.setText("Post ID: ${d.postId}")
            textBody.setText("Post Body: ${d.text}")

            nameField.visibility=View.VISIBLE
            roomIdField.visibility=View.VISIBLE
            userIdField.visibility=View.VISIBLE
            postId.visibility=View.VISIBLE
            textBody.visibility=View.VISIBLE
        }
        else{
            userIdField.visibility=View.GONE
            postId.visibility=View.GONE

            nameField.setText("Drink name: ${d.name}")
            roomIdField.setText("Room ID: ${d.id}")
            textBody.setText("Description: ${d.desc}")
        }
    }

    //-----------------------------------------------

    private fun handleSetup() {
//        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
//        val int = intent
////        Toast.makeText(this, " "+int.hasExtra("id"), Toast.LENGTH_SHORT).show()
//        Log.d("intented", "handleSetup: "+" "+int.getIntExtra("id", 0))
//        val id = intent.getIntExtra("id", 0)

//        progressBar = findViewById<CircularProgressIndicator>(R.id.commentProgressBar)


//        if (drinkViewModel.drink==null)
//            finish()

        val drink :Drink = intent.getSerializableExtra("drink") as Drink

        populateDrink(listOf(drink))

        GlobalScope.launch {
            drinkViewModel.fetchComments((intent.getSerializableExtra("drink") as Drink).postId)
        }

//        drinkViewModel.drink?.observe(this){
//            drink -> populateDrink(drink)
//        }

//        drinkViewModel.commentsLoading?.observe(this){
//            loading ->  handleLoading(loading[0])
//        }

        drinkViewModel.comments?.observe(this){
            comments -> receivedRvData(comments)
        }

        findViewById<ImageView>(R.id.detailsBack).setOnClickListener(View.OnClickListener { finish() })
    }

    private fun receivedRvData(comments: MutableList<Comment>) {

        if (comments.isEmpty()) {
            commentsRv.visibility = View.INVISIBLE
            findViewById<TextView>(R.id.detailsEmptyCmntsTxt).visibility = View.VISIBLE
        }
        else {
            commentsRv.visibility = View.VISIBLE
            findViewById<TextView>(R.id.detailsEmptyCmntsTxt).visibility=View.INVISIBLE
        }

        ada.setCmntData(comments)
    }

    private fun handleLoading(loading: Boolean) {
        Log.d("loading", "comments $loading: ")
        if (loading){
//            commentsRv.visibility=View.GONE
            progressBar.visibility=View.VISIBLE
        }
        else{
            commentsRv.visibility=View.VISIBLE
            progressBar.visibility=View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
//        myDrink[0].toGet=false
//        GlobalScope.launch {
//            drinkViewModel.upd(myDrink[0])
//        }
//        showSnackbar=true
    }
}