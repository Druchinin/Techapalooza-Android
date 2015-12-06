package com.consultica.techapalooza.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.ui.fragments.about.AboutCancerCare;
import com.consultica.techapalooza.ui.fragments.about.AboutTechapalooza;
import com.consultica.techapalooza.utils.FontFactory;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);

        TextView textView;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            textView = (TextView) findViewById(R.id.toolbar_title_about);

            String what = null;

            Intent intent = getIntent();

            if (intent.hasExtra("what")){
                if (intent.getStringExtra("what").equals(AboutTechapalooza.class.getSimpleName())){
                    AboutTechapalooza.getInstance().show(getSupportFragmentManager());
                    textView.setText("About Techapalooza");
                    textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

                } else if (intent.getStringExtra("what").equals(AboutCancerCare.class.getSimpleName())){
                    AboutCancerCare.getInstance().show(getSupportFragmentManager());
                    textView.setText("About Cancer Care");
                    textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));
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
