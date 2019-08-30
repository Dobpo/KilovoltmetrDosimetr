package com.idobro.kilovoltmetr_dosimetr.bluetooth.entities;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BluetoothDevices implements Parcelable {
    private final ArrayList<BluetoothDevice> devices;

    public BluetoothDevices(ArrayList<BluetoothDevice> devices) {
        this.devices = devices;
    }

    public ArrayList<BluetoothDevice> getDevices() {
        return devices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.devices);
    }

    private BluetoothDevices(Parcel in) {
        this.devices = in.createTypedArrayList(BluetoothDevice.CREATOR);
    }

    public static final Parcelable.Creator<BluetoothDevices> CREATOR = new Parcelable.Creator<BluetoothDevices>() {
        @Override
        public BluetoothDevices createFromParcel(Parcel source) {
            return new BluetoothDevices(source);
        }

        @Override
        public BluetoothDevices[] newArray(int size) {
            return new BluetoothDevices[size];
        }
    };
}
