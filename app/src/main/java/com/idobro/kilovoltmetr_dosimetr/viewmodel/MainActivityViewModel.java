package com.idobro.kilovoltmetr_dosimetr.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idobro.kilovoltmetr_dosimetr.Constants;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.core.BluetoothService;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothServiceImpl;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<String> serverResponse;
    private MutableLiveData<SocketStatus> connectStatus;
    private BluetoothService bluetoothService;

    public enum SocketStatus {
        DISCONNECT,
        COULD_NOT_CONNECT,
        PENDING,
        CONNECTED,
        WAIT_X_RAY,
        LOAD_CHART_DATA
    }


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        bluetoothService = new BluetoothServiceImpl(getContext(), mHandler);
    }

    public LiveData<String> getServerResponseLiveData() {
        if (serverResponse == null) {
            serverResponse = new MutableLiveData<>();
        }
        return serverResponse;
    }

    public LiveData<SocketStatus> getStatusLiveData() {
        if (connectStatus == null) {
            connectStatus = new MutableLiveData<>();
            connectStatus.setValue(SocketStatus.DISCONNECT);
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

    private void disconnect() {
        bluetoothService.stop();
        bluetoothService = null;
    }

    public void enableNewMeasure() {
        bluetoothService.enableNewMeasure();
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_CONNECT_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothServiceImpl.STATE_CONNECTED:
                            connectStatus.postValue(SocketStatus.CONNECTED);
                            break;
                        case BluetoothServiceImpl.STATE_CONNECTING:
                            connectStatus.postValue(SocketStatus.PENDING);
                            break;
                        case BluetoothServiceImpl.STATE_NONE:
                            connectStatus.postValue(SocketStatus.DISCONNECT);
                            break;
                    }
                    break;
                case Constants.MESSAGE_COULD_NOT_CONNECT:
                    connectStatus.postValue(SocketStatus.COULD_NOT_CONNECT);
                    Toast.makeText(getContext(), "Could not connect the sensor", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_SENSOR_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothServiceImpl.WAIT_FOR_X_RAY:
                            connectStatus.postValue(SocketStatus.WAIT_X_RAY);
                            break;
                        case BluetoothServiceImpl.WAIT_FOR_END_X_RAY:
                            connectStatus.postValue(SocketStatus.LOAD_CHART_DATA);
                            break;
                    }
                    break;
                case Constants.MESSAGE_MEASURE_DONE:
                    serverResponse.postValue("complete");
                    Toast.makeText(getContext(), "Measure done", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
