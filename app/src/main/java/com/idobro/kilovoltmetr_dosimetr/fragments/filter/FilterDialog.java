package com.idobro.kilovoltmetr_dosimetr.fragments.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.base.BaseDialog;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityFilterModel;

import butterknife.ButterKnife;

public class FilterDialog extends BaseDialog {
    private GraphsVisibilityFilterModel filter;

    private FilterDialog(@NonNull Context context) {
        super(context);
    }

    public static void start(@NonNull Context context, GraphsVisibilityFilterModel filter) {
        Intent starter = new Intent(context, FilterDialog.class);
        //starter.putExtra();
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_graph_visibility);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        // TODO: 02.06.2020
    }
}