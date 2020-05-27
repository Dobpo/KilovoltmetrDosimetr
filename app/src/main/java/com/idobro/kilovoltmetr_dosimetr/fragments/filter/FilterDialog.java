package com.idobro.kilovoltmetr_dosimetr.fragments.filter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.idobro.kilovoltmetr_dosimetr.base.BaseDialog;

public class FilterDialog extends BaseDialog {
    private FilterDialog(@NonNull Context context) {
        super(context);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FilterDialog.class);
        //starter.putExtra();
        context.startActivity(starter);
    }
}