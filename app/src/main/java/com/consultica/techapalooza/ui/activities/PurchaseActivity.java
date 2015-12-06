package com.consultica.techapalooza.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.ui.fragments.purchase.BandListFragment;
import com.consultica.techapalooza.ui.fragments.purchase.RedeemFragment;
import com.consultica.techapalooza.utils.FontFactory;

public class PurchaseActivity extends AppCompatActivity {

    public static final String WHAT_PURCHASE = "purchase";
    public static final String WHAT_REDEEM = "redeem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Toolbar toolbar = (Toolbar) findViewById(R.id.purchase_toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.toolbar_title_purchase);
        textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));


        String what = null;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("what")) {
            what = intent.getStringExtra("what");
            if (what != null) {
                if (what.equals(WHAT_PURCHASE)) {
                    textView.setText("Purchase Tickets");
                    BandListFragment.getInstance().show(getSupportFragmentManager());
                } else if (what.equals(WHAT_REDEEM)) {
                    textView.setText("Input redeem code");
                    RedeemFragment.getInstance().show(getSupportFragmentManager());
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
