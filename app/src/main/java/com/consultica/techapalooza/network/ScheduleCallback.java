package com.consultica.techapalooza.network;

import android.widget.Toast;

import com.consultica.techapalooza.App;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class ScheduleCallback<T> implements Callback<T> {
    @Override
    public void success(T o, Response response) {
        if (response.getStatus() / 10 == 20) {
            success(o);
        } else {
            Toast.makeText(App.getInstance(), "Error connect. Please, try later.", Toast.LENGTH_SHORT).show();
        }
    }

    public abstract void success(T response);

    @Override
    public void failure(RetrofitError error) {

    }
}
