package com.consultica.techapalooza.ui.fragments.tickets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.ui.MainActivity;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TicketsLoginFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoginFragment";

    private View view;
    private EditText mEmail, mEtPassword;
    private TextView mHidePass;
    private boolean isShownPsw = true;

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

                        SharedPreferences pref = App.getInstance().getSharedPreferences(MainActivity.USER_PREF, Context.MODE_PRIVATE);
                        pref.edit().clear().commit();
                        pref.edit().putString("email", signInResponse.getUserEmail()).putString("id", signInResponse.getUserId()).apply();

                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();

                        checkTickets();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("LogIn error", "Status: " + error.getMessage());
                    }
                });
            }
        });
    }

    private void checkTickets() {
        Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
            @Override
            public void success(Ticket.TicketResponse ticketResponse, Response response) {
                if (ticketResponse.getTickets().size() > 0)
                    startTicketsLoggedFragment(ticketResponse.getTickets());
                else startNoTicketsFragment();
            }

            @Override
            public void failure(RetrofitError error) {
                startNoTicketsFragment();
            }
        });
    }

    private void startTicketsLoggedFragment(List<Ticket> list) {
        TicketsLoggedInFragment fragment = new TicketsLoggedInFragment();
        fragment.setTickets(list);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_tickets_container, fragment, TicketsLoggedInFragment.TAG);
        ft.commit();
    }

    private void saveCookie(Response response) {
        if (Interceptor.getInstance().getCookie() == null) {
            Interceptor.getInstance().buildUserIdCookieFromString(response.getHeaders());
        }
    }

    private void startNoTicketsFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_tickets_container, new TicketsLoggedInNoTicketsFragment(), TicketsLoggedInNoTicketsFragment.TAG);
        ft.commit();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
