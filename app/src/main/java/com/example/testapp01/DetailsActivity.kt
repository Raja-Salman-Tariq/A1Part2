package com.example.testapp01

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.db.utils.Drink
import com.example.testapp01.db.utils.DrinkViewModel
import com.example.testapp01.retrofit.Comment
import com.example.testapp01.retrofit.JsonPlaceholderApi
import com.example.testapp01.rv_adapters.CommentsRVAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsActivity : BaseActivity() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/

//    private val drinkViewModel :DrinkViewModel= myDrinkViewModel!!
    lateinit var myDrink:List<Drink>
    lateinit var commentsRv : RecyclerView
    private lateinit var ada: CommentsRVAdapter
    private lateinit var progressBar : CircularProgressIndicator
    private lateinit var favIcon : ImageView
    private lateinit var favTxt : TextView


    //-----------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //--------------------------------------------
        val id= (intent.extras?.get("drink") as Drink).postId
//        val id=(intent.getSerializableExtra("drink") as Drink).postId
        Log.d("cmnts", "onCreate: recvd id: $id")
//        Log.d("cmnts", "onCreate: recvd nondrink id: ${(intent.extras?.get("drink") as Drink).id}")

        val jsonPlaceholderApi = handleRetrofit()

        progressBar = findViewById<CircularProgressIndicator>(R.id.commentsLoading)
        progressBar.show()

        if (id!=null) {
//            progressBar.startAnimation()
            runOnUiThread(Runnable { getRemoteComments(id, jsonPlaceholderApi)})
        }
        else
            populateRv(mutableListOf())

        handleSetup()
//        populateRv()
    }


    @WorkerThread
    fun getRemoteComments(postId:Int, jsonPlaceholderApi:JsonPlaceholderApi){
        Log.d("cmnts", "getingRemoteComments against id : $postId")
        var remoteData = ArrayList<Comment>()

        val call= jsonPlaceholderApi?.getComments(postId)
        call?.enqueue(object: Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    Log.d("retro", "onResponse responce unsuccessful: ")
                }

                else {
                    val body = response.body()
                    if (body != null) {
                        for (comment:Comment in body)
                            remoteData.add(Comment(comment))
                    }
                }

                runOnUiThread(Runnable {
                    populateRv(remoteData)
//                    receivedRvData(remoteData)
                })
                Log.d("cmnts", "populated data of size ${remoteData.size}: ")
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
//                Toast.makeText(application?.applicationContext, "Responce failed", Toast.LENGTH_SHORT).show()
                Log.d("retro", "onFailure: no internet")
            }
        })
    }


    fun handleRetrofit(): JsonPlaceholderApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(JsonPlaceholderApi::class.java)
    }
    //-----------------------------------------------

    private fun populateRv(remoteData:MutableList<Comment>) {
        commentsRv= findViewById(R.id.commentsRv)
        commentsRv.layoutManager=LinearLayoutManager(this)
        ada = CommentsRVAdapter(this, remoteData)
        commentsRv.adapter= ada

        Log.d("cmnts", "receivedRvData: cmnts rcvd of size ${remoteData.size}")

        Log.d("cmnts", "*****    ${Looper.myLooper() == Looper.getMainLooper()}    *****")

        if (remoteData.isEmpty()) {
//            commentsRv.visibility = View.INVISIBLE
            findViewById<TextView>(R.id.detailsEmptyCmntsTxt).visibility = View.VISIBLE
        }
        else {
//            commentsRv.visibility = View.VISIBLE
            findViewById<TextView>(R.id.detailsEmptyCmntsTxt).visibility=View.INVISIBLE
        }

        progressBar.hide()
    }

    //-----------------------------------------------

    private fun populateDrink(drink:List<Drink>) {

        if (drink.isEmpty())
            return

        myDrink = drink

        val d=drink[0]
        handleTextViews(d)

        favIcon   = findViewById<ImageView>(R.id.detailFavIcon)
        favTxt    = findViewById<TextView>(R.id.detailFavTxt)
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

        val drink :Drink = intent.getSerializableExtra("drink") as Drink
        populateDrink(listOf(drink))

        val backBtn = findViewById<ImageView>(R.id.detailsBack)
        backBtn.setOnClickListener(
                View.OnClickListener {
                    super.onResume()
                    finish()
                }
            )
    }

    private fun receivedRvData(comments: MutableList<Comment>) {
        Log.d("cmnts", "receivedRvData: cmnts rcvd of size ${comments.size}")

        Log.d("cmnts", "*****    ${Looper.myLooper() == Looper.getMainLooper()}    *****")

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

//    private fun handleLoading(loading: Boolean) {
//        Log.d("loading", "comments $loading: ")
//        if (loading){
////            commentsRv.visibility=View.GONE
//            progressBar.visibility=View.VISIBLE
//        }
//        else{
//            commentsRv.visibility=View.VISIBLE
//            progressBar.visibility=View.GONE
//        }
//    }

    override fun onPause() {
        super.onPause()
        val drink = myDrink[0]

        Log.d("faving", "drink:${drink.fav} ? act:${favIcon.tag=="favourite"} ? going in : ${drink.fav!=(favIcon.tag=="favourite")}")

        if (drink.fav!=(favTxt.text=="Mark\nUnfavourite"))
            GlobalScope.launch {
                drink.fav = !drink.fav
                myDrinkViewModel?.upd(drink)
            }
    }



}