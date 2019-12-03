package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface BluetoothManager {
    void connect(BluetoothDevice device);

    void stop();

    void enableNewMeasure();
}