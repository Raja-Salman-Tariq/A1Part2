package com.example.testapp01

import android.app.AlertDialog
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
import com.example.testapp01.db.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import android.content.DialogInterface
import android.text.Editable
import android.text.InputType

import android.widget.EditText
import java.security.AccessController.getContext


class MyRVAdapter(private val ctxt : Fragment, var data:MutableList<Drink>):RecyclerView.Adapter<MyRVAdapter.MyViewHolder>(){

    /*###############################################
    * -----         M A N D A T O R Y          -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    class MyViewHolder(val view: View, // default constructor, viewholder elements initialized
                              val layout:RelativeLayout=view.findViewById(R.id.rowLayout),
                              val name:TextView=view.findViewById(R.id.drinkName),
                              val desc:TextView=view.findViewById(R.id.drinkDesc),
                              val del:ImageButton=view.findViewById(R.id.drinkDel),
                              val edit:ImageButton=view.findViewById(R.id.drinkEdit),
                              val chkBx:CheckBox=view.findViewById(R.id.drinkChkBx)
                            ):RecyclerView.ViewHolder(view)
    //-----------------------------------------------
    // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{

        val viewHolder= MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_view, parent, false))

        if (ctxt.javaClass==FragmentFav::class.java){
            Log.d("favFrag", "onCreateViewHolder: it holds tru")
            viewHolder.edit.visibility=(View.INVISIBLE)
            viewHolder.del.visibility=(View.INVISIBLE)
        }

        return viewHolder
    }
    //-----------------------------------------------
    // actual interactions with view; ie setting and changing of the view / listeners
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (id, name, descr, fav)=data[position]
        holder.name.text=name; holder.desc.text= "$id: $descr"
        holder.chkBx.isChecked=fav

        when (fav){
            false -> holder.desc.text="$id: $descr # is NOT my fav"
            true -> holder.desc.text="$id: $descr # is my fav <3"
        }
        handleListeners(holder, position);
    }
    //-----------------------------------------------
    override fun getItemCount()= data.size
    //-----------------------------------------------





    /*###############################################
    * -----         C U S T O M A R Y          -----*
    * =============================================*/
    fun setDrinksData(drinkData: List<Drink> ){
        data= drinkData as MutableList<Drink>
        notifyDataSetChanged()
    }
    //-----------------------------------------------





    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    private fun handleListeners(h:MyViewHolder, position: Int){
        val mainActivity=(ctxt.activity as MainActivity)
        h.view.setOnClickListener{ Toast.makeText(h.view.context, "Drink \"${data[position].name}\" --- ${data[position].desc}", Toast.LENGTH_SHORT).show()}
        h.del.setOnClickListener{ handleDelListener(h, mainActivity,position);}
        h.edit.setOnClickListener{ handleEditListener(h, mainActivity, position)  }
        handleChkBxListener(h, mainActivity, position)
    }
    //-----------------------------------------------
    private fun handleEditListener(h: MyRVAdapter.MyViewHolder, mainActivity: MainActivity, position: Int) {
        Toast.makeText(h.view.context, "Editing row "+(position+1), Toast.LENGTH_SHORT).show()

        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle("Editing Mode").setIcon(R.drawable.edit_drink_icon)

        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        val viewInflated: View = LayoutInflater.from(mainActivity.applicationContext    )
            .inflate(R.layout.edit_dialogue, h.view as ViewGroup, false)
        // Set up the input
        val newName:EditText = viewInflated.findViewById(R.id.editName)
        val newDesc:EditText = viewInflated.findViewById(R.id.editDesc)
        val newFav:CheckBox   = viewInflated.findViewById(R.id.editCb)

        newName.setText(data[position].name)
        newDesc.setText(data[position].desc)
        newFav.isChecked = data[position].fav
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated)

        // Set up the buttons

        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            data[position].name= newName.text.toString()
            data[position].desc= newDesc.text.toString()
            data[position].fav= newFav.isChecked
            GlobalScope.launch {mainActivity.drinkViewModel.upd(data[position]) }
        }

        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }
    //-----------------------------------------------
    private fun handleChkBxListener(h:MyViewHolder, mainActivity: MainActivity, position: Int){ // TODO: LOGIC CORRECT, WHY NOT WORKING ?
        h.chkBx.setOnClickListener{
            Log.d("t1", "chkbx enterd:+ ${h.chkBx.isChecked}")
            data[position].fav=h.chkBx.isChecked
            GlobalScope.launch {
                mainActivity.drinkViewModel.upd(data[position])
            }
        }
    }
    //-----------------------------------------------
    private fun handleDelListener(h:MyViewHolder, mainActivity: MainActivity, position: Int) {
        GlobalScope.launch { mainActivity.drinkViewModel.del(data[position]); }
    }
}

