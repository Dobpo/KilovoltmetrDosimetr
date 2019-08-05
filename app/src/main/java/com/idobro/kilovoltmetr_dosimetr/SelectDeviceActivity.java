package com.idobro.kilovoltmetr_dosimetr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectDeviceActivity extends AppCompatActivity {

    private ListView devices_list_view;
    private ArrayAdapter<BluetoothDevice> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initUI();
        setContentView(R.layout.activity_select_device);

        Intent intent = getIntent();
        BluetoothDevices devices = intent.getParcelableExtra("devices");
        ArrayList<BluetoothDevice> deviceList = devices.getDevices();

        if (deviceList.size() > 0){
            listAdapter = new ArrayAdapter<BluetoothDevice>(this, 0, deviceList){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                   BluetoothDevice device = deviceList.get(position);
                   if(convertView == null)
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
        }else{
            Log.d("LOG", this.getClass().getSimpleName() + " No devices");
        }
    }

    private void initUI() {
        devices_list_view = findViewById(R.id.devices_list_view);
    }
}
