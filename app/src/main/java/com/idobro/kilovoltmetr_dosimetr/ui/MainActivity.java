package com.idobro.kilovoltmetr_dosimetr.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothDevices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listItems = new ArrayList<>();
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    private void refreshDeviceList() {
        listItems.clear();
        if (bluetoothAdapter != null) {
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    listItems.add(device);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.bluetooth_devices:
                intent = new Intent(this, SelectDeviceActivity.class);
                refreshDeviceList();
                BluetoothDevices devices = new BluetoothDevices(listItems);
                intent.putExtra(SelectDeviceActivity.DEVICES, devices);
                startActivityForResult(intent, SelectDeviceActivity.GET_DEVICE_REQUEST);
                return true;
            case R.id.bluetooth_settings:
                intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SelectDeviceActivity.GET_DEVICE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                Log.d("LOG", this.getClass().getSimpleName() + " selected device -> " +
                        data.getStringExtra(SelectDeviceActivity.SELECTED_DEVICE));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}