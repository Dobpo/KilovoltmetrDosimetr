package com.idobro.kilovoltmetr_dosimetr.bluetooth;

public interface SerialListener {
    void onSerialConnect      ();
    void onSerialConnectError (Exception e);
    void onSerialRead         (byte[] data, int len);
    void onSerialIoError      (Exception e);
}
