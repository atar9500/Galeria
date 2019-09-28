package com.atar.galeria.db

import androidx.lifecycle.LiveData

/**
 * A repository class that provides a simple interface to save an query cache data
 */
class CacheRepository(private val mPhotoDao: PhotoDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allPhotos: LiveData<List<Photo>> = mPhotoDao.loadPhotos()

    // The suspend modifier tells the compiler that this must be called from a
    // coroutine or another suspend function.
    suspend fun updatePhotosCache(photos: List<Photo>) {
        mPhotoDao.updateData(photos)
    }

    suspend fun getPhotoById(cacheId: Int): Photo = mPhotoDao.getPhotoById(cacheId)

}