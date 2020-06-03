package com.idobro.kilovoltmetr_dosimetr.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityModel;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsVisibilityModel.GraphVisibility;

import java.util.ArrayList;
import java.util.List;

public final class GraphsVisibilityMapper {
    private static Gson gson = new GsonBuilder().create();

    private GraphsVisibilityMapper() {
    }

    public static GraphsVisibilityModel toVisibilityModel(String json) {
        if (TextUtils.isEmpty(json))
            return new GraphsVisibilityModel(createModel());
        else
            return gson.fromJson(json, GraphsVisibilityModel.class);
    }

    public static String toString(GraphsVisibilityModel graphsVisibilityModel) {
        return gson.toJson(graphsVisibilityModel);
    }

    private static List<GraphVisibility> createModel() {
        List<GraphVisibility> items = new ArrayList<>();
        items.add(new GraphVisibility("title1", "#ff0000", true));
        items.add(new GraphVisibility("title2", "#00ff00", false));
        items.add(new GraphVisibility("title3", "#0000ff", true));
        return items;
    }
}