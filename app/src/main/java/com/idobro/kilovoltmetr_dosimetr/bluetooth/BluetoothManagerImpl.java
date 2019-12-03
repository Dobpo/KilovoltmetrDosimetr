package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.Constants;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.ChartDataModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothManagerImpl implements BluetoothManager {
    // Unique UUID for this application
    private static final UUID BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter adapter;
    private final Handler handler;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private int state;
    private int sensorState = WAIT_FOR_ENABLE_MEASURE;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTING = 1; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 2;  // now connected to a remote device

    //Commands to sensor
    private static final byte[] ENABLE_START = {0x01};
    private static final byte[] GET_FRONT = {0x03};
    private static final byte[] GET_FULL = {0x04};

    //Constants that indicate sensor state
    private static final int WAIT_FOR_ENABLE_MEASURE = 0;
    public static final int WAIT_FOR_X_RAY = 1;
    public static final int WAIT_FOR_END_X_RAY = 2;
    private static final int WAIT_FOR_FRONT_CHART = 3;
    private static final int WAIT_FOR_FULL_CHART = 4;

    public BluetoothManagerImpl(Context context, Handler handler) {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.handler = handler;
    }

    private synchronized void notifyOnConnectStatusChange() {
        handler.obtainMessage(Constants.MESSAGE_CONNECT_STATE_CHANGE, state, -1).sendToTarget();
    }

    private synchronized void notifyOnSensorStatusChange() {
        handler.obtainMessage(Constants.MESSAGE_SENSOR_STATE_CHANGE, sensorState, -1).sendToTarget();
    }

    @Override
    public synchronized void connect(BluetoothDevice device) {
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        connectThread = new ConnectThread(device);
        connectThread.start();
        notifyOnConnectStatusChange();
    }

    private synchronized void connected(BluetoothSocket socket) {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        notifyOnConnectStatusChange();
    }

    @Override
    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        state = STATE_NONE;
        notifyOnConnectStatusChange();
    }

    @Override
    public void enableNewMeasure() {
        write(ENABLE_START);
        sensorState = WAIT_FOR_X_RAY;
        notifyOnSensorStatusChange();
    }

    private void write(byte[] out) {
        ConnectedThread r;

        synchronized (this) {
            if (state != STATE_CONNECTED) return;
            r = connectedThread;
        }
        r.write(out);
    }

    private void connectionFailed() {
        handler.obtainMessage(Constants.MESSAGE_COULD_NOT_CONNECT, -1, -1).sendToTarget();
        state = STATE_NONE;
    }

    private void connectionLost() {
        state = STATE_NONE;
        notifyOnConnectStatusChange();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;

        ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP);

            } catch (IOException e) {
                Log.e("LOG", "Socket: create() failed", e);
            }
            socket = tmp;
            state = STATE_CONNECTING;
        }

        public void run() {
            setName("ConnectThread");
            adapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e2) {
                    Log.e("LOG", "unable to close() socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            synchronized (BluetoothManagerImpl.this) {
                connectThread = null;
            }
            connected(socket);
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("LOG", "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inStream;
        private final OutputStream outputStream;

        ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("LOG", "temp sockets not created", e);
            }

            inStream = tmpIn;
            outputStream = tmpOut;
            state = STATE_CONNECTED;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            int count = 0;
            ArrayList<Byte> endCommandArrayList = new ArrayList<>();
            ArrayList<Byte> frontDataArrayList = new ArrayList<>();
            ArrayList<Byte> fullDataArrayList = new ArrayList<>();
            ChartDataModel chartDataModel = new ChartDataModel();

            // Keep listening to the InputStream while connected
            while (state == STATE_CONNECTED) {
                try {
                    switch (sensorState) {
                        case WAIT_FOR_ENABLE_MEASURE:
                            // TODO: 30.08.2019  get answer from sensor
                            break;
                        case WAIT_FOR_X_RAY:
                            do {
                                bytes = inStream.read(buffer, 0, 1);
                                count += bytes;
                            } while (count < 1);
                            count = 0;
                            sensorState = WAIT_FOR_END_X_RAY;
                            notifyOnSensorStatusChange();
                            break;
                        case WAIT_FOR_END_X_RAY:
                            do {
                                bytes = inStream.read(buffer, 0, 5);
                                count += bytes;
                                for (int i = 0; i < bytes; i++) {
                                    endCommandArrayList.add(buffer[i]);
                                }
                            } while (count < 5);
                            chartDataModel = new ChartDataModel(endCommandArrayList);
                            Log.d("LOG", "ConnectedThread -> run : Meassure done = " + count);
                            count = 0;
                            sensorState = WAIT_FOR_FRONT_CHART;
                            write(GET_FRONT);
                            break;
                        case WAIT_FOR_FRONT_CHART:
                            do {
                                bytes = inStream.read(buffer, 0, 1024);
                                count += bytes;
                                for (int i = 0; i < bytes; i++) {
                                    frontDataArrayList.add(buffer[i]);
                                }
                            } while (count < 15000);
                            chartDataModel.setFrontDataArray(frontDataArrayList);
                            frontDataArrayList = new ArrayList<>();
                            Log.d("LOG", "ConnectedThread -> run : Front = " + count);
                            count = 0;
                            sensorState = WAIT_FOR_FULL_CHART;
                            write(GET_FULL);
                            break;
                        case WAIT_FOR_FULL_CHART:
                            do {
                                bytes = inStream.read(buffer);
                                count += bytes;
                                for (int i = 0; i < bytes; i++) {
                                    fullDataArrayList.add(buffer[i]);
                                }
                            } while (count < 60000);
                            chartDataModel.setFullDataArray(fullDataArrayList);
                            fullDataArrayList = new ArrayList<>();
                            Log.d("LOG", "ConnectedThread -> run : Full = " + count);
                            count = 0;
                            sensorState = WAIT_FOR_ENABLE_MEASURE;
                            handler.obtainMessage(Constants.MESSAGE_MEASURE_DONE, -1, -1, chartDataModel)
                                    .sendToTarget();
                            break;
                    }
                } catch (IOException e) {
                    Log.e("LOG", "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
            } catch (IOException e) {
                Log.e("LOG", "Exception during write", e);
            }
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("LOG", "close() of connect socket failed", e);
            }
        }
    }
}