package com.atar.galeria.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.TransitionInflater
import com.atar.galeria.api.PhotosFetchStatus
import com.atar.galeria.db.Photo
import com.atar.galeria.ui.misc.ListDivider
import com.atar.galeria.ui.misc.NavigateEvent
import com.atar.galeria.ui.misc.PhotosAdapter
import com.atar.galeria.utils.Constants
import kotlinx.android.synthetic.main.gallery_fragment.*
import android.content.res.Configuration
import com.atar.galeria.R
import com.google.android.material.snackbar.Snackbar


class GalleryFragment : Fragment() {

    companion object {
        private const val ROWS_PORTRAIT = 2
        private const val ROWS_LANDSCAPE = 3
    }

    /**
     * Data
     */
    private lateinit var mViewModel: GalleryViewModel
    private val mPhotosResponseObserver = Observer<List<Photo>> {
        mPhotosAdapter.updateList(it)
    }
    private val mLikesObserver = Observer<List<String>> {
        mPhotosAdapter.updateLikes(it)
    }
    private val mPhotosResponseStatusListener = Observer<PhotosFetchStatus?> {
        when (it) {
            PhotosFetchStatus.SUCCESS -> {
                fgal_refresh.isRefreshing = false
                fgal_empty_anim.setAnimation(Constants.GALLERY_EMPTY_ANIMATION)
                fgal_empty_label.text = getString(com.atar.galeria.R.string.gallery_empty_label)
            }
            PhotosFetchStatus.FETCHING -> {
                fgal_refresh.isRefreshing = true
            }
            PhotosFetchStatus.FAILED -> {
                fgal_refresh.isRefreshing = false
                fgal_empty_anim.setAnimation(Constants.GALLERY_EMPTY_ANIMATION)
                fgal_empty_label.text = getString(com.atar.galeria.R.string.gallery_empty_label)

                Snackbar.make(view!!, R.string.gallery_empty_label, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) {
                        mViewModel.fetchPhotos()
                    }.show()
            }
            PhotosFetchStatus.NO_NETWORK -> {
                fgal_refresh.isRefreshing = false
                fgal_empty_anim.setAnimation(Constants.NO_NETWORK_ANIMATION)
                fgal_empty_label.text = getString(R.string.gallery_no_network_label)

                Snackbar.make(
                    view!!,
                    R.string.gallery_no_network_label,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) {
                        mViewModel.fetchPhotos()
                    }.show()
            }
        }
    }
    private val mPhotoClickListener = object : PhotosAdapter.PhotoClickListener {
        override fun onPhotoClick(
            photo: Photo?,
            photoUrl: String?,
            extras: FragmentNavigator.Extras?,
            viewToExclude: View
        ) {
            if (photo == null) {
                return
            }
            mViewModel.queryPhoto(photo.cacheId)

            val arguments = Bundle()
            arguments.putString(PhotoFragment.ARG_PHOTO_ID, photo.photoId)
            arguments.putInt(PhotoFragment.ARG_PHOTO_VIEWS, photo.views ?: 0)
            arguments.putInt(PhotoFragment.ARG_PHOTO_LIKES, photo.likes ?: 0)
            arguments.putString(PhotoFragment.ARG_PHOTO_URL, photoUrl)

            val galleryExitTransition = TransitionInflater.from(context)
                .inflateTransition(com.atar.galeria.R.transition.gallery_exit_transition)
            galleryExitTransition.duration = Constants.GALLERY_EXIT_TRANSITION_DURATION
            galleryExitTransition.startDelay = Constants.GALLERY_EXIT_TRANSITION_DELAY_DURATION
            galleryExitTransition.excludeTarget(viewToExclude, true)
            exitTransition = galleryExitTransition

            mViewModel.setNavigateEvent(
                NavigateEvent(
                    com.atar.galeria.R.id.action_galleryFragment_to_photoFragment,
                    arguments,
                    extras
                )
            )
        }

        override fun onLikeClick(photoId: String?) {
            if (photoId != null) {
                mViewModel.togglePhotoLike(photoId)
            }
        }

    }
    private val mOnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        mViewModel.fetchPhotos()
    }
    private var mPhotosAdapter = PhotosAdapter(mPhotoClickListener)

    /**
     * Fragment Functions
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(com.atar.galeria.R.layout.gallery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(GalleryViewModel::class.java)
        mViewModel.photos.observe(this, mPhotosResponseObserver)
        mViewModel.likes.observe(this, mLikesObserver)
        mViewModel.photosResponseStatus.observe(this, mPhotosResponseStatusListener)

        fgal_refresh.setOnRefreshListener(mOnRefreshListener)

        val gridSpacing =
            resources.getDimensionPixelOffset(com.atar.galeria.R.dimen.photos_list_spacing)
        fgal_list.setEmptyView(fgal_empty_view)
        val orientation = resources.configuration.orientation
        var gridSize = ROWS_PORTRAIT
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridSize = ROWS_LANDSCAPE
        }

        fgal_list.apply {
            adapter = mPhotosAdapter
            layoutManager = GridLayoutManager(context!!, gridSize)
            addItemDecoration(ListDivider(gridSpacing, gridSize))
            postponeEnterTransition()
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.atar.galeria.R.menu.gallery_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.atar.galeria.R.id.fgal_menu_refresh -> {
                mViewModel.fetchPhotos()
                true
            }
            else -> {
                false
            }
        }
    }

}
