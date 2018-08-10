package com.ild.geocouponalert;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ild.geocouponalert.adapter.LocationSpinnerAdapter;
import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.adapter.SelectedMerchantAdapter.ViewcouponAsyncTask;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.imagefile.ImageLoaderFull;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.webmethod.RestCallManager;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


	public class RedeemCouponDetails extends TabActivity  implements OnClickListener{
		RelativeLayout settingBack;
		String buss_name,banner_img,all_coupon_location_same,user_latitude,user_longitude,loc_id,buss_id,bus_name,bus_banner,loc_name,location_id,location_name;
		TextView starBucksHeading,location_textview,edit;
		ImageView banner_image,overlayImg,location_arrow,cross;
		public ImageLoaderFull imageLoader;
		RelativeLayout relImgTxt,relImgheading,relLoc;
		LocationSpinnerAdapter locationadapter=null;
		List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
		List<BusinessLocationMaster> locationList = new ArrayList<BusinessLocationMaster>();
		ListView listViewLocation;
		LinearLayout linear_tabhost;
		GPSTracker gps;
		Button got_it;
		Button close_loc;
		Context mContext;
		public BusinessMaster bussObj;
		
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.coupon_listing);
			mContext = this;
			close_loc = findViewById(R.id.close_loc);
			close_loc.setOnClickListener(this);
			relLoc = findViewById(R.id.relLoc);
			relImgheading = findViewById(R.id.relImgheading);
			relImgheading.setOnClickListener(this);
			got_it = findViewById(R.id.got_it);
			got_it.setOnClickListener(this);
			edit = findViewById(R.id.levelText);
			edit.setOnClickListener(this);
	        linear_tabhost =  findViewById(R.id.linear_tabhost);
			listViewLocation =  findViewById(R.id.listViewLocation);
			location_textview = findViewById(R.id.location_textview);
			location_textview.setOnClickListener(this);
			location_arrow = findViewById(R.id.location_arrow);
			location_arrow.setOnClickListener(this);
			banner_image = findViewById(R.id.banner_image);
			overlayImg = findViewById(R.id.overlayImg);
			overlayImg.setOnClickListener(this);
			relImgTxt = findViewById(R.id.relImgTxt);
			imageLoader=new ImageLoaderFull(this.getApplicationContext());

			all_coupon_location_same = getIntent().getExtras().getString("all_coupon_location_same");
			buss_id =  getIntent().getExtras().getString("buss_id");
			bussObj = DataStore.getInstance().GetBusinessdetails(buss_id);
			bus_name = bussObj.name;
			bus_banner = bussObj.banner_img;
			couponLocation = DataStore.getInstance().getCouponLocation();
			locationList = couponLocation.get(0).location_details;
			Collections.sort(locationList,new DistanceComp1());

			if(all_coupon_location_same.equalsIgnoreCase("0")){
				overlayImg.setVisibility(View.VISIBLE);
				relImgTxt.setVisibility(View.VISIBLE);
			} else {
				overlayImg.setVisibility(View.GONE);
				relImgTxt.setVisibility(View.GONE);
				
			}
			linear_tabhost.setVisibility(View.VISIBLE);
			banner_image.setVisibility(View.VISIBLE);
			buss_name = getIntent().getExtras().getString("buss_name");
			banner_img = getIntent().getExtras().getString("banner_img"); 
			location_id = getIntent().getExtras().getString("location_id");
			if(!location_id.equals("")){
				
				if(locationList.size()>0){
					for(int i=0;i<locationList.size();i++){
						if(location_id.equalsIgnoreCase(locationList.get(i).id)){
							location_name  = locationList.get(i).address1;
							break;
						}
					}
					location_textview.setText(location_name);
					ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
	   				manageActivation.execute();
					
				}
			}
			
			/*imageLoader.clearCache();
			imageLoader.DisplayImage(banner_img.trim().toString(), banner_image);*/
			Picasso.with(mContext)
					.load(banner_img.trim().toString())
					.fit()
					.placeholder(R.drawable.no_image_full)
					.into(banner_image);
			settingBack = (RelativeLayout)findViewById(R.id.settingBack);
			starBucksHeading = (TextView)findViewById(R.id.starBucksHeading); 
			starBucksHeading.setText(buss_name);
			settingBack.setOnClickListener(this); 
			
			gps = new GPSTracker(this);
			if(gps.canGetLocation()){ 
				
				user_latitude = String.valueOf(gps.getLatitude());
				user_longitude = String.valueOf(gps.getLongitude());
			} else {
			       	// can't get location
			       	// GPS or Network is not enabled
			       	// Ask user to enable GPS/network in settings
			       	gps.showSettingsAlert(); 
			}

			BusinessLocationMaster locObjAllLocation = new BusinessLocationMaster();
			locObjAllLocation.id="";
			locObjAllLocation.address1 = "All Locations";
			locationList.add(0,locObjAllLocation);
			if (bussObj.hasOnline.equalsIgnoreCase("O")){
				BusinessLocationMaster locObjOnlineLocation = new BusinessLocationMaster();
				locObjOnlineLocation.id="O";
				locObjOnlineLocation.address1 = "Online Coupons";
				locationList.add(1,locObjOnlineLocation);
			}

			tabrefresh();
				
		}
		
		public void tabrefresh(){
			//super.onResume();
			TabHost tabHost = getTabHost();   
			tabHost.clearAllTabs();
			
			// Android tab
			Intent intentCoupons = new Intent().setClass(this, CouponRedeemFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			TabSpec tabSpecCoupons = tabHost 
				.newTabSpec("Coupons")
				.setIndicator("Coupons")
				.setContent(intentCoupons); 
				
			
			// Android tab
			Intent intentRedeemed = new Intent().setClass(this, RedeemFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			TabSpec tabSpecRedeemed = tabHost
				.newTabSpec("Redeemed")
				.setIndicator("Redeemed")
 				.setContent(intentRedeemed);

			// Apple tab
			Intent intentLocations = new Intent().setClass(this, LocationRedeemFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			TabSpec tabSpecLocations = tabHost
				.newTabSpec("Locations")
				.setIndicator("Locations")
				.setContent(intentLocations); 
			 
			
		
			// add all tabs 
			tabHost.addTab(tabSpecCoupons);
			tabHost.addTab(tabSpecRedeemed);
			tabHost.addTab(tabSpecLocations);
			
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setAllCaps(false); 
			tv.setTextSize(14);
			
			TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
			tv1.setTextColor(Color.parseColor("#ffffff"));
			tv1.setAllCaps(false); 
			 tv1.setTextSize(14);
			 
			TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
			tv2.setTextColor(Color.parseColor("#ffffff"));
			tv2.setAllCaps(false); 
			 tv2.setTextSize(14);

			
			//set Windows tab as default (zero based)
			tabHost.setCurrentTab(0);
		}

		public class EditMerchantAsyncTask extends AsyncTask<Void, Void, Void> {
			boolean bSuccess;
			ProgressDialog pDialog;
			Context mFriendLogin;

			public EditMerchantAsyncTask(Context activity) {
				mFriendLogin = activity;
			}
			@Override
			protected Void doInBackground(Void... params) {

				bSuccess=RestCallManager.getInstance().getMerchanFavouriteAndParticluarLocationAlert(COUtils.getDefaults("emailID", mContext),buss_id);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				pDialog.dismiss();
				if(bSuccess){
					Intent intent = new Intent(mContext, EditMerchantDetails.class);
					intent.putExtra("buss_id", buss_id);
					intent.putExtra("buss_name", buss_name);
					intent.putExtra("banner_img", banner_img);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

				} else {
					Toast.makeText(mContext, "No coupons found.", Toast.LENGTH_LONG).show();
				}

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(mFriendLogin);
				pDialog.setMessage("Loading Please Wait...");
				pDialog.setCancelable(false);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.show();
			}


		}

		@Override
		public void onClick(View v) {
			if(v==edit){
				new EditMerchantAsyncTask(mContext).execute();
			}

			if(v == settingBack){
				Intent intent = new Intent(RedeemCouponDetails.this, MerchantListHomePage.class);
				intent.putExtra("business_id", "");
				intent.putExtra("location_id", "");
				intent.putExtra("new_merchant_notification", "");
				startActivity(intent); 
				//finish();
			}
			if(v == got_it){
				
				overlayImg.setVisibility(View.GONE);
				relImgTxt.setVisibility(View.GONE);
			}
			if(v == location_textview){
				overlayImg.setVisibility(View.VISIBLE);
				relLoc.setVisibility(View.VISIBLE);
		         if(locationList.size() >0){
					 locationadapter = new LocationSpinnerAdapter(this,locationList);
		        	 listViewLocation.setAdapter(locationadapter);
		        	 listViewLocation.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View v,
					        int position, long id) {
					    	  
								  overlayImg.setVisibility(View.GONE);
								  relLoc.setVisibility(View.GONE);
								  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = vholder.loc_name;
						    	  location_textview.setText(loc_name);
						    	  loc_id = vholder.loc_id;

						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
					      }
		        	 });
		         }   
			}
			if(v == location_arrow){
				overlayImg.setVisibility(View.VISIBLE);
				relLoc.setVisibility(View.VISIBLE);
				if(locationList.size() >0){
		        	 locationadapter = new LocationSpinnerAdapter(this,locationList);
		        	 listViewLocation.setAdapter(locationadapter);
		        	 listViewLocation.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View v,
					        int position, long id) {
					    	  
								  overlayImg.setVisibility(View.GONE);
								  relLoc.setVisibility(View.GONE);

						    	  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = vholder.loc_name;
						    	  location_textview.setText(loc_name);
						    	  loc_id = vholder.loc_id;

						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();

					      }
		        	 });
		         } 
				
			}
			if(v == relImgheading){
				relImgTxt.setVisibility(View.GONE);
				relLoc.setVisibility(View.VISIBLE);
		         if(locationList.size() >0){
					 locationadapter = new LocationSpinnerAdapter(this,locationList);
		        	 listViewLocation.setAdapter(locationadapter);
		        	 listViewLocation.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View v,
					        int position, long id) {
					    	  
					    	  overlayImg.setVisibility(View.GONE);
							  relLoc.setVisibility(View.GONE);

						    	  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = vholder.loc_name;
						    	  location_textview.setText(loc_name);
						    	  loc_id = vholder.loc_id;

						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
					      }
		        	 });
		         }
			}
			
			if(v == close_loc){
				overlayImg.setVisibility(View.GONE);
				relLoc.setVisibility(View.GONE);
			}
		}
		
		public class ViewcouponAsyncTask extends AsyncTask<Void, Void, Void> {
			boolean bSuccess;
			ProgressDialog pDialog;
			Activity mFriendLogin; 
			
			public ViewcouponAsyncTask(Activity activity) { 
				mFriendLogin = activity;
			}
			@Override
			protected Void doInBackground(Void... params) {   
				
				if (loc_id.equalsIgnoreCase("")||loc_id.equalsIgnoreCase("O")){
					bSuccess=RestCallManager.getInstance().downloadAllLocationOfBusiness(Secure.getString(mFriendLogin.getContentResolver(),Secure.ANDROID_ID),buss_id,user_latitude,user_longitude,COUtils.getDefaults("emailID", mFriendLogin));
				} else {
					bSuccess=RestCallManager.getInstance().downloadAllLocationCouponByLocationId(Secure.getString(mFriendLogin.getContentResolver(),Secure.ANDROID_ID),buss_id,user_latitude,user_longitude,COUtils.getDefaults("emailID", mFriendLogin),loc_id);
				}
				return null; 
			}  
			
			@Override  
			protected void onPostExecute(Void result) {   
				
				pDialog.dismiss();
				if(bSuccess){
					linear_tabhost.setVisibility(View.VISIBLE);
					banner_image.setVisibility(View.VISIBLE);
					tabrefresh();
				}
			}   
	  
			@Override
			protected void onPreExecute() { 
				super.onPreExecute();
				pDialog = new ProgressDialog(mFriendLogin);
				pDialog.setMessage("Loading Please Wait...");
				pDialog.setCancelable(false);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.show(); 
			}
		}
	}