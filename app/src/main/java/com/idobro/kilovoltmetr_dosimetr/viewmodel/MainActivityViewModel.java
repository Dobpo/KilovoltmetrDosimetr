package com.idobro.kilovoltmetr_dosimetr.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idobro.kilovoltmetr_dosimetr.Constants;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothService;

public class MainActivityViewModel extends AndroidViewModel{
    private MutableLiveData<String> serverResponse;
    private MutableLiveData<Connected> connectStatus;
    private BluetoothService bluetoothService;
    public enum Connected {False, Failure, Pending, True}

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        bluetoothService = new BluetoothService(getContext(), mHandler);
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
       bluetoothService.connect(device);
    }

    public void disconnect() {
        bluetoothService.stop();
        bluetoothService = null;
    }

    public void send(String string) {
        if (connectStatus.getValue() != Connected.True) {
            Toast.makeText(getContext(), "not connect", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] data = string.getBytes();
            bluetoothService.write(data);
        } catch (Exception e) {
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

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            connectStatus.postValue(Connected.True);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            connectStatus.postValue(Connected.Pending);
                            break;
                        case BluetoothService.STATE_NONE:
                            connectStatus.postValue(Connected.False);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    // TODO: 22.08.2019 Here output data come
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    int len = msg.arg1;
                    int totalCounter = msg.arg2;
                    String str = new String(readBuf, 0, readBuf.length) + " Len  = " + len;
                    Log.d("DATA", "ViewModel: -> " + str);
                    String outStr = new String(readBuf,0,readBuf.length)+"\n";

                    //serverResponse.postValue(outStr);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    //TODO: none
                    break;
                case Constants.MESSAGE_TOAST:
                    // TODO: 22.08.2019 none
                    break;
            }
        }
    };
}
