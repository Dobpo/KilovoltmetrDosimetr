package com.idobro.kilovoltmetr_dosimetr.viewmodel;

public interface ResponseCallback<T> {
    void onSuccess(T response);

    void onError(Error error);
}