package com.example.testapp01.db.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


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

    suspend fun del(drink: Drink){
        mRepository.delete(drink)
    }

    suspend fun upd(drink: Drink){
        mRepository.upd(drink)
    }

    suspend fun insert(drink: Drink) {
        mRepository.insert(drink)
    }
}