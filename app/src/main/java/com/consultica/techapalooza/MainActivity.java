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
import com.consultica.techapalooza.fragment.LineUpGalleryFragment;
import com.consultica.techapalooza.fragment.NewsFeedFragment;
import com.consultica.techapalooza.fragment.ScheduleFragment;
import com.consultica.techapalooza.fragment.ScheduleListFragment;
import com.consultica.techapalooza.fragment.TicketsFragmentContainer;
import com.consultica.techapalooza.fragment.TicketsMainFragment;
import com.consultica.techapalooza.fragment.VenueFragment;
import com.nestlean.sdk.Nestlean;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tv_tabOne, tv_tabTwo, tv_tabThree, tv_tabFour, tv_tabFive;
    private TabsPagerFragmentAdapter adapter;
    private Toolbar toolbar;
    private android.support.v4.app.FragmentManager manager;

    private String currentTab;

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

        manager = getSupportFragmentManager();

        Nestlean.init(MainActivity.this, "842d9562aaf24200372231e36c22e64a");

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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
        adapter.addFragment(new NewsFeedFragment(), Constants.TAB_NEWS_FEED_LABLE);
        adapter.addFragment(new ScheduleFragment(), Constants.TAB_SCHEDULE_LABLE);
        adapter.addFragment(new TicketsFragmentContainer(), Constants.TAB_TICKETS_LABLE);
        adapter.addFragment(new LineUpFragment(), Constants.TAB_LINE_UP_LABLE);
        adapter.addFragment(new VenueFragment(), Constants.TAB_VENUE_LABLE);
        viewPager.setAdapter(adapter);

    }

    private void setupTabIcons() {
        tv_tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabOne.setText(R.string.tab_lable_news_feed);
        tv_tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tv_tabOne);

        tv_tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabTwo.setText(R.string.tab_lable_schedule);
        tv_tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tv_tabTwo);

        tv_tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabThree.setText(R.string.tab_lable_tickets);
        tv_tabThree.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tv_tabThree);

        tv_tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabFour.setText(R.string.tab_lable_line_up);
        tv_tabFour.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tabLayout.getTabAt(3).setCustomView(tv_tabFour);

        tv_tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabFive.setText(R.string.tab_lable_venue);
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
        
        int count = manager.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            manager.popBackStackImmediate();
        }

        setupDisplayHomeAsUpEnabled(tab);
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

    private void setupDisplayHomeAsUpEnabled(TabLayout.Tab tab){

        if (tab != null) {
            switch (tab.getText().toString()) {
                case Constants.TAB_NEWS_FEED_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    break;
                case Constants.TAB_SCHEDULE_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(ScheduleListFragment.TAG);
                    break;
                case Constants.TAB_TICKETS_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(TicketsMainFragment.TAG);
                    break;
                case Constants.TAB_LINE_UP_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(LineUpGalleryFragment.TAG);
                    break;
                case Constants.TAB_VENUE_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    break;


            }
        }

    }

    private void setupBackStackChangeListener(String tag) {
        currentTab = tag;
        manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = manager.getBackStackEntryCount();
                if (count != 0) {
                    String currentFragmentTag = manager.getBackStackEntryAt(count - 1).getName();
                    if (currentFragmentTag.equals(currentTab)) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    } else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });
    }
}


