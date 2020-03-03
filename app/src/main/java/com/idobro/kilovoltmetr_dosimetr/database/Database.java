package com.idobro.kilovoltmetr_dosimetr.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.idobro.kilovoltmetr_dosimetr.database.dao.GraphDao;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsDates;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.ResponseCallback;

import java.util.List;

public class Database implements DatabaseManager {
    private GraphDao graphDao;
    private Handler handler = new Handler(Looper.getMainLooper());

    public Database(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "kilovoltmetrDb")
                .fallbackToDestructiveMigration().build();
        graphDao = db.graphDao();
    }

    @Override
    public void addNewChart(Graph graph) {
        new Thread(() -> {
            List<Graph> graphs = graphDao.getAll();
            if (graphs.size() >= 30) {
                Graph firstEmp = graphDao.getFirstInsert();
                graphDao.delete(firstEmp);
            }
            graphDao.insert(graph);
        }).start();
    }

    @Override
    public void getLastChart(ResponseCallback<Graph> callback) {
        Graph graph = graphDao.getLastInsert();
        handler.post(() -> callback.onSuccess(graph));
    }

    @Override
    public void deleteAllChart() {
        new Thread(() -> {
            List<Graph> graphs = graphDao.getAll();
            graphDao.deleteAll(graphs);
        }).start();
    }

    @Override
    public void getChartRecordsNumber(ResponseCallback<Integer> callback) {
        new Thread(() -> {
            int count = graphDao.getAll().size();
            handler.post(() -> callback.onSuccess(count));
        }).start();
    }

    @Override
    public void getChartById(ResponseCallback<Graph> callback, long id) {
        new Thread(() -> {
            Graph graph = graphDao.getById(id);
            handler.post(() -> callback.onSuccess(graph));
        }).start();
    }

    @Override
    public void getGraphsDates(ResponseCallback<List<GraphsDates>> callback) {
        new Thread(() -> {
            List<GraphsDates> graphsDates = graphDao.getGraphsDate();
            handler.post(() -> callback.onSuccess(graphsDates));
        }).start();
    }
}