package com.atar.galeria.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atar.galeria.R;
import com.atar.galeria.utils.Constants;
import com.atar.galeria.utils.PhotoHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    /**
     * Required empty public constructor
     */
    public PhotoFragment() {
    }


    /**
     * To present some immediate info without being dependent on async calls to DB/Server,
     * we'll have to pass some initial metadata through PhotoFragment's arguments.
     * <p>
     * For now PhotoFragment present only URL, likes & views, when we'll want to present
     * much more data than this we'll have to query it through async API call/DB query
     */
    public static final String ARG_PHOTO_ID = "arg_photo_id";
    public static final String ARG_PHOTO_LIKES = "arg_photo_likes";
    public static final String ARG_PHOTO_VIEWS = "arg_photo_views";

    // We want to pass the same url that is used in our GalleryFragment
    // for faster loading from Picasso Cache
    public static final String ARG_PHOTO_URL = "arg_photo_url";

    /**
     * UI Widgets
     */
    private ImageView mPhotoImage;
    private TextView mLikesCount;
    private TextView mViewsCount;

    /**
     * Data
     */
    private GalleryViewModel mViewModel;
    private Observer<List<String>> mLikeObserver = new Observer<List<String>>() {
        @Override
        public void onChanged(List<String> likes) {
            Bundle args = getArguments();
            if (args == null) {
                return;
            }
            String photoId = args.getString(ARG_PHOTO_ID);
            int likesCount = args.getInt(ARG_PHOTO_LIKES, 0);
            if (photoId != null) {
                boolean isLiked = PhotoHelper.isPhotoLiked(likes, photoId);
                updateLikeCounter(likesCount, isLiked);
            }
        }
    };
    private View.OnClickListener mLikesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle args = getArguments();
            if (args == null) {
                return;
            }
            String photoId = args.getString(ARG_PHOTO_ID);
            if (photoId != null) {
                mViewModel.togglePhotoLike(photoId);
            }
        }
    };

    /**
     * Fragment Methods
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoImage = view.findViewById(R.id.fpho_photo);
        mLikesCount = view.findViewById(R.id.fpho_likes);
        mViewsCount = view.findViewById(R.id.fpho_views);

        Bundle args = getArguments();
        if (args != null) {
            String photoId = args.getString(ARG_PHOTO_ID);
            mPhotoImage.setTransitionName(photoId);

            TransitionInflater transitionInflater = TransitionInflater.from(getContext());
            Transition sharedElementTransition = transitionInflater.inflateTransition(R.transition.photo_transition);
            sharedElementTransition.setDuration(Constants.PHOTO_ENTER_TRANSITION_DURATION);
            setSharedElementEnterTransition(sharedElementTransition);
            setSharedElementReturnTransition(sharedElementTransition);

            Transition fade = new Fade();
            fade.setDuration(Constants.PHOTO_ENTER_TRANSITION_DURATION);
            setEnterTransition(fade);
            setExitTransition(fade);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            mViewModel = ViewModelProviders.of(getActivity()).get(GalleryViewModel.class);
            mViewModel.getLikes().observe(this, mLikeObserver);
        }

        Bundle args = getArguments();
        if (args != null) {
            Picasso.get()
                    .load(args.getString(ARG_PHOTO_URL))
                    .placeholder(R.drawable.ic_photo_placeholder)
                    .error(R.drawable.ic_photo_error)
                    .into(mPhotoImage);

            int viewsCount = args.getInt(ARG_PHOTO_VIEWS, 0);
            mViewsCount.setText(viewsCount + " " + getResources().getString(R.string.views));
        }

        mLikesCount.setOnClickListener(mLikesClickListener);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.photo_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fpho_details:
                PhotoDetailsDialogFragment.newInstance()
                        .show(getChildFragmentManager(), PhotoDetailsDialogFragment.TAG);
                return true;
            default:
                return false;
        }
    }

    /**
     * PhotoFragment Functions
     */
    @SuppressLint("SetTextI18n")
    private void updateLikeCounter(int likes, boolean isLiked) {
        mLikesCount.setText((likes + (isLiked ? 1 : 0)) + " " + getResources().getString(R.string.likes));
        int likedIconRes = isLiked ? R.drawable.ic_like_full : R.drawable.ic_like_empty;
        mLikesCount.setCompoundDrawablesRelativeWithIntrinsicBounds(likedIconRes, 0, 0, 0);
    }

}
