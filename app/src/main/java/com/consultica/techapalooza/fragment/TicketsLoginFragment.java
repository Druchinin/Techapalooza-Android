package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.consultica.techapalooza.R;


public class TicketsLoginFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoginFragment";

    private View view;
    private EditText mEmail, mEtPassword;
    private TextView mHidePass;
    private boolean isShownPsw = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_login, container, false);
        Log.d("Fragment Tickets:", "onCreateView()");

        mEmail = (EditText) view.findViewById(R.id.reg_et_name);

        mEtPassword = (EditText) view.findViewById(R.id.et_tickets_login_password);
        mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        mHidePass = (TextView) view.findViewById(R.id.tv_hide_psw);
        mHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShownPsw) {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShownPsw = false;
                } else {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isShownPsw = true;
                }
                mEtPassword.setSelection(mEtPassword.getText().length());
            }
        });

        TextView mForgotPassword = (TextView) view.findViewById(R.id.tv_tickets_forgot_psw);
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment resetFragment = new ResetPasswordFragment();
                if (mEmail.getText().toString() != null)
                resetFragment.setEmailForRestore(mEmail.getText().toString());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_tickets_container, resetFragment, ResetPasswordFragment.TAG);
                ft.addToBackStack(ResetPasswordFragment.TAG);
                ft.commit();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment Tickets:", "onResume()");

    }

    @Override
    public void onPause() {
        Log.d("Fragment Tickets:", "onPause()");

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.d("Fragment Tickets:", "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("Fragment Tickets:", "onDestroy()");
        super.onDestroyView();
    }
}
