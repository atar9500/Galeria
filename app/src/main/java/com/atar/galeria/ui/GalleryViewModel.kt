package com.atar.galeria.ui

import android.app.Application
import androidx.lifecycle.*
import com.atar.galeria.api.GaleriaApiRepository
import com.atar.galeria.api.PhotosFetchStatus
import com.atar.galeria.api.responses.PhotosResponse
import com.atar.galeria.db.CacheDatabase
import com.atar.galeria.db.CacheRepository
import com.atar.galeria.db.LikesCacheManager
import com.atar.galeria.db.Photo
import com.atar.galeria.ui.misc.NavigateEvent
import com.atar.galeria.ui.misc.SingleEvent
import com.atar.galeria.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Repositories
     */
    private val mApiRepository: GaleriaApiRepository = GaleriaApiRepository()

    private val mCacheRepository: CacheRepository

    /**
     * Fetching & caching photos related
     */
    val photos: LiveData<List<Photo>>

    val photosResponseStatus = MutableLiveData<PhotosFetchStatus?>()

    fun fetchPhotos() = viewModelScope.launch {
        photosResponseStatus.value = PhotosFetchStatus.FETCHING
        val response: Response<PhotosResponse>?
        try {
            response = mApiRepository.fetchPhotos(
                Constants.MEMBER_ID,
                Constants.GET_LIKES,
                Constants.LIMIT,
                Constants.START
            )
            if (response != null && response.isSuccessful) {
                val body = response.body()

                // We want to be sure that our response was a success before we update cache
                if (body?.isSuccess == true) {
                    photosResponseStatus.value = PhotosFetchStatus.SUCCESS
                    mCacheRepository.updatePhotosCache(body.photos ?: emptyList())
                } else {
                    photosResponseStatus.value = PhotosFetchStatus.FAILED
                }
            } else {
                photosResponseStatus.value = PhotosFetchStatus.FAILED
            }
        } catch (e: Exception) {
            photosResponseStatus.value = PhotosFetchStatus.NO_NETWORK
        }
    }

    /**
     * Liking photos related
     */
    val likes = MutableLiveData<List<String>>()

    @Synchronized
    fun togglePhotoLike(photoId: String) {
        likes.value = LikesCacheManager.updateLike(getApplication(), photoId)
    }

    /**
     * LiveData that will be used in case we want to query a photo from cache to view it separately,
     * rather than passing the whole [Photo] object as [Serializable] or Parcelable
     * through the arguments bundle of a Fragment
     */
    val queriedPhoto = MutableLiveData<Photo>()

    fun queryPhoto(cacheId: Int) = viewModelScope.launch {
        queriedPhoto.value = mCacheRepository.getPhotoById(cacheId)
    }

    /**
     * Navigating between fragments related data
     */
    private val mNavigateEvent = MutableLiveData<SingleEvent<NavigateEvent>>()

    fun getNavigateEvent(): LiveData<SingleEvent<NavigateEvent>> {
        return mNavigateEvent
    }

    fun setNavigateEvent(event: NavigateEvent) {
        mNavigateEvent.value = SingleEvent(event)
    }

    init {
        // Gets reference to photoDao from CacheDatabase to construct
        // the correct CacheRepository.
        val photoDao = CacheDatabase.getDatabase(application).photoDao()
        mCacheRepository = CacheRepository(photoDao)
        photos = mCacheRepository.allPhotos

        likes.value = LikesCacheManager.getLikes(application)

        fetchPhotos()
    }

}
