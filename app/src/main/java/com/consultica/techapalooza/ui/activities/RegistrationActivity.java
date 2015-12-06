package com.consultica.techapalooza.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.ui.fragments.authentication.RegistrationFragment;
import com.consultica.techapalooza.utils.FontFactory;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        setSupportActionBar(toolbar);

        TextView textView;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            textView = (TextView) findViewById(R.id.toolbar_title_registration);
            textView.setText("Sign up");
            textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));


        }

        RegistrationFragment.getInstance().show(getSupportFragmentManager());

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
