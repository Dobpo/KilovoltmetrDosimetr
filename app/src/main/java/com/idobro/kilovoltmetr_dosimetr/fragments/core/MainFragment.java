package com.idobro.kilovoltmetr_dosimetr.fragments.core;

public interface MainFragment {
    void onDisconnect();
    void onConnecting();
    void waitForNewMeasure();
    void onXRay();
    void waitForXRay();
}
