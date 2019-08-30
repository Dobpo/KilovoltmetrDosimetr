package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.Constants;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.core.BluetoothService;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.ChartDataModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothServiceImpl implements BluetoothService {
    // Unique UUID for this application
    private static final UUID BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
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

    public BluetoothServiceImpl(Context context, Handler mHandler) {
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = mHandler;
    }

    /**
     * Update UI title according to the current state of the connection
     */
    private synchronized void notifyOnConnectStatusChange() {
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constants.MESSAGE_CONNECT_STATE_CHANGE, mState, -1).sendToTarget();
    }

    private synchronized void notifyOnSensorStatusChange() {
        mHandler.obtainMessage(Constants.MESSAGE_SENSOR_STATE_CHANGE, sensorState, -1).sendToTarget();
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    @Override
    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        // Update UI title
        notifyOnConnectStatusChange();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    private synchronized void connected(BluetoothSocket socket) {
        Log.d("LOG", "connected, Socket");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Update UI title
        notifyOnConnectStatusChange();
    }

    /**
     * Stop all threads
     */
    @Override
    public synchronized void stop() {
        Log.d("LOG", "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mState = STATE_NONE;
        // Update UI title
        notifyOnConnectStatusChange();
    }

    @Override
    public void enableNewMeasure() {
        write(ENABLE_START);
        sensorState = WAIT_FOR_X_RAY;
        notifyOnSensorStatusChange();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    private void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }


    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        mHandler.obtainMessage(Constants.MESSAGE_COULD_NOT_CONNECT, -1, -1).sendToTarget();
        mState = STATE_NONE;
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        mState = STATE_NONE;
        notifyOnConnectStatusChange();
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP);

            } catch (IOException e) {
                Log.e("LOG", "Socket: create() failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e("LOG", "unable to close() socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothServiceImpl.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket);
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("LOG", "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(BluetoothSocket socket) {
            Log.d("LOG", "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("LOG", "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            Log.i("LOG", "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;
            int count = 0;
            ArrayList<Byte> endCommandArrayList = new ArrayList<>();
            ArrayList<Byte> frontDataArrayList = new ArrayList<>();
            ArrayList<Byte> fullDataArrayList = new ArrayList<>();

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    switch (sensorState) {
                        case WAIT_FOR_ENABLE_MEASURE:
                            // TODO: 30.08.2019  get answer from sensor
                            //sensorState = WAIT_FOR_END_X_RAY;
                            //notifyOnSensorStatusChange();
                            break;
                        case WAIT_FOR_X_RAY:
                            do {
                                bytes = mmInStream.read(buffer, 0, 1);
                                count += bytes;
                            }while (count<1);
                            Log.d("LOG", "ConnectedThread -> run : bytes 1 = " + bytes);
                            count = 0;
                            sensorState = WAIT_FOR_END_X_RAY;
                            notifyOnSensorStatusChange();
                            break;
                        case WAIT_FOR_END_X_RAY:
                            do {
                                bytes = mmInStream.read(buffer, 0, 5);
                                count += bytes;
                                for (int i = 0; i < bytes; i++) {
                                    endCommandArrayList.add(buffer[i]);
                                }
                            } while (count < 5);
                            ChartDataModel chartDataModel = new ChartDataModel(endCommandArrayList);
                            Log.d("LOG", "ConnectedThread -> run : count 2 = " + count);
                            count = 0;
                            sensorState = WAIT_FOR_FRONT_CHART;
                            write(GET_FRONT);
                            break;
                        case WAIT_FOR_FRONT_CHART:
                            do {
                                bytes = mmInStream.read(buffer, 0, 1024);
                                count += bytes;
                                for (int i = 0; i < bytes; i++) {
                                    frontDataArrayList.add(buffer[i]);
                                }
                            } while (count < 15000);
                            Log.d("LOG", "ConnectedThread -> run : count 3 = " + count);
                            count = 0;
                            sensorState = WAIT_FOR_FULL_CHART;
                            write(GET_FULL);
                            break;
                        case WAIT_FOR_FULL_CHART:
                            do {
                                bytes = mmInStream.read(buffer);
                                count += bytes;
                                for (int i = 0; i < bytes; i++) {
                                    fullDataArrayList.add(buffer[i]);
                                }
                            } while (count < 60000);
                            Log.d("LOG", "ConnectedThread -> run : count 4 = " + count);
                            count = 0;
                            sensorState = WAIT_FOR_ENABLE_MEASURE;
                            mHandler.obtainMessage(Constants.MESSAGE_MEASURE_DONE).sendToTarget();
                            break;
                    }
                } catch (IOException e) {
                    Log.e("LOG", "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e("LOG", "Exception during write", e);
            }
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("LOG", "close() of connect socket failed", e);
            }
        }
    }
}
