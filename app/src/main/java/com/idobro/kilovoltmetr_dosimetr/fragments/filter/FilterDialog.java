package com.idobro.kilovoltmetr_dosimetr.fragments.filter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.base.BaseDialog;
import com.idobro.kilovoltmetr_dosimetr.fragments.filter.FilterAdapter.OnGraphVisibilityChangeListener;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterDialog extends BaseDialog implements OnGraphVisibilityChangeListener {
    @BindView(R.id.vClose)
    View vClose;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btnSubmit)
    Button buttonSubmit;

    private GraphsVisibilityModel visibilityModel;
    private SubmitGraphVisibilityListener submitListener;
    private FilterAdapter adapter;

    private FilterDialog(@NonNull Context context, GraphsVisibilityModel visibilityModel, SubmitGraphVisibilityListener submitListener) {
        super(context);
        this.submitListener = submitListener;
        this.visibilityModel = visibilityModel;
        show();
    }

    public static void start(@NonNull Context context, GraphsVisibilityModel visibilityModel, SubmitGraphVisibilityListener submitListener) {
        FilterDialog dialog = new FilterDialog(context, visibilityModel, submitListener);
        dialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_graph_visibility);
        ButterKnife.bind(this);

        initViews();
        showVisibilities();
    }

    private void initViews() {
        if (adapter == null)
            adapter = new FilterAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getOwnerActivity()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    private void showVisibilities() {
        adapter.setItems(visibilityModel.getItems());
    }

    @OnClick(R.id.vClose)
    void close() {
        dismiss();
    }

    @OnClick(R.id.btnSubmit)
    void submit() {
        submitListener.onSubmit(visibilityModel);
        dismiss();
    }

    @Override
    public void onCheckChanged(boolean isChecked, int position) {
        visibilityModel.getItems().get(position).setChecked(isChecked);
    }
}