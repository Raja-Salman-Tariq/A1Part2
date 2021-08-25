package com.example.testapp01.db.utils

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.testapp01.retrofit.Comment
import com.example.testapp01.retrofit.JsonPlaceholderApi
import com.example.testapp01.retrofit.Post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// data management and house keeping                (encapsulation)
// data acquisition dependancies and duplication    (decoupling - in terms of acquiring)
// single source of truth
// testability
class DrinkRepo(
    private val application: Application?,
    private val drinkDao: DrinkDao? = DrinkDB.getDatabase(application)?.getDrinkDao(),
//    private val commentDao: CommentDao? = DrinkDB.getDatabase(application)?.getCommentDao(),
    private var jsonPlaceholderApi: JsonPlaceholderApi?=null,
    private var allDrinks: LiveData<List<Drink>>?=null,
    private var favDrinks: LiveData<List<Drink>>?=null,
    private var drinkToGet:LiveData<List<Drink>>?=null,
    private val comments: MutableLiveData<MutableList<Comment>> = MutableLiveData<MutableList<Comment>>(),
    private val commentsLoading : MutableLiveData<MutableList<Boolean>> = MutableLiveData<MutableList<Boolean>>()

){

    init{
        jsonPlaceholderApi=handleRetrofit()
//        jsonPlaceholderApi?.getRemoteData()
        getRemoteData()
//        getRemoteComments()
        allDrinks=drinkDao?.getAll()
        favDrinks=drinkDao?.getFavs()
        drinkToGet=drinkDao?.getDrink()
        comments.value= mutableListOf()
        commentsLoading.value= mutableListOf()
        commentsLoading.value!!.add(false)
    }

    //============================================================
    //---------        D R I N K       F U N C S     ------------
    //============================================================

    fun getAll(): LiveData<List<Drink>>? {
        return allDrinks
    }

    fun getFavs(): LiveData<List<Drink>>? {
        return favDrinks
    }

    fun getDrink(): LiveData<List<Drink>>? {
        return drinkToGet
    }

    fun getComments(intId:Int?): LiveData<MutableList<Comment>>? {
        if (null != intId) {
            getRemoteComments(intId)
        }
        else{
            comments?.value?.clear()
        }
        return comments
//        return Transformations.switchMap(comments){
//
//        }
    }

    fun getCommentsLoading(): MutableLiveData<MutableList<Boolean>> {
        return commentsLoading
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun upd(drink: Drink){
        drinkDao?.toggleFav(drink)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(drink: Drink){
        drinkDao?.deleteDrink(drink)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(drink: Drink) {
        drinkDao?.addDrink(drink)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(drinks: List<Drink>) {
        drinkDao?.addDrinks(drinks)
    }

    //============================================================
    //---------          C M N T       F U N C S     ------------
    //============================================================

//    fun getDrinkComments(): LiveData<List<Comment>>?{
//        return commentDao?.getDrinkComments()
//    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insertAllComments(comments: List<Comment>) {
//        commentDao?.addComments(comments)
//    }

    @WorkerThread
    fun getRemoteData(){
//        GlobalScope.launch {
//            insertAll(jsonPlaceholderApi?.getRemoteData() as List<Drink>)
//        }

        val call= jsonPlaceholderApi?.getPosts()
        call?.enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful)
                    Toast.makeText(application?.applicationContext, "Responce unsuccessful", Toast.LENGTH_SHORT).show()

                else {
                    val body = response.body()?.dropLast(response.body()?.size!! -10)
                    val remoteData = ArrayList<Drink>()
                    if (body != null) {
                        for (post:Post in body)
                            remoteData.add(Drink(post))
                        GlobalScope.launch {
                            insertAll(remoteData.apply { sortByDescending { it.postId }})
                        }
                    }

                }

            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                Toast.makeText(application?.applicationContext, "Responce failed", Toast.LENGTH_SHORT).show()
                Log.d("rerto", "onFailure: no internet")
            }
        })
    }

    @WorkerThread
    fun getRemoteComments(postId:Int){
//        commentsLoading?.value?.removeAt(0)
//        commentsLoading?.value?.add(true)
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


                comments.value?.clear()
                comments!!.value?.addAll(remoteData)
//                Log.d("retro", "changed: ${comments!!.value?.add(remoteData[0])}")
                Log.d("retro", "populated data of size ${comments!!.value!!.size}: ")
//                commentsLoading?.value?.clear()
//                if (commentsLoading?.value?.add(false)==false)
//                    Log.d("loading", "messed up")
//                Log.d("loading", "value at end: ${commentsLoading?.value?.get(0)}")

            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
//                Toast.makeText(application?.applicationContext, "Responce failed", Toast.LENGTH_SHORT).show()
                Log.d("retro", "onFailure: no internet")
            }
        })
    }

//
//    @WorkerThread
//    fun getRemoteComments(){
//        val call= jsonPlaceholderApi?.getPosts()
//        call?.enqueue(object: Callback<List<Post>> {
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                if (!response.isSuccessful)
//                    Toast.makeText(application?.applicationContext, "Responce unsuccessful", Toast.LENGTH_SHORT).show()
//
//                else {
//                    var n=getAll()?.value?.size
//                    if (n==null)
//                        n=10
//                    val body = response.body()?.dropLast(response.body()?.size!! -10)
//                    val remoteData = ArrayList<Drink>()
//                    if (body != null) {
//                        for (post:Post in body)
//                            remoteData.add(Drink(0,post))
//                        //                    Toast.makeText(baseContext,
//                        //                        "ID: "+post?.id+"\nuID: "+post?.userId+"\ntitle: "+post?.title+"\nText: "+post?.text,
//                        //                        Toast.LENGTH_LONG).show()
//                        GlobalScope.launch {
//                            insertAll(remoteData)
//                        }
//                    }
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
////                Toast.makeText(application?.applicationContext, "Responce failed", Toast.LENGTH_SHORT).show()
//                Log.d("rerto", "onFailure: no internet")
//            }
//        })
//    }

}

fun handleRetrofit(): JsonPlaceholderApi {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(JsonPlaceholderApi::class.java)
}