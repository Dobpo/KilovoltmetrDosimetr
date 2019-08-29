package com.idobro.kilovoltmetr_dosimetr.fragments;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.BaseFragment;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.MainFragment;

public class MainFragmentImpl extends BaseFragment implements MainFragment {
    private TextView text_view;
    private ProgressBar progress_bar;

    @Override
    protected int getResourceID() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initUI(View rootView) {
        text_view = rootView.findViewById(R.id.text_view);
        progress_bar = rootView.findViewById(R.id.progress_bar);
    }

    @Override
    public void setText(String string) {
        text_view.setText(string);
    }

    @Override
    public void doSomething() {
        progress_bar.setIndeterminate(true);
    }

    @Override
    public void doSomethingElse() {
        progress_bar.setIndeterminate(false);
        progress_bar.setProgress(20);
    }
}
