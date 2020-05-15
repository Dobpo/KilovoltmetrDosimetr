package com.idobro.kilovoltmetr_dosimetr.utils;

import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;

import java.util.Locale;

public final class PrintUtil {
    public static void printAverageRelation(Graph graph) {
        float[] first = graph.getFrontFirstChanelGraph();
        float[] second = graph.getFrontSecondChanelGraph();
        float[] third = graph.getFrontThirdChanelGraph();

        float firstAverage = getArrayAverage(first, 100);
        float secondAverage = getArrayAverage(second, 100);
        float thirdAverage = getArrayAverage(third, 100);

        String sb = "Id = " + graph.getId() +
                "\t\t" +
                "3 to 1 = " +
                String.format(Locale.getDefault(), "%.2f", (thirdAverage / firstAverage)) +
                "\t\t" +
                "3 to 2 = " +
                String.format(Locale.getDefault(), "%.2f", thirdAverage / secondAverage) +
                "\t\t" +
                "2 to 1 = " +
                String.format(Locale.getDefault(), "%.2f", secondAverage / firstAverage);
        Log.d("LOG", sb);

    }

    private static float getArrayAverage(float[] source, int from) {
        float average = 0;
        for (int i = from; i < source.length; i++) {
            average += source[i];
        }
        return average / (source.length - (from - 1));
    }


    /*
    *  for (int i = 7; i < 22; i++) {

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ID = ")
                            .append(response.get(i).getId())
                            .append("\t")
                            .append(" Point = 500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[500] / response.get(i).getFrontFirstChanelGraph()[500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[500] / response.get(i).getFrontSecondChanelGraph()[500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[500] / response.get(i).getFrontFirstChanelGraph()[500]))

                            .append("\tPoint = 1500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[1500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[1500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[1500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[1500] / response.get(i).getFrontFirstChanelGraph()[1500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[1500] / response.get(i).getFrontSecondChanelGraph()[1500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[1500] / response.get(i).getFrontFirstChanelGraph()[1500]))

                            .append("\tPoint = 2500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[2500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[2500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[2500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[2500] / response.get(i).getFrontFirstChanelGraph()[2500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[2500] / response.get(i).getFrontSecondChanelGraph()[2500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[2500] / response.get(i).getFrontFirstChanelGraph()[2500]))

                            .append("\tPoint = 3500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[3500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[3500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[3500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[3500] / response.get(i).getFrontFirstChanelGraph()[3500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[3500] / response.get(i).getFrontSecondChanelGraph()[3500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[3500] / response.get(i).getFrontFirstChanelGraph()[3500]));

                    Log.d("LOG", stringBuilder.toString());
                }*/
}
