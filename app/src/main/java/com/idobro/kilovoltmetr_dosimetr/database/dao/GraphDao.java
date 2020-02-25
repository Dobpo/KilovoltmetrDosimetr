package com.idobro.kilovoltmetr_dosimetr.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;

import java.util.List;

@Dao
public interface GraphDao {
    @Query("SELECT * FROM Graph")
    List<Graph> getAll();

    @Query("SELECT * FROM Graph ORDER BY id DESC LIMIT 1")
    Graph getLastInsert();

    @Query("SELECT * FROM Graph ORDER BY id ASC LIMIT 1")
    Graph getFirstInsert();

    @Query("SELECT * FROM Graph WHERE id = :id")
    Graph getById(long id);

    @Insert
    long insert(Graph graph);

    @Update
    void update(Graph graph);

    @Delete
    void delete(Graph graph);

    @Delete
    void deleteAll(List<Graph> graphs);
}