package com.idobro.kilovoltmetr_dosimetr.fragments.charts_screen;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.activities.MainActivity;
import com.idobro.kilovoltmetr_dosimetr.base.BaseFragment;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityModel;
import com.idobro.kilovoltmetr_dosimetr.utils.GraphManager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartsFragment extends BaseFragment {
    @BindView(R.id.frontChart)
    LineChart frontChart;

    @BindView(R.id.fullChart)
    LineChart fullChart;

    @BindView(R.id.rvFrontInfo)
    RecyclerView rvFrontInfo;

    private ChartInfoAdapter infoAdapter;

    private float[] frontFirstChanelArray;
    private float[] frontSecondChanelArray;
    private float[] frontThirdChanelArray;

    private float[] fullFirstChanelArray;
    private float[] fullSecondChanelArray;
    private float[] fullThirdChanelArray;

    public static ChartsFragment start(Graph graph) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Graph.GRAPH, graph);
        ChartsFragment chartsFragment = new ChartsFragment();
        chartsFragment.setArguments(bundle);
        return chartsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((MainActivity) Objects.requireNonNull(getActivity())).getGraphLiveData().observe(this, this::showGraph);
    }

    private void showGraph(Graph graph) {
        if (graph != null) {
            frontFirstChanelArray = graph.getFrontFirstChanelGraph();
            frontSecondChanelArray = graph.getFrontSecondChanelGraph();
            frontThirdChanelArray = graph.getFrontThirdChanelGraph();

            fullFirstChanelArray = graph.getFullFirstChanelGraph();
            fullSecondChanelArray = graph.getFullSecondChanelGraph();
            fullThirdChanelArray = graph.getFullThirdChanelGraph();
        }

        setDataToCharts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (infoAdapter == null) {
            infoAdapter = new ChartInfoAdapter();
        }

        rvFrontInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFrontInfo.setHasFixedSize(true);
        rvFrontInfo.setAdapter(infoAdapter);

        initChart(frontChart);
        initChart(fullChart);

        if (getArguments() != null) {
            Graph graph = getArguments().getParcelable(Graph.GRAPH);
            showGraph(graph);
        }
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
        GraphsVisibilityModel filter = ((MainActivity) getActivity()).getViewModel().getGraphsVisibility();
        //Front chart
        ArrayList<ILineDataSet> frontDataSets = new ArrayList<>();
        ArrayList<InfoItem> infoItems = new ArrayList<>();

        /* График 3-го канала (зеленый),код - #4caf50*/
        if (false) {
            ArrayList<Entry> frontThirdValue = new ArrayList<>();

            for (int i = 0; i < frontFirstChanelArray.length; i++) {
                frontThirdValue.add(new Entry(i, frontThirdChanelArray[i]));
            }

            LineDataSet frontThirdLineDataSet = new LineDataSet(frontThirdValue, "DataSet FrontThird");

            customizeLineForChart(frontDataSets, frontThirdLineDataSet, "#4caf50");
            infoItems.add(new InfoItem("#4caf50", String.format(getString(R.string.average_value)
                    + "%.3f", GraphManager.getAverage(frontThirdChanelArray))));
            infoItems.add(new InfoItem("#4caf50", String.format(getString(R.string.standard_deviation_value)
                    + "%.3f", GraphManager.getStandardDeviation(frontThirdChanelArray))));
        }

        /* График 2-го канала (красный),код - #cc3333*/
        if (false) {
            ArrayList<Entry> frontSecondValue = new ArrayList<>();

            for (int i = 0; i < frontFirstChanelArray.length; i++) {
                frontSecondValue.add(new Entry(i, frontSecondChanelArray[i]));
            }

            LineDataSet frontSecondLineDataSet = new LineDataSet(frontSecondValue, "DataSet FrontSecond");

            customizeLineForChart(frontDataSets, frontSecondLineDataSet, "#cc3333");
            infoItems.add(new InfoItem("#cc3333", String.format(getString(R.string.average_value)
                    + "%.3f", GraphManager.getAverage(frontSecondChanelArray))));
            infoItems.add(new InfoItem("#cc3333", String.format(getString(R.string.standard_deviation_value)
                    + "%.3f", GraphManager.getStandardDeviation(frontSecondChanelArray))));
        }

        /* График 1-го канала (синий),код - #3F51B5*/
        if (false) {
            ArrayList<Entry> frontFirstValue = new ArrayList<>();

            for (int i = 0; i < frontFirstChanelArray.length; i++) {
                frontFirstValue.add(new Entry(i, frontFirstChanelArray[i]));
            }

            LineDataSet frontFirstLineDataSet = new LineDataSet(frontFirstValue, "DataSet FrontFirst");

            customizeLineForChart(frontDataSets, frontFirstLineDataSet, "#3F51B5");
            infoItems.add(new InfoItem("#3F51B5", String.format(getString(R.string.average_value)
                    + "%.3f", GraphManager.getAverage(frontFirstChanelArray))));
            infoItems.add(new InfoItem("#3F51B5", String.format(getString(R.string.standard_deviation_value)
                    + "%.3f", GraphManager.getStandardDeviation(frontFirstChanelArray))));
        }

        /* График отношения 3-го канала к 1-ому (зеленого к синему),
         * резкльтирующий график бирюзовый, код - #468083*/
        if (false) {
            ArrayList<Entry> thirdToFirstValue = new ArrayList<>();

            float[] thirdToFirstArray = GraphManager.getRelationGraph(frontThirdChanelArray, frontFirstChanelArray);

            for (int i = 0; i < thirdToFirstArray.length; i++) {
                thirdToFirstValue.add(new Entry(i, thirdToFirstArray[i]));
            }

            LineDataSet frontThirdLineDataSet = new LineDataSet(thirdToFirstValue, "DataSet");

            customizeLineForChart(frontDataSets, frontThirdLineDataSet, "#468083");
            infoItems.add(new InfoItem("#468083", String.format(getString(R.string.average_value)
                    + "%.3f", GraphManager.getAverage(thirdToFirstArray))));
            infoItems.add(new InfoItem("#468083", String.format(getString(R.string.standard_deviation_value)
                    + "%.3f", GraphManager.getStandardDeviation(thirdToFirstArray))));
        }

        /* График отношения 2-го канала к 1-ому (красного к синему),
         * резкльтирующий график коричневый, код - #8C7142*/
        if (false) {
            ArrayList<Entry> secondToFirstValue = new ArrayList<>();

            float[] secondToFirstArray = GraphManager.getRelationGraph(frontSecondChanelArray, frontFirstChanelArray);

            for (int i = 0; i < secondToFirstArray.length; i++) {
                secondToFirstValue.add(new Entry(i, secondToFirstArray[i]));
            }

            LineDataSet frontThirdLineDataSet = new LineDataSet(secondToFirstValue, "DataSet");

            customizeLineForChart(frontDataSets, frontThirdLineDataSet, "#8C7142");
            infoItems.add(new InfoItem("#8C7142", String.format(getString(R.string.average_value)
                    + "%.3f", GraphManager.getAverage(secondToFirstArray))));
            infoItems.add(new InfoItem("#8C7142", String.format(getString(R.string.standard_deviation_value)
                    + "%.3f", GraphManager.getStandardDeviation(secondToFirstArray))));
        }

        /* График отношения 3-го канала к 2-ому (зеленого к красному),
         * резкльтирующий график сереневый, код - #864274*/
        if (false) {
            ArrayList<Entry> thirdToSecondValue = new ArrayList<>();

            float[] thirdToSecondArray = GraphManager.getRelationGraph(frontSecondChanelArray, frontFirstChanelArray);

            for (int i = 0; i < thirdToSecondArray.length; i++) {
                thirdToSecondValue.add(new Entry(i, thirdToSecondArray[i]));
            }

            LineDataSet frontThirdLineDataSet = new LineDataSet(thirdToSecondValue, "DataSet");

            customizeLineForChart(frontDataSets, frontThirdLineDataSet, "#864274");
            infoItems.add(new InfoItem("#864274", String.format(getString(R.string.average_value)
                    + "%.3f", GraphManager.getAverage(thirdToSecondArray))));
            infoItems.add(new InfoItem("#864274", String.format(getString(R.string.standard_deviation_value)
                    + "%.3f", GraphManager.getStandardDeviation(thirdToSecondArray))));
        }

        if (!infoItems.isEmpty())
            infoAdapter.setItems(infoItems);

        frontChart.setData(new LineData(frontDataSets));
        frontChart.getLegend().setEnabled(false);
        frontChart.invalidate();

        //Full chart
        ArrayList<ILineDataSet> fullDataSets = new ArrayList<>();
        ArrayList<Entry> fullFirstValue = new ArrayList<>();
        ArrayList<Entry> fullSecondValue = new ArrayList<>();
        ArrayList<Entry> fullThirdValue = new ArrayList<>();

        for (int i = 0; i < fullFirstChanelArray.length; i++) {
            fullFirstValue.add(new Entry(i, fullFirstChanelArray[i]));
            fullSecondValue.add(new Entry(i, fullSecondChanelArray[i]));
            fullThirdValue.add(new Entry(i, fullThirdChanelArray[i]));
        }
        LineDataSet fullFirstLineDataSet = new LineDataSet(fullFirstValue, "DataSet FullFirst");
        LineDataSet fullSecondLineDataSet = new LineDataSet(fullSecondValue, "DataSet FullSecond");
        LineDataSet fullThirdLineDataSet = new LineDataSet(fullThirdValue, "DataSet FullThird");

        //Blue
        customizeLineForChart(fullDataSets, fullFirstLineDataSet, "#3F51B5");
        //Red
        customizeLineForChart(fullDataSets, fullSecondLineDataSet, "#cc3333");
        //Green
        customizeLineForChart(fullDataSets, fullThirdLineDataSet, "#4caf50");

        fullChart.setData(new LineData(fullDataSets));
        fullChart.getLegend().setEnabled(false);
        fullChart.invalidate();
    }

    private void customizeLineForChart(ArrayList<ILineDataSet> dataSets, LineDataSet lineDataSet, String color) {
        lineDataSet.setColor(Color.parseColor(color));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawFilled(false);
        dataSets.add(lineDataSet);
    }
}