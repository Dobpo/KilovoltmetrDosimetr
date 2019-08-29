package com.idobro.kilovoltmetr_dosimetr.fragments;

import android.view.View;
import android.widget.TextView;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.BaseFragment;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.MainFragment;

public class MainFragmentImpl extends BaseFragment implements MainFragment {
    private TextView text_view;

    @Override
    protected int getResourceID() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initUI(View rootView) {
        text_view = rootView.findViewById(R.id.text_view);
    }

    @Override
    public void setText(String string) {
        text_view.setText(string);
    }
}
