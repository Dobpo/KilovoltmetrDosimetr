package com.idobro.kilovoltmetr_dosimetr.utils;

// TODO: 21.05.2020 develop methods for graph's management
public final class GraphManager {

    /**
     * Отношение первого графика ко второму
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

    private static float[] smoothGraph(float[] array, int iterationCount) {
        if (iterationCount < 1)
            throw new IllegalArgumentException("Iteration count can't be negative or zero");

        float[] resultArray = new float[array.length];

        for (int j = 0; j < iterationCount; j++) {
            for (int i = 0; i < array.length - 4; i++) {
                resultArray[i] = (array[i] +
                        array[i + 1] +
                        array[i + 2] +
                        array[i] + 3) / 4;
            }

            for (int i = array.length; i > 4; i--) {
                resultArray[i] = (array[i] +
                        array[i - 1] +
                        array[i - 2] +
                        array[i] - 3) / 4;
            }
        }

        return resultArray;
    }
}
