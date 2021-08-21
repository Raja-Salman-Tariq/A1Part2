package com.example.testapp01.retrofit

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
public interface CommentDao {
    @Insert
    fun addComment(comment: Comment)

    @Delete
    fun deleteComment(comment: Comment)

    @Update
    fun updateComment(comment:Comment)

    @Query("SELECT * FROM Comment WHERE drinkId IN (SELECT id FROM Drink WHERE toGet=1) ORDER BY id ASC")
    fun getDrinkComments(): LiveData<List<Comment>>

    @Query("SELECT * FROM Comment ORDER BY name ASC")
    fun getAll(): LiveData<List<Comment>>

    @Query("DELETE FROM Comment")
    fun delAll()

    @Insert
    fun addComments(Comments: List<Comment>)

}