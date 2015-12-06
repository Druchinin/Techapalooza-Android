package com.consultica.techapalooza.ui.fragments.authentication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.DialogFactory;
import com.consultica.techapalooza.utils.FontFactory;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.LoginFragment";

    private View view;

    private Typeface typeface;

    private EditText mEmail, mEtPassword;

    private TextView mHidePass, tv_tickets_login_email, tv_tickets_login_password;

    private boolean isShownPsw = true;

    private static LoginFragment instance;

    public static LoginFragment getInstance() {
        if (instance == null)
            instance = new LoginFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_login, container, false);

        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        init();

        return view;
    }

    private void init() {

        tv_tickets_login_email = (TextView) view.findViewById(R.id.tv_tickets_login_email);
        tv_tickets_login_email.setTypeface(typeface);
        tv_tickets_login_email.setFocusable(false);

        tv_tickets_login_password = (TextView) view.findViewById(R.id.tv_tickets_login_password);
        tv_tickets_login_password.setTypeface(typeface);
        tv_tickets_login_password.setFocusable(false);

        mEmail = (EditText) view.findViewById(R.id.reg_et_name);
        mEmail.setTypeface(typeface);
        mEmail.setFocusable(true);

        mEtPassword = (EditText) view.findViewById(R.id.et_tickets_login_password);
        mEtPassword.setTypeface(typeface);

        mHidePass = (TextView) view.findViewById(R.id.tv_hide_psw);
        mHidePass.setTypeface(typeface);
        mHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShownPsw) {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShownPsw = false;
                    mHidePass.setText("SHOW");
                } else {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isShownPsw = true;
                    mHidePass.setText("HIDE");
                }
                mEtPassword.setSelection(mEtPassword.getText().length());
            }
        });

        Button btnSignIn = (Button) view.findViewById(R.id.btn_tickets_login_sign_in);
        btnSignIn.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard();

                DialogFactory.showProgressDialog(getActivity());

                Client.getAPI().signIn(mEmail.getText().toString(), mEtPassword.getText().toString(), new Callback<SignInResponse>() {
                    @Override
                    public void success(SignInResponse signInResponse, Response response) {
                        Log.d("LogIn", "Status: " + response.getStatus());

                        saveCookie(response);

                        FakeDB.getInstance(getActivity()).savePassword(mEtPassword.getText().toString());

                        FakeDB.getInstance(getActivity()).saveEmail(signInResponse.getUserEmail());
                        FakeDB.getInstance(getActivity()).saveUserId(signInResponse.getUserId());

                        DialogFactory.hideProgressDialog();

                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();

                        AppCompatActivity activity = (AppCompatActivity) getActivity();

                        activity.setResult(Activity.RESULT_OK);
                        activity.finish();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("LogIn error", "Status: " + error.getMessage());
                        DialogFactory.hideProgressDialog();
                        Toast.makeText(getActivity(), "Login error. Please check email and password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveCookie(Response response) {
        if (Interceptor.getInstance().getCookie() == null) {
            Interceptor.getInstance().buildUserIdCookieFromString(response.getHeaders());
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showSoftKeyboard() {
        mEmail.requestFocus();
        InputMethodManager imm = (InputMethodManager) mEmail.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public String getName() {
        return LoginFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.login_container;
    }

}
