package com.idobro.kilovoltmetr_dosimetr.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.idobro.kilovoltmetr_dosimetr.ui.CircularProgress;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.MainActivityViewModel;
import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothDevices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();
    private TextView status_text_view;
    private CircularProgress progressBar;
    private LinearLayout charts_container;
    private RelativeLayout content_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        status_text_view = findViewById(R.id.status_text_view);
        progressBar = findViewById(R.id.progress_bar);
        charts_container = findViewById(R.id.charts_container_linear_layout);
        content_layout = findViewById(R.id.content_relative_layout);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getServerResponseLiveData().observe(this, new OnDataChartReceivedListener());
        viewModel.getStatusLiveData().observe(this, new OnStatusChangeListener());
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
    protected void onPause() {
        super.onPause();
        viewModel.setInitStart(false);
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
                viewModel.disconnect();
                Log.d("LOG", this.getClass().getSimpleName() + " selected device -> " +
                        data.getStringExtra(SelectDeviceActivity.SELECTED_DEVICE));
                viewModel.connect(bluetoothAdapter.getRemoteDevice(data.getStringExtra
                        (SelectDeviceActivity.SELECTED_DEVICE)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class OnDataChartReceivedListener implements Observer<String> {

        @Override
        public void onChanged(String s) {
            Log.d("LOG", "OnDataChartReceivedList -> onChanged : data was received");
        }
    }

    class OnStatusChangeListener implements Observer<MainActivityViewModel.Connected> {

        @Override
        public void onChanged(MainActivityViewModel.Connected status) {
            switch (status) {
                case False:
//                    if (!viewModel.isInitStart()) {
//                        charts_container.setVisibility(View.GONE);
//                    }
                    status_text_view.setText(R.string.disconnected);
                    break;
                case Pending:
                    // TODO: 15.08.2019 Show progress bar
                    status_text_view.setText(R.string.connecting);
                    progressBar.setVisibility(View.VISIBLE);
                    content_layout.setBackgroundColor(getResources().getColor(R.color.secondaryLightColor));
                    charts_container.setVisibility(View.GONE);
                    break;
                case True:
                    charts_container.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    content_layout.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                    status_text_view.setText(R.string.connected);
                    break;
            }
        }
    }
}