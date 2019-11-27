package com.idobro.kilovoltmetr_dosimetr.database;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Chart;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.ResponseCallback;

public interface DatabaseManager {
    void addNewChart(Chart chart);
    void getLastChart(ResponseCallback<Chart> chart);
    void deleteAllChart();
    void getChartRecordsNumber(ResponseCallback<Integer> count);
    void getChartById(ResponseCallback<Chart> chart, long id);
}
