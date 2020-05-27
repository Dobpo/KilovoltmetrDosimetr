package com.idobro.kilovoltmetr_dosimetr.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Graph")
public class Graph implements Parcelable {
    public static final String GRAPH = "graph";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private byte[] frontGraphData;
    private byte[] fullGraphData;

    private int virtualZero;

    @ColumnInfo(index = true)
    private long date;

    @Ignore
    public Graph(int virtualZero) {
        this.virtualZero = virtualZero;
    }

    public Graph(byte[] frontGraphData, byte[] fullGraphData, int virtualZero, long date) {
        this.frontGraphData = frontGraphData;
        this.fullGraphData = fullGraphData;
        this.virtualZero = virtualZero;
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFrontGraphData(byte[] frontGraphData) {
        this.frontGraphData = frontGraphData;
    }

    public void setFullGraphData(byte[] fullGraphData) {
        this.fullGraphData = fullGraphData;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public byte[] getFrontGraphData() {
        return frontGraphData;
    }

    public byte[] getFullGraphData() {
        return fullGraphData;
    }

    public int getVirtualZero() {
        return virtualZero;
    }

    public long getDate() {
        return date;
    }

    public float[] getFrontGraph() {
        int length = frontGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = ((frontGraphData[i * 3] & 0xFF) - virtualZero);
            //+ ((frontGraphData[i * 3 + 1] & 0xFF) - virtualZero)
            //+ ((frontGraphData[i * 3 + 2] & 0xFF) - virtualZero)) / 3;
        }
        return data;
    }

    public float[] getFrontFirstChanelGraph() {
        int length = frontGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = (frontGraphData[i * 3] & 0xFF)
                    // + (frontGraphData[(i + 1) * 3] & 0xFF)
                    // + (frontGraphData[(i + 2) * 3] & 0xFF)
                    // + (frontGraphData[(i + 3) * 3] & 0xFF)) / 4)
                    - virtualZero;
        }
        return data;
    }

    public float[] getFrontSecondChanelGraph() {
        int length = frontGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length /*- 4*/; i++) {
            data[i] = (frontGraphData[i * 3 + 1] & 0xFF)
                    //+ (frontGraphData[(i + 1) * 3 + 1] & 0xFF)
                    //+ (frontGraphData[(i + 2) * 3 + 1] & 0xFF)
                    //+ (frontGraphData[(i + 3) * 3 + 1] & 0xFF)) / 4)
                    - virtualZero;
        }
        return data;
    }

    public float[] getFrontThirdChanelGraph() {
        int length = frontGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = (frontGraphData[i * 3 + 2] & 0xFF)
                    //+ (frontGraphData[(i + 1) * 3 + 2] & 0xFF)
                    //+ (frontGraphData[(i + 2) * 3 + 2] & 0xFF)
                    //+ (frontGraphData[(i + 3) * 3 + 2] & 0xFF)) / 4)
                    - virtualZero;
        }
        return data;
    }

    public float[] getFullGraph() {
        int length = fullGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = ((fullGraphData[i * 3] & 0xFF) - virtualZero);
            // + ((fullGraphData[i * 3 + 1] & 0xFF) - virtualZero)
            // + ((fullGraphData[i * 3 + 2] & 0xFF) - virtualZero)) / 3;
        }
        return data;
    }

    public float[] getFullFirstChanelGraph() {
        int length = fullGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = (fullGraphData[i * 3] & 0xFF) - virtualZero;
        }
        return data;
    }

    public float[] getFullSecondChanelGraph() {
        int length = fullGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = (fullGraphData[i * 3 + 1] & 0xFF) - virtualZero;
        }
        return data;
    }

    public float[] getFullThirdChanelGraph() {
        int length = fullGraphData.length / 3;
        float[] data = new float[length];

        for (int i = 0; i < length; i++) {
            data[i] = (fullGraphData[i * 3 + 2] & 0xFF) - virtualZero;
        }
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByteArray(this.frontGraphData);
        dest.writeByteArray(this.fullGraphData);
        dest.writeInt(this.virtualZero);
        dest.writeLong(this.date);
    }

    protected Graph(Parcel in) {
        this.id = in.readLong();
        this.frontGraphData = in.createByteArray();
        this.fullGraphData = in.createByteArray();
        this.virtualZero = in.readInt();
        this.date = in.readLong();
    }

    public static final Creator<Graph> CREATOR = new Creator<Graph>() {
        @Override
        public Graph createFromParcel(Parcel source) {
            return new Graph(source);
        }

        @Override
        public Graph[] newArray(int size) {
            return new Graph[size];
        }
    };
}