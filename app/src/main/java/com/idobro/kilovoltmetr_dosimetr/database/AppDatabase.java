package com.idobro.kilovoltmetr_dosimetr.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.idobro.kilovoltmetr_dosimetr.database.dao.GraphDao;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;

@Database(entities = {Graph.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GraphDao graphDao();
}