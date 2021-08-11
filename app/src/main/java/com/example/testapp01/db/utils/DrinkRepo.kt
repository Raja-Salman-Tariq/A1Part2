package com.example.testapp01.db.utils

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


// data management and house keeping                (encapsulation)
// data acquisition dependancies and duplication    (decoupling - in terms of acquiring)
// single source of truth
// testability
class DrinkRepo(
    application: Application?,
    private val drinkDao: DrinkDao? = DrinkDB.getDatabase(application)?.getDrinkDao(),
    private val allDrinks: LiveData<List<Drink>>? =drinkDao?.getAll(),
    private val favDrinks: LiveData<List<Drink>>? =drinkDao?.getFavs()
){
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
}