package com.idobro.kilovoltmetr_dosimetr.utils;

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
    public static float getAverage(float[] array) {
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
            for (int j = 0; j < array.length - 9; j++) {
                resultArray[j] = (array[j] +
                        array[j + 1] +
                        array[j + 2] +
                        array[j + 3] +
                        array[j + 4] +
                        array[j + 5] +
                        array[j + 6] +
                        array[j + 7] +
                        array[j + 8] +
                        array[j + 9]) / 10;
            }

            resultArray[array.length - 1] = array[array.length - 1];
            resultArray[array.length - 2] = array[array.length - 2];
            resultArray[array.length - 3] = array[array.length - 3];
            resultArray[array.length - 4] = array[array.length - 4];
            resultArray[array.length - 5] = array[array.length - 5];
            resultArray[array.length - 6] = array[array.length - 6];
            resultArray[array.length - 7] = array[array.length - 7];
            resultArray[array.length - 8] = array[array.length - 8];
            resultArray[array.length - 9] = array[array.length - 9];

            for (int k = array.length - 1; k > 9; k--) {
                resultArray[k] = (resultArray[k] +
                        resultArray[k - 1] +
                        resultArray[k - 2] +
                        resultArray[k - 3] +
                        resultArray[k - 4] +
                        resultArray[k - 5] +
                        resultArray[k - 6] +
                        resultArray[k - 7] +
                        resultArray[k - 8] +
                        resultArray[k - 9]) / 10;
            }
        }

        return resultArray;
    }
}
