package com.idobro.kilovoltmetr_dosimetr.utils;

import android.util.Log;

// TODO: 21.05.2020 develop methods for graph's management
public final class GraphManager {

    /**
     * Отношение одного графика ко второму
     */
    public static float[] getRelationGraph(float[] firstArray, float[] secondArray) {
        if (firstArray.length != secondArray.length)
            throw new IllegalArgumentException("Different arrays size");

        float[] resultArray = new float[firstArray.length];

        for (int i = 0; i < firstArray.length; i++) {
            resultArray[i] = firstArray[i] / secondArray[i];
        }

        return resultArray;
    }

    /**
     * Среднее значение графика
     */
    private static float getAverage(float[] array) {
        float result = 0;
        for (float point : array) {
            result += point;
        }
        return result / array.length;
    }

    /**
     * Среднеквадратическое отклонение
     */
    public static double getStandardDeviation(float[] array) {
        double result = 0;
        double average = getAverage(array);

        for (float point : array) {
            result += (average - point) * (average - point);
        }

        result = result / array.length;
        result = Math.sqrt(result);
        return result;
    }

    /**
     * Выравнивание графиков
     */
    public static float[] smoothGraph(float[] array, int iterationCount) {
        if (iterationCount < 1)
            throw new IllegalArgumentException("Iteration count can't be negative or zero");

        float[] resultArray = new float[array.length];

        for (int i = 0; i < iterationCount; i++) {
            for (int j = 0; j < array.length - 3; j++) {
                resultArray[j] = (array[j] +
                        array[j + 1] +
                        array[j + 2] +
                        array[j + 3]) / 4;
            }

            resultArray[array.length - 1] = array[array.length - 1];
            resultArray[array.length - 2] = array[array.length - 2];
            resultArray[array.length - 3] = array[array.length - 3];

            for (int k = array.length - 1; k > 3; k--) {
                resultArray[k] = (resultArray[k] +
                        resultArray[k - 1] +
                        resultArray[k - 2] +
                        resultArray[k - 3]) / 4;
            }
        }

        return resultArray;
    }
}
