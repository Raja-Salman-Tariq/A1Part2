package com.example.testapp01.db.utils

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Drink::class], version=1, exportSchema = false)
abstract class DrinkDB: RoomDatabase(){
    abstract fun getDrinkDao():DrinkDao

    // companion object makes all the below static. Thus we make the db singleton
    companion object {
        private var INSTANCE: DrinkDB? = null

        // def for a callback for populating dummy data into db when creating db   <==================
        private val sRoomDatabaseCallback: Callback = object : Callback() {                        //^
            override fun onOpen(db: SupportSQLiteDatabase) {                                       //|
                super.onOpen(db)                                                                   //|
                PopulateDbAsync(INSTANCE!!.getDrinkDao()).execute()                                //|
            }                                                                                      //|
        }                                                                                          //|

        open fun getDatabase(context: Context?): DrinkDB? {
            if (this.INSTANCE == null) {                                                           //^
                synchronized(DrinkDB::class.java) {                                                //|
                    if (INSTANCE == null) {                                                        //|
                        INSTANCE = Room.databaseBuilder(                                           //|
                            context!!.applicationContext,                                          //|
                            DrinkDB::class.java, "drink_db"                                  //|
                        ).fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build()                                                               //^
                    }                                                                              //|
                }                                                                                  //|
            }                                                                                      //|
            return INSTANCE                                                                        //|
        }                                                                                          //|
    }

                                                                                                   //^
                                                                                                   //|
// def for the fun being used within onOpen / db build callback ==================================>//|
// put in originally dummy data into the database via a seperate worker thread. Before that
//    though, delete all prev data. This is just a trivial/dont care choice.
    private class PopulateDbAsync(val drinkDao: DrinkDao): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            drinkDao.delAll()
            drinkDao.addDrink(Drink(0,"One", "Drinke de la genesis", false))
            return null
        }

    }
}