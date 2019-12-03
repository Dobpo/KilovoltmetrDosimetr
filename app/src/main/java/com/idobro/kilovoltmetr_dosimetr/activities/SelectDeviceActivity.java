package com.idobro.kilovoltmetr_dosimetr.activities;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.BluetoothDevices;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDeviceActivity extends AppCompatActivity {
    public final static String DEVICES = "devices";
    public final static String SELECTED_DEVICE = "selected_device_address";
    public final static int GET_DEVICE_REQUEST = 1;

    @BindView(R.id.devicesListView)
    ListView devicesListView;

    @BindView(R.id.header)
    TextView header;

    private ArrayList<BluetoothDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_device);
        ButterKnife.bind(this);

        devicesListView.setOnItemClickListener(new OnDeviceClickListener());

        Intent intent = getIntent();
        BluetoothDevices devices = intent.getParcelableExtra(DEVICES);
        if (devices != null) {
            deviceList = devices.getDevices();
            initListView(deviceList);
        }
    }

    private void initListView(ArrayList<BluetoothDevice> deviceList) {
        if (deviceList.size() > 0) {
            ArrayAdapter<BluetoothDevice> listAdapter = new ArrayAdapter<BluetoothDevice>(this, 0, deviceList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    BluetoothDevice device = deviceList.get(position);
                    if (convertView == null)
                        convertView = getLayoutInflater().inflate(R.layout.device_list_item,
                                parent, false);
                    TextView text1 = convertView.findViewById(R.id.nameText);
                    TextView text2 = convertView.findViewById(R.id.macText);
                    text1.setText(device.getName());
                    text2.setText(device.getAddress());
                    return convertView;
                }
            };
            devicesListView.setAdapter(listAdapter);
        } else {
            header.setText(this.getString(R.string.no_device_found));
        }
    }

    private class OnDeviceClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.putExtra(SELECTED_DEVICE, deviceList.get(position).getAddress());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}