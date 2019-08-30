package com.idobro.kilovoltmetr_dosimetr.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.Chart;
import com.idobro.kilovoltmetr_dosimetr.activities.core.BaseActivity;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.ChartDataModel;
import com.idobro.kilovoltmetr_dosimetr.fragments.ChartsFragment;
import com.idobro.kilovoltmetr_dosimetr.fragments.MainFragmentImpl;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.MainFragment;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.MainActivityViewModel;
import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.BluetoothDevices;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private MainActivityViewModel viewModel;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();
    private TextView status_text_view;

    private ImageView bluetooth_status_image_view;
    private RelativeLayout content_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        status_text_view = findViewById(R.id.status_text_view);
        bluetooth_status_image_view = findViewById(R.id.bluetooth_status_image_view);
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
            case R.id.new_measure_item:
                viewModel.enableNewMeasure();
                return true;
            case R.id.show_chart:
                Bundle bundle = new Bundle();
                bundle.putParcelable(ChartDataModel.CHARTS, new ChartDataModel());
                Fragment chartsFragment = new ChartsFragment();
                chartsFragment.setArguments(bundle);
                addFragmentToContainer(chartsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SelectDeviceActivity.GET_DEVICE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                viewModel.connect(bluetoothAdapter.getRemoteDevice(data.getStringExtra
                        (SelectDeviceActivity.SELECTED_DEVICE)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class OnDataChartReceivedListener implements Observer<ChartDataModel> {

        @Override
        public void onChanged(ChartDataModel charts) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ChartDataModel.CHARTS, charts);
            Fragment chartsFragment = new ChartsFragment();
            chartsFragment.setArguments(bundle);
            addFragmentToContainer(chartsFragment);
        }
    }

    class OnStatusChangeListener implements Observer<MainActivityViewModel.SocketStatus> {

        @Override
        public void onChanged(MainActivityViewModel.SocketStatus status) {
            switch (status) {
                case DISCONNECT:
                    status_text_view.setText(R.string.disconnected);
                    bluetooth_status_image_view.setImageResource(R.drawable.ic_bluetooth_disabled_24dp);
                    Toast.makeText(MainActivity.this, "Connect with sensor was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PENDING:
                    status_text_view.setText(R.string.connecting);
                    bluetooth_status_image_view.setImageResource(R.drawable.ic_bluetooth_searching_24dp);
                    if (isMainFragmentExist())
                        getMainFragment().onConnecting();
                    break;
                case COULD_NOT_CONNECT:
                    status_text_view.setText(R.string.disconnected);
                    bluetooth_status_image_view.setImageResource(R.drawable.ic_bluetooth_disabled_24dp);
                    Toast.makeText(MainActivity.this, "Couldn't connect to sensor",
                            Toast.LENGTH_SHORT).show();
                    if (isMainFragmentExist())
                        getMainFragment().onDisconnect();
                    break;
                case CONNECTED:
                    bluetooth_status_image_view.setImageResource(R.drawable.ic_bluetooth_connected_24dp);
                    content_layout.setBackgroundColor(ContextCompat.getColor(getBaseContext(),
                            R.color.secondaryColor));
                    status_text_view.setText(R.string.connected);
                    if (isMainFragmentExist())
                        getMainFragment().waitForNewMeasure();
                    break;
                case WAIT_X_RAY:
                    if (!isMainFragmentExist()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(MainFragment.MESSAGE, getString(R.string.waiting_for_xray));
                        Fragment mainFragment = new MainFragmentImpl();
                        mainFragment.setArguments(bundle);
                        addFragmentToContainer(mainFragment);
                    } else {
                        getMainFragment().waitForXRay();
                    }
                    break;
                case LOAD_CHART_DATA:
                    if (isMainFragmentExist())
                        getMainFragment().onXRay();
                    break;
            }
        }
    }
}