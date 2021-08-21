package com.example.testapp01


import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.db.utils.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyRVAdapter(private val ctxt : Fragment, private var data:MutableList<Drink>):RecyclerView.Adapter<MyRVAdapter.MyViewHolder>(){

    lateinit var anotherViewHolder:MyViewHolder

    /*###############################################
    * -----         M A N D A T O R Y          -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    class MyViewHolder(val view: View, // default constructor, viewholder elements initialized
                              val name:TextView=view.findViewById(R.id.drinkName),
                              val desc:TextView=view.findViewById(R.id.drinkDesc),
                              val del:ImageButton=view.findViewById(R.id.drinkDel),
                              val favIcon:ImageView=view.findViewById(R.id.favIcon),
                       val drinkImg:CircleImageView=view.findViewById(R.id.drinkImage)
                            ):RecyclerView.ViewHolder(view)
    //-----------------------------------------------
    // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{

        val viewHolder= MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_view, parent, false))

        if (ctxt.javaClass==FragmentFav::class.java){
            Log.d("favFrag", "onCreateViewHolder: it holds tru")
            viewHolder.del.visibility=(View.INVISIBLE)
        }

        anotherViewHolder=viewHolder

        return viewHolder
    }
    //-----------------------------------------------
    // actual interactions with view; ie setting and changing of the view / listeners
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (id, name, descr, fav)=data[position]
        holder.name.text=name; holder.desc.text= descr

        when (id%4){
            0   -> holder.drinkImg.setImageResource(R.drawable.one)
            1   -> holder.drinkImg.setImageResource(R.drawable.two)
            2   -> holder.drinkImg.setImageResource(R.drawable.three)
            3   -> holder.drinkImg.setImageResource(R.drawable.four)
        }

        when (fav){
            false -> holder.favIcon.setImageResource(R.drawable.unfavourite_icon)
            true  -> holder.favIcon.setImageResource(R.drawable.favourite_icon)
        }
        handleListeners(holder, position)
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
        h.view.setOnClickListener{ DialogueUtility(mainActivity, "My Drink", R.drawable.drink_icon, data[position] ).show()}
        h.del.setOnClickListener{ handleDelListener(mainActivity,position);}
        handleFavListener(h, mainActivity, position)
    }
    //-----------------------------------------------
    private fun handleEditListener(h: MyViewHolder, mainActivity: MainActivity, position: Int) {
//        Toast.makeText(h.view.context, "Editing row "+(position+1), Toast.LENGTH_SHORT).show()
        DialogueUtility(mainActivity, data, position, "Edit Drink", R.drawable.edit_drink_icon).show()
    }
    //-----------------------------------------------
    private fun handleFavListener(h:MyViewHolder, mainActivity: MainActivity, position: Int){
        h.favIcon.setOnClickListener{
//            Toast.makeText(h.view.context, "Tapped!", Toast.LENGTH_SHORT).show()

            val drink:Drink = data[0]
//            Log.d("showw","Before: size=${data.size},\ttag=${h.favIcon.tag}  ")
            Toast.makeText(h.view.context,"Before: size=${data.size},\ttag=${h.favIcon.tag}  ", Toast.LENGTH_LONG).show()
            Toast.makeText(h.view.context,"Item: ${drink.id},\tname=${drink.name}, \tdesc=${drink.desc}, fav=${drink.fav}  ", Toast.LENGTH_LONG).show()
            Log.d("abc", "Item: ${drink.id},\tname=${drink.name}, \tdesc=${drink.desc}, fav=${drink.fav}  ")


//            Log.d("t1", "chkbx enterd:+ ${h.chkBx.isChecked}")


            when (h.favIcon.tag) {
                "unfavourite" -> {
                    h.favIcon.setImageResource(R.drawable.favourite_icon)
                    h.favIcon.tag = "favourite"
                    data[position].fav = true
                }
                "favourite" -> {
                    h.favIcon.setImageResource(R.drawable.unfavourite_icon)
                    h.favIcon.tag = "unfavourite"
                    data[position].fav = false
                }
            }

            GlobalScope.launch {
                mainActivity.drinkViewModel.upd(data[position])
//                Log.d("showw","during: size=${data.size},\ttag=${h.favIcon.tag}  ")
            }
            //        Log.d("showw","After: size=${data.size},\ttag=${h.favIcon.tag}  ")
        Toast.makeText(h.view.context, "After: size=${data.size},\ttag=${h.favIcon.tag}  ", Toast.LENGTH_LONG).show()
            Toast.makeText(h.view.context,"Item: ${drink.id},\tname=${drink.name}, \tdesc=${drink.desc}, fav=${drink.fav}  ", Toast.LENGTH_LONG).show()
            if (mainActivity.drinkViewModel.favDrinks?.value?.size!! <=1 && ctxt.javaClass==FragmentFav::class.java) {
                mainActivity.tvBuffer.text="You haven't marked any favourite drinks. " +
                        "\nCheck out the all drinks tab to choose some favourites !"
                mainActivity.tvBuffer.visibility=View.VISIBLE
            }
            notifyDataSetChanged()
        }
    }
    //-----------------------------------------------
    private fun handleDelListener(mainActivity: MainActivity, position: Int) {
        if (mainActivity.drinkViewModel.mAllDrinks?.value?.size!! <=1) {
            mainActivity.tvBuffer.text="You have no drinks to list here. " +
            "\nTap the button on the top right to add a new drink !"
            mainActivity.tvBuffer.visibility=View.VISIBLE
        }
        GlobalScope.launch { mainActivity.drinkViewModel.del(data[position]); }
    }
}

