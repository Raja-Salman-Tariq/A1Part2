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
import retrofit2.http.Body


class CommentsRVAdapter(private val ctxt : AppCompatActivity, private var data:MutableList<Comment> = mutableListOf()):RecyclerView.Adapter<CommentsRVAdapter.MyViewHolder>(){

//    lateinit var anotherViewHolder: MyViewHolder

    /*###############################################
    * -----         M A N D A T O R Y          -----*
    * =============================================*/
    //-----------------------------------------------
    //-----------------------------------------------
    class MyViewHolder(val view: View, // default constructor, viewholder elements initialized
                       val postId:TextView=view.findViewById(R.id.commentPostId),
                       val id:TextView=view.findViewById(R.id.commentId),
                       val name:TextView=view.findViewById(R.id.commentName),
                       val email: TextView=view.findViewById(R.id.commentEmail),
                       val body: TextView = view.findViewById(R.id.commentBody)
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
        val comment = data[position]

//        Log.d("cmnts", "onBindViewHolder: Post/Drink_ID=${comment.drinkId},\n myId=${comment.id},\n " +
//                "name=${comment.name},\n email=${comment.email},\n body=${comment.body}\n" +
//                "-----------------------------\n\n ")


        if (data.isNotEmpty()) {
            holder.postId.text="Post ID: ${comment.drinkId.toString()}"
            holder.id.text="My Comment ID: ${comment.id.toString()}"
            holder.name.text="Name: ${comment.name}"
            holder.email.text="Email: ${comment.email}"
            holder.body.text="Comment Body: ${comment.body}"
        }
    }
    //-----------------------------------------------
    override fun getItemCount()= data.size
    //-----------------------------------------------





    /*###############################################
    * -----         C U S T O M A R Y          -----*
    * =============================================*/
    fun setCmntData(cmntData: List<Comment> ){
        data.clear()
        data.addAll(cmntData)
        notifyDataSetChanged()


        for (comment in data)
            logCmnt(comment)
        Log.d("cmnts", " setCmntData inside adapter notifiedDatasetChanged & found size ${data.size}: ")
    }
    //-----------------------------------------------

    fun logCmnt(comment: Comment){
            Log.d("cmnts", "onBindViewHolder: Post/Drink_ID=${comment.drinkId},\n myId=${comment.id},\n " +
                "name=${comment.name},\n email=${comment.email},\n body=${comment.body}\n" +
                "-----------------------------\n\n ")}
}
