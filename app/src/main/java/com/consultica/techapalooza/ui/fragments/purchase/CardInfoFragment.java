package com.consultica.techapalooza.ui.fragments.purchase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import com.consultica.techapalooza.model.Checkout;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.DialogFactory;
import com.consultica.techapalooza.utils.FontFactory;
import com.flurry.android.FlurryAgent;
import com.nestlean.sdk.Nestlean;
import com.squareup.picasso.Picasso;
import com.stripe.android.model.Card;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Token;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CardInfoFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.ui.fragments.purchase.CardInfoFragment";

    private View view;

    private Checkout checkout;

    private EditText et_card_frag_number, et_card_frag_valid_thru_month, et_card_frag_valid_thru_year, et_card_frag_cvc;
    private ImageView iv_card_frag_card_icon;
    private Button btn_card_frag_card_purchase;
    private Picasso picasso;

    private Typeface typeface;

    private Card card;
    private Token token;
    private Map<String, Object> tokenParams;

    private Handler handler;

    private AppCompatActivity activity;

    private static CardInfoFragment instance;

    public static CardInfoFragment getInstance() {
        if (instance == null)
            instance = new CardInfoFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card_info, container, false);

        activity = (AppCompatActivity) getActivity();
        picasso = Picasso.with(getActivity());
        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        if (activity.getSupportActionBar() != null) {
            TextView textView = (TextView) activity.findViewById(R.id.toolbar_title_purchase);
            textView.setText("Input card information");
        }

        setupTypeFaceForTextViews();
        setupInput();

        return view;
    }

    private void setupInput() {

        card = new Card(null, null, null, null);

        if (checkout == null) {
            checkout = new Checkout();
            checkout.setBand("");
            checkout.setNumberOfTickets(1);
        } else if (checkout.getBand() == null){
            checkout.setBand("");
        }

        iv_card_frag_card_icon = (ImageView) view.findViewById(R.id.iv_card_frag_card_icon);

        et_card_frag_number = (EditText) view.findViewById(R.id.et_card_frag_number);
        et_card_frag_number.setTypeface(typeface);

        et_card_frag_valid_thru_month = (EditText) view.findViewById(R.id.et_card_frag_valid_thru_month);
        et_card_frag_valid_thru_month.setTypeface(typeface);

        et_card_frag_valid_thru_year = (EditText) view.findViewById(R.id.et_card_frag_valid_thru_year);
        et_card_frag_valid_thru_year.setTypeface(typeface);

        et_card_frag_cvc = (EditText) view.findViewById(R.id.et_card_frag_cvc);
        et_card_frag_cvc.setTypeface(typeface);


        initHandler();

        setupNumberInput();
        setupMonthExpInput();
        setupYearExpInput();
        setupCvcInput();

        setupBtnPurchase();

    }

    private void setupTypeFaceForTextViews() {

        TextView tv_card_frag_number = (TextView) view.findViewById(R.id.tv_card_frag_number);
        TextView tv_card_frag_exp = (TextView) view.findViewById(R.id.tv_card_frag_exp);
        TextView tv_card_frag_cvc = (TextView) view.findViewById(R.id.tv_card_frag_cvc);

        tv_card_frag_number.setTypeface(typeface);
        tv_card_frag_exp.setTypeface(typeface);
        tv_card_frag_cvc.setTypeface(typeface);

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                DialogFactory.hideProgressDialog();
                if (msg.what == 1) {
                    Toast.makeText(getActivity(), "Purchase successful", Toast.LENGTH_SHORT).show();
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();

                } else if (msg.what == 2) {
                    Toast.makeText(getActivity(), "Purchase error. Please, try later...", Toast.LENGTH_SHORT).show();
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                } else if (msg.what == 3) {
                    Toast.makeText(getActivity(), "Purchase error.\nCheck yor internet connection or try later", Toast.LENGTH_SHORT).show();
                    activity.setResult(Activity.RESULT_CANCELED);
                    activity.finish();
                }
            }
        };
    }

    private void setupBtnPurchase() {

        btn_card_frag_card_purchase = (Button) view.findViewById(R.id.btn_card_frag_card_purchase);
        btn_card_frag_card_purchase.setTypeface(FontFactory.getTypeface(FontFactory.FONT_ROBOTO_REGULAR));

        btn_card_frag_card_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("Band", checkout.getBand());
                bundle.putString("TicketsCount", String.valueOf(checkout.getNumberOfTickets()));
                bundle.putString("TotalPrice", checkout.getTotalPrice());
                Nestlean.event("Tickets Purchase", bundle);

                Map<String, String> map = new HashMap<>();
                map.put("Band", checkout.getBand());
                map.put("TicketsCount", String.valueOf(checkout.getNumberOfTickets()));
                map.put("TotalPrice", checkout.getTotalPrice());
                FlurryAgent.logEvent("Tickets Purchase", map);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                DialogFactory.showProgressDialog(getActivity());


                try {
                    card.setNumber(et_card_frag_number.getText().toString());
                    card.setExpMonth(Integer.parseInt(et_card_frag_valid_thru_month.getText().toString()));
                    card.setExpYear(Integer.parseInt(et_card_frag_valid_thru_year.getText().toString()));
                    card.setCVC(et_card_frag_cvc.getText().toString());

                    tokenParams = new HashMap<String, Object>();
                    Map<String, Object> cardParams = new HashMap<String, Object>();
                    cardParams.put("number", card.getNumber());
                    cardParams.put("exp_month", card.getExpMonth());
                    cardParams.put("exp_year", card.getExpYear());
                    cardParams.put("cvc", card.getCVC());
                    tokenParams.put("card", cardParams);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                token = Token.create(tokenParams);

                                if (token != null) {
                                    if (!token.getCard().getId().isEmpty()) {
                                        if (checkout.getBand().equals("")){

                                            Client.getAPI().purchaseTicketWithoutBand(checkout.getNumberOfTickets(), token.getId(), new Callback<Ticket.TicketResponse>() {
                                                @Override
                                                public void success(Ticket.TicketResponse ticketResponse, Response response) {
                                                    FakeDB.getInstance(getContext()).saveCanRedeem(ticketResponse.canReedem());
                                                    if (!ticketResponse.getTickets().isEmpty()) {
                                                        handler.sendEmptyMessage(1);
                                                    } else {
                                                        handler.sendEmptyMessage(2);
                                                    }
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    handler.sendEmptyMessage(2);
                                                }
                                            });

                                        } else {
                                            Client.getAPI().purchaseTicket(checkout.getBand(), checkout.getNumberOfTickets(), token.getId(), new Callback<Ticket.TicketResponse>() {
                                                @Override
                                                public void success(Ticket.TicketResponse ticketResponse, Response response) {
                                                    FakeDB.getInstance(getContext()).saveCanRedeem(ticketResponse.canReedem());
                                                    if (!ticketResponse.getTickets().isEmpty()) {
                                                        handler.sendEmptyMessage(1);
                                                    } else {
                                                        handler.sendEmptyMessage(2);
                                                    }
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    handler.sendEmptyMessage(2);
                                                }
                                            });
                                        }
                                    } else {
                                        handler.sendEmptyMessage(3);
                                    }
                                } else {
                                    handler.sendEmptyMessage(3);
                                }
                            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException e) {
                                handler.sendEmptyMessage(3);
                                e.printStackTrace();
                            }

                        }
                    });

                    thread.start();

                } catch (NumberFormatException e) {
                    DialogFactory.hideProgressDialog();
                    Toast.makeText(getContext(), "Please check input data. All fields are required", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupCvcInput() {

        et_card_frag_cvc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    card.setCVC(et_card_frag_cvc.getText().toString());

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return false;
                }
                return false;
            }
        });

        et_card_frag_cvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_card_frag_cvc.length() == 4) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } else if (et_card_frag_cvc.length() == 0) {
                    et_card_frag_valid_thru_year.requestFocus();
                    et_card_frag_valid_thru_year.setSelection(et_card_frag_valid_thru_year.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupYearExpInput() {
        et_card_frag_valid_thru_year.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (et_card_frag_valid_thru_year.length() > 0) {
                    int year = Integer.parseInt(et_card_frag_valid_thru_year.getText().toString());
                    card.setExpYear(20 + year);
                }
            }
        });

        et_card_frag_valid_thru_year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_card_frag_valid_thru_year.length() == 2)
                    et_card_frag_cvc.requestFocus();
                else if (et_card_frag_valid_thru_year.length() == 0) {
                    et_card_frag_valid_thru_month.requestFocus();
                    et_card_frag_valid_thru_month.setSelection(et_card_frag_valid_thru_month.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupMonthExpInput() {
        et_card_frag_valid_thru_month.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (et_card_frag_valid_thru_month.getText().length() > 0) {
                        int month = Integer.parseInt(et_card_frag_valid_thru_month.getText().toString());

                        if (month > 0 && month <= 12) {
                            card.setExpMonth(month);
                            Log.d("Stripe", "CardType: " + card.getType() + " Month: " + card.getExpMonth()
                                    + " Year: " + card.getExpYear());
                        } else {
                            et_card_frag_valid_thru_month.setText("");
                            Toast.makeText(getActivity(), "Input valid expiration Month", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        et_card_frag_valid_thru_month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_card_frag_valid_thru_month.length() == 2)
                    et_card_frag_valid_thru_year.requestFocus();
                else if (et_card_frag_valid_thru_month.getText().length() == 0) {
                    et_card_frag_number.requestFocus();
                    et_card_frag_number.setSelection(et_card_frag_number.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupNumberInput() {

        et_card_frag_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String number = et_card_frag_number.getText().toString();
                    if (number.length() > 0) {
                        card.setNumber(number);
                        card.validateNumber();
                        if (card.validateNumber()) {
                            Log.d("Stripe", "CardType: " + card.getType());
                            getCardType(card.getType());
                        } else
                            picasso.load(R.drawable.cards_other).into(iv_card_frag_card_icon);
                    }
                }
            }
        });

        et_card_frag_number.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pos = 0;
                while (true) {
                    if (pos >= s.length()) break;
                    if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                        s.delete(pos, pos + 1);
                    } else {
                        pos++;
                    }
                }

                pos = 4;
                while (true) {
                    if (pos >= s.length()) break;
                    final char c = s.charAt(pos);
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space);
                    }
                    pos += 5;
                }

                if (s.length() == 19) {
                    et_card_frag_valid_thru_month.requestFocus();
                }
            }
        });
    }

    private void getCardType(String cardBrand) {
        switch (cardBrand) {
            case "MasterCard":
                picasso.load(R.drawable.cards_mc).into(iv_card_frag_card_icon);
                break;
            case "Visa":
                picasso.load(R.drawable.cards_visa).into(iv_card_frag_card_icon);
                break;
            case "American Express":
                picasso.load(R.drawable.cards_amex).into(iv_card_frag_card_icon);
                break;
            case "Discover":
                picasso.load(R.drawable.cards_discover).into(iv_card_frag_card_icon);
                break;
            default:
                picasso.load(R.drawable.cards_other).into(iv_card_frag_card_icon);
                break;

        }
    }

    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    @Override
    public String getName() {
        return CardInfoFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.purchase_container;
    }
}
