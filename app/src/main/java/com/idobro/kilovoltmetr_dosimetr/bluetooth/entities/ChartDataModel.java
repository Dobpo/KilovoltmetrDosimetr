package com.idobro.kilovoltmetr_dosimetr.bluetooth.entities;

import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.utils.ByteToIntConverter;

import java.util.ArrayList;

public class ChartDataModel {
    private final int frontChartLength;
    private final int fullChartLength;
    private ArrayList<Byte> inputDataArrayList;
    private ArrayList<Byte> frontDataArray;
    private ArrayList<Byte> fullDataArray;

    private float[] frontFirstChanel;
    private float[] frontSecondChanel;
    private float[] frontThirdChanel;
    private float[] frontTotal;

    private float[] fullFirstChanel;
    private float[] fullSecondChanel;
    private float[] fullThirdChanel;
    private float[] fullTotal;

    public ChartDataModel(ArrayList<Byte> arrayList) {
        inputDataArrayList = arrayList;
        this.frontChartLength = ByteToIntConverter.getUnsignedInt(arrayList.get(1), arrayList.get(2));
        this.fullChartLength = ByteToIntConverter.getUnsignedInt(arrayList.get(3), arrayList.get(4));
        Log.d("LOG", "ChartDataModel -> ChartDataModel : front = " + frontChartLength);
        Log.d("LOG", "ChartDataModel -> ChartDataModel : full = " + fullChartLength);
        Log.d("LOG", "ChartDataModel -> ChartDataModel : totalSize = " + inputDataArrayList.size());
    }

    public void setFrontDataArray(ArrayList<Byte> frontDataArray) {
        this.frontDataArray = frontDataArray;
        for (byte b : frontDataArray) {
            // TODO: 29.08.2019
        }
    }

    public void setFullDataArray(ArrayList<Byte> fullDataArray) {
        this.fullDataArray = fullDataArray;
    }
}
