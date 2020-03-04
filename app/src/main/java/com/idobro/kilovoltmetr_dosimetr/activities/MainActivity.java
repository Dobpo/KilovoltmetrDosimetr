package com.idobro.kilovoltmetr_dosimetr.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.custom_views.BluetoothStatusView;
import com.idobro.kilovoltmetr_dosimetr.custom_views.BluetoothStatusView.State;
import com.idobro.kilovoltmetr_dosimetr.database.entities.Graph;
import com.idobro.kilovoltmetr_dosimetr.fragments.ChartsFragment;
import com.idobro.kilovoltmetr_dosimetr.fragments.MainFragment;
import com.idobro.kilovoltmetr_dosimetr.fragments.MainFragmentImpl;
import com.idobro.kilovoltmetr_dosimetr.fragments.graph_dialog.GetGraphDialog;
import com.idobro.kilovoltmetr_dosimetr.models.BluetoothDevices;
import com.idobro.kilovoltmetr_dosimetr.models.GraphsDates;
import com.idobro.kilovoltmetr_dosimetr.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements GetGraphDialog.OnGraphSelectedListener {
    @BindView(R.id.statusTextView)
    TextView statusTextView;

    @BindView(R.id.bluetoothStatusImageView)
    BluetoothStatusView bluetoothStatusImageView;

    @BindView(R.id.contentRelativeLayout)
    RelativeLayout contentRelativeLayout;

    @BindView(R.id.newMeasureImageButton)
    AppCompatImageButton newMeasureImageButton;

    private MainActivityViewModel viewModel;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();
    private MutableLiveData<Graph> graphLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(findViewById(R.id.toolbar));
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        graphLiveData = new MutableLiveData<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getServerResponseLiveData().observe(this, this::showGraph);
        viewModel.getStatusLiveData().observe(this, new OnStatusChangeListener());

        viewModel.showImportantData();
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

    public LiveData<Graph> getGraphLiveData() {
        return graphLiveData;
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
                startMeasure();
                return true;
            case R.id.get_battery_charge:
                viewModel.getBatteryCharge();
                return true;
            case R.id.save_chart:
                viewModel.saveChart();
                return true;
            case R.id.chart_count:
                viewModel.showSavedChartsCount();
                return true;
            case R.id.load_graph:
                viewModel.getGraphsDates().observe(this, this::showGraphSelectDialog);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showGraphSelectDialog(List<GraphsDates> graphsDates) {
        GetGraphDialog.start(this, graphsDates, this);
    }

    @OnClick(R.id.newMeasureImageButton)
    void startMeasure() {
        viewModel.enableNewMeasure();
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

    private void showGraph(Graph graph) {
        addFragmentToContainer(ChartsFragment.start(graph));
    }

    @Override
    public void onGraphSelected(long id) {
        viewModel.getGraphById(id).observe(this, this::showGraph);
    }

    class OnStatusChangeListener implements Observer<MainActivityViewModel.SocketStatus> {
        @Override
        public void onChanged(MainActivityViewModel.SocketStatus status) {
            switch (status) {
                case DISCONNECT:
                    statusTextView.setText(R.string.disconnected);
                    bluetoothStatusImageView.setState(State.DISCONNECTED);
                    Toast.makeText(MainActivity.this, "Connect with sensor was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PENDING:
                    statusTextView.setText(R.string.connecting);
                    bluetoothStatusImageView.setState(State.CONNECTING);
                    if (isMainFragmentExist())
                        getMainFragment().onConnecting();
                    break;
                case COULD_NOT_CONNECT:
                    statusTextView.setText(R.string.disconnected);
                    bluetoothStatusImageView.setState(State.DISCONNECTED);
                    Toast.makeText(MainActivity.this, "Couldn't connect to sensor",
                            Toast.LENGTH_SHORT).show();
                    if (isMainFragmentExist())
                        getMainFragment().onDisconnect();
                    break;
                case CONNECTED:
                    statusTextView.setText(R.string.connected);
                    bluetoothStatusImageView.setState(State.CONNECTED);
                    contentRelativeLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(),
                            R.color.secondaryColor));
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