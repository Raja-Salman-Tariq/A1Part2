package com.example.testapp01.db.utils

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testapp01.retrofit.Comment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// prepares the data for viewing in the UserProfileFragment and reacts to user interactions.
//              store and manage UI-related data             (decoupling - in terms of holding)
//              lifecycle awareness eg rotation              (fault tolerance)
//              memory leaks, house keeping, asynchronicity  (efficiency & effectiveness)
//              avoid redundant ops via decoupling           ("state" preserved for reference)
//              ownership clarity and data ctrl
//              divide & conquore; lightweight rather than monolith
class DrinkViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mRepository: DrinkRepo = DrinkRepo(application)
    val mAllDrinks: LiveData<List<Drink>>? =mRepository.getAll()
    val favDrinks: LiveData<List<Drink>>? = mRepository.getFavs()
    val drink:LiveData<List<Drink>>?      = mRepository.getDrink()
    val comments:LiveData<List<Comment>>? = mRepository.getComments()

    suspend fun del(drink: Drink){
        mRepository.delete(drink)
    }

    suspend fun upd(drink: Drink){
        mRepository.upd(drink)
    }

    suspend fun insert(drink: Drink) {
        mRepository.insert(drink)
    }

//    fun getDrink(id:Int, _drink:MutableLiveData<ArrayList<Drink>>) {
//        viewModelScope.launch {
//            drink = mRepository.getDrink(id)
//            _drink.value?.add(drink?.value?.get(0)!!)
//        }
//    }

//    suspend fun getDrink(id:Int) {
//        drink = mRepository.getDrink()
//    }

//    suspend fun getComments(id:Int){
//            comments = mRepository.getDrinkComments(id)
//    }
}