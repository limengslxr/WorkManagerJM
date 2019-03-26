package com.sh3h.dataprovider.data.remote;


import android.os.Handler;
import android.os.Looper;

import com.sh3h.dataprovider.data.BusEvent;
import com.squareup.otto.Bus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


class UnauthorisedInterceptor implements Interceptor {
    private Bus mBus;

    UnauthorisedInterceptor(Bus bus) {
        mBus = bus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        response = chain.proceed(chain.request());
        //try {
        if (response.code() == 401) {
            new Handler(Looper.getMainLooper()).post(() -> mBus.post(new BusEvent.AuthenticationError()));
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return response;
    }
}
