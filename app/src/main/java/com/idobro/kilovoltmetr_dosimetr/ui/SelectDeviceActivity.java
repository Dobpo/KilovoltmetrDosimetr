package com.idobro.kilovoltmetr_dosimetr.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.bluetooth.BluetoothDevices;

import java.util.ArrayList;

public class SelectDeviceActivity extends AppCompatActivity{

    public final static String DEVICES = "devices";
    public final static String SELECTED_DEVICE = "selected_device_address";
    public final static int GET_DEVICE_REQUEST = 1;

    private ListView devices_list_view;
    private TextView header;
    private ArrayAdapter<BluetoothDevice> listAdapter;
    private ArrayList<BluetoothDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_device);

        devices_list_view = findViewById(R.id.devices_list_view);
        header = findViewById(R.id.header);

        devices_list_view.setOnItemClickListener(new OnDeviceClickListener());

        Intent intent = getIntent();
        BluetoothDevices devices = intent.getParcelableExtra(DEVICES);
        deviceList = devices.getDevices();
        initListView(deviceList);
    }

    private void initListView(ArrayList<BluetoothDevice> deviceList){
        if (deviceList.size() > 0) {
            listAdapter = new ArrayAdapter<BluetoothDevice>(this, 0, deviceList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    BluetoothDevice device = deviceList.get(position);
                    if (convertView == null)
                        convertView = getLayoutInflater().inflate(R.layout.device_list_item,
                                parent, false);
                    TextView text1 = convertView.findViewById(R.id.text1);
                    TextView text2 = convertView.findViewById(R.id.text2);
                    text1.setText(device.getName());
                    text2.setText(device.getAddress());
                    return convertView;
                }
            };
            devices_list_view.setAdapter(listAdapter);
        } else {
            header.setText(this.getString(R.string.no_device_found));
        }
    }

    private class OnDeviceClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.putExtra(SELECTED_DEVICE, deviceList.get(position).getAddress());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
