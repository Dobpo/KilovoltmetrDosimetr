package com.idobro.kilovoltmetr_dosimetr.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected static View generateView(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    protected Context getHolderContext() {
        return itemView.getContext();
    }
}