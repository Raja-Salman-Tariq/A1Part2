package com.example.testapp01

import android.app.Application
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import com.example.testapp01.db.utils.Drink
import com.example.testapp01.db.utils.DrinkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class BSheetHelper(val ctxt:Context, val view: View, val drinkViewModel: DrinkViewModel) {
    val mBottomSheet = view.findViewById<ConstraintLayout>(R.id.bottom_sheet)
    val addBtn       = view.findViewById<AppCompatImageButton>(R.id.myAddBtn)

    val mBottomSheetBehaviour = BottomSheetBehavior.from(mBottomSheet)

    val addName = view.findViewById<TextInputEditText>(R.id.addNameInp)
    val addDesc = view.findViewById<TextInputEditText>(R.id.addDescInp)
    val favIcon = view.findViewById<ImageView>(R.id.addBtnFav)

    val tick    = view.findViewById<ImageButton>(R.id.addBtnAddBtn)
    val cros    = view.findViewById<ImageButton>(R.id.addBtnCancelBtn)

    init {
        mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_HIDDEN
        addBtn.setOnClickListener(View.OnClickListener {            //  & inserts into db
            mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_EXPANDED
        })

        favIcon.setOnClickListener(View.OnClickListener {

        })


        tick.setOnClickListener(View.OnClickListener {

            if (addName.text.isNullOrBlank() || addDesc.text.isNullOrBlank()){
                if (addName.text.isNullOrBlank())
                    addName.error = "Please enter the drink name."
                if (addDesc.text.isNullOrBlank())
                    addDesc.error = "Please enter the drink description"
            }

            else {
                val aname=addName.text.toString()
                val adesc=addDesc.text.toString()
                val afave=favIcon.tag=="favourite"
                val drink = Drink(
                    0,
                    aname,
                    adesc,
                    true
                )
                GlobalScope.launch { drinkViewModel.insert(drink) }
                mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_HIDDEN
                try {
                        (ctxt.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(view.windowToken, 0);
                    } catch (e : Exception) {
                    }
            }

        })

        cros.setOnClickListener(View.OnClickListener {
            addName.setText("")
            addDesc.setText("")
            favIcon.setTag("unfavourite")
            favIcon.setImageResource(R.drawable.unfavourite_icon)
            mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_HIDDEN
        })
    }


}