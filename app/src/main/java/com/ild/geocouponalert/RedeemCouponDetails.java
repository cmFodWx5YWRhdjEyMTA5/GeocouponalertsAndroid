package com.ild.geocouponalert;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ild.geocouponalert.adapter.LocationSpinnerAdapter;
import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.adapter.SelectedMerchantAdapter.ViewcouponAsyncTask;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.imagefile.ImageLoaderFull;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.webmethod.RestCallManager;

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
		List<BusinessLocationMaster> location = new ArrayList<BusinessLocationMaster>();
		ListView listViewLocation;
		LinearLayout linear_tabhost;
		GPSTracker gps;
		Button got_it;
		Button close_loc;
		int position1 = 0;
		Context mContext;
		String bussId;
		
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.coupon_listing);
			mContext = this;
			close_loc = (Button)findViewById(R.id.close_loc);
			close_loc.setOnClickListener(this);
			relLoc = (RelativeLayout)findViewById(R.id.relLoc);
			relImgheading = (RelativeLayout)findViewById(R.id.relImgheading);
			relImgheading.setOnClickListener(this);
			got_it = (Button)findViewById(R.id.got_it);
			got_it.setOnClickListener(this);
			edit = (TextView)findViewById(R.id.levelText);
			edit.setOnClickListener(this);
			//cross = (ImageView)findViewById(R.id.cross);
			//cross.setOnClickListener(this);
			couponLocation = DataStore.getInstance().getCouponLocation();
	        location = couponLocation.get(0).location_details;
	        Collections.sort(location,new DistanceComp1());
	        linear_tabhost = (LinearLayout) findViewById(R.id.linear_tabhost);
			listViewLocation = (ListView) findViewById(R.id.listViewLocation);
			location_textview = (TextView)findViewById(R.id.location_textview);
			location_textview.setOnClickListener(this);
			location_arrow = (ImageView)findViewById(R.id.location_arrow);
			location_arrow.setOnClickListener(this);
			banner_image = (ImageView)findViewById(R.id.banner_image);
			overlayImg = (ImageView)findViewById(R.id.overlayImg);
			overlayImg.setOnClickListener(this);
			relImgTxt = (RelativeLayout)findViewById(R.id.relImgTxt);
			imageLoader=new ImageLoaderFull(this.getApplicationContext());
			all_coupon_location_same = getIntent().getExtras().getString("all_coupon_location_same");
			bussId =  getIntent().getExtras().getString("buss_id");
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
				
				if(location.size()>0){
					for(int i=0;i<location.size();i++){
						if(location_id.equalsIgnoreCase(location.get(i).id)){
							location_name  = location.get(i).address1;
							break;
						}
					}
					location_textview.setText(location_name);
					ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
	   				manageActivation.execute();
					
				}
			}
			
			imageLoader.clearCache();
			imageLoader.DisplayImage(banner_img.trim().toString(), banner_image);
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

				bSuccess=RestCallManager.getInstance().getMerchanFavouriteAndParticluarLocationAlert(COUtils.getDefaults("emailID", mContext),bussId);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				pDialog.dismiss();
				if(bSuccess){
					Intent intent = new Intent(mContext, EditMerchantDetails.class);
					intent.putExtra("buss_id", bussId);
					intent.putExtra("buss_name", buss_name);
					intent.putExtra("banner_img", banner_img);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					/*Intent intent = new Intent(mContext, RedeemCouponDetails.class);

					lst_business_coupon =  DataStore.getInstance().getCouponLocation();
					//Toast.makeText(getApplicationContext(), lst_business_coupon.get(0).all_coupon_location_same, 3000).show();
					buss_name = lst_business_coupon.get(0).buss_name;
					banner_img = lst_business_coupon.get(0).banner_img;
					intent.putExtra("buss_name", buss_name);
					intent.putExtra("banner_img", banner_img);
					intent.putExtra("all_coupon_location_same", "1");
					intent.putExtra("location_id", location_id);

					MerchantListHomePage.this.startActivity(intent);
					MerchantListHomePage.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);*/
				} else {
					Toast.makeText(mContext, "No coupons found.", 3000).show();
				}

				/*if(bSuccess){
					Toast.makeText(activity, "Item edited successfully.", 3000).show();
				} else {
					Toast.makeText(activity, "Edit failed..", 3000).show();
				}*/


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
			// TODO Auto-generated method stub
			if(v==edit){
				//Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
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
		         if(location.size() >0){
		        	 locationadapter = new LocationSpinnerAdapter(this,couponLocation);
		        	 listViewLocation.setAdapter(locationadapter);
		        	 listViewLocation.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View v,
					        int position, long id) {
					    	  
					    	  overlayImg.setVisibility(View.GONE);
							  relLoc.setVisibility(View.GONE);
							  position1 = position;
							  if(position == 0){
								 
								  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = "All Locations";
						    	  location_textview.setText(loc_name);
						    	  loc_id = "0";
						    	  buss_id = vholder.buss_id;
						    	  bus_name = vholder.bus_name;
						    	  bus_banner = vholder.bus_banner;
						    	  
						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
							  }
							  else {
								 
					    	  //cross.setVisibility(View.GONE);
						    	  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = vholder.loc_name;
						    	  location_textview.setText(loc_name);
						    	  loc_id = vholder.loc_id;
						    	  buss_id = vholder.buss_id;
						    	  bus_name = vholder.bus_name;
						    	  bus_banner = vholder.bus_banner;
						    	  
						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
							  }
					      }
		        	 });
		         }   
			}
			if(v == location_arrow){ 
				
				overlayImg.setVisibility(View.VISIBLE);
				relLoc.setVisibility(View.VISIBLE);
				if(location.size() >0){
		        	 locationadapter = new LocationSpinnerAdapter(this,couponLocation);
		        	 listViewLocation.setAdapter(locationadapter);
		        	 listViewLocation.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View v,
					        int position, long id) {
					    	  
					    	  overlayImg.setVisibility(View.GONE);
							  relLoc.setVisibility(View.GONE);
							  position1 = position;
							  if(position == 0){
								  
								  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = "All Locations";
						    	  location_textview.setText(loc_name);
						    	  loc_id = "0";
						    	  buss_id = vholder.buss_id;
						    	  bus_name = vholder.bus_name;
						    	  bus_banner = vholder.bus_banner;
						    	  
						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
							  }
							  else {
					    	  //cross.setVisibility(View.GONE);
						    	  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = vholder.loc_name;
						    	  location_textview.setText(loc_name);
						    	  loc_id = vholder.loc_id;
						    	  buss_id = vholder.buss_id;
						    	  bus_name = vholder.bus_name;
						    	  bus_banner = vholder.bus_banner;
						    	  
						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
							  }
						      
					      }
		        	 });
		         } 
				
			}
			if(v == relImgheading){

				relImgTxt.setVisibility(View.GONE);
				relLoc.setVisibility(View.VISIBLE);
		         if(location.size() >0){
		        	 locationadapter = new LocationSpinnerAdapter(this,couponLocation);
		        	 listViewLocation.setAdapter(locationadapter);
		        	 listViewLocation.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View v,
					        int position, long id) {
					    	  
					    	  overlayImg.setVisibility(View.GONE);
							  relLoc.setVisibility(View.GONE);
							  position1 = position;
							  if(position == 0){
								  
								  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = "All Locations";
						    	  location_textview.setText(loc_name);
						    	  loc_id = "0";
						    	  buss_id = vholder.buss_id;
						    	  bus_name = vholder.bus_name;
						    	  bus_banner = vholder.bus_banner;
						    	  
						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
							  }
							  else {
					    	  //cross.setVisibility(View.GONE);
						    	  ViewHolder vholder = (ViewHolder) v.getTag();
						    	  loc_name = vholder.loc_name;
						    	  location_textview.setText(loc_name);
						    	  loc_id = vholder.loc_id;
						    	  buss_id = vholder.buss_id;
						    	  bus_name = vholder.bus_name;
						    	  bus_banner = vholder.bus_banner;
						    	  
						    	  ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(RedeemCouponDetails.this);
				   				  manageActivation.execute();
							  }
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
				
				if(position1 == 0){
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
					/*finish();
					Intent intent = new Intent(RedeemCouponDetails.this, RedeemCouponDetails.class);
					intent.putExtra("buss_name", bus_name);
					intent.putExtra("banner_img", bus_banner);
					intent.putExtra("all_coupon_location_same", "1");
					intent.putExtra("location_name", loc_name);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in,R.anim.fade_out);*/
					linear_tabhost.setVisibility(View.VISIBLE);
					banner_image.setVisibility(View.VISIBLE);
					//onResume();
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