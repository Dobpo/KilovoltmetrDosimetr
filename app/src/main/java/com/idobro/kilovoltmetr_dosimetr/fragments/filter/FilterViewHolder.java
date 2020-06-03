package com.idobro.kilovoltmetr_dosimetr.fragments.filter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.base.BaseViewHolder;
import com.idobro.kilovoltmetr_dosimetr.fragments.filter.FilterAdapter.OnGraphVisibilityChangeListener;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityModel.GraphVisibility;

import butterknife.BindView;

public class FilterViewHolder extends BaseViewHolder {
    @BindView(R.id.checkbox)
    CheckBox checkbox;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.vLine)
    View vLine;

    private OnGraphVisibilityChangeListener listener;

    private FilterViewHolder(View itemView, OnGraphVisibilityChangeListener listener) {
        super(itemView);
        this.listener = listener;
    }

    static FilterViewHolder create(ViewGroup parent, OnGraphVisibilityChangeListener listener) {
        return new FilterViewHolder(generateView(parent, R.layout.item_graph_visibility), listener);
    }

    public void bind(GraphVisibility visibilityModel) {
        if (visibilityModel == null)
            return;

        if (visibilityModel.getChecked())
            checkbox.setChecked(true);

        if (listener != null)
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onCheckChanged(isChecked, getLayoutPosition()));

        if (!visibilityModel.getTitle().isEmpty())
            tvTitle.setText(visibilityModel.getTitle());

        if (!visibilityModel.getColor().isEmpty())
            vLine.setBackgroundColor(Color.parseColor(visibilityModel.getColor()));
    }
}