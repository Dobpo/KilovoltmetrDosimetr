package com.idobro.kilovoltmetr_dosimetr;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialListener;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialSocket;

public class MainActivityViewModel extends AndroidViewModel implements SerialListener {
    private MutableLiveData<String> serverResponse;
    private MutableLiveData<String> connectStatus;

    private enum Connected { False, Pending, True }

    private String deviceAddress;

    private SerialSocket socket;
    private boolean initialStart = true;
    private Connected connected = Connected.False;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getServerResponseLiveData(){
        if(serverResponse == null){
            serverResponse  = new MutableLiveData<>();
        }
        return serverResponse;
    }

    public LiveData<String> getStatusLiveData(){
        if(connectStatus == null){
            connectStatus = new MutableLiveData<>();
        }
        return connectStatus;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private Context getContext(){
        return getApplication().getApplicationContext();
    }

    //SerialListener
    @Override
    public void onSerialConnect() {

    }

    @Override
    public void onSerialConnectError(Exception e) {

    }

    @Override
    public void onSerialRead(byte[] data) {

    }

    @Override
    public void onSerialIoError(Exception e) {

    }
}
