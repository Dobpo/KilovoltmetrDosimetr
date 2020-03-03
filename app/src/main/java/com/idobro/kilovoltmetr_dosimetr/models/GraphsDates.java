package com.idobro.kilovoltmetr_dosimetr.models;

import androidx.room.ColumnInfo;

public class GraphsDates {
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "date")
    public long date;

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }
}