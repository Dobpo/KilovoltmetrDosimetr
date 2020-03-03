package com.idobro.kilovoltmetr_dosimetr.fragments.graph_dialog;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idobro.kilovoltmetr_dosimetr.models.GraphsDates;

import java.util.ArrayList;
import java.util.List;

public class GraphsAdapter extends RecyclerView.Adapter<GraphViewHolder> {
    private List<GraphsDates> graphs = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;

    GraphsAdapter(@NonNull OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    void setGraphs(@NonNull List<GraphsDates> graphs) {
        this.graphs = graphs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GraphViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return GraphViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphViewHolder holder, int position) {
        holder.bind(graphs.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return graphs.size();
    }

    interface OnItemClickListener {
        void onItemClick(String testString);
    }
}