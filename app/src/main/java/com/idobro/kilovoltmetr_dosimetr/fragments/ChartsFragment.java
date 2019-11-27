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
import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.ChartDataModel;

import java.util.ArrayList;

public class ChartsFragment extends BaseFragment {
    private LineChart front_chart;
    private LineChart full_chart;

    private float[] frontArray;
    private float[] frontFirstChanelArray;
    private float[] frontSecondChanelArray;
    private float[] frontThirdChanelArray;
    private float[] fullArray;
    private float[] fullFirstChanelArray;
    private float[] fullSecondChanelArray;
    private float[] fullThirdChanelArray;

    @Override
    protected int getResourceID() {
        return R.layout.charts_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ChartDataModel charts = getArguments().getParcelable(ChartDataModel.CHARTS);
            if (charts != null) {
                frontArray = charts.getFrontChartData();
                frontFirstChanelArray = charts.getFrontFirstChanel();
                frontSecondChanelArray = charts.getFrontSecondChanel();
                frontThirdChanelArray = charts.getFrontThirdChanel();
                fullArray = charts.getFullChartData();
                fullFirstChanelArray = charts.getFullFirstChanel();
                fullSecondChanelArray = charts.getFullSecondChanel();
                fullThirdChanelArray = charts.getFullThirdChanel();
            }
        }
    }

    @Override
    protected void initUI(View rootView) {
        front_chart = rootView.findViewById(R.id.frontChart);
        full_chart = rootView.findViewById(R.id.fullChart);

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
        ArrayList<Entry> frontFirstValue = new ArrayList<>();
        ArrayList<Entry> frontSecondValue = new ArrayList<>();
        ArrayList<Entry> frontThirdValue = new ArrayList<>();

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
        //frontDataSets.add(frontLineDataSet);

        for (int i = 0; i < frontFirstChanelArray.length; i++) {
            frontFirstValue.add(new Entry(i, frontFirstChanelArray[i]));
            frontSecondValue.add(new Entry(i, frontSecondChanelArray[i]));
            frontThirdValue.add(new Entry(i, frontThirdChanelArray[i]));
        }
        LineDataSet frontFirstLineDataSet = new LineDataSet(frontFirstValue, "DataSet FrontFirst");
        LineDataSet frontSecondLineDataSet = new LineDataSet(frontSecondValue, "DataSet FrontSecond");
        LineDataSet frontThirdLineDataSet = new LineDataSet(frontThirdValue, "DataSet FrontThird");

        frontFirstLineDataSet.setColor(Color.parseColor("#3F51B5"));
        frontFirstLineDataSet.setLineWidth(2f);
        frontFirstLineDataSet.setDrawValues(false);
        frontFirstLineDataSet.setDrawCircles(false);
        frontFirstLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        frontFirstLineDataSet.setDrawFilled(false);
        frontDataSets.add(frontFirstLineDataSet);

        frontSecondLineDataSet.setColor(Color.parseColor("#cc3333"));
        frontSecondLineDataSet.setLineWidth(2f);
        frontSecondLineDataSet.setDrawValues(false);
        frontSecondLineDataSet.setDrawCircles(false);
        frontSecondLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        frontSecondLineDataSet.setDrawFilled(false);
        frontDataSets.add(frontSecondLineDataSet);

        frontThirdLineDataSet.setColor(Color.parseColor("#931aa8"));
        frontThirdLineDataSet.setLineWidth(2f);
        frontThirdLineDataSet.setDrawValues(false);
        frontThirdLineDataSet.setDrawCircles(false);
        frontThirdLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        frontThirdLineDataSet.setDrawFilled(false);
        frontDataSets.add(frontThirdLineDataSet);

        front_chart.setData(new LineData(frontDataSets));
        front_chart.getLegend().setEnabled(false);
        front_chart.invalidate();

        //Full chart
        ArrayList<ILineDataSet> fullDataSets = new ArrayList<>();
        ArrayList<Entry> fullValue = new ArrayList<>();
        ArrayList<Entry> fullFirstValue = new ArrayList<>();
        ArrayList<Entry> fullSecondValue = new ArrayList<>();
        ArrayList<Entry> fullThirdValue = new ArrayList<>();

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
        //fullDataSets.add(fullLineDataSet);

        for (int i = 0; i < fullFirstChanelArray.length; i++) {
            fullFirstValue.add(new Entry(i, fullFirstChanelArray[i]));
            fullSecondValue.add(new Entry(i, fullSecondChanelArray[i]));
            fullThirdValue.add(new Entry(i, fullThirdChanelArray[i]));
        }
        LineDataSet fullFirstLineDataSet = new LineDataSet(fullFirstValue, "DataSet FrontFirst");
        LineDataSet fullSecondLineDataSet = new LineDataSet(fullSecondValue, "DataSet FrontSecond");
        LineDataSet fullThirdLineDataSet = new LineDataSet(fullThirdValue, "DataSet FrontThird");

        fullFirstLineDataSet.setColor(Color.parseColor("#3F51B5"));
        fullFirstLineDataSet.setLineWidth(2f);
        fullFirstLineDataSet.setDrawValues(false);
        fullFirstLineDataSet.setDrawCircles(false);
        fullFirstLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        fullFirstLineDataSet.setDrawFilled(false);
        fullDataSets.add(fullFirstLineDataSet);

        fullSecondLineDataSet.setColor(Color.parseColor("#cc3333"));
        fullSecondLineDataSet.setLineWidth(2f);
        fullSecondLineDataSet.setDrawValues(false);
        fullSecondLineDataSet.setDrawCircles(false);
        fullSecondLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        fullSecondLineDataSet.setDrawFilled(false);
        fullDataSets.add(fullSecondLineDataSet);

        fullThirdLineDataSet.setColor(Color.parseColor("#931aa8"));
        fullThirdLineDataSet.setLineWidth(2f);
        fullThirdLineDataSet.setDrawValues(false);
        fullThirdLineDataSet.setDrawCircles(false);
        fullThirdLineDataSet.setMode(LineDataSet.Mode.LINEAR);
        fullThirdLineDataSet.setDrawFilled(false);
        fullDataSets.add(fullThirdLineDataSet);

        full_chart.setData(new LineData(fullDataSets));
        full_chart.getLegend().setEnabled(false);
        full_chart.invalidate();
    }
}