package com.example.testapp01.rv_adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp01.*
import com.example.testapp01.db.utils.*
import com.example.testapp01.retrofit.Comment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CommentsRVAdapter(private val ctxt : AppCompatActivity, private var data:MutableList<Comment>):RecyclerView.Adapter<CommentsRVAdapter.MyViewHolder>(){

//    lateinit var anotherViewHolder: MyViewHolder

    /*###############################################
    * -----         M A N D A T O R Y          -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    class MyViewHolder(val view: View, // default constructor, viewholder elements initialized
                       val name:TextView=view.findViewById(R.id.commentName),
                       val desc:TextView=view.findViewById(R.id.commentComment),
                            ):RecyclerView.ViewHolder(view)
    //-----------------------------------------------
    // Create a new view, which defines the UI of the list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val viewHolder= MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_list_row, parent, false))

//        anotherViewHolder=viewHolder

        return viewHolder
    }
    //-----------------------------------------------
    // actual interactions with view; ie setting and changing of the view / listeners
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = data[position].name
        val cmnt = data[position].body
        holder.name.text=name; holder.desc.text= cmnt
    }
    //-----------------------------------------------
    override fun getItemCount()= data.size
    //-----------------------------------------------





    /*###############################################
    * -----         C U S T O M A R Y          -----*
    * =============================================*/
    fun setCmntData(cmntData: List<Comment> ){
        data= cmntData as MutableList<Comment>
        notifyDataSetChanged()
    }
    //-----------------------------------------------
}
