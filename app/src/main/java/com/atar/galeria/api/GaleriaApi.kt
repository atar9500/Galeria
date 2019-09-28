package com.atar.galeria.api

import com.atar.galeria.api.responses.PhotosResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GaleriaApi {

    @FormUrlEncoded
    @Headers("X-API-VERSION: 20", "X-ENV: ANDROID")
    @POST("get_photos_public")
    suspend fun fetchPhotos(
        @Field("member_id") memberId: String,
        @Field("get_likes") getLikes: Boolean,
        @Field("limit") limit: Int,
        @Field("start") start: Int
    ): Response<PhotosResponse>?

}