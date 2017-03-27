package com.travel.re7lah;

import java.util.ArrayList;
import java.util.Locale;

import com.travel.re7lah.R;
import com.travel.re7lah.adapter.NavDrawerListAdapter;
import com.travel.re7lah.model.NavDrawerItem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

public class MenuSelectionAcitivity extends Activity {

	CommonFunctions cf;
	boolean doublePress = false;

	private Locale myLocale;
	Fragment fragment;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	ImageView menuBtn;
	ImageView ivToggle;

	LinearLayout llslider;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	// slide menu items
	private String[] navMenuTitles;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	static int menu_pos = -1;

	static String lan = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cf = new CommonFunctions(this);
		loadLocale();
		
		lan = CommonFunctions.lang;
		
		if(CommonFunctions.updateApp) {
			AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

	        // Setting Dialog Title
	        alertDialog.setTitle("New update available");

	        // Setting Dialog Message
	        alertDialog.setMessage("New version of application is availabale, please update application to continue..");

	        // Setting OK Button
	        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//intent to move mobile settings
					final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
					try {
						finishAffinity();
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					    
					} catch (android.content.ActivityNotFoundException anfe) {
						finishAffinity();
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
					}
				}});
	        alertDialog.setNegativeButton(getResources().getString(R.string.error_no_internet_close_app), new DialogInterface.OnClickListener() {
	    		
	    		@Override
	    		public void onClick(DialogInterface dialog, int which) {
	    			// TODO Auto-generated method stub
	    			finishAffinity();
	    		}
	    	});

	        // Showing Alert Message
	        alertDialog.setCancelable(false);
	        alertDialog.show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mDrawerList.setItemChecked(menu_pos, false);
		mDrawerList.setItemChecked(0, true);
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		if(!lan.equalsIgnoreCase(CommonFunctions.lang)) {
			loadLocale();
			lan = CommonFunctions.lang;
		}
		super.onRestart();
	}
	
	private void loadAppBar() {
		// ============== Define a Custom Header for Navigation
		// drawer=================//
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		llslider = (LinearLayout) findViewById(R.id.ll_slider);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
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
//		ivLogo = (ImageView) findViewById(R.id.iv_logo);

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

//		ivLogo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});

		ivToggle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CommonFunctions.lang.equalsIgnoreCase("en"))
					CommonFunctions.lang = "ar";
				else
					CommonFunctions.lang = "en";

				lan = CommonFunctions.lang;
				changeLang(CommonFunctions.lang);

				setContentView(R.layout.activity_main_selection);
				loadAppBar();
				addListItems();

			}
		});

	}

	public void clicker(View v) {
		if (cf.isConnectingToInternet()) {
			Intent next;
			switch (v.getId()) {
			case R.id.rl_flight:
				next = new Intent(MenuSelectionAcitivity.this,
						FragmentHolderActivity.class);
				next.putExtra("type", "flight");
				startActivity(next);
				break;
				
			case R.id.rl_hotel:
				next = new Intent(MenuSelectionAcitivity.this,
						FragmentHolderActivity.class);
				next.putExtra("type", "hotel");
				startActivity(next);
				break;
				
			case R.id.rl_booking:
				next = new Intent(MenuSelectionAcitivity.this,
						SearchPageActivity.class);
				next.putExtra("url", CommonFunctions.main_url
						+ CommonFunctions.lang + "/MyBooking/ManageMyBookings");
				startActivity(next);
				break;
			case R.id.rl_register:
				next = new Intent(MenuSelectionAcitivity.this,
						SearchPageActivity.class);
				next.putExtra("url", CommonFunctions.main_url
						+ CommonFunctions.lang + "/shared/SignAccount");
				next.putExtra("IsRegister", true);
				startActivity(next);
				break;
			default:
				break;
			}
		} else {
			cf.noInternetAlert();
		}
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

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			NavDrawerItem obj = new NavDrawerItem();
			if (menu_pos != position) {
				obj = (NavDrawerItem) adapter.getItem(position);
				System.out.println("Clicked id is:" + obj.getId());
				displayView(obj.getId(), position);
			}
		}
	}

	private void displayView(int id, int position) {
		// update the main content by replacing fragments
		mDrawerLayout.closeDrawer(llslider);
//		mDrawerList.setItemChecked(position, true);
		Intent inte;
		switch (id) {
		case 0:
			
			break;
		case 1:
			inte=new Intent(MenuSelectionAcitivity.this, SearchPageActivity.class);
			inte.putExtra("url", CommonFunctions.main_url+CommonFunctions.lang+
					"/MyBooking/ManageMyBookings");
			startActivity(inte);
			break;
		
		case 2: 
			inte=new Intent(MenuSelectionAcitivity.this, SearchPageActivity.class);
			inte.putExtra("url", CommonFunctions.main_url+CommonFunctions.lang+
				"/Shared/Contact");
			startActivity(inte);
			break;
		case 3: {
//			mDrawerList.setItemChecked(position, false);
			String email = getResources().getString(R.string.contact_email);
			inte = new Intent(Intent.ACTION_SEND);
			inte.setType("message/rfc822");
			inte.putExtra(Intent.EXTRA_SUBJECT, "Enquiry");
			inte.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			Intent mailer = Intent.createChooser(inte, "Send Mail");
			startActivity(mailer);
			break;
		}
		default: {
			if (fragment != null) {
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
			} else {
				// error in creating fragment
				Log.e("MainActivity", "Error in creating fragment");
				break;
			}
		}
		}
		menu_pos = position;
	}
	
	private void loadLocale() {
		// TODO Auto-generated method stub
		SharedPreferences sharedpreferences  = this.getSharedPreferences("CommonPrefs", Context.MODE_PRIVATE);
		String lang = sharedpreferences.getString("Language", "en");
		System.out.println("Default lang: "+lang);
		if(lang.equalsIgnoreCase("ar"))
		{
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext().getResources().updateConfiguration(config, this.getBaseContext().getResources().getDisplayMetrics());
			CommonFunctions.lang = "ar";
		}
		else{
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext().getResources().updateConfiguration(config, this.getBaseContext().getResources().getDisplayMetrics());
			CommonFunctions.lang = "en";
		}
		setContentView(R.layout.activity_main_selection);
		loadAppBar();
		addListItems();
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (doublePress)
			finishAffinity();
		else {
			doublePress = true;
			cf.showToast(getResources().getString(R.string.exit_msg));
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					doublePress = false;
				}
			}, 2000);
		}

	}

}
