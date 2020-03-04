package com.idobro.kilovoltmetr_dosimetr.database;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsDates;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.ResponseCallback;

import java.util.List;

public interface DatabaseManager {
    void addNewChart(Graph graph);

    void getLastChart(ResponseCallback<Graph> graph);

    void deleteAllChart();

    void getChartRecordsNumber(ResponseCallback<Integer> count);

    void getGraphById(long id, ResponseCallback<Graph> chart);

    void getGraphsDates(ResponseCallback<List<GraphsDates>> callback);
}