package com.example.testapp01.db.utils

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
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
    private var jsonPlaceholderApi: JsonPlaceholderApi?=null,
    private var allDrinks: LiveData<List<Drink>>?=null,
    private var favDrinks: LiveData<List<Drink>>?=null
){

    init{
        jsonPlaceholderApi=handleRetrofit()
        getRemoteData()
        allDrinks=drinkDao?.getAll()
        favDrinks=drinkDao?.getFavs()
    }

    fun getAll(): LiveData<List<Drink>>? { // TODO : SHOULD THESE ALSO BE SUSPEND FUNS ?
        return allDrinks
    }

    fun getFavs(): LiveData<List<Drink>>? {
        return favDrinks
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

    @WorkerThread
    fun getRemoteData(){
        val call= jsonPlaceholderApi?.getPosts()
        call?.enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful)
                    Toast.makeText(application?.applicationContext, "Responce unsuccessful", Toast.LENGTH_SHORT).show()

                else {
                    var n=getAll()?.value?.size
                    if (n==null)
                        n=10
                    val body = response.body()?.dropLast(response.body()?.size!! -10)
                    val remoteData = ArrayList<Drink>()
                    if (body != null) {
                        for (post:Post in body)
                            remoteData.add(Drink(0,post))
                //                    Toast.makeText(baseContext,
                //                        "ID: "+post?.id+"\nuID: "+post?.userId+"\ntitle: "+post?.title+"\nText: "+post?.text,
                //                        Toast.LENGTH_LONG).show()
                        GlobalScope.launch {
                            insertAll(remoteData)
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

}

fun handleRetrofit(): JsonPlaceholderApi {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(JsonPlaceholderApi::class.java)
}
