package com.atar.galeria.ui.misc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atar.galeria.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class LabelsAdapter extends RecyclerView.Adapter<LabelsAdapter.LabelViewHolder> {

    /**
     * Data
     */
    private List<String> mLabels;

    /**
     * RecyclerView.Adapter<LabelsAdapter.LabelViewHolder> Methods
     */
    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LabelViewHolder(inflater.inflate(R.layout.photo_label, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        holder.bind(mLabels.get(position));
    }

    @Override
    public int getItemCount() {
        return mLabels == null ? 0 : mLabels.size();
    }

    /**
     * LabelsAdapter Methods
     */
    public void update(List<String> labels) {
        mLabels = labels;
        notifyDataSetChanged();
    }

    /**
     * Inner Classes
     */
    class LabelViewHolder extends RecyclerView.ViewHolder {

        /**
         * UI Widgets
         */
        private Chip mLabel;

        /**
         * Constructor
         */
        LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            mLabel = itemView.findViewById(R.id.photo_label_chip);
        }

        /**
         * LabelViewHolder Methods
         */
        void bind(String label) {
            mLabel.setText(label);
        }
    }

}
