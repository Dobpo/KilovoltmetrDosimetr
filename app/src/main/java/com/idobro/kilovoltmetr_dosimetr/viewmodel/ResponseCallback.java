package com.idobro.kilovoltmetr_dosimetr.viewmodel;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.ChartDataModel;

public interface ResponseCallback<T> {
    void onSuccess(T response);
    void onError(Error error);
}
