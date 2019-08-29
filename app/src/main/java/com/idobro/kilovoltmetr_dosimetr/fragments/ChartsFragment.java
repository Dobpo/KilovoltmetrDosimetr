package com.idobro.kilovoltmetr_dosimetr.fragments;

import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.BaseFragment;

public class ChartsFragment extends BaseFragment {
    private LineChart front_chart;
    private LineChart full_chart;

    @Override
    protected int getResourceID() {
        return R.layout.charts_fragment;
    }

    @Override
    protected void initUI(View rootView) {
        front_chart = rootView.findViewById(R.id.front_chart);
        full_chart = rootView.findViewById(R.id.full_chart);
    }
}
