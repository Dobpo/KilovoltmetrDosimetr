package com.idobro.kilovoltmetr_dosimetr.database.core;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.idobro.kilovoltmetr_dosimetr.database.dao.ChartDao;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Chart;

@Database(entities = {Chart.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChartDao chartDao();
}
