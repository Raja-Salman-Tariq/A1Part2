package com.example.testapp01.db.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink(@PrimaryKey(autoGenerate = true) var id:Int=-1, var name:String="", var desc:String="", var fav:Boolean=true)
