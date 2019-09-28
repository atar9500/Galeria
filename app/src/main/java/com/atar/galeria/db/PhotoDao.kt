package com.atar.galeria.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.atar.galeria.utils.Constants

@Dao
interface PhotoDao {

    /**
     * We can find a more efficient way to update our cache rather then clear all & insert
     */
    @Transaction
    suspend fun updateData(photos: List<Photo>) {
        clearPhotos()
        addPhotos(photos)
    }

    @Insert
    suspend fun addPhotos(photos: List<Photo>)

    @Query("DELETE FROM ${Constants.PHOTOS_TABLE}")
    suspend fun clearPhotos()

    @Query("SELECT * FROM ${Constants.PHOTOS_TABLE}")
    fun loadPhotos(): LiveData<List<Photo>>

    @Query("SELECT * FROM ${Constants.PHOTOS_TABLE} WHERE cacheId LIKE :cacheId")
    suspend fun getPhotoById(cacheId: Int): Photo

}