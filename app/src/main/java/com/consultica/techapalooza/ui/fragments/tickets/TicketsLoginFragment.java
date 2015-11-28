package com.consultica.techapalooza.ui.fragments.tickets;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.activities.LoginActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TicketsLoginFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoginFragment";

    private View view;
    private EditText mEmail, mEtPassword;
    private TextView mHidePass;
    private boolean isShownPsw = true;

    private static TicketsLoginFragment instance;

    public static TicketsLoginFragment getInstance() {
        if (instance == null)
            instance = new TicketsLoginFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_login, container, false);

        init();

        return view;
    }

    private void init() {
        mEmail = (EditText) view.findViewById(R.id.reg_et_name);

        mEtPassword = (EditText) view.findViewById(R.id.et_tickets_login_password);

        mHidePass = (TextView) view.findViewById(R.id.tv_hide_psw);
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
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard();

                Client.getAPI().signIn(mEmail.getText().toString(), mEtPassword.getText().toString(), new Callback<SignInResponse>() {
                    @Override
                    public void success(SignInResponse signInResponse, Response response) {
                        Log.d("LogIn", "Status: " + response.getStatus());

                        saveCookie(response);

                        FakeDB.getInstance(getActivity()).savePassword(mEtPassword.getText().toString());

                        FakeDB.getInstance(getActivity()).saveEmail(signInResponse.getUserEmail());
                        FakeDB.getInstance(getActivity()).saveUserId(signInResponse.getUserId());

                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();

                        AppCompatActivity activity = (AppCompatActivity) getActivity();

                        activity.setResult(Activity.RESULT_OK);
                        activity.finish();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("LogIn error", "Status: " + error.getMessage());
                        Toast.makeText(getActivity(), "Login error. Please check email and password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void checkTickets() {
        Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
            @Override
            public void success(Ticket.TicketResponse ticketResponse, Response response) {
                if (!ticketResponse.getTickets().isEmpty()) {
                    TicketsLoggedInFragment.getInstance().setTickets(ticketResponse.getTickets());
                    TicketsLoggedInFragment.getInstance().show(getActivity().getSupportFragmentManager());
                } else {
                    TicketsLoggedInNoTicketsFragment.getInstance().show(getActivity().getSupportFragmentManager());
                }
//                clearBackStack();

            }

            @Override
            public void failure(RetrofitError error) {
                TicketsLoggedInNoTicketsFragment.getInstance().show(getActivity().getSupportFragmentManager());
                clearBackStack();
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

    @Override
    public String getName() {
        return TicketsLoginFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.login_container;
    }

    private void clearBackStack(){
        FragmentManager fm = getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
