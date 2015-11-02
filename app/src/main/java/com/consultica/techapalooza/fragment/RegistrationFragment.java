package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.RegistrationFragment";

    private View view;
    private ImageView mRegNameImageCheck, mRegEmailImageCheck, mRegPswImageCheck;
    private EditText mEtName, mEtEmail, mEtPsw;
    private TextView mTvHidePsw;
    private Button mBtnSignUp;
    private boolean isShownPsw = true;

    private boolean isNameValid, isEmailValid, isPswVaild;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);

        setupCheckImages();
        setupInput();

        return view;
    }

    private void setupInput() {
        setupCheckImages();
        setupHidePasswordBtn();

        mEtName = (EditText) view.findViewById(R.id.reg_et_name);
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isNameValid(s.toString())) {
                    mRegNameImageCheck.setImageResource(R.mipmap.ic_action_pass);
                    mRegNameImageCheck.setVisibility(View.VISIBLE);
                    isNameValid = true;
                } else {
                    mRegNameImageCheck.setImageResource(R.mipmap.ic_action_fail);
                    mRegNameImageCheck.setVisibility(View.VISIBLE);
                    isNameValid = false;
                }
            }
        });

        mEtEmail = (EditText) view.findViewById(R.id.reg_et_email);
        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEmailValid(s.toString())) {
                    mRegEmailImageCheck.setImageResource(R.mipmap.ic_action_pass);
                    mRegEmailImageCheck.setVisibility(View.VISIBLE);
                    isEmailValid = true;
                } else {
                    mRegEmailImageCheck.setImageResource(R.mipmap.ic_action_fail);
                    mRegEmailImageCheck.setVisibility(View.VISIBLE);
                    isEmailValid = false;
                }
            }
        });

        mEtPsw = (EditText) view.findViewById(R.id.reg_et_psw);
        mEtPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    if (isPassValid(s.toString())) {
                        mRegPswImageCheck.setImageResource(R.mipmap.ic_action_pass);
                        mRegPswImageCheck.setVisibility(View.VISIBLE);
                        isPswVaild = true;
                    } else {
                        mRegPswImageCheck.setImageResource(R.mipmap.ic_action_fail);
                        mRegPswImageCheck.setVisibility(View.VISIBLE);
                        isPswVaild = false;
                    }
                } else {
                    mRegPswImageCheck.setImageResource(R.mipmap.ic_action_fail);
                    mRegPswImageCheck.setVisibility(View.VISIBLE);
                    isPswVaild = false;
                }
            }
        });

        mBtnSignUp = (Button) view.findViewById(R.id.btn_reg_sign_up);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameValid && isEmailValid && isPswVaild)
                    Client.getAPI().signUp(
                            mEtName.getText().toString(),
                            mEtEmail.getText().toString(),
                            mEtPsw.getText().toString(),
                            new Callback<SignInResponse>() {

                                @Override
                                public void success(SignInResponse signInResponse, Response response) {
                                    Client.getAPI().signIn(mEtEmail.getText().toString(), mEtPsw.getText().toString(), new Callback<SignInResponse>() {
                                        @Override
                                        public void success(SignInResponse signInResponse, Response response) {
                                            Log.d("LogIn", "Status: " + response.getStatus());

                                            if (Interceptor.getInstance().getCookie() == null) {
                                                Interceptor.getInstance().buildUserIdCookieFromString(response.getHeaders());
                                            }

                                            Toast.makeText(getActivity(), "Registration & Login successful", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.d("LogIn error", "Status: " + error.getMessage());
                                        }
                                    });
                                    Log.d("SignInResponse", "Status: " + response.getStatus());

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d("SignInResponse", "Error: "+error.getMessage());
                                    Toast.makeText(getActivity(), "Registration failed: "+error.getBody(), Toast.LENGTH_LONG).show();
                                }
                            });

            }
        });
    }

    private void setupHidePasswordBtn() {
        mTvHidePsw = (TextView) view.findViewById(R.id.reg_tv_hide_psw);
        mTvHidePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShownPsw) {
                    mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShownPsw = false;
                    mTvHidePsw.setText("SHOW");
                } else {
                    mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isShownPsw = true;
                    mTvHidePsw.setText("HIDE");
                }
                mEtPsw.setSelection(mEtPsw.getText().length());
            }
        });
    }

    private boolean isNameValid(String s) {
        return s.length()>4 && s.length() < 32;
    }

    private boolean isEmailValid(String s) {
        if (!TextUtils.isEmpty(s)) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
        }
        return false;
    }

    private boolean isPassValid(String s) {
        if (s.length() >= 6 || TextUtils.isEmpty(s.trim())) return true;
        return false;
    }

    private void setupCheckImages() {
        mRegNameImageCheck = (ImageView) view.findViewById(R.id.reg_name_image_check);
        mRegNameImageCheck.setVisibility(View.INVISIBLE);

        mRegEmailImageCheck = (ImageView) view.findViewById(R.id.reg_email_image_check);
        mRegEmailImageCheck.setVisibility(View.INVISIBLE);

        mRegPswImageCheck = (ImageView) view.findViewById(R.id.reg_psw_image_check);
        mRegPswImageCheck.setVisibility(View.INVISIBLE);
    }
}
