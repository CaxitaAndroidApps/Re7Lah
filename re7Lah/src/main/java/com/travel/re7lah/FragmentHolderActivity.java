package com.travel.re7lah;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toolbar;

import com.travel.re7lah.adapter.NavDrawerListAdapter;
import com.travel.re7lah.fragments.FlightFragment;
import com.travel.re7lah.fragments.HotelFragment;
import com.travel.re7lah.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentHolderActivity extends Activity {

    static int menu_pos = -1;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView menuBtn, ivLogo;
    ImageView ivToggle;

    LinearLayout llslider;
    CommonFunctions cf;
    boolean loggedIn = false;
    private Locale myLocale;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    // slide menu items
    private String[] navMenuTitles;
    // private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        cf = new CommonFunctions(this);

        loadAppBar();
        addListItems();

        switch (getIntent().getExtras().getString("type", "flight")) {
            case "flight":
                fragment = new FlightFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.frame_container, fragment,
                        "Flight");
                fragmentTransaction.commit();
                break;

            case "hotel":
                fragment = new HotelFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction
                        .replace(R.id.frame_container, fragment, "hotel");
                fragmentTransaction.commit();
                break;
            default:
                break;
        }

    }

    private void loadAppBar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        llslider = (LinearLayout) findViewById(R.id.ll_slider);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        // ============== Define a Custom Header for Navigation
        // drawer=================//
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.header, null);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(v);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                Toolbar parent = (Toolbar) v.getParent();
                parent.setContentInsetsAbsolute(0, 0);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        menuBtn = (ImageView) findViewById(R.id.iv_menu_btn);
        ivToggle = (ImageView) findViewById(R.id.iv_lang);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);

        menuBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDrawerLayout.isDrawerOpen(llslider)) {
                    mDrawerLayout.closeDrawer(llslider);
                } else {
                    mDrawerLayout.openDrawer(llslider);
                }
            }
        });

        ivLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        ivToggle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CommonFunctions.lang.equalsIgnoreCase("en"))
                    CommonFunctions.lang = "ar";
                else
                    CommonFunctions.lang = "en";

                changeLang(CommonFunctions.lang);

                setContentView(R.layout.activity_flight);
                loadAppBar();
                addListItems();

                switch (getIntent().getExtras().getString("type", "flight")) {
                    case "flight":
                        fragment = new FlightFragment();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.frame_container, fragment,
                                "Flight");
                        fragmentTransaction.commit();
                        break;

                    case "hotel":
                        fragment = new HotelFragment();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction
                                .replace(R.id.frame_container, fragment, "hotel");
                        fragmentTransaction.commit();
                        break;
                    default:
                        break;
                }

            }
        });

    }

    private void addListItems() {
        // TODO Auto-generated method stub
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], 0));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], 1));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], 2));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], 3));

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    private void displayView(int id) {
        // update the main content by replacing fragments
        mDrawerLayout.closeDrawer(llslider);
        Intent inte;
        switch (id) {
            case 0:
                finish();
                break;
            case 1:
                inte = new Intent(FragmentHolderActivity.this,
                        SearchPageActivity.class);
                inte.putExtra("url", CommonFunctions.main_url
                        + CommonFunctions.lang + "/MyBooking/ManageMyBookings");
                startActivity(inte);
                break;

            case 2:
                inte = new Intent(FragmentHolderActivity.this,
                        SearchPageActivity.class);
                inte.putExtra("url", CommonFunctions.main_url
                        + CommonFunctions.lang + "/Shared/Contact");
                startActivity(inte);
                break;
            case 3: {
                String email = getResources().getString(R.string.contact_email);
                inte = new Intent(Intent.ACTION_SEND);
                inte.setType("message/rfc822");
                inte.putExtra(Intent.EXTRA_SUBJECT, "Enquiry");
                inte.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                Intent mailer = Intent.createChooser(inte, "Send Mail");
                startActivity(mailer);
                if (menu_pos == -1)
                    mDrawerList.setItemChecked(5, false);
                else {
                    mDrawerList.setItemChecked(menu_pos, true);
                }
                break;
            }
            default: {
                if (fragment != null) {
                    // update selected item and title, then close the drawer
                    mDrawerList.setItemChecked(menu_pos, true);
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                    break;
                }
            }
        }
        mDrawerList.setItemChecked(menu_pos, true);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        this.getBaseContext()
                .getResources()
                .updateConfiguration(
                        config,
                        this.getBaseContext().getResources()
                                .getDisplayMetrics());
    }

    public void saveLocale(String lang) {
        CommonFunctions.lang = lang;
        String langPref = "Language";
        SharedPreferences prefs = this.getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

}
