package com.idobro.kilovoltmetr_dosimetr.database;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.ResponseCallback;

public interface DatabaseManager {
    void addNewChart(Graph graph);

    void getLastChart(ResponseCallback<Graph> graph);

    void deleteAllChart();

    void getChartRecordsNumber(ResponseCallback<Integer> count);

    void getChartById(ResponseCallback<Graph> chart, long id);
}