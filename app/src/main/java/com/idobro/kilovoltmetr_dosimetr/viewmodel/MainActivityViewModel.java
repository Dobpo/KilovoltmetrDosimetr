package com.idobro.kilovoltmetr_dosimetr.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialListener;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialSocket;

import java.io.IOException;

public class MainActivityViewModel extends AndroidViewModel implements SerialListener {
    private MutableLiveData<String> serverResponse;
    private MutableLiveData<Connected> connectStatus;
    private SerialSocket socket;

    public enum Connected {False, Failure, Pending, True}

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getServerResponseLiveData() {
        if (serverResponse == null) {
            serverResponse = new MutableLiveData<>();
        }
        return serverResponse;
    }

    public LiveData<Connected> getStatusLiveData() {
        if (connectStatus == null) {
            connectStatus = new MutableLiveData<>();
            connectStatus.setValue(Connected.False);
        }
        return connectStatus;
    }

    @Override
    protected void onCleared() {
        disconnect();
        super.onCleared();
    }

    private Context getContext() {
        return getApplication().getApplicationContext();
    }

    public void connect(BluetoothDevice device) {
        try {
            connectStatus.postValue(Connected.Pending);
            socket = new SerialSocket();
            socket.connect(getContext(), this, device);
        } catch (IOException e) {
            onSerialIoError(e);
        }
    }

    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public void send(String string) {
        if (connectStatus.getValue() != Connected.True) {
            Toast.makeText(getContext(), "not connect", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] data = string.getBytes();
            socket.write(data);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void processingInputData(byte[] data, int len) {
        String str;
        switch (data[0]) {
            case 0x53:
                str = String.format("Command: %h, Data = %h%h%h%h /FullDataSize: %s\n"
                        , data[0], data[1], data[2], data[3], data[4], String.valueOf(len));
                break;
            case 0x46:
                str = String.format("Command: %h, Data = %h%h%h%h /FullDataSize: %s\n"
                        , data[0], data[1], data[2], data[3], data[4], String.valueOf(len));
                break;
            default:
                str = "";
                break;
        }

        serverResponse.postValue(str);
    }

    //SerialListener
    @Override
    public void onSerialConnect() {
        connectStatus.postValue(Connected.True);
    }

    @Override
    public void onSerialConnectError(Exception e) {
        disconnect();
        connectStatus.postValue(Connected.Failure);
        Log.d("LOG", "MainActivityViewModel -> onSerialConnectError : ");
    }

    @Override
    public void onSerialRead(byte[] data, int len)
    {
        processingInputData(data, len);
    }

    @Override
    public void onSerialIoError(Exception e) {
        disconnect();
        connectStatus.postValue(Connected.False);
        serverResponse.postValue(e.getMessage() + "\n");
        Log.d("LOG", "MainActivityViewModel -> onSerialIoError : ");
    }
}
