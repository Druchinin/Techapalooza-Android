package com.consultica.techapalooza.ui.fragments.authentication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.DialogFactory;
import com.consultica.techapalooza.utils.FontFactory;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.RegistrationFragment";

    private View view;
    private ImageView mRegNameImageCheck, mRegEmailImageCheck, mRegPswImageCheck;
    private EditText mEtName, mEtEmail, mEtPsw;
    private TextView mTvHidePsw, reg_tv_name, reg_tv_email, reg_tv_password;
    private Button mBtnSignUp;
    private boolean isShownPsw = true;
    private boolean isNameValid, isEmailValid, isPswVaild;

    private Typeface typeface;

    private static RegistrationFragment instance;

    public static RegistrationFragment getInstance() {
        if (instance == null)
            instance = new RegistrationFragment();
        return instance;
    }

    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);

        activity = (AppCompatActivity) getActivity();
        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        init();

        return view;
    }

    private void init() {
        setupCheckImages();
        setupHidePasswordBtn();

        reg_tv_name = (TextView) view.findViewById(R.id.reg_tv_name);
        reg_tv_name.setTypeface(typeface);

        reg_tv_email = (TextView) view.findViewById(R.id.reg_tv_email);
        reg_tv_email.setTypeface(typeface);

        reg_tv_password = (TextView) view.findViewById(R.id.reg_tv_password);
        reg_tv_password.setTypeface(typeface);

        mEtName = (EditText) view.findViewById(R.id.reg_et_name);
        mEtName.setTypeface(typeface);

        mEtEmail = (EditText) view.findViewById(R.id.reg_et_email);
        mEtEmail.setTypeface(typeface);

        mEtPsw = (EditText) view.findViewById(R.id.reg_et_psw);
        mEtPsw.setTypeface(typeface);

        setupInput();

        mBtnSignUp = (Button) view.findViewById(R.id.btn_reg_sign_up);
        mBtnSignUp.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));

        setupBtnSignUp();
    }

    private void setupInput() {

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

        mEtPsw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyboard();
                }

                return false;
            }
        });
    }


    private void setupBtnSignUp() {

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameValid && isEmailValid && isPswVaild) {
                    DialogFactory.showProgressDialog(getActivity());
                    hideSoftKeyboard();
                    signIn();
                } else {
                    Toast.makeText(getActivity(), "Input error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn() {

        mBtnSignUp.setEnabled(false);

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

                                saveCookie(response);
                                saveCurrentUser(signInResponse);

                                DialogFactory.hideProgressDialog();

                                Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_LONG).show();

                                activity.setResult(Activity.RESULT_OK);
                                activity.finish();

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(activity, "Sign-up failed", Toast.LENGTH_SHORT).show();
                                DialogFactory.hideProgressDialog();
                            }
                        });

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("SignInResponse", "Error: " + error.getMessage());
                        Toast.makeText(activity, "Sign-up failed", Toast.LENGTH_SHORT).show();
                        activity.setResult(Activity.RESULT_CANCELED);
                        activity.finish();
                    }
                });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void saveCookie(Response response) {
        if (Interceptor.getInstance().getCookie() == null) {
            Interceptor.getInstance().buildUserIdCookieFromString(response.getHeaders());
        }
    }

    private void saveCurrentUser(SignInResponse signInResponse) {
        FakeDB.getInstance(activity).saveEmail(signInResponse.getUserEmail());
        FakeDB.getInstance(activity).saveUserId(signInResponse.getUserId());
        FakeDB.getInstance(activity).savePassword(mEtPsw.getText().toString());
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
        return s.length() > 2 && s.length() < 32;
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

    @Override
    public String getName() {
        return RegistrationFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.registration_container;
    }
}
