package com.idobro.kilovoltmetr_dosimetr;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialListener;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialService;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.SerialSocket;

public class MainActivityViewModel extends AndroidViewModel implements LifecycleObserver,
        ServiceConnection, SerialListener {
    MutableLiveData<String> serverResponse;
    MutableLiveData<String> connectStatus;

    private enum Connected { False, Pending, True }

    private String deviceAddress;

    private SerialSocket socket;
    private SerialService service;
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
        //refresh resource
    }

    private Context getContext(){
        return getApplication();
    }

    //LifecycleObserver
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void activityOnStart(){

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void activityOnStop(){}
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void activityOnCreate(){}
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void activityOnDestroy(){}


    //ServiceConnection
    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

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
