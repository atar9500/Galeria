package com.atar.galeria.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atar.galeria.R;
import com.atar.galeria.db.Photo;
import com.atar.galeria.ui.misc.LabelsAdapter;
import com.atar.galeria.ui.misc.ListDivider;
import com.atar.galeria.utils.PhotoHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PhotoDetailsDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = PhotoDetailsDialogFragment.class.getSimpleName();

    public static PhotoDetailsDialogFragment newInstance() {
        return new PhotoDetailsDialogFragment();
    }

    /**
     * Constructor
     */
    public PhotoDetailsDialogFragment() {
    }

    /**
     * UI Widgets
     */
    private TextView mDate;
    private TextView mWidth;
    private TextView mHeight;
    private TextView mAspectRatio;

    /**
     * Data
     */
    private LabelsAdapter mLabelsAdapter;
    private Observer<Photo> mQueryPhotoEventObserver = new Observer<Photo>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(Photo photo) {
            mWidth.setText(getString(R.string.width) + ": " + photo.getWidth() + getString(R.string.px));
            mHeight.setText(getString(R.string.height) + ": " + photo.getHeight() + getString(R.string.px));
            mAspectRatio.setText(getString(R.string.aspect_ratio) + ": " + photo.getRatio());
            mDate.setText(getString(R.string.date_prefix) + " " + PhotoHelper.formatDate(photo.getUploadDate() * 1000L));
            mLabelsAdapter.update(photo.getLabels());
        }
    };

    /**
     * BottomSheetDialogFragment Methods
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_details_dialog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            GalleryViewModel viewModel = ViewModelProviders.of(getActivity()).get(GalleryViewModel.class);
            viewModel.getQueriedPhoto().observe(this, mQueryPhotoEventObserver);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView labels = view.findViewById(R.id.fpdd_labels);
        mLabelsAdapter = new LabelsAdapter();
        labels.setAdapter(mLabelsAdapter);
        labels.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        int labelsSpacing = getResources().getDimensionPixelOffset(R.dimen.photos_labels_spacing);
        labels.addItemDecoration(new ListDivider(labelsSpacing, 1, true));

        mDate = view.findViewById(R.id.fpdd_date);
        mWidth = view.findViewById(R.id.fpdd_width);
        mHeight = view.findViewById(R.id.fpdd_height);
        mAspectRatio = view.findViewById(R.id.fpdd_aspect_ratio);
    }
}
