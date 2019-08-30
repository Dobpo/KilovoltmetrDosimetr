package com.idobro.kilovoltmetr_dosimetr.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.BaseFragment;

import java.util.ArrayList;

public class ChartsFragment extends BaseFragment {
    private LineChart front_chart;
    private LineChart full_chart;

    private float[] frontArray = {0,1,2,3,4,5,6,7,8,9};
    private float[] fullArray = {10,10,10,10,10,10,10,10,10,10,10,10,10,10};

    @Override
    protected int getResourceID() {
        return R.layout.charts_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUI(View rootView) {
        front_chart = rootView.findViewById(R.id.front_chart);
        full_chart = rootView.findViewById(R.id.full_chart);

        initChart(front_chart);
        initChart(full_chart);

        setDataToCharts();
    }

    private void initChart(LineChart chart) {
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(true);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.invalidate();
    }

    private void setDataToCharts() {
        //Front chart
        ArrayList<ILineDataSet> frontDataSets = new ArrayList<>();
        ArrayList<Entry> frontValue = new ArrayList<>();

        for (int i = 0; i < frontArray.length; i++) {
            frontValue.add(new Entry(i, frontArray[i]));
        }

        LineDataSet frontLineDataSet = new LineDataSet(frontValue, "DataSet Front");

        frontLineDataSet.setColor(Color.parseColor("#4caf50"));
        frontLineDataSet.setLineWidth(2f);
        frontLineDataSet.setDrawValues(false);
        frontLineDataSet.setDrawCircles(false);
        frontLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        frontLineDataSet.setDrawFilled(false);
        frontDataSets.add(frontLineDataSet);

        front_chart.setData(new LineData(frontDataSets));
        front_chart.getLegend().setEnabled(false);
        front_chart.invalidate();

        //Full chart
        ArrayList<ILineDataSet> fullDataSets = new ArrayList<>();
        ArrayList<Entry> fullValue = new ArrayList<>();

        for (int i = 0; i < fullArray.length; i++) {
            fullValue.add(new Entry(i, fullArray[i]));
        }

        LineDataSet fullLineDataSet = new LineDataSet(fullValue, "DataSet Full");

        fullLineDataSet.setColor(Color.parseColor("#4caf50"));
        fullLineDataSet.setLineWidth(2f);
        fullLineDataSet.setDrawValues(false);
        fullLineDataSet.setDrawCircles(false);
        fullLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        fullLineDataSet.setDrawFilled(false);

        fullDataSets.add(fullLineDataSet);

        full_chart.setData(new LineData(fullDataSets));
        full_chart.getLegend().setEnabled(false);
        full_chart.invalidate();
    }
}
