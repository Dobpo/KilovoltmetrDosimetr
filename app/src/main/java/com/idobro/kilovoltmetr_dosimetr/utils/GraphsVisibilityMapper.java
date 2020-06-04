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
            //return new GraphsVisibilityModel(createModel());
            return gson.fromJson(json, GraphsVisibilityModel.class);
    }

    public static String toString(GraphsVisibilityModel graphsVisibilityModel) {
        return gson.toJson(graphsVisibilityModel);
    }

    private static List<GraphVisibility> createModel() {
        List<GraphVisibility> items = new ArrayList<>();

        items.add(new GraphVisibility("Front first chanel", "#3F51B5", true));
        items.add(new GraphVisibility("Front second chanel", "#cc3333", false));
        items.add(new GraphVisibility("Front third chanel", "#4caf50", false));

        items.add(new GraphVisibility("Front third to second", "#864274", false));
        items.add(new GraphVisibility("Front third to first", "#468083", false));
        items.add(new GraphVisibility("Front second to first", "#8C7142", false));

        return items;
    }
}