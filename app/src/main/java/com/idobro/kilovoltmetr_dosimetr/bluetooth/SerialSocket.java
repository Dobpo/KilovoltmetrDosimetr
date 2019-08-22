package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.idobro.kilovoltmetr_dosimetr.BuildConfig;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;

public class SerialSocket implements Runnable {
    private static final UUID BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";

    private final BroadcastReceiver disconnectBroadcastReceiver;

    private Context context;
    private SerialListener listener;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private boolean connected;

    public SerialSocket() {
        disconnectBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (listener != null)
                    listener.onSerialIoError(new IOException("background disconnect"));
                disconnect();
            }
        };
    }

    /**
     * connect-success and most connect-errors are returned asynchronously to listener
     */
    public void connect(Context context, SerialListener listener, BluetoothDevice device) throws IOException {
        if (connected || socket != null)
            throw new IOException("already connected");
        this.context = context;
        this.listener = listener;
        this.device = device;
        context.registerReceiver(disconnectBroadcastReceiver, new IntentFilter(INTENT_ACTION_DISCONNECT));
        Executors.newSingleThreadExecutor().submit(this);
    }


    public void disconnect() {
        listener = null; // ignore remaining data and errors
        // connected = false; // run loop will reset connected
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
            socket = null;
        }
        try {
            context.unregisterReceiver(disconnectBroadcastReceiver);
        } catch (Exception ignored) {
        }
    }

    public void write(byte[] data) throws IOException {
        if (!connected)
            throw new IOException("not connected");
        socket.getOutputStream().write(data);
    }

    @Override
    public void run() {
        try {
            socket = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP);
            socket.connect();
            if (listener != null)
                listener.onSerialConnect();
        } catch (Exception e) {
            if (listener != null)
                listener.onSerialConnectError(e);
            try {
                socket.close();
            } catch (Exception ignored) {
            }
            socket = null;
            return;
        }
        connected = true;
        try {
            byte[] buffer = new byte[1024];
            int len;
            //noinspection InfiniteLoopStatement
            while (true) {
                len = socket.getInputStream().read(buffer);
                Log.d("LOG", this.getClass().getSimpleName() + "*** len = " + len + "***");
                for (int i = 0; i < len; i++) {
                    Log.d("LOG", this.getClass().getSimpleName() + " byte = " + buffer[i]);
                }
                Log.d("LOG", "**************************************");

                //byte[] data = Arrays.copyOf(buffer, len);
                if (listener != null)
                    listener.onSerialRead(buffer, len);
            }
        } catch (Exception e) {
            Log.d("LOG", "SerialSocket -> run : Error: " + e.getMessage());

            connected = false;
            if (listener != null)
                listener.onSerialIoError(e);
            try {
                socket.close();
            } catch (Exception ignored) {

            }
            socket = null;
        }
    }
}
