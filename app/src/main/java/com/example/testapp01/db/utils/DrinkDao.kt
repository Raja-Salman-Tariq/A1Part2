package com.example.testapp01.db.utils

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testapp01.retrofit.Comment

@Dao
interface DrinkDao {
    @Insert
    fun addDrink(drink: Drink)

    @Delete
    fun deleteDrink(drink: Drink)

    @Update
    fun updateDrink(drink:Drink)

    @Update
    fun toggleFav(drink: Drink)

    @Query("SELECT * FROM Drink WHERE fav=1 ORDER BY name ASC")
    fun getFavs(): LiveData<List<Drink>>

    @Query("SELECT * FROM Drink ORDER BY name ASC")
    fun getAll(): LiveData<List<Drink>>

    @Query("DELETE FROM Drink")
    fun delAll()

    @Insert
    fun addDrinks(drinks: List<Drink>)

    @Query("SELECT * FROM Drink WHERE toGet=1")
    fun getDrink() : LiveData<List<Drink>>
}