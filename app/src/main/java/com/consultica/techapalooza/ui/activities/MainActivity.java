package com.consultica.techapalooza.ui.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.consultica.techapalooza.App;
import com.consultica.techapalooza.R;
import com.consultica.techapalooza.adapters.TabsPagerFragmentAdapter;
import com.consultica.techapalooza.database.DBMaster;
import com.consultica.techapalooza.database.FakeDB;
import com.consultica.techapalooza.model.Ticket;
import com.consultica.techapalooza.network.Client;
import com.consultica.techapalooza.network.Interceptor;
import com.consultica.techapalooza.network.SignInResponse;
import com.consultica.techapalooza.ui.fragments.lineup.LineUpFragment;
import com.consultica.techapalooza.ui.fragments.lineup.LineUpGalleryFragment;
import com.consultica.techapalooza.ui.fragments.news.NewsFeedFragment;
import com.consultica.techapalooza.ui.fragments.schedule.ScheduleFragment;
import com.consultica.techapalooza.ui.fragments.schedule.ScheduleListFragment;
import com.consultica.techapalooza.ui.fragments.tickets.TicketsFragmentContainer;
import com.consultica.techapalooza.ui.fragments.tickets.TicketsLoggedInFragment;
import com.consultica.techapalooza.ui.fragments.tickets.TicketsMainFragment;
import com.consultica.techapalooza.ui.fragments.venue.VenueFragment;
import com.consultica.techapalooza.ui.fragments.venue.VenueFragmentContainer;
import com.consultica.techapalooza.utils.Constants;
import com.consultica.techapalooza.utils.FontFactory;
import com.flurry.android.FlurryAgent;
import com.nestlean.sdk.Nestlean;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    public static final String USER_PREF = "CurrentUser";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerFragmentAdapter adapter;
    private Toolbar toolbar;
    private android.support.v4.app.FragmentManager manager;

    private Typeface typeface;

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

    private List<Ticket> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nestlean.init(MainActivity.this, "842d9562aaf24200372231e36c22e64a");

        typeface = FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG);

        manager = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initTabs();
        checkLoginAndTickets();

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

    private void setupViewPager(final ViewPager viewPager) {
        adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(NewsFeedFragment.getInstance(), Constants.TAB_NEWS_FEED_LABLE);
        adapter.addFragment(ScheduleFragment.getInstance(), Constants.TAB_SCHEDULE_LABLE);
        adapter.addFragment(TicketsFragmentContainer.getInstance(), Constants.TAB_TICKETS_LABLE);
        adapter.addFragment(LineUpFragment.getInstance(), Constants.TAB_LINE_UP_LABLE);
        adapter.addFragment(VenueFragmentContainer.getInstance(), Constants.TAB_VENUE_LABLE);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
    }

    private void setupTabIcons() {
        TextView tv_tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabOne.setText(R.string.tab_lable_news_feed);
        tv_tabOne.setTypeface(typeface);
        tv_tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tv_tabOne);

        TextView tv_tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabTwo.setTypeface(typeface);
        tv_tabTwo.setText(R.string.tab_lable_schedule);
        tv_tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tv_tabTwo);

        TextView tv_tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabThree.setTypeface(typeface);
        tv_tabThree.setText(R.string.tab_lable_tickets);
        tv_tabThree.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tv_tabThree);

        TextView tv_tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabFour.setTypeface(typeface);
        tv_tabFour.setText(R.string.tab_lable_line_up);
        tv_tabFour.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tabLayout.getTabAt(3).setCustomView(tv_tabFour);

        TextView tv_tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_tabFive.setTypeface(typeface);
        tv_tabFive.setText(R.string.tab_lable_venue);
        tv_tabFive.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[4], 0, 0);
        tabLayout.getTabAt(4).setCustomView(tv_tabFive);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setCurrentTabSelected(tab);

        View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });

        setupDisplayHomeAsUpEnabled(tab);
    }

    private void setCurrentTabSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();

        TextView textView = (TextView) view.findViewById(R.id.tab);
        textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

        textView.setTextColor(getResources().getColor(R.color.textColorActive));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsActive[tab.getPosition()], 0, 0);

        tabLayout.getTabAt(tab.getPosition()).setCustomView(textView);

        if (viewPager.getCurrentItem() != tab.getPosition())
            viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        View view = tab.getCustomView();
        TextView textView = (TextView) view.findViewById(R.id.tab);
        textView.setTypeface(FontFactory.getTypeface(FontFactory.FONT_SANS_NARROW_WEB_REG));

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

    private void setupDisplayHomeAsUpEnabled(TabLayout.Tab tab) {

        if (tab != null) {
            switch (tab.getText().toString()) {
                case Constants.TAB_NEWS_FEED_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toolbar.getMenu().clear();
                    break;
                case Constants.TAB_SCHEDULE_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(ScheduleListFragment.TAG);
                    toolbar.getMenu().clear();
                    break;
                case Constants.TAB_TICKETS_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(TicketsMainFragment.TAG);
                    Client.getAPI().getCurrentUser(new Callback<SignInResponse>() {
                        @Override
                        public void success(SignInResponse signInResponse, Response response) {
                            if (signInResponse.getUserId() != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setupBtnLogout();
                                    }
                                });
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                    break;
                case Constants.TAB_LINE_UP_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(LineUpGalleryFragment.TAG);
                    toolbar.getMenu().clear();
                    break;
                case Constants.TAB_VENUE_LABLE:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setupBackStackChangeListener(VenueFragment.TAG);
                    toolbar.getMenu().clear();
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
                hideSoftKeyboard();

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

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().findViewById(android.R.id.content).getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        hideSoftKeyboard();
        super.onBackPressed();
    }

    private void setupBtnLogout() {
        if (!toolbar.getMenu().hasVisibleItems()) {

            toolbar.inflateMenu(R.menu.menu_logout);

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Client.getAPI().logOut(new Callback<SignInResponse>() {

                        @Override
                        public void success(SignInResponse signInResponse, Response response) {
                            FakeDB.getInstance(MainActivity.this).resetLoginAndPassword();
                            Interceptor.getInstance().clearCookie();

                            Nestlean.event("Account Logout");
                            FlurryAgent.logEvent("Account Logout");

                            toolbar.getMenu().clear();
                            if (tabLayout.getSelectedTabPosition() == 2) {
                                TicketsMainFragment.getInstance().show(getSupportFragmentManager());
                            }

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

    private boolean checkLoginAndTickets() {
        if (FakeDB.getInstance(this).getUserId() != null) {
            if (!DBMaster.getInstance(this).getAllTickets().isEmpty()) {
                tickets = DBMaster.getInstance(this).getAllTickets();
                if (tickets != null){
                    TicketsLoggedInFragment.getInstance().setTickets(tickets);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}


