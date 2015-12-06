package com.consultica.techapalooza.ui.fragments.purchase;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RedeemFragment extends BaseFragment{

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.purchase.RedeemFragment";

    private View view;

    private Button btn_redeem_frag_redeem;
    private EditText et_redeem_frag_code;

    private Typeface typeface;

    private AppCompatActivity activity;

    private static RedeemFragment instance;

    public static RedeemFragment getInstance() {
        if (instance == null)
            instance = new RedeemFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_redeem_tickets, container, false);


        activity = (AppCompatActivity) getActivity();
        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        init();

        return view;
    }

    private void init() {

        TextView tv_redeem_frag_code = (TextView) view.findViewById(R.id.tv_redeem_frag_code);
        tv_redeem_frag_code.setTypeface(typeface);

        et_redeem_frag_code = (EditText) view.findViewById(R.id.et_redeem_frag_code);
        et_redeem_frag_code.setTypeface(typeface);

        btn_redeem_frag_redeem = (Button) view.findViewById(R.id.btn_redeem_frag_redeem);
        btn_redeem_frag_redeem.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));
        setupBtnRedeem();
    }

    private void setupBtnRedeem() {

        btn_redeem_frag_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                if (et_redeem_frag_code.length() > 0) {
                    String code = et_redeem_frag_code.getText().toString();

                    Client.getAPI().redeemPromoCode(code, new Callback<Ticket.TicketResponse>() {
                        @Override
                        public void success(Ticket.TicketResponse ticketResponse, Response response) {
                            FakeDB.getInstance(activity).saveCanRedeem(ticketResponse.canReedem());
                            activity.setResult(Activity.RESULT_OK);
                            activity.finish();
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public String getName() {
        return RedeemFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.purchase_container;
    }
}
