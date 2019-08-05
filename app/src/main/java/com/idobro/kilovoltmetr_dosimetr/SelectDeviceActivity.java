package com.idobro.kilovoltmetr_dosimetr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

public class SelectDeviceActivity extends AppCompatActivity {

    ListView devices_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initUI();
        setContentView(R.layout.activity_select_device);
    }

    private void initUI() {
        devices_list_view = findViewById(R.id.devices_list_view);
    }
}
