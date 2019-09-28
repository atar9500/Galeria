package com.atar.galeria.db

import android.content.Context

/**
 * A singleton that implements an easier interface to get & update liked photos cache,
 * basically a JSON array of liked photo ids. Because a "like" consists such a small data we've
 * got small amount of photos to like, a faster approach will be to store them in a shared preferences.
 *
 * Most likely in a real life case we'll need to save big amounts of cache and a like will
 * contain much more data than just a photo id, a more efficient approach will be to store & manage
 * likes in an SQLite DB.
 */
object LikesCacheManager {

    private const val PREFS_LIKES = "likes_preferences"
    private const val KEY_LIKES = "likes"

    private val mConverters = Converters()

    fun getLikes(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFS_LIKES, Context.MODE_PRIVATE)
        val rawLikesJson = prefs.getString(KEY_LIKES, null) ?: return emptyList()
        return mConverters.fromString(rawLikesJson)
    }

    private fun saveLikes(context: Context, likes: List<String>): List<String> {
        val prefs = context.getSharedPreferences(PREFS_LIKES, Context.MODE_PRIVATE)
        val rawLikesJson = mConverters.fromList(likes)
        with(prefs.edit()) {
            putString(KEY_LIKES, rawLikesJson)
            commit()
        }
        return likes
    }

    /**
     * We want to avoid a scenario when user toggles the same like button twice
     * before this function is finished, therefore we use @Synchronized
     */
    @Synchronized
    fun updateLike(context: Context, photoId: String): List<String> {
        val likes = getLikes(context).toMutableList()
        val indexOfLike = likes.indexOf(photoId)
        if (indexOfLike == -1) {
            likes.add(photoId)
        } else {
            likes.removeAt(indexOfLike)
        }
        return saveLikes(context, likes)
    }

}