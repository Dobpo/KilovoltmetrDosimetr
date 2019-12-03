package com.idobro.kilovoltmetr_dosimetr.custom_views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.idobro.kilovoltmetr_dosimetr.R;

public class BluetoothStatusView extends AppCompatImageView {
    private State state;

    public BluetoothStatusView(Context context) {
        super(context);
    }

    public BluetoothStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        switch (state) {
            case CONNECTED:
                setImageResource(R.drawable.ic_bluetooth_connected);
                break;
            case CONNECTING:
                setImageResource(R.drawable.ic_bluetooth_searching);
                break;
            case DISCONNECTED:
                setImageResource(R.drawable.ic_bluetooth_disabled);
                break;
            default:
                break;
        }
    }

    public enum State {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }
}