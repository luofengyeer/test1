package com.cmcc.internalcontact.base;

import com.cmcc.internalcontact.utils.Constant;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class MyObserver<T> implements Observer<T> {
    private OnTokenValidListener onTokenValidListener;

    public MyObserver(OnTokenValidListener onTokenValidListener) {
        this.onTokenValidListener = onTokenValidListener;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if(Constant.EXCEPTION_TOKEN_INVALID.equals(e.getMessage())){
            if (onTokenValidListener != null) {
                onTokenValidListener.onTokenValid();
            }
        }
    }

    @Override
    public void onComplete() {

    }
}
