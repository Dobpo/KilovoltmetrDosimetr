package com.idobro.kilovoltmetr_dosimetr.viewmodel;

import com.idobro.kilovoltmetr_dosimetr.bluetooth.entities.ChartDataModel;

public interface ResponseCallback {
    void onSuccess(Model model);
    void onError(Error error);
}
