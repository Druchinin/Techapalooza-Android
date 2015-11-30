package com.consultica.techapalooza.ui.fragments.tickets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.TicketsAdapter;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.activities.PurchaseActivity;
import com.consultica.techapalooza.ui.fragments.BaseFragment;
import com.consultica.techapalooza.utils.FontFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TicketsLoggedInFragment extends BaseFragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoggedInFragment";
    private static final int REQUEST_PURCHASE = 1;

    private View view;
    private List<Ticket> tickets;
    private TicketsAdapter adapter;
    private int totalItemCount;
    private LinearLayoutManager manager;

    private RecyclerView ticket_recycle_view;

    private TextView tv_tickets_count, tv_tickets_redeem, tv_tickets_purchase_more;

    private AppCompatActivity activity;

    private Toolbar toolbar;

    private Typeface typeface;

    private static TicketsLoggedInFragment instance;
    private boolean canRedeem;


    public static TicketsLoggedInFragment getInstance() {
        if (instance == null)
            instance = new TicketsLoggedInFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_logged_in, container, false);

        activity = (AppCompatActivity) getActivity();
        toolbar = (Toolbar) activity.findViewById(R.id.main_toolbar);
        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        setupViews();

        return view;
    }

    private void setupViews() {

        tv_tickets_count = (TextView) view.findViewById(R.id.tv_tickets_count);
        tv_tickets_count.setTypeface(typeface);

        tv_tickets_redeem = (TextView) view.findViewById(R.id.tv_tickets_redeem);
        tv_tickets_redeem.setTypeface(typeface);

        if (!FakeDB.getInstance(getActivity()).getCanRedeem()) {
            tv_tickets_redeem.setVisibility(View.GONE);
        }

        tv_tickets_purchase_more = (TextView) view.findViewById(R.id.tv_tickets_purchase_more);
        tv_tickets_purchase_more.setTypeface(typeface);

        setupBtnRedeem();
        setupBtnPurchase();
        checkTickets();
    }

    public void checkTickets() {
        if (tickets == null) {

            Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
                @Override
                public void success(Ticket.TicketResponse ticketResponse, Response response) {
                    tickets = ticketResponse.getTickets();
                    if (tickets.size() >= 1)
                        setupRecycleView();
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });

        } else {
            if (tickets.size() >= 1)
                setupRecycleView();
            else {
                TicketsLoggedInNoTicketsFragment.getInstance().show(getActivity().getSupportFragmentManager());
            }
        }
    }

    private void setupBtnPurchase() {
        tv_tickets_purchase_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PurchaseActivity.class).putExtra("what", PurchaseActivity.WHAT_PURCHASE),
                        REQUEST_PURCHASE);
            }
        });
    }

    private void setupBtnRedeem() {
        tv_tickets_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PurchaseActivity.class).putExtra("what", PurchaseActivity.WHAT_REDEEM),
                        REQUEST_PURCHASE);
            }
        });
    }

    private void setupBtnLogout() {
        SpannableStringBuilder title = new SpannableStringBuilder("Logout");
        title.setSpan(typeface, 0, title.length(), 0);

        if (!toolbar.getMenu().hasVisibleItems()) {

            toolbar.inflateMenu(R.menu.menu_logout);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Client.getAPI().logOut(new Callback<SignInResponse>() {

                        @Override
                        public void success(SignInResponse signInResponse, Response response) {
                            toolbar.getMenu().clear();

                            FakeDB.getInstance(getActivity()).resetLoginAndPassword();
                            Interceptor.getInstance().clearCookie();

                            TicketsMainFragment.getInstance().show(getFragmentManager());

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


                    return false;
                }
            });
        }
    }

    private void setupRecycleView() {
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);

        if (FakeDB.getInstance(getActivity()).getCanRedeem()) {
            tv_tickets_redeem.setVisibility(View.VISIBLE);
        } else {
            tv_tickets_redeem.setVisibility(View.GONE);
        }

        if (viewPager.getCurrentItem() == 2)
            setupBtnLogout();

        if (!tickets.isEmpty()) {

            ticket_recycle_view = (RecyclerView) view.findViewById(R.id.ticket_recycle_view);
            adapter = new TicketsAdapter(getActivity(), tickets);

            totalItemCount = adapter.getItemCount();

            manager = new LinearLayoutManager(getActivity());

            ticket_recycle_view.setLayoutManager(manager);
            ticket_recycle_view.setAdapter(adapter);

            ticket_recycle_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    tv_tickets_count.setText((manager.findFirstVisibleItemPosition() + 1) + "/" + totalItemCount);
                }
            });

            tv_tickets_count.setText(1 + "/" + totalItemCount);

            Task task = new Task();
            task.execute();

        } else {
            TicketsLoggedInNoTicketsFragment.getInstance().show(getFragmentManager());
        }
    }

    @Override
    public String getName() {
        return TicketsLoggedInFragment.class.getSimpleName();
    }

    @Override
    public int getContainer() {
        return R.id.fragment_tickets_container;
    }

    public void setCanRedeem(boolean canReedem) {
        if (tv_tickets_redeem != null) {
            if (canReedem) {
                tv_tickets_redeem.setVisibility(View.VISIBLE);
            } else {
                tv_tickets_redeem.setVisibility(View.GONE);
            }
        } else {
            this.canRedeem = canReedem;
        }
    }

    class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            File cacheDir = App.getInstance().getCacheDir();
            File f;
            Bitmap bitmap;
            FileOutputStream out;

            f = new File(cacheDir, "ticket_background");
            try {
                bitmap = BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.individual_ticket);
                out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            for (Ticket ticket : tickets) {
                f = new File(cacheDir, ticket.getCode());
                try {
                    bitmap = generateQrCode(ticket.getCode());
                    out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();
                } catch (IOException | WriterException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    private static Bitmap generateQrCode(String myCodeText) throws WriterException {

        QRCodeWriter writer = new QRCodeWriter();

        ByteMatrix bitMatrix = writer.encode(myCodeText, BarcodeFormat.QR_CODE, 512, 512);
        int width = 512, height = 512;

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y) == 0)
                    bmp.setPixel(x, y, Color.BLACK);
                else
                    bmp.setPixel(x, y, Color.WHITE);
            }
        }

        return bmp;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public boolean hasTickets() {
        if (tickets != null)
            return !tickets.isEmpty();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PURCHASE){
                Client.getAPI().getTicketsList(new Callback<Ticket.TicketResponse>() {
                    @Override
                    public void success(Ticket.TicketResponse ticketResponse, Response response) {
                        FakeDB.getInstance(getActivity()).saveCanRedeem(ticketResponse.canReedem());
                        tickets = ticketResponse.getTickets();

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupRecycleView();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }
}
