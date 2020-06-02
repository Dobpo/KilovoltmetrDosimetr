package com.idobro.kilovoltmetr_dosimetr.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceManager {
    private final String GRAPH_VISIBILITY = "GRAPH_VISIBILITY";

    private SharedPreferences sharedPreferences;

    public SharedPreferenceManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveGraphVisibility(String visibility) {
        sharedPreferences.edit().putString(GRAPH_VISIBILITY, visibility).apply();
    }

    public String getGraphVisibility() {
        return sharedPreferences.getString(GRAPH_VISIBILITY, "");
    }
}