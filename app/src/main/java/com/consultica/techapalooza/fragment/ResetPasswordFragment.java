package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.consultica.techapalooza.R;

public class ResetPasswordFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.ResetPasswordFragment";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button mBtnResetPsw = (Button) view.findViewById(R.id.btn_tickets_reset_psw);
        mBtnResetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "Check your email for instructions", Snackbar.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
