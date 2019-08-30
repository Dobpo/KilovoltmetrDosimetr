package com.idobro.kilovoltmetr_dosimetr.activities.core;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.idobro.kilovoltmetr_dosimetr.R;
import com.idobro.kilovoltmetr_dosimetr.fragments.MainFragmentImpl;
import com.idobro.kilovoltmetr_dosimetr.fragments.core.MainFragment;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStartFragment();
    }

    private void setStartFragment() {
        List<Fragment> existFragments = getSupportFragmentManager()
                .getFragments();
        if ((existFragments.isEmpty())) {
            this.fragment = new MainFragmentImpl();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(getIdFragmentContainer(), fragment, MainFragmentImpl.class.getSimpleName())
                    .commit();
        } else {
            if (existFragments.get(0) instanceof MainFragment)
                fragment = existFragments.get(0);
        }
    }

    private int getIdFragmentContainer() {
        return R.id.fragment_container;
    }

    protected MainFragment getMainFragment() {
        return (MainFragment) fragment;
    }

    protected void addFragmentToContainer(Fragment fragment) {
        Fragment sameFragment = getSupportFragmentManager()
                .findFragmentByTag(fragment.getClass().getSimpleName());
        if (!(sameFragment != null && sameFragment.isAdded())) {
            if (!(fragment instanceof MainFragment)) {
                this.fragment = null;
            } else {
                this.fragment = fragment;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getIdFragmentContainer(), fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    protected boolean isMainFragmentExist() {
        return fragment != null;
    }
}
