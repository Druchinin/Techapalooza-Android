package com.consultica.techapalooza;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.consultica.techapalooza.adapter.TabsPagerFragmentAdapter;
import com.consultica.techapalooza.fragment.LineUpFragment;
import com.consultica.techapalooza.fragment.NewsFeedFragment;
import com.consultica.techapalooza.fragment.ScheduleFragment;
import com.consultica.techapalooza.fragment.TicketsFragment;
import com.consultica.techapalooza.fragment.VenueFragment;
import com.nestlean.sdk.Nestlean;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tv_tabOne, tv_tabTwo, tv_tabThree, tv_tabFour, tv_tabFive;
    private TabsPagerFragmentAdapter adapter;

    private int[] tabIcons = {
            R.mipmap.ic_action_news_feed,
            R.mipmap.ic_action_schedule,
            R.mipmap.ic_action_tickets,
            R.mipmap.ic_action_line_up,
            R.mipmap.ic_action_venue,
    };

    private int[] tabIconsActive = {
            R.mipmap.ic_action_news_feed_active,
            R.mipmap.ic_action_schedule_active,
            R.mipmap.ic_action_tickets_active,
            R.mipmap.ic_action_line_up_active,
            R.mipmap.ic_action_venue_active,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Nestlean.init(MainActivity.this, "9a2113954f0f7069cfd9b8795405d2ee");

        initTabs();
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorHeight(3);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.textColorActive));

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        setCurrentTabSelected(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));

        tabLayout.setOnTabSelectedListener(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFeedFragment(), "News");
        adapter.addFragment(new ScheduleFragment(), "Shedule");
        adapter.addFragment(new TicketsFragment(), "Tickets");
        adapter.addFragment(new LineUpFragment(), "Line-up");
        adapter.addFragment(new VenueFragment(), VenueFragment.TAG);
        viewPager.setAdapter(adapter);

    }

    private void setupTabIcons() {
        tv_tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabOne.setText("News Feed");
        tv_tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tv_tabOne);

        tv_tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabTwo.setText("Schedule");
        tv_tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tv_tabTwo);

        tv_tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabThree.setText("Tickets");
        tv_tabThree.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tv_tabThree);

        tv_tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabFour.setText("Line-up");
        tv_tabFour.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tabLayout.getTabAt(3).setCustomView(tv_tabFour);

        tv_tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabFive.setText("Venue");
        tv_tabFive.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[4], 0, 0);
        tabLayout.getTabAt(4).setCustomView(tv_tabFive);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setCurrentTabSelected(tab);
    }

    private void setCurrentTabSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();

        TextView textView = (TextView) view.findViewById(R.id.tab);

        textView.setTextColor(getResources().getColor(R.color.textColorActive));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsActive[tab.getPosition()], 0, 0);

        tabLayout.getTabAt(tab.getPosition()).setCustomView(textView);

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView textView = (TextView) view.findViewById(R.id.tab);

        textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[tab.getPosition()], 0, 0);

        tabLayout.getTabAt(tab.getPosition()).setCustomView(textView);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}


