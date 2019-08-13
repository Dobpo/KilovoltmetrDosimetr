package com.idobro.kilovoltmetr_dosimetr;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainActivityViewModel extends AndroidViewModel implements LifecycleObserver {
    MutableLiveData<String> serverResponse;
    MutableLiveData<String> connectStatus;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getServerResponceLiveData(){
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
}
