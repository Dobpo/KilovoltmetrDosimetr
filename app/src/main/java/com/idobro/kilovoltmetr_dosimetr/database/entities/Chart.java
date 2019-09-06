package com.idobro.kilovoltmetr_dosimetr.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "charts")
public class Chart {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private byte[] frontChartData;
    private byte[] fullChartData;

    @ColumnInfo(index = true)
    private long date;

    public Chart(byte[] frontChartData, byte[] fullChartData, long date) {
        this.frontChartData = frontChartData;
        this.fullChartData = fullChartData;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public byte[] getFrontChartData() {
        return frontChartData;
    }

    public byte[] getFullChartData() {
        return fullChartData;
    }

    public long getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFrontChartData(byte[] frontChartData) {
        this.frontChartData = frontChartData;
    }

    public void setFullChartData(byte[] fullChartData) {
        this.fullChartData = fullChartData;
    }

    public void setDate(long date) {
        this.date = date;
    }


}
