package com.example.testapp01.rv_adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.*
import com.example.testapp01.db.utils.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class MyRVAdapter(private val ctxt : Fragment, private var data:MutableList<Drink>):RecyclerView.Adapter<MyRVAdapter.MyViewHolder>(){

    lateinit var anotherViewHolder: MyViewHolder

    /*###############################################
    * -----         M A N D A T O R Y          -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    class MyViewHolder(val view: View, // default constructor, viewholder elements initialized
                       val name:TextView=view.findViewById(R.id.drinkName),
                       val roomId:TextView = view.findViewById(R.id.roomId),
                       val userId:TextView = view.findViewById(R.id.userId),
                       val postId:TextView = view.findViewById(R.id.myPostId),
                       val desc:TextView=view.findViewById(R.id.drinkDesc),
                       val del:ImageButton=view.findViewById(R.id.drinkDel),
                       val favIcon:ImageView=view.findViewById(R.id.favIcon),
                       val drinkImg:CircleImageView=view.findViewById(R.id.drinkImage)
                            ):RecyclerView.ViewHolder(view){

        fun handleTextViews(d: Drink) {

            if (null != d.postId){
                name.setText("Post Title: ${d.title}")
                roomId.setText("Room ID: ${d.id}")
                userId.setText("Post User ID : ${d.userId}")
                postId.setText("Post ID: ${d.postId}")
                desc.setText("Post Body: ${d.text}")

                name.visibility=View.VISIBLE
                roomId.visibility=View.VISIBLE
                userId.visibility=View.VISIBLE
                postId.visibility=View.VISIBLE
                desc.visibility=View.VISIBLE
            }
            else{
                userId.visibility=View.GONE
                postId.visibility=View.GONE

                name.setText("Drink name: ${d.name}")
                roomId.setText("Room ID: ${d.id}")
                desc.setText("Description: ${d.desc}")
            }
        }
                            }
    //-----------------------------------------------
    // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val viewHolder= MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_view, parent, false))

        if (ctxt.javaClass== FragmentFav::class.java){
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
        holder.name.text= "($id): $name"; holder.desc.text= descr

        holder.handleTextViews(data[position])


        when (id%4){
            0   -> holder.drinkImg.setImageResource(R.drawable.one)
            1   -> holder.drinkImg.setImageResource(R.drawable.two)
            2   -> holder.drinkImg.setImageResource(R.drawable.three)
            3   -> holder.drinkImg.setImageResource(R.drawable.four)
        }

        when (fav){
            false -> {
                holder.favIcon.setImageResource(R.drawable.unfavourite_icon)
                holder.favIcon.tag="unfavourite"
            }
            true  -> {
                holder.favIcon.setImageResource(R.drawable.favourite_icon)
                holder.favIcon.tag="favourite"
            }
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
    private fun handleListeners(h: MyViewHolder, position: Int){
        val mainActivity=(ctxt.activity as MainActivity)
        h.view.setOnClickListener { startDetailsActivity(mainActivity, data[position]) }
        h.del.setOnClickListener{ handleDelListener(mainActivity,position);}
        handleFavListener(h, mainActivity, position)
    }
    //-----------------------------------------------

    private fun startDetailsActivity(mainActivity: MainActivity, drink: Drink) {
        drink.toGet=true
        
        GlobalScope.launch {
            mainActivity.drinkViewModel.upd(drink)
        }

        ctxt.startActivity(
            Intent(ctxt.activity, DetailsActivity::class.java)
                .apply {
                    putExtra("drink", drink)
                })
    }


    //-----------------------------------------------
    private fun handleFavListener(h: MyViewHolder, mainActivity: MainActivity, position: Int){
        h.favIcon.setOnClickListener{

            val drink:Drink = data[0]
            Log.d("abc", "Item: ${drink.id},\tname=${drink.name}, \tdesc=${drink.desc}, fav=${drink.fav}  ")


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
            }

            if (mainActivity.drinkViewModel.favDrinks?.value?.size!! <=1 && ctxt.javaClass== FragmentFav::class.java) {
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

