package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.Constants;
import com.idobro.kilovoltmetr_dosimetr.utils.ByteToIntConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BluetoothService {
    // Unique UUID for this application
    private static final UUID BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private int mNewState;
    private MkState mkState = MkState.WAIT_FOR_X_RAY;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTING = 1; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 2;  // now connected to a remote device

    private enum MkState {
        WAIT_FOR_X_RAY,
        WAIT_FOR_END_X_RAY,
        WAIT_FOR_FRONT_CHART,
        WAIT_FOR_FULL_CHART
    }


    public BluetoothService(Context context, Handler mHandler) {
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = mHandler;
    }

    /**
     * Update UI title according to the current state of the chat connection
     */
    private synchronized void updateUserInterfaceTitle() {
        mState = getState();
        Log.d("LOG", "updateUserInterfaceTitle() " + mNewState + " -> " + mState);
        mNewState = mState;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }


    /**
     * Return the current connection state.
     */
    synchronized int getState() {
        return mState;
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        Log.d("LOG", "connect to: " + device);

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
        updateUserInterfaceTitle();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device) {
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

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * Stop all threads
     */
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
        updateUserInterfaceTitle();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
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
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
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
            Log.i("LOG", "BEGIN mConnectThread");
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
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
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

        public ConnectedThread(BluetoothSocket socket) {
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
            int counter = 0;
            int totalCounter = 0;
            ChartDataModel chartDataModel = null;
            ArrayList<Byte> inputDataArrayList = new ArrayList<>();

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    counter += bytes;
                    totalCounter += bytes;

                    //Log.d("LOG", "ConnectedThread -> run : bytes = " + bytes);
                    Log.d("LOG", "ConnectedThread -> run : counter = " + counter);
                    //Log.d("LOG", "ConnectedThread -> run : TotalCounter = " + totalCounter);

                    byte[] data = Arrays.copyOf(buffer, bytes);

                    if (counter < 63004) {
                        for (byte b : data) {
                            inputDataArrayList.add(b);
                        }
                    } else {
                        for (byte b : data) {
                            inputDataArrayList.add(b);
                        }
                        chartDataModel = new ChartDataModel(inputDataArrayList);
                        inputDataArrayList = new ArrayList<>();
                        counter = 0;
                    }

//                    switch (mkState) {
//                        case WAIT_FOR_X_RAY:
//                            Log.d("LOG", "ConnectedThread -> run : OnXRay " +
//                                    ByteToIntConverter.getUnsignedInt(data[0]));
//                            for (byte b : data) {
//                                inputDataArrayList.add(b);
//                            }
//                            mkState = MkState.WAIT_FOR_END_X_RAY;
//                            break;
//                        case WAIT_FOR_END_X_RAY:
//                            if (counter < 305) {
//                                for (byte b : data) {
//                                    inputDataArrayList.add(b);
//                                }
//                            }else{
//                                for (byte b : data) {
//                                    inputDataArrayList.add(b);
//                                }
//                                chartDataModel = new ChartDataModel(inputDataArrayList);
//                                mkState = MkState.WAIT_FOR_X_RAY;
//                                counter = 0;
//                                inputDataArrayList = new ArrayList<>();
//                                }
//                            break;
//                        case WAIT_FOR_FRONT_CHART:
//                            for (byte b : data) {
//                                frontArrayList.add(b);
//                            }
//
//                            if (chartDataModel != null && counter >= chartDataModel.getFrontChartLength()) {
//                                Log.d("LOG", "ConnectedThread -> run : step2");
//                                counter = 0;
//                                mkState = MkState.WAIT_FOR_FULL_CHART;
//                                chartDataModel.setFrontData(frontArrayList);
//                                frontArrayList = new ArrayList<>();
//                            }
//                            break;
//                        case WAIT_FOR_FULL_CHART:
//                            for (byte b : data) {
//                                fullDataArrayList.add(b);
//                            }
//                            if (chartDataModel != null && counter >= chartDataModel.getFullChartLength()) {
//                                Log.d("LOG", "ConnectedThread -> run : step3");
//                                counter = 0;
//                                chartDataModel.setFullData(fullDataArrayList);
//                                fullDataArrayList = new ArrayList<>();
//                                mkState = MkState.WAIT_FOR_X_RAY;
//                            }
//                            break;
//                       }
//                    }

                    //mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, counter, data)
                    //      .sendToTarget();
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
        public void write(byte[] buffer) {
            Log.d("LOG", "ConnectedThread -> write : go");

            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                Log.e("LOG", "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("LOG", "close() of connect socket failed", e);
            }
        }
    }
}
