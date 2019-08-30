package com.idobro.kilovoltmetr_dosimetr.fragments;

import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.BaseFragment;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.MainFragment;

public class MainFragmentImpl extends BaseFragment implements MainFragment {
    private TextView text_view;
    private ProgressBar progress_bar;
    private Handler handler = new Handler();
    private int progressStatus = 0;

    @Override
    protected int getResourceID() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initUI(View rootView) {
        text_view = rootView.findViewById(R.id.text_view);
        if (getArguments() != null)
        text_view.setText(getArguments().getString(MainFragment.MESSAGE));
        progress_bar = rootView.findViewById(R.id.progress_bar);
    }

    @Override
    public void onDisconnect() {
        text_view.setText(R.string.connect_the_sensor);
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onConnecting() {
        text_view.setText(R.string.connecting);
        progress_bar.setIndeterminate(true);
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void waitForNewMeasure() {
        text_view.setText(R.string.enable_new_measure);
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onXRay() {
        text_view.setText(R.string.loading_data);
        progress_bar.setIndeterminate(false);
        progress_bar.setProgress(0);
        progress_bar.setMax(1000);
        progress_bar.setVisibility(View.VISIBLE);
        progressStatus = 0;

        new Thread(() -> {
            while(progressStatus < 1000){
                progressStatus +=1;

                try{
                    Thread.sleep(15);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                handler.post(() -> progress_bar.setProgress(progressStatus));
            }
        }).start();
    }

    @Override
    public void waitForXRay() {
        text_view.setText(R.string.waiting_for_xray);
        progress_bar.setVisibility(View.GONE);
    }
}
