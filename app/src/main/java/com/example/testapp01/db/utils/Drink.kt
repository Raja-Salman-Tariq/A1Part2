package com.example.testapp01.db.utils

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testapp01.retrofit.Post
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Drink(@PrimaryKey(autoGenerate = true) var id:Int=-1, var name:String="", var desc:String="", var fav:Boolean=true, var toGet:Boolean=false,
                 val userId:Int?=null, val postId:Int?=null, val title:String?=null, val text:String?=null
) : Serializable{
    constructor(p: Post) : this(id=0, name="", desc="", fav =false, toGet=false,
        userId=p.userId, postId=p.id, title=p.title, text=p.text
        )
}
