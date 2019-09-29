package com.atar.galeria.ui.misc

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.atar.galeria.R
import com.atar.galeria.db.Photo
import com.atar.galeria.utils.PhotoHelper
import com.squareup.picasso.Picasso

class PhotosAdapter(clickListener: PhotoClickListener) :
    RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    /**
     * Data
     */
    private var mPhotos: List<Photo> = emptyList()
    private var mLikes: List<String> = emptyList()
    private val mClickListener = clickListener

    /**
     * RecyclerView.Adapter Functions
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return PhotoViewHolder(itemView, mClickListener)
    }

    override fun getItemCount(): Int = mPhotos.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = mPhotos[position]
        val liked = mLikes.indexOf(photo.photoId) != -1
        holder.bind(photo, liked)
    }

    /**
     * PhotosAdapter Functions
     */
    fun updateList(photos: List<Photo>) {
        mPhotos = photos
        notifyDataSetChanged()
    }

    fun updateLikes(likes: List<String>) {
        mLikes = likes
        notifyDataSetChanged()
    }

    /**
     * Internal Classes
     */
    class PhotoViewHolder(
        itemView: View,
        clickListener: PhotoClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        private val mCover: ImageView = itemView.findViewById(R.id.photo_cover)
        private val mLikes: TextView = itemView.findViewById(R.id.photo_likes)
        private val mViews: TextView = itemView.findViewById(R.id.photo_views)
        private val mClickListener = clickListener
        private var mPhoto: Photo? = null
        private var mPhotoUrl: String? = null

        init {
            mCover.setOnClickListener {
                var extras: FragmentNavigator.Extras? = null
                val photoId = mPhoto?.photoId

                if (photoId != null) {
                    extras = FragmentNavigatorExtras(
                        mCover as View to photoId
                    )
                }

                mClickListener.onPhotoClick(mPhoto, mPhotoUrl, extras, it)
            }

            mLikes.setOnClickListener {
                mClickListener.onLikeClick(mPhoto?.photoId)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(photo: Photo, liked: Boolean) {
            mPhoto = photo

            mLikes.text = "${(photo.likes
                ?: 0) + if (liked) 1 else 0} ${itemView.resources.getString(R.string.likes)}"
            mViews.text = "${PhotoHelper.formatCountLabel(
                photo.views ?: 0
            )} ${itemView.resources.getString(R.string.views)}"

            var likedIconRes = R.drawable.ic_like_empty
            if (liked) {
                likedIconRes = R.drawable.ic_like_full
            }
            mLikes.setCompoundDrawablesRelativeWithIntrinsicBounds(likedIconRes, 0, 0, 0)

            val photoHeight = itemView.resources.getDimensionPixelOffset(R.dimen.photo_item_height)
            var photoWidth = 0
            if (photo.ratio != null) {
                photoWidth = (photoHeight * photo.ratio).toInt()
            }

            if (photo.photoId != null) {
                mCover.transitionName = photo.photoId
            }

            mPhotoUrl = PhotoHelper.getPhotoUrl(photo, photoHeight, photoWidth)
            Picasso.get()
                .load(mPhotoUrl)
                .placeholder(R.drawable.ic_photo_placeholder)
                .error(R.drawable.ic_photo_error)
                .into(mCover)
        }

    }

    interface PhotoClickListener {
        fun onPhotoClick(
            photo: Photo?,
            photoUrl: String?,
            extras: FragmentNavigator.Extras?,
            viewToExclude: View
        )

        fun onLikeClick(
            photoId: String?
        )
    }

}