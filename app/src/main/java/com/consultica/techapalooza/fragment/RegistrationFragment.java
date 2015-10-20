package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;

public class RegistrationFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.RegistrationFragment";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);

        return view;
    }
}
