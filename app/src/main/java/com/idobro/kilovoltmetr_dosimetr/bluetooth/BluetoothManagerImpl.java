package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import com.idobro.kilovoltmetr_dosimetr.Constants;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.utils.ByteToIntConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.NoConnectionPendingException;
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
    private static final byte[] GET_BATTERY_CHARGE = {0x05};

    //Constants that indicate sensor state
    private static final int WAIT_FOR_ENABLE_MEASURE = 0;
    public static final int WAIT_FOR_X_RAY = 1;
    public static final int WAIT_FOR_END_X_RAY = 2;
    private static final int WAIT_FOR_FRONT_CHART = 3;
    private static final int WAIT_FOR_FULL_CHART = 4;
    private static final int WAIT_FOR_BATTERY_CHARGE = 5;

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

    private synchronized void notifyOnBatteryChargeMeassured(int batteryCharge) {
        handler.obtainMessage(Constants.MESSAGE_BATTERY_CHARGE, batteryCharge, -1).sendToTarget();
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

    @Override
    public void getBatteryCharge() {
        sensorState = WAIT_FOR_BATTERY_CHARGE;
        write(GET_BATTERY_CHARGE);
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
                e.printStackTrace();
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
                    e.printStackTrace();
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
                e.printStackTrace();
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

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inStream = tmpIn;
            outputStream = tmpOut;
            state = STATE_CONNECTED;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            int count = 0;
            byte[] endCommandArray = null;
            Graph graph = new Graph(-1);

            while (state == STATE_CONNECTED) {
                try {
                    switch (sensorState) {
                        case WAIT_FOR_ENABLE_MEASURE:
                            break;
                        case WAIT_FOR_BATTERY_CHARGE:
                            inStream.read(buffer, 0, 1);

                            notifyOnBatteryChargeMeassured(ByteToIntConverter.getUnsignedInt(buffer[0]));

                            sensorState = WAIT_FOR_ENABLE_MEASURE;
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
                            endCommandArray = new byte[6];

                            do {
                                bytes = inStream.read(buffer, 0, 1024);

                                System.arraycopy(buffer, 0, endCommandArray, count, bytes);

                                count += bytes;
                            } while (count < 6);

                            count = 0;
                            graph = new Graph(ByteToIntConverter.getUnsignedInt(endCommandArray[5]));
                            sensorState = WAIT_FOR_FRONT_CHART;
                            write(GET_FRONT);
                            break;
                        case WAIT_FOR_FRONT_CHART:
                            if (endCommandArray != null) {
                                int frontDataLength = ByteToIntConverter.getUnsignedInt(endCommandArray[1], endCommandArray[2]);
                                byte[] frontDataArray = new byte[frontDataLength];

                                do {
                                    bytes = inStream.read(buffer, 0, 1024);

                                    System.arraycopy(buffer, 0, frontDataArray, count, bytes);

                                    count += bytes;

                                } while (count < frontDataLength);

                                count = 0;
                                graph.setFrontGraphData(frontDataArray);
                                sensorState = WAIT_FOR_FULL_CHART;
                                write(GET_FULL);
                            } else {
                                throw new NoConnectionPendingException();
                            }
                            break;
                        case WAIT_FOR_FULL_CHART:
                            if (endCommandArray != null) {
                                int fullDataLength = ByteToIntConverter.getUnsignedInt(endCommandArray[3], endCommandArray[4]);
                                byte[] fullDataArray = new byte[fullDataLength];

                                do {
                                    bytes = inStream.read(buffer);

                                    System.arraycopy(buffer, 0, fullDataArray, count, bytes);

                                    count += bytes;
                                } while (count < fullDataLength);

                                count = 0;
                                graph.setFullGraphData(fullDataArray);
                                sensorState = WAIT_FOR_ENABLE_MEASURE;
                                endCommandArray = null;
                                handler.obtainMessage(Constants.MESSAGE_MEASURE_DONE, -1, -1, graph)
                                        .sendToTarget();
                            } else {
                                throw new NoConnectionPendingException();
                            }
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }

        void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}