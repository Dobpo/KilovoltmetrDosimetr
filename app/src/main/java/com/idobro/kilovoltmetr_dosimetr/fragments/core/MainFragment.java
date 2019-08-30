package com.idobro.kilovoltmetr_dosimetr.fragments.core;

public interface MainFragment {
    String MESSAGE = "message";

    void onDisconnect();
    void onConnecting();
    void waitForNewMeasure();
    void onXRay();
    void waitForXRay();
}
