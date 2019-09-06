package com.idobro.kilovoltmetr_dosimetr.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "charts")
public class Chart {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private ArrayList<Byte> frontChartData;
    private ArrayList<Byte> fullChartData;

    @ColumnInfo(index = true)
    private long date;

    public Chart(ArrayList<Byte> frontChartData, ArrayList<Byte> fullChartData, long date) {
        this.frontChartData = frontChartData;
        this.fullChartData = fullChartData;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public ArrayList<Byte> getFrontChartData() {
        return frontChartData;
    }

    public ArrayList<Byte> getFullChartData() {
        return fullChartData;
    }

    public long getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFrontChartData(ArrayList<Byte> frontChartData) {
        this.frontChartData = frontChartData;
    }

    public void setFullChartData(ArrayList<Byte> fullChartData) {
        this.fullChartData = fullChartData;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
