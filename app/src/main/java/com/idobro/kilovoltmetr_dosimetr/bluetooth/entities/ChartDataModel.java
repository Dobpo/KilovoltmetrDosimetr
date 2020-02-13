package com.idobro.kilovoltmetr_dosimetr.bluetooth.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Chart;
import com.idobro.kilovoltmetr_dosimetr.utils.ByteToIntConverter;

import java.util.ArrayList;

public class ChartDataModel implements Parcelable {
    public static final String CHARTS = "charts";

    private int frontChartLength;
    private int fullChartLength;
    private int virtualZero;

    private ArrayList<Byte> frontDataArray;
    private float[] frontChartData;
    private float[] frontFirstChanel;
    private float[] frontSecondChanel;
    private float[] frontThirdChanel;
    private ArrayList<Byte> fullDataArray;
    private float[] fullChartData;
    private float[] fullFirstChanel;
    private float[] fullSecondChanel;
    private float[] fullThirdChanel;

    public ChartDataModel() {
    }

    public ChartDataModel(Chart chart) {
        frontDataArray = new ArrayList<>();
        fullDataArray = new ArrayList<>();
        for (byte b : chart.getFrontChartData()) frontDataArray.add(b);
        for (byte b : chart.getFullChartData()) fullDataArray.add(b);

        frontChartData = new float[frontDataArray.size() / 3];
        frontFirstChanel = new float[frontDataArray.size() / 3];
        frontSecondChanel = new float[frontDataArray.size() / 3];
        frontThirdChanel = new float[frontDataArray.size() / 3];


        for (int i = 0; i < (frontDataArray.size() / 3); i++) {
            frontFirstChanel[i] = (float) ((frontDataArray.get(i * 3) & 0xFF));
            frontSecondChanel[i] = (float) ((frontDataArray.get(i * 3 + 1) & 0xFF));
            frontThirdChanel[i] = (float) ((frontDataArray.get(i * 3 + 2) & 0xFF));
        }

        fullChartData = new float[(fullDataArray.size() / 3)];
        fullFirstChanel = new float[fullDataArray.size() / 3];
        fullSecondChanel = new float[fullDataArray.size() / 3];
        fullThirdChanel = new float[fullDataArray.size() / 3];

        for (int i = 0; i < (fullDataArray.size() / 3); i++) {
            fullChartData[i] = (float) ((fullDataArray.get(i) & 0xFF) +
                    (fullDataArray.get(i * 3 + 1) & 0xFF) +
                    (fullDataArray.get(i * 3 + 2) & 0xFF)) / 3;
        }

        for (int i = 0; i < (fullDataArray.size() / 3); i++) {
            fullFirstChanel[i] = (float) ((fullDataArray.get(i * 3) & 0xFF));
            fullSecondChanel[i] = (float) ((fullDataArray.get(i * 3 + 1) & 0xFF));
            fullThirdChanel[i] = (float) ((fullDataArray.get(i * 3 + 2) & 0xFF));
        }
    }

    public ChartDataModel(ArrayList<Byte> measureCompleteArrayList) {
        this.frontChartLength = ByteToIntConverter.getUnsignedInt(measureCompleteArrayList.get(1), measureCompleteArrayList.get(2));
        this.fullChartLength = ByteToIntConverter.getUnsignedInt(measureCompleteArrayList.get(3), measureCompleteArrayList.get(4));
        this.virtualZero = ByteToIntConverter.getUnsignedInt(measureCompleteArrayList.get(5));
    }

    public byte[] getFrontByteArray() {
        byte[] array = new byte[frontDataArray.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = frontDataArray.get(i);
        }
        return array;
    }

    public byte[] getFullByteArray() {
        byte[] array = new byte[fullDataArray.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = fullDataArray.get(i);
        }
        return array;
    }

    public void setFrontDataArray(ArrayList<Byte> frontDataArray) {
        this.frontDataArray = frontDataArray;

        int length = frontDataArray.size() / 3;

        frontChartData = new float[length];
        frontFirstChanel = new float[length];
        frontSecondChanel = new float[length];
        frontThirdChanel = new float[length];

        for (int i = 0; i < length; i++) {
            frontFirstChanel[i] = (float) ((frontDataArray.get(i * 3) & 0xFF) - virtualZero);
            frontSecondChanel[i] = (float) ((frontDataArray.get(i * 3 + 1) & 0xFF) - virtualZero);
            frontThirdChanel[i] = (float) ((frontDataArray.get(i * 3 + 2) & 0xFF) - virtualZero);

            frontChartData[i] = (frontFirstChanel[i] + frontSecondChanel[i] + frontThirdChanel[i]) / 3;
        }
    }

    public void setFullDataArray(ArrayList<Byte> fullDataArray) {
        this.fullDataArray = fullDataArray;

        int length = fullDataArray.size() / 3;

        fullChartData = new float[length];
        fullFirstChanel = new float[length];
        fullSecondChanel = new float[length];
        fullThirdChanel = new float[length];

        for (int i = 0; i < length; i++) {
            fullFirstChanel[i] = (float) ((fullDataArray.get(i * 3) & 0xFF) - virtualZero);
            fullSecondChanel[i] = (float) ((fullDataArray.get(i * 3 + 1) & 0xFF) - virtualZero);
            fullThirdChanel[i] = (float) ((fullDataArray.get(i * 3 + 2) & 0xFF) - virtualZero);

            fullChartData[i] = (fullFirstChanel[i] + fullSecondChanel[i] + fullThirdChanel[i]) / 3;
        }
    }

    public ArrayList<Byte> getFrontDataArray() {
        return frontDataArray;
    }

    public ArrayList<Byte> getFullDataArray() {
        return fullDataArray;
    }

    public float[] getFrontChartData() {
        return frontChartData;
    }

    public float[] getFrontFirstChanel() {
        return frontFirstChanel;
    }

    public float[] getFrontSecondChanel() {
        return frontSecondChanel;
    }

    public float[] getFrontThirdChanel() {
        return frontThirdChanel;
    }

    public float[] getFullChartData() {
        return fullChartData;
    }

    public float[] getFullFirstChanel() {
        return fullFirstChanel;
    }

    public float[] getFullSecondChanel() {
        return fullSecondChanel;
    }

    public float[] getFullThirdChanel() {
        return fullThirdChanel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.frontChartLength);
        dest.writeInt(this.fullChartLength);
        dest.writeInt(this.virtualZero);
        dest.writeList(this.frontDataArray);
        dest.writeFloatArray(this.frontChartData);
        dest.writeFloatArray(this.frontFirstChanel);
        dest.writeFloatArray(this.frontSecondChanel);
        dest.writeFloatArray(this.frontThirdChanel);
        dest.writeList(this.fullDataArray);
        dest.writeFloatArray(this.fullChartData);
        dest.writeFloatArray(this.fullFirstChanel);
        dest.writeFloatArray(this.fullSecondChanel);
        dest.writeFloatArray(this.fullThirdChanel);
    }

    private ChartDataModel(Parcel in) {
        this.frontChartLength = in.readInt();
        this.fullChartLength = in.readInt();
        this.virtualZero = in.readInt();
        this.frontDataArray = new ArrayList<>();
        in.readList(this.frontDataArray, Byte.class.getClassLoader());
        this.frontChartData = in.createFloatArray();
        this.frontFirstChanel = in.createFloatArray();
        this.frontSecondChanel = in.createFloatArray();
        this.frontThirdChanel = in.createFloatArray();
        this.fullDataArray = new ArrayList<>();
        in.readList(this.fullDataArray, Byte.class.getClassLoader());
        this.fullChartData = in.createFloatArray();
        this.fullFirstChanel = in.createFloatArray();
        this.fullSecondChanel = in.createFloatArray();
        this.fullThirdChanel = in.createFloatArray();
    }

    public static final Creator<ChartDataModel> CREATOR = new Creator<ChartDataModel>() {
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