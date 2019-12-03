package com.idobro.kilovoltmetr_dosimetr.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.idobro.kilovoltmetr_dosimetr.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragmentImpl extends BaseFragment implements MainFragment {
    @BindView(R.id.messageTextView)
    TextView messageTextView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Handler handler = new Handler();
    private int progressStatus = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null)
            messageTextView.setText(getArguments().getString(MainFragment.MESSAGE));
    }

    @Override
    public void onDisconnect() {
        messageTextView.setText(R.string.connect_the_sensor);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onConnecting() {
        messageTextView.setText(R.string.connecting);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void waitForNewMeasure() {
        messageTextView.setText(R.string.enable_new_measure);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onXRay() {
        messageTextView.setText(R.string.loading_data);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(0);
        progressBar.setMax(1000);
        progressBar.setVisibility(View.VISIBLE);
        progressStatus = 0;

        new Thread(() -> {
            while(progressStatus < 1000){
                progressStatus +=1;

                try{
                    Thread.sleep(15);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                handler.post(() -> progressBar.setProgress(progressStatus));
            }
        }).start();
    }

    @Override
    public void waitForXRay() {
        messageTextView.setText(R.string.waiting_for_xray);
        progressBar.setVisibility(View.GONE);
    }
}
