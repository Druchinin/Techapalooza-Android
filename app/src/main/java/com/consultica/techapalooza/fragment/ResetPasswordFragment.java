package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.consultica.techapalooza.R;

public class ResetPasswordFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.ResetPasswordFragment";
    private View view;
    private TextView tv_reset_info;
    private String email;
    private EditText mEtEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        mEtEmail = (EditText) view.findViewById(R.id.reg_et_name);

        tv_reset_info = (TextView) view.findViewById(R.id.tv_reset_info);
        tv_reset_info.setVisibility(View.GONE);
        final Button mBtnResetPsw = (Button) view.findViewById(R.id.btn_tickets_reset_psw);
        mBtnResetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_reset_info.setText("Check your email for instructions");
                tv_reset_info.setVisibility(View.VISIBLE);
                mBtnResetPsw.setEnabled(false);
            }
        });

        return view;
    }

    public void setEmailForRestore(String email){
        this.email = email;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (email!= null) mEtEmail.setText(email);
    }
}
