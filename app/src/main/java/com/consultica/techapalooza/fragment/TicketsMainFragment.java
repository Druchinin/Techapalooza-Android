package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.consultica.techapalooza.R;

public class TicketsMainFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsMainFragment";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_main, container, false);

        Button btnLogin = (Button) view.findViewById(R.id.btn_tickets_sign_in);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_tickets_container, new TicketsLoginFragment(), TicketsLoginFragment.TAG);
                ft.addToBackStack(TicketsLoginFragment.TAG);
                ft.commit();
            }
        });

        Button btnSignUp = (Button) view.findViewById(R.id.btn_tickets_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_tickets_container, new RegistrationFragment(), RegistrationFragment.TAG);
                ft.addToBackStack(RegistrationFragment.TAG);
                ft.commit();
            }
        });

        return view;
    }
}
