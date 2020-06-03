package com.idobro.kilovoltmetr_dosimetr.fragments.filter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.idobro.kilovoltmetr_dosimetr.base.BaseAdapter;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityModel.GraphVisibility;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends BaseAdapter<FilterViewHolder> {
    private List<GraphVisibility> items = new ArrayList<>();
    private OnGraphVisibilityChangeListener listener;

    FilterAdapter(OnGraphVisibilityChangeListener listener) {
        this.listener = listener;
    }

    void setItems(List<GraphVisibility> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FilterViewHolder.create(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    interface OnGraphVisibilityChangeListener {
        void onCheckChanged(boolean isChecked, int position);
    }
}