package com.idobro.kilovoltmetr_dosimetr.bluetooth.core;

import android.bluetooth.BluetoothDevice;

public interface BluetoothService {
    void connect(BluetoothDevice device);
    void stop();
    void enableNewMeasure();
}
