package com.atar.galeria.api

import com.atar.galeria.api.responses.PhotosResponse
import retrofit2.Response

/**
 * A repository class that provides a simple interface to fetch photos from the server
 */
class GaleriaApiRepository {

    private val mGaleriaApi: GaleriaApi = GaleriaBridge.instance

    /**
     * Since Retrofit 2.6.0 there'ss now suspend functions support for use in ViewModel's coroutines
     * to simplify code that executes asynchronously.
     *
     * Quoting the link below:
     *
     * "Behind the scenes this behaves as if defined as fun user(...): Call<User>
     *     and then invoked with Call.enqueue."
     *
     *  Source: https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
     */
    suspend fun fetchPhotos(
        memberId: String,
        getLikes: Boolean,
        limit: Int,
        start: Int
    ): Response<PhotosResponse>? {
        return mGaleriaApi.fetchPhotos(memberId, getLikes, limit, start)
    }

}