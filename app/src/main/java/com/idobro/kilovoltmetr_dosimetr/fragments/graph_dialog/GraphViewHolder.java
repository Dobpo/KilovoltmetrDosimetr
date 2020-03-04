package com.idobro.kilovoltmetr_dosimetr.fragments.graph_dialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.base.BaseViewHolder;
import com.idobro.kilovoltmetr_dosimetr.fragments.graph_dialog.GraphsAdapter.OnItemClickListener;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsDates;
import com.idobro.kilovoltmetr_dosimetr.utils.StringUtils;

import butterknife.BindView;

class GraphViewHolder extends BaseViewHolder {
    @BindView(R.id.idTextView)
    TextView idTextView;

    @BindView(R.id.dateTextView)
    TextView dateTextView;


    private GraphViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    static GraphViewHolder create(ViewGroup parent) {
        return new GraphViewHolder(generateView(parent, R.layout.item_graph));
    }

    void bind(GraphsDates graphsDates, OnItemClickListener onItemClickListener) {
        idTextView.setText(String.valueOf(graphsDates.getId()));
        dateTextView.setText(graphsDates.getDate() == 0 ? StringUtils.DEFAULT_PLACEHOLDER
                : String.valueOf(graphsDates.getDate()));

        if (onItemClickListener != null)
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(graphsDates.getId()));
    }
}