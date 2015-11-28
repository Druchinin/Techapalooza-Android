package com.consultica.techapalooza.ui.fragments.tickets;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RedeemFragment extends Fragment{

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.tickets.RedeemFragment";

    private View view;

    private Button btn_redeem_frag_redeem;
    private EditText et_redeem_frag_code;
    private String band;

    private static RedeemFragment instance;

    public static RedeemFragment getInstance() {
        if (instance == null)
            instance = new RedeemFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_redeem_tickets, container, false);

        init();

        return view;
    }

    private void init() {
        if (!getArguments().isEmpty()){
            band = getArguments().getString("band");
        }

        et_redeem_frag_code = (EditText) view.findViewById(R.id.et_redeem_frag_code);

        btn_redeem_frag_redeem = (Button) view.findViewById(R.id.btn_redeem_frag_redeem);
        setupBtnRedeem();
    }

    private void setupBtnRedeem() {

        btn_redeem_frag_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                if (et_redeem_frag_code.length() > 0){
                    String code = et_redeem_frag_code.getText().toString();

                    Client.getAPI().redeemPromoCode(band, code, new Callback<Ticket.TicketResponse>() {
                        @Override
                        public void success(Ticket.TicketResponse ticketResponse, Response response) {
                            startTicketMainFragment();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), "Code not valid", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void startTicketMainFragment() {
        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_tickets_container, new TicketsMainFragment(), TicketsMainFragment.TAG);
        tr.commit();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
