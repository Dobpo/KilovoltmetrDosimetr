package com.idobro.kilovoltmetr_dosimetr.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Chart;

import java.util.List;

@Dao
public interface ChartDao {
    @Query("SELECT * FROM charts")
    List<Chart> getAll();

    @Query("SELECT * FROM charts ORDER BY id DESC LIMIT 1")
    Chart getLastInsert();

    @Query("SELECT * FROM charts ORDER BY id ASC LIMIT 1")
    Chart getFirstInsert();

    @Query("SELECT * FROM charts WHERE id = :id")
    Chart getById(long id);

    @Insert
    long insert(Chart chart);

    @Update
    void update(Chart chart);

    @Delete
    void delete(Chart chart);

    @Delete
    void deleteAll(List<Chart> charts);
}