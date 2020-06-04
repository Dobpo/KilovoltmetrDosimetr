package com.idobro.kilovoltmetr_dosimetr.fragments.charts_screen;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.idobro.kilovoltmetr_dosimetr.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class GraphsInfoAdapter extends BaseAdapter<GraphsInfoViewHolder> {
    private List<InfoItem> items = new ArrayList<>();

    void setItems(List<InfoItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GraphsInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return GraphsInfoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphsInfoViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}