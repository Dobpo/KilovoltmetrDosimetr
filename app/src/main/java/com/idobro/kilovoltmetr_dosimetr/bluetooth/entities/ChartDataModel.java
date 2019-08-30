package com.idobro.kilovoltmetr_dosimetr.bluetooth.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.utils.ByteToIntConverter;

import java.util.ArrayList;

public class ChartDataModel implements Parcelable {
    public static final String CHARTS = "charts";

    private int frontChartLength;
    private int fullChartLength;

    private ArrayList<Byte> frontDataArray;
    private ArrayList<Byte> fullDataArray;

    //private float[] frontFirstChanel;
    //private float[] frontSecondChanel;
    //private float[] frontThirdChanel;
    private float[] frontChartData = new float[5000];

    //private float[] fullFirstChanel;
    //private float[] fullSecondChanel;
    //private float[] fullThirdChanel;
    private float[] fullChartData = new float[20000];

    public ChartDataModel() {
        frontChartData = new float[5000];
        fullChartData = new float[20000];

        for (int i = 0; i < frontChartData.length; i++) {
            frontChartData[i] = i;
        }

        for (int i = 0; i < fullChartData.length; i++) {
            fullChartData[i] = 100;
        }
    }

    public ChartDataModel(ArrayList<Byte> arrayList) {
        this.frontChartLength = ByteToIntConverter.getUnsignedInt(arrayList.get(1), arrayList.get(2));
        this.fullChartLength = ByteToIntConverter.getUnsignedInt(arrayList.get(3), arrayList.get(4));
        Log.d("LOG", "ChartDataModel -> ChartDataModel : front = " + frontChartLength);
        Log.d("LOG", "ChartDataModel -> ChartDataModel : full = " + fullChartLength);
    }

    public void setFrontDataArray(ArrayList<Byte> frontDataArray) {
        for (int i = 0; i < (frontDataArray.size() / 3) - 10; i++) {
            frontChartData[i] = (float) (frontDataArray.get(i) * 0xFF
                    + frontDataArray.get(i + 1) * 0xFF
                    + frontDataArray.get(i + 2) * 0xFF);
        }
    }

    public void setFullDataArray(ArrayList<Byte> fullDataArray) {
        for (int i = 0; i < (fullDataArray.size() / 3) - 10; i++) {
            fullChartData[i] = (float) (fullDataArray.get(i) * 0xFF
                    + fullDataArray.get(i + 1) * 0xFF
                    + fullDataArray.get(i + 2) * 0xFF);
        }
    }

    public float[] getFrontChartData() {
        return frontChartData;
    }

    public float[] getFullChartData() {
        return fullChartData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.frontChartLength);
        dest.writeInt(this.fullChartLength);
        dest.writeList(this.frontDataArray);
        dest.writeList(this.fullDataArray);
        dest.writeFloatArray(this.frontChartData);
        dest.writeFloatArray(this.fullChartData);
    }

    protected ChartDataModel(Parcel in) {
        this.frontChartLength = in.readInt();
        this.fullChartLength = in.readInt();
        this.frontDataArray = new ArrayList<Byte>();
        in.readList(this.frontDataArray, Byte.class.getClassLoader());
        this.fullDataArray = new ArrayList<Byte>();
        in.readList(this.fullDataArray, Byte.class.getClassLoader());
        this.frontChartData = in.createFloatArray();
        this.fullChartData = in.createFloatArray();
    }

    public static final Parcelable.Creator<ChartDataModel> CREATOR = new Parcelable.Creator<ChartDataModel>() {
        @Override
        public ChartDataModel createFromParcel(Parcel source) {
            return new ChartDataModel(source);
        }

        @Override
        public ChartDataModel[] newArray(int size) {
            return new ChartDataModel[size];
        }
    };
}
