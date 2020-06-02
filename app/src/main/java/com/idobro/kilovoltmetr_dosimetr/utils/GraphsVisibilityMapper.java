package com.idobro.kilovoltmetr_dosimetr.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityFilterModel;

public final class GraphsVisibilityMapper {
    private static Gson gson = new GsonBuilder().create();

    private GraphsVisibilityMapper() {
    }

    public static GraphsVisibilityFilterModel toVisibilityModel(String json) {
        GraphsVisibilityFilterModel graphsVisibilityFilterModel;
        if (TextUtils.isEmpty(json))
            return new GraphsVisibilityFilterModel();
        else
            return gson.fromJson(json, GraphsVisibilityFilterModel.class);
    }

    public static String toString(GraphsVisibilityFilterModel graphsVisibilityFilterModel) {
        return gson.toJson(graphsVisibilityFilterModel);
    }
}