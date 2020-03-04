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
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothManager;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothManagerImpl;
import com.idobro.kilovoltmetr_dosimetr.database.Database;
import com.idobro.kilovoltmetr_dosimetr.database.DatabaseManager;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsDates;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Graph> charts;
    private MutableLiveData<SocketStatus> connectStatus;
    private BluetoothManager bluetoothManager;
    private DatabaseManager databaseManager;
    private Graph graph;

    // TODO: 04.03.2020
    public void showImportantData() {
        databaseManager.getAllCharts(new ResponseCallback<List<Graph>>() {
            @Override
            public void onSuccess(List<Graph> response) {
                for (int i = 7; i < 22; i++) {

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ID = ")
                            .append(response.get(i).getId())
                            .append("\t")
                            .append(" Point = 500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[500] / response.get(i).getFrontFirstChanelGraph()[500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[500] / response.get(i).getFrontSecondChanelGraph()[500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[500] / response.get(i).getFrontFirstChanelGraph()[500]))

                            .append("\tPoint = 1500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[1500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[1500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[1500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[1500] / response.get(i).getFrontFirstChanelGraph()[1500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[1500] / response.get(i).getFrontSecondChanelGraph()[1500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[1500] / response.get(i).getFrontFirstChanelGraph()[1500]))

                            .append("\tPoint = 2500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[2500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[2500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[2500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[2500] / response.get(i).getFrontFirstChanelGraph()[2500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[2500] / response.get(i).getFrontSecondChanelGraph()[2500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[2500] / response.get(i).getFrontFirstChanelGraph()[2500]))

                            .append("\tPoint = 3500 ")
                            .append(response.get(i).getFrontFirstChanelGraph()[3500])
                            .append("|")
                            .append(response.get(i).getFrontSecondChanelGraph()[3500])
                            .append("|")
                            .append(response.get(i).getFrontThirdChanelGraph()[3500])

                            .append("\t")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[3500] / response.get(i).getFrontFirstChanelGraph()[3500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontThirdChanelGraph()[3500] / response.get(i).getFrontSecondChanelGraph()[3500]))
                            .append("|")
                            .append(String.format("%.2f", response.get(i).getFrontSecondChanelGraph()[3500] / response.get(i).getFrontFirstChanelGraph()[3500]));

                    Log.d("LOG", stringBuilder.toString());
                }
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

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
        bluetoothManager = new BluetoothManagerImpl(getContext(), mHandler);
        databaseManager = new Database(getContext());
    }

    public LiveData<Graph> getServerResponseLiveData() {
        if (charts == null) {
            charts = new MutableLiveData<>();
        }
        return charts;
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
        bluetoothManager.connect(device);
    }

    private void disconnect() {
        bluetoothManager.stop();
        bluetoothManager = null;
    }

    public void enableNewMeasure() {
        bluetoothManager.enableNewMeasure();
    }

    public void getBatteryCharge() {
        bluetoothManager.getBatteryCharge();
    }

    public void saveChart() {
        if (graph != null) {
            databaseManager.addNewChart(graph);
            showSavedChartsCount();
        }
    }

    public void showSavedChartsCount() {
        databaseManager.getChartRecordsNumber(new ResponseCallback<Integer>() {
            @Override
            public void onSuccess(Integer response) {
                Toast.makeText(getContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    public LiveData<List<GraphsDates>> getGraphsDates() {
        MutableLiveData<List<GraphsDates>> liveData = new MutableLiveData<>();
        databaseManager.getGraphsDates(new ResponseCallback<List<GraphsDates>>() {
            @Override
            public void onSuccess(List<GraphsDates> response) {
                liveData.setValue(response);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getContext(), "Не удалось загрузить графики", Toast.LENGTH_SHORT).show();
            }
        });

        return liveData;
    }

    public LiveData<Graph> getGraphById(long id) {
        MutableLiveData<Graph> liveData = new MutableLiveData<>();
        databaseManager.getGraphById(id, new ResponseCallback<Graph>() {
            @Override
            public void onSuccess(Graph response) {
                liveData.setValue(response);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getContext(), "Не удалось загрузить график", Toast.LENGTH_SHORT).show();
            }
        });

        return liveData;
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_CONNECT_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothManagerImpl.STATE_CONNECTED:
                            connectStatus.postValue(SocketStatus.CONNECTED);
                            break;
                        case BluetoothManagerImpl.STATE_CONNECTING:
                            connectStatus.postValue(SocketStatus.PENDING);
                            break;
                        case BluetoothManagerImpl.STATE_NONE:
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
                        case BluetoothManagerImpl.WAIT_FOR_X_RAY:
                            connectStatus.postValue(SocketStatus.WAIT_X_RAY);
                            break;
                        case BluetoothManagerImpl.WAIT_FOR_END_X_RAY:
                            connectStatus.postValue(SocketStatus.LOAD_CHART_DATA);
                            break;
                    }
                    break;
                case Constants.MESSAGE_MEASURE_DONE:
                    graph = (Graph) msg.obj;
                    charts.postValue(graph);
                    Toast.makeText(getContext(), "Measure done", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_BATTERY_CHARGE:
                    Toast.makeText(getContext(), msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}