package com.travel.re7lah;

import java.util.Locale;

import com.travel.re7lah.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

public class SearchPageActivity extends Activity {

	private Locale myLocale;
	ProgressBar pbLine;
	String url, loadingURL;
	int dp1;
	boolean flag = true;
	WebView wv1;
	ImageButton menuBtn;
	CommonFunctions cf;

	@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loadLocale();
		setContentView(R.layout.activity_web);
		cf = new CommonFunctions(this);
		url 	= getIntent().getExtras().getString("url","Empty");
		boolean blRegister = getIntent().getExtras().getBoolean("IsRegister", false);
		
		if(blRegister)
			loadAppBar();
		else
			getActionBar().hide();
		
//		loaderDialog = new Dialog(SearchPageActivity.this, android.R.style.Theme_Translucent);
//		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		loaderDialog.getWindow().setGravity(Gravity.TOP);
//		loaderDialog.setContentView(R.layout.dialog_loader);
//		loaderDialog.setCancelable(false);http://fuuf.caxita.ca/en http://fuuf.caxita.ca/en
		
		wv1=(WebView) findViewById(R.id.wv_main);
		pbLine	= (ProgressBar) findViewById(R.id.pb_line);
		
		wv1.getSettings().setRenderPriority(RenderPriority.HIGH);
		wv1.getSettings().setJavaScriptEnabled(true);

		wv1.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				Log.d("Started","Loading");
				System.out.println("Loading url"+url);
				loadingURL = url;
				if(url.contains("/Home/MyBooking/MyBooking"))
					flag = false;
				if (url.equals(CommonFunctions.main_url)
						|| url.equals(CommonFunctions.main_url + "ar")
						|| url.equals(CommonFunctions.main_url + "en")
						&& flag) {
					view.stopLoading();
					finish();
					Intent home = new Intent(SearchPageActivity.this,
							MenuSelectionAcitivity.class);
					home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(home);
				}
				else if(url.equals(CommonFunctions.main_url)
						|| url.equals(CommonFunctions.main_url + "ar")
						|| url.equals(CommonFunctions.main_url + "en"))
					flag = true;
//					loaderDialog.show();
				else
					pbLine.setVisibility(View.VISIBLE);
				super.onPageStarted(view, null, favicon);
			}
			
			@Override
			public void onPageFinished(final WebView view, String url) {
				// TODO Auto-generated method stub
					
				Log.d("Finished","Loading"+url);
				pbLine.setVisibility(View.GONE);
				super.onPageFinished(view, url);
					
			}
			
			@Override 
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				pbLine.setProgress(wv1.getProgress());
			}
		});
				
		wv1.loadUrl(url);
		
	}
	
	private void loadAppBar()
	{
		//============== Define a Custom Header for Navigation drawer=================//
		LayoutInflater inflator=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.search_page_header, null);
			
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setCustomView(v);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			try{
				Toolbar parent = (Toolbar) v.getParent(); 
				parent.setContentInsetsAbsolute(0, 0);
			} catch(ClassCastException e) {
				e.printStackTrace(); 
			}
		}
		
		ImageView menuBtn 	= (ImageView) v.findViewById(R.id.iv_menu_btn);
		ImageView ivLogo		= (ImageView) v.findViewById(R.id.iv_logo);
		ImageButton ibHome  = (ImageButton) v.findViewById(R.id.ib_home);
		
		ibHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!loadingURL.contains("/Shared/ProgressPayment")) {
					finish();
					Intent home = new Intent(SearchPageActivity.this, MenuSelectionAcitivity.class);
					home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(home);

				}
			}
		});
		
		ivLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!loadingURL.contains("/Shared/ProgressPayment")) {
					if (loadingURL.contains("/Flight/ShowTicket") ||
							loadingURL.contains("/Hotel/Voucher")) {
						finish();
						Intent home = new Intent(SearchPageActivity.this, MenuSelectionAcitivity.class);
						home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(home);
					} else if (wv1.canGoBack()) {
						wv1.goBack();
					} else {
						finish();
					}
				}
			} 
		});
		
		menuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!loadingURL.contains("/Shared/ProgressPayment")) {
					if (loadingURL.contains("/Flight/ShowTicket") ||
							loadingURL.contains("/Hotel/Voucher")) {
						finish();
						Intent home = new Intent(SearchPageActivity.this, MenuSelectionAcitivity.class);
						home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(home);
					} else if (wv1.canGoBack()) {
						wv1.goBack();
					} else {
						finish();
					}
				} 
			}
		});
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		wv1.clearCache(true);
		wv1.clearFormData();
		wv1.destroy();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (wv1.canGoBack()) {
	        wv1.goBack();
		}else 
		{
			wv1.stopLoading();
			wv1.clearCache(true);
			finish();
	    }
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
	}
	
	public void saveLocale(String lang)
	{
		CommonFunctions.lang = lang;
		String langPref = "Language";
		SharedPreferences prefs = this.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(langPref, lang);
		editor.commit();
	}
	
}
