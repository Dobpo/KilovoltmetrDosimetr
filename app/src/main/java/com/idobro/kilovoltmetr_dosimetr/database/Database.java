package com.idobro.kilovoltmetr_dosimetr.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.idobro.kilovoltmetr_dosimetr.database.dao.ChartDao;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Chart;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.ResponseCallback;

import java.util.List;

public class Database implements DatabaseManager {
    private ChartDao chartDao;
    private Handler handler = new Handler(Looper.getMainLooper());

    public Database(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "kilovoltmetrDb")
        .fallbackToDestructiveMigration().build();
        chartDao = db.chartDao();
    }

    @Override
    public void addNewChart(Chart chart) {
        new Thread(()->{
            List<Chart> charts = chartDao.getAll();
            if (charts.size() >= 30) {
                Chart firstEmp = chartDao.getFirstInsert();
                chartDao.delete(firstEmp);
            }
            chartDao.insert(chart);
        }).start();
    }

    @Override
    public void getLastChart(ResponseCallback<Chart> callback) {
        Chart chart = chartDao.getLastInsert();
        handler.post(()->callback.onSuccess(chart));
    }

    @Override
    public void deleteAllChart() {
        new Thread(() -> {
            List<Chart> charts = chartDao.getAll();
            chartDao.deleteAll(charts);
        }).start();
    }

    @Override
    public void getChartRecordsNumber(ResponseCallback<Integer> callback) {
        new Thread(() -> {
            int count = chartDao.getAll().size();
            handler.post(()->callback.onSuccess(count));
        }).start();
    }

    @Override
    public void getChartById(ResponseCallback<Chart> callback, long id) {
        new Thread(()->{
            Chart chart = chartDao.getById(id);
            handler.post(()-> callback.onSuccess(chart));
        }).start();
    }
}
