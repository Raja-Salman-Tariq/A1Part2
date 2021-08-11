package com.example.testapp01

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.LiveData
import com.example.testapp01.db.utils.Drink
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// Simply a helper class
//  just uses a typical AlertDialogBox implementation for the edit dialog box
class DialogueUtility(private val mainActivity: MainActivity, private val data: MutableList<Drink>, private val position:Int, private val h:MyRVAdapter.MyViewHolder) {
    private val builder = AlertDialog.Builder(mainActivity)

    // I'm using fragment here so I'm using getView() to provide ViewGroup
    // but you can provide here any other instance of ViewGroup from your Fragment / Activity
    private val viewInflated: View = LayoutInflater.from(mainActivity.applicationContext    )
        .inflate(R.layout.edit_dialogue, h.view as ViewGroup, false)
    // Set up the input
    private val newName: EditText = viewInflated.findViewById(R.id.editName)
    private val newDesc: EditText = viewInflated.findViewById(R.id.editDesc)
    private val newFav: CheckBox = viewInflated.findViewById(R.id.editCb)

    init{
        builder.setTitle("Editing Mode").setIcon(R.drawable.edit_drink_icon)
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
    }

    fun show() {
        builder.show()
    }
}