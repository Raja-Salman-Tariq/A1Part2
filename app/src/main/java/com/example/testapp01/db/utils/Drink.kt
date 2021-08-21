package com.example.testapp01.db.utils

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testapp01.retrofit.Post

@Entity
data class Drink(@PrimaryKey(autoGenerate = true) var id:Int=-1, var name:String="", var desc:String="", var fav:Boolean=true, var toGet:Boolean=false){
    constructor(id:Int?, p: Post) : this(id!!, name=("${p.id}::${p.userId} "+p.title), desc=p.text.substring(0,25), fav = p.userId%2==0)
}
