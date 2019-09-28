package com.atar.galeria.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atar.galeria.utils.Constants
import com.google.gson.annotations.SerializedName

/**
 * A data class for photos we fetch from the server, it servers as well as an entity for our SQLite DB,
 * in case we would like to cache photos we fetched when network is N/A or when we've got a server error.
 */
@Entity(tableName = Constants.PHOTOS_TABLE)
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val cacheId: Int = 0,

    @SerializedName("id")
    val photoId: String?,

    @SerializedName("member_id")
    val memberId: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("views")
    val views: Int?,

    @SerializedName("adult")
    val adult: Boolean?,

    @SerializedName("event_id")
    val eventId: Long?,

    @SerializedName("width")
    val width: Int?,

    @SerializedName("height")
    val height: Int?,

    @SerializedName("upload_date")
    val uploadDate: Long,

    @SerializedName("labels")
    val labels: List<String>?,

    @SerializedName("ratio")
    val ratio: Float?,

    @SerializedName("likes")
    val likes: Int?

)