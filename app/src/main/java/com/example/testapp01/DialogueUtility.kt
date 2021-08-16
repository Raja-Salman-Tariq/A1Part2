package com.example.testapp01

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.testapp01.db.utils.Drink
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import android.text.Editable








// Simply a helper class
//  just uses a typical AlertDialogBox implementation for the edit dialog box
class DialogueUtility(val context: Context,
                      title:String, iconID:Int,
                      private var builder: AlertDialog.Builder?=null) {

    private val viewInflated:View = LayoutInflater.from(context).inflate(R.layout.edit_dialogue, null)

    // Set up the input
    private val newName: EditText = viewInflated.findViewById(R.id.dialogueName)
    private val newDesc: EditText = viewInflated.findViewById(R.id.dialogueDesc)
    private val newFav: CheckBox = viewInflated.findViewById(R.id.dialogueCb)

    init {
        builder= AlertDialog.Builder(context)
        builder?.setTitle(title)?.setIcon(iconID)
        builder!!.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel()}
    }

    fun show() {
        builder?.show()
    }



    // Show Existing Dialogue
    constructor(
        mainActivity: MainActivity,
        title: String,
        iconID: Int,
        drink: Drink,
    ):this(mainActivity,title, iconID){
        Log.d("constr", "show existing called ")

        newName.setText(drink.name)
        newDesc.setText(drink.desc)
        newFav.isChecked = drink.fav
        newName.keyListener=null
        newDesc.keyListener=null
        newFav.keyListener=null

        builder?.setView(viewInflated)

        builder?.setNegativeButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
        }
    }

    // Create New Dialogue
    constructor(context: Context,
                title:String,
                iconID:Int,
                drinkId:Int
    ):this(context,title, iconID){
        Log.d("constr", "create new called ")
        var drink=Drink()

        newName.setHint("Drink Name...")
        newDesc.setHint("Drink Description...")
        newFav.isChecked = !drink.fav

        builder?.setView(viewInflated)

        builder?.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->

            if (newName.text.isNullOrBlank() || newDesc.text.isNullOrBlank()){
                Toast.makeText(context, "Please fill the drink name and description both to add a new drink", Toast.LENGTH_SHORT).show()
            }

            else {
                dialog.dismiss()
                drink.id = drinkId
                drink.name = newName.text.toString()
                drink.desc = newDesc.text.toString()
                drink.fav = newFav.isChecked
                Log.d("drinkvals", ":===${drink.id}   ${drink.name}  ${drink.desc}   ${drink.fav}")
                GlobalScope.launch { (context as MainActivity).drinkViewModel.insert(drink) }
            }
        }

//        val dialogue: AlertDialog = builder?.create()!!
//        val btnOk = dialogue.getButton(AlertDialog.BUTTON_POSITIVE);
//        btnOk?.isEnabled = false
//
//        newName.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (!newName.text.isNullOrBlank() && !newDesc.text.isNullOrBlank()) {
//                    btnOk.isEnabled=true
//                }
//            }
//        })
//
//        newDesc.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (!newName.text.isNullOrBlank() && !newDesc.text.isNullOrBlank()) {
//                    btnOk.isEnabled=true
//                }
//            }
//        })
    }

    // Edit Dialogue
    constructor(mainActivity: MainActivity,
                data: MutableList<Drink>,
                position:Int,
                title:String,
                iconID:Int)
            :this(mainActivity, title, iconID) {
        newName.setText(data[position].name)
        newDesc.setText(data[position].desc)
        newFav.isChecked = data[position].fav

        builder?.setView(viewInflated)

        // Set up the buttons

        builder?.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            if (newName.text.isNullOrBlank() || newDesc.text.isNullOrBlank()) {
//                callSnackBar(mainActivity)
                Toast.makeText(mainActivity, "Please fill the drink name and description both to perform an edit", Toast.LENGTH_LONG).show()
//                builder!!.setTitle("Edit Drink - Please fill all fields")
////                builder!!.show()
            }

            else {
                dialog.dismiss()
                data[position].name = newName.text.toString()
                data[position].desc = newDesc.text.toString()
                data[position].fav = newFav.isChecked
                GlobalScope.launch { mainActivity.drinkViewModel.upd(data[position]) }
            }
        }

//        val dialogue: AlertDialog = builder?.create()!!
//        val btnOk = dialogue.getButton(AlertDialog.BUTTON_POSITIVE);
//        btnOk?.isEnabled = false
//        btnOk?.visibility=View.INVISIBLE
//        btnOk?.textC
//
//        newName.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (!newName.text.isNullOrBlank() && !newDesc.text.isNullOrBlank()) {
//                    btnOk.isEnabled=true
//                }
//            }
//        })
//
//        newDesc.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (!newName.text.isNullOrBlank() && !newDesc.text.isNullOrBlank()) {
//                    btnOk.isEnabled=true
//                }
//            }
//        })
    }

    private fun callSnackBar(mainActivity: MainActivity){
        Snackbar.make(viewInflated, "Please provide a name and description both to perform an edit.", Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .show()
    }


}