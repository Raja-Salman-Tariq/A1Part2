package com.example.testapp01

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.room.Room

import androidx.lifecycle.AndroidViewModel
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.annotation.NonNull

import androidx.room.RoomDatabase
import android.os.AsyncTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Entity
data class Drink(@PrimaryKey(autoGenerate = true) val id:Int=-1, var name:String="", var desc:String="", var fav:Boolean=true)

@Dao
interface DrinkDao {
    @Insert
    fun addDrink(drink: Drink)

    @Delete
    fun deleteDrink(drink: Drink)

    @Update
    fun toggleFav(drink: Drink)

    @Query("SELECT * FROM Drink WHERE fav=1 ORDER BY name ASC")
    fun getFavs(): LiveData<List<Drink>>
//    fun getFavs(): Flow<List<Drink>>

    @Query("SELECT * FROM Drink ORDER BY name ASC")
    fun getAll():LiveData<List<Drink>>

    @Query("DELETE FROM Drink")
    fun delAll()

}

@Database(entities = [Drink::class], version=1, exportSchema = false)
abstract class DrinkDB:RoomDatabase(){
    abstract fun getDrinkDao():DrinkDao

    companion object {
        private var INSTANCE: DrinkDB? = null

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbAsync(INSTANCE!!.getDrinkDao()).execute()
            }
        }

        open fun getDatabase(context: Context?): DrinkDB? {
            if (this.INSTANCE == null) {
                synchronized(DrinkDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context!!.applicationContext,
                            DrinkDB::class.java, "drink_db"
                        ).fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }




    private class PopulateDbAsync(val drinkDao: DrinkDao):AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            drinkDao.delAll()
            drinkDao.addDrink(Drink(0,"One", "Drinke de la genesis", false))
            return null
        }

    }
}

class DrinkRepo(
    application: Application?,
    val drinkDao: DrinkDao? = DrinkDB.getDatabase(application)?.getDrinkDao(),
    val allDrinks: LiveData<List<Drink>>? =drinkDao?.getAll(),
    val favDrinks: LiveData<List<Drink>>? =drinkDao?.getFavs()
){
    fun getAll(): LiveData<List<Drink>>? {
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
    suspend fun insert(drink: Drink) {
        drinkDao?.addDrink(drink)
    }
}

class DrinkViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mRepository: DrinkRepo = DrinkRepo(application)
    val mAllDrinks: LiveData<List<Drink>>? =mRepository.getAll()
    val favDrinks: LiveData<List<Drink>>? = mRepository.getFavs()
//    val allWords: LiveData<List<Drink>>? =mAllDrinks

    suspend fun upd(drink: Drink){
        mRepository.upd(drink)
    }

    suspend fun insert(drink: Drink) {
        mRepository.insert(drink)
    }
}

class MyRVAdapter(val ctxt : Fragment, var data:MutableList<Drink>):RecyclerView.Adapter<MyRVAdapter.MyViewHolder>(){
    class MyViewHolder(val view: View, // default constructor, viewholder elements initialized
                              val layout:RelativeLayout=view.findViewById(R.id.rowLayout),
                              val name:TextView=view.findViewById(R.id.drinkName),
                              val desc:TextView=view.findViewById(R.id.drinkDesc),
                              val del:ImageButton=view.findViewById(R.id.drinkDel),
                              val edit:ImageButton=view.findViewById(R.id.drinkEdit),
                              val chkBx:CheckBox=view.findViewById(R.id.drinkChkBx)
                            ):RecyclerView.ViewHolder(view)  // inheritance/extension

    fun setDrinksData(drinkData: List<Drink> ){
        data= drinkData as MutableList<Drink>
        notifyDataSetChanged()
    }

    // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_view, parent, false))
    }

    // the actual interations with the view described here;
    //      ie setting and changing of the view
    //      and listeners
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (_, name, descr, fav)=data[position]
        holder.name.text=name
        holder.desc.text=descr
        if (fav)
            holder.chkBx.isChecked=true
        handleListeners(holder, position);
    }

    override fun getItemCount()= data.size

    private fun handleListeners(h:MyViewHolder, position: Int){
        val mainActivity=(ctxt.activity as MainActivity)

        h.view.setOnClickListener{ Toast.makeText(h.view.context, "You tapped row "+(position+1), Toast.LENGTH_SHORT).show()}
        h.del.setOnClickListener{ data.removeAt(position); notifyItemRemoved(position)}
        h.edit.setOnClickListener{ Toast.makeText(h.view.context, "Editing row "+(position+1), Toast.LENGTH_SHORT).show()}
        handleChkBxListener(h, mainActivity, position)
    }





    private fun handleChkBxListener(h:MyViewHolder, mainActivity: MainActivity, position: Int){
        h.chkBx.setOnClickListener{
            Log.d("t1", "chkbx enterd: ")
//            when (mainActivity.selectedFragment()){
//                0 -> {
//                    Log.d("t1", "when all frags: ")
//
//                    if (h.chkBx.isChecked) {
//                        Log.d("t1", "if entrd: means unchecking box")
//                        data[position].fav=true
//                        mainActivity.dataFav.add(data[position])
//                        (mainActivity.myFragmentPagerAdapter.getItem(1) as FragmentFav).ada.notifyDataSetChanged()
//                    }
//                }
//                1 -> {
//                    data[position].fav=false
//                    data.removeAt(position); notifyItemRemoved(position)
//                    (mainActivity.myFragmentPagerAdapter.getItem(0) as FragmentAll).recyclerView.adapter?.notifyDataSetChanged()
//                }
//            }

            val cbstate=h.chkBx.isChecked
            val myDrink=data[position]
//            when (mainActivity.selectedFragment()){
//                0 -> {
                    when (cbstate){
                        false -> {
                            myDrink.fav=true
                        }
                        true -> {
                            myDrink.fav=false
                        }
                    }
//                }
//                1 -> {
//                    when (cbstate){
//                        false -> {
//
//                        }
//                        true -> {
//
//                        }
//                    }

            GlobalScope.launch {
                mainActivity.drinkViewModel.upd(myDrink)
            }

                }
//            }
//        }
    }
}