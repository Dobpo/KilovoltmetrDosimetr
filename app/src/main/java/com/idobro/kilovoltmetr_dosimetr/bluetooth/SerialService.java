package com.idobro.kilovoltmetr_dosimetr.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Queue;

public class SerialService extends Service implements SerialListener {
    private final Handler mainLooper;
    private final IBinder binder;
    private final Queue<QueueItem> queue1;
    private final Queue<QueueItem> queue2;

    private SerialListener listener;
    private boolean isConnected;

    public SerialService() {
        mainLooper = new Handler(Looper.getMainLooper());
        binder = new SerialBinder();
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    public void connect(SerialListener listener, String notificationMsg) {
        this.listener = listener;
        isConnected = true;
    }

    public void disconnect() {
        listener = null;
        isConnected = false;
    }

    public void attach(SerialListener listener) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread())
            throw new IllegalArgumentException("not in main thread");
        // use synchronized() to prevent new items in queue2
        // new items will not be added to queue1 because mainLooper.post and attach() run in main thread
        if (isConnected) {
            synchronized (this) {
                this.listener = listener;
            }
        }
        for (QueueItem item : queue1) {
            switch (item.type) {
                case Connect:
                    listener.onSerialConnect();
                    break;
                case ConnectError:
                    listener.onSerialConnectError(item.e);
                    break;
                case Read:
                    listener.onSerialRead(item.data);
                    break;
                case IoError:
                    listener.onSerialIoError(item.e);
                    break;
            }
        }
        for (QueueItem item : queue2) {
            switch (item.type) {
                case Connect:
                    listener.onSerialConnect();
                    break;
                case ConnectError:
                    listener.onSerialConnectError(item.e);
                    break;
                case Read:
                    listener.onSerialRead(item.data);
                    break;
                case IoError:
                    listener.onSerialIoError(item.e);
                    break;
            }
        }
        queue1.clear();
        queue2.clear();
    }

    public void detach() {
        if (isConnected)
        // items already in event queue (posted before detach() to mainLooper) will end up in queue1
        // items occurring later, will be moved directly to queue2
        // detach() and mainLooper.post run in the main thread, so all items are caught
        listener = null;
    }

    @Override
    public void onSerialConnect() {
        if (isConnected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialConnect();
                        } else {
                            queue1.add(new QueueItem(QueueType.Connect, null, null));
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.Connect, null, null));
                }
            }
        }
    }

    @Override
    public void onSerialConnectError(Exception e) {
        if (isConnected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialConnectError(e);
                        } else {
                            queue1.add(new QueueItem(QueueType.ConnectError, null, e));
                            disconnect();
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.ConnectError, null, e));
                    disconnect();
                }
            }
        }
    }

    @Override
    public void onSerialRead(byte[] data) {
        if (isConnected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialRead(data);
                        } else {
                            queue1.add(new QueueItem(QueueType.Read, data, null));
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.Read, data, null));
                }
            }
        }
    }

    @Override
    public void onSerialIoError(Exception e) {
        if (isConnected) {
            synchronized (this) {
                if (listener != null) {
                    mainLooper.post(() -> {
                        if (listener != null) {
                            listener.onSerialIoError(e);
                        } else {
                            queue1.add(new QueueItem(QueueType.IoError, null, e));
                            disconnect();
                        }
                    });
                } else {
                    queue2.add(new QueueItem(QueueType.IoError, null, e));
                    disconnect();
                }
            }
        }
    }

    public class SerialBinder extends Binder {
        public SerialService getService() {
            return SerialService.this;
        }
    }

    private enum QueueType {Connect, ConnectError, Read, IoError}

    private class QueueItem {
        QueueType type;
        byte[] data;
        Exception e;

        QueueItem(QueueType type, byte[] data, Exception e) {
            this.type = type;
            this.data = data;
            this.e = e;
        }
    }
}
