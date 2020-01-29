package com.idobro.kilovoltmetr_dosimetr;

public interface Constants {
    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_CONNECT_STATE_CHANGE = 1;
    int MESSAGE_SENSOR_STATE_CHANGE = 2;
    int MESSAGE_COULD_NOT_CONNECT = 3;
    int MESSAGE_MEASURE_DONE = 4;
    int MESSAGE_BATTERY_CHARGE = 5;
}