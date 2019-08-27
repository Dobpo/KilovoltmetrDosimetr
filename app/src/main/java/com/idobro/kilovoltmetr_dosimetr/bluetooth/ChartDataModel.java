package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.utils.ByteToIntConverter;

import java.util.ArrayList;

public class ChartDataModel {
    final int frontChartLength;
    final int fullChartLength;
    private ArrayList<Byte> inputDataArrayList;


    public ChartDataModel(ArrayList<Byte> arrayList) {
        inputDataArrayList = arrayList;
        this.frontChartLength = ByteToIntConverter.getUnsignedInt(arrayList.get(0), arrayList.get(1));
        this.fullChartLength = ByteToIntConverter.getUnsignedInt(arrayList.get(2), arrayList.get(3));
        Log.d("LOG", "ChartDataModel -> ChartDataModel : front = " + frontChartLength);
        Log.d("LOG", "ChartDataModel -> ChartDataModel : full = " + fullChartLength);
        Log.d("LOG", "ChartDataModel -> ChartDataModel : totalSize = " + inputDataArrayList.size());

    }
}
