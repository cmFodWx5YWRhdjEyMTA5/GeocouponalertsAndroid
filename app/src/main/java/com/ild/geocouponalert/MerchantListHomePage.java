package com.ild.geocouponalert;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import static com.ild.geocouponalert.CommonUtilities.EXTRA_MESSAGE;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.NavDrawerListAdapter;
import com.ild.geocouponalert.adapter.SelectedMerchantAdapter;
import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.NavDrawerItem;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
import com.ild.geocouponalert.webmethod.RestCallManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MerchantListHomePage extends AppCompatActivity implements OnClickListener, OnMapReadyCallback {
	
	Context mContext;
	ListView listViewMerchant;
	List<BusinessMaster> lst_business;
	List<BusinessMaster> lst_filtered_buss;
	List<BusinessCouponLocation> lst_business_coupon;
	SelectedMerchantAdapter selectedmerchantadapter=null;
    CategoryAdapter catadapter=null;
    ImageView bgtransparent,category_arrow,category_down_arrow,menuicon;
    GPSTracker gps;
    TextView category_textview;
    ListView listViewCategory;
    String buss_id,user_latitude,user_longitude,buss_name,banner_img,location_id;
    RelativeLayout relheading;
    int push_notification_mode;
 // LogCat tag
  	private static final String TAG = MerchantListHomePage.class.getSimpleName();
 	/**
 	 * Geofences Array 
 	 */
 	ArrayList<Geofence> mGeofences;
 	/**
 	 * Geofence Coordinates
 	 */
 	ArrayList<LatLng> mGeofenceCoordinates;
 	/**
 	 * Geofence Store
 	 */
 	private GeofenceStore mGeofenceStore;
 	
 // Alert dialog manager
 	AlertDialogManager alert = new AlertDialogManager();
	// Connection detector
	ConnectionDetector cd;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	String regId;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	RelativeLayout listRel,mapRel,searchRel;
	ImageView imgList,imgMap,imgSearch;
	TextView list,map,search,edit;
	boolean doubleBackToExitPressedOnce = false;
	// Google Map
	private GoogleMap googleMap;
	SupportMapFragment mapFragment;
	View mapView;

	LinearLayout gmapLin;
	RelativeLayout relcat,searchMerchantRel,catDropdownRel;

	List<BusinessLocationMaster> lst_bus_location = new ArrayList<>();
	List<BusinessMaster> new_lst_filtered_buss = null;
	EditText searchText;

	private static final int NORMAL = 0;
	private static final int PUSH_NEW_MERCHANT = 1;
	private static final int PUSH_EXPIRING_CARD_7_DAYS = 2;
	private static final int PUSH_EXPIRING_CARD_1_DAY = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_list_home_page_main);
		
		mContext=this;
		lst_business = new ArrayList<>();
		lst_filtered_buss = new ArrayList<>();
		lst_business_coupon = new ArrayList<>();
		mGeofences = new ArrayList<>();
		mGeofenceCoordinates = new ArrayList<>();
		initView();
	}
	private void initializeMap() {
		mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.gmap);
		mapView = mapFragment.getView();
		mapFragment.getMapAsync(this);
	}
	@Override
	public void onMapReady(GoogleMap googleMapDefault) {
		googleMap = googleMapDefault;
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// Get the button view
		View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
		// and next place it, on bottom right (as Google Maps app)
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
				locationButton.getLayoutParams();
		// position on right bottom
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layoutParams.setMargins(0, 0, 30, 30);

		//Showing Current Location
		googleMap.setMyLocationEnabled(true); // false to disable
		//googleMap.getUiSettings().setZoomControlsEnabled(true);
		//googleMap.getUiSettings().setCompassEnabled(true);
		//Location Button
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		CameraUpdate center =
				CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(user_latitude), Double.parseDouble(user_longitude)));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
		lst_business = DataStore.getInstance().getSelectedBusiness();
		populateMarkerOnMap(lst_business);
	}

	public void populateMarkerOnMap(List<BusinessMaster> lst_business){
		try {
			googleMap.clear();
			if(lst_business.size()>0){
				for(int i=0;i<lst_business.size();i++){

					lst_bus_location = lst_business.get(i).geofence_location_details;
					for(int j=0;j<lst_bus_location.size();j++) {
						Location dest_location = new Location("");
						final Double latt = Double.valueOf(lst_bus_location.get(j).buss_loc_lat);
						final Double longtt = Double.valueOf(lst_bus_location.get(j).buss_loc_long);
						dest_location.setLatitude(latt);
						dest_location.setLongitude(longtt);

						String address;
						if (lst_bus_location.get(j).address2.toString().equalsIgnoreCase("")) {
							address = lst_bus_location.get(j).address1 + ", " + lst_bus_location.get(j).city;
						} else {
							address = lst_bus_location.get(j).address1 + ", " +
									lst_bus_location.get(j).address2 + ", " + lst_bus_location.get(j).city;
						}

						MarkerOptions marker = new MarkerOptions().
								position(new LatLng(latt, longtt)).
								title(lst_business.get(i).name)
								.snippet(address);

						// adding marker

						googleMap.addMarker(marker);

						googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
							@Override
							public void onInfoWindowClick(Marker arg0) {
								LatLng coordinate2 = arg0.getPosition();

								Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
										Uri.parse("http://maps.google.com/maps?daddr=" + coordinate2.latitude + "," + coordinate2.longitude));
								startActivity(intent);
							}
						});
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private TextWatcher searchtextWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		public void afterTextChanged(Editable s) {
		}
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//if (s.length() != 0) {
				//Toast.makeText(mContext,"You have entered : " + searchText.getText(),Toast.LENGTH_SHORT).show();
				selectedmerchantadapter.getFilter().filter(s.toString());
			//}
		}
	};
	@SuppressWarnings("ResourceType")
	private void initView(){

		edit = findViewById(R.id.levelText);
		edit.setOnClickListener(this);
		list      = findViewById(R.id.list);
		map       = findViewById(R.id.map);
		search    = findViewById(R.id.search);
		searchText =  findViewById(R.id.searchText);
		searchText.addTextChangedListener(searchtextWatcher);
		relcat =  findViewById(R.id.relcat);
		relcat.setOnClickListener(this);
		listRel   =  findViewById(R.id.listRel);
		listRel.setOnClickListener(this);
		mapRel    =  findViewById(R.id.mapRel);
		mapRel.setOnClickListener(this);
		searchRel =  findViewById(R.id.searchRel);
		searchRel.setOnClickListener(this);
		searchMerchantRel =  findViewById(R.id.searchMerchantRel);
		catDropdownRel =  findViewById(R.id.catDropdownRel);
		imgList     = findViewById(R.id.imgList);
		imgMap      = findViewById(R.id.imgMap);
		imgSearch   = findViewById(R.id.imgSearch);

		gmapLin =  findViewById(R.id.gmapLin);
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_after_login);
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons_after_login);
		mDrawerLayout =  findViewById(R.id.drawer_layout);
		mDrawerList   =  findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<>();
		// adding nav drawer items to array
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		//custom header setting for menu list
		View header = getLayoutInflater().inflate(R.layout.navheader, null);
		mDrawerList.addHeaderView(header);
		mDrawerList.setAdapter(adapter);
		// menu list witdh increase
		int width = (int) (getResources().getDisplayMetrics().widthPixels/1.2);
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
		params.width = width;
		mDrawerList.setLayoutParams(params);

		category_down_arrow = findViewById(R.id.category_down_arrow);
		category_down_arrow.setOnClickListener(this);
		//categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
		bgtransparent = findViewById(R.id.bgtransparent);
		category_textview = (TextView)findViewById(R.id.category_textview);
		//category_textview.setOnClickListener(this);
		//category_textview.setVisibility(View.GONE);
		category_arrow = findViewById(R.id.category_arrow);
		//category_arrow.setOnClickListener(this);
		
		menuicon = findViewById(R.id.menuicon);
		menuicon.setOnClickListener(this);

		listViewMerchant = findViewById(R.id.listViewMerchant);
		listViewCategory = findViewById(R.id.listViewCategory);
		relheading = findViewById(R.id.relheading);

		gps = new GPSTracker(mContext);
		if(gps.canGetLocation()){

			user_latitude = String.valueOf(gps.getLatitude());
			user_longitude = String.valueOf(gps.getLongitude());
		}

	if(!getIntent().getExtras().getString("business_id").equalsIgnoreCase("")
			&& !getIntent().getExtras().getString("location_id").equalsIgnoreCase("")){ // call when click on notification
			relheading.setVisibility(View.GONE);
			buss_id = getIntent().getExtras().getString("business_id");
			location_id = getIntent().getExtras().getString("location_id");

			ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(this);
			manageActivation.execute();
			
	} else {
			long storeDateInMilis = COUtils.getDefaultsLong("myDateKey",mContext);
			if (storeDateInMilis!=0){
				Date myCurrentDates = Calendar.getInstance().getTime();
				long myCurrentDatesInMilis = myCurrentDates.getTime();
				long difference = Math.abs(myCurrentDatesInMilis - storeDateInMilis);
				if (difference>24*60*60*1000){
					new CheckLocationRangeAsyncTask(mContext).execute();
				}else{
					new SelectedMerchantAsyncTask(this).execute(COUtils.getDefaults("loadNearByLocation",mContext));
				}
			}else{
				new CheckLocationRangeAsyncTask(mContext).execute();
			}
	}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			displayViewAfterLogin(position);
		}
	}

	private void displayViewAfterLogin(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		/*case 0:
			fragment = new ReservationFragment();
			break;*/
			case 1:
				//closing the drawer if it is opened
				if(mDrawerLayout.isDrawerOpen(Gravity.START)){
					mDrawerLayout.closeDrawer(Gravity.START);
				}
				break;
			case 2:
				Intent intent = new Intent(MerchantListHomePage.this, ActivationCodeScreen.class);
				intent.putExtra("mode", "1");
				startActivity(intent);
				break;
			/*case 3:
				if(gps.canGetLocation()){
					Intent mintent = new Intent(MerchantListHomePage.this, NearbyMerchants.class);
					startActivity(mintent);
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}
				break;*/
			case 3:
				Intent mintent = new Intent(MerchantListHomePage.this, SuggestMerchant.class);
				startActivity(mintent);
				break;
			case 4:
				Intent apintent = new Intent(MerchantListHomePage.this, AlertSetting.class);
				startActivity(apintent);
				break;
			case 5:
				Intent cpintent = new Intent(MerchantListHomePage.this, ChangePassword.class);
				startActivity(cpintent);
				break;
			case 6:
				Intent czintent = new Intent(MerchantListHomePage.this, ChangeZipCode.class);
				startActivity(czintent);
				break;
			case 7:
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-terms-and-conditions"));
				startActivity(browserIntent);
				break;
			case 8:
				Intent ppbrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-privacy-policy"));
				startActivity(ppbrowserIntent);
				break;

			default:
				break;
		}
	}
	
	public class SelectedMerchantAsyncTask extends AsyncTask<String, Void, Void> {

		ProgressDialog pDialog;
		Context mFriendLogin;
		boolean bSuccess;
		
		public SelectedMerchantAsyncTask(Context activity) {
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(String... params) {
			String loadNearByLocation = params[0];
			bSuccess=RestCallManager.getInstance().getSelectedMerchant(Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID),
					COUtils.getDefaults("emailID", mContext),
					"Android", COUtils.getDefaults("DeviceTokenFCM",mContext),"Android",user_latitude,user_longitude,loadNearByLocation);
			return null;
		} 

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			if(bSuccess) {
				push_notification_mode = getIntent().getIntExtra("push_notification_mode", 0);
				if (push_notification_mode == NORMAL){
					lst_business = DataStore.getInstance().getSelectedBusiness();
					selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_business);
					listViewMerchant.setAdapter(selectedmerchantadapter);
					selectedmerchantadapter.notifyDataSetChanged();
				}
				else if(push_notification_mode==PUSH_NEW_MERCHANT){
					category_textview.setText("New Merchants");
					for(int i=0;i<lst_business.size();i++){
				    	   if(lst_business.get(i).isNewBusiness.equalsIgnoreCase("yes")){
				    		   lst_filtered_buss.add(lst_business.get(i));
				    	   }
				       }
		    		   selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_filtered_buss);
		    		   listViewMerchant.setAdapter(selectedmerchantadapter);
					   selectedmerchantadapter.notifyDataSetChanged();
				}
				else if(push_notification_mode==PUSH_EXPIRING_CARD_7_DAYS || push_notification_mode==PUSH_EXPIRING_CARD_1_DAY){
					//2,3 card expiring alert
					LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					View promptView = layoutInflater2.inflate(R.layout.alert_card_expiring_days, null);
					AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					alertDialogBuilder2.setView(promptView);
					final AlertDialog alertD2 = alertDialogBuilder2.create();
					TextView alertTxt =  promptView.findViewById(R.id.alertTxt);
					TextView topTxt =  promptView.findViewById(R.id.topTxt);
					if (push_notification_mode==PUSH_EXPIRING_CARD_7_DAYS)
						topTxt.setText("Only 7 days Left");
					else
						topTxt.setText("Only 1 days Left");
					Button btnSupport =  promptView.findViewById(R.id.btnSupportCharity);
					btnSupport.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v){
							alertD2.dismiss();
							try {
								JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("pushNotificationJsonString"));
								String web_url = jsonObj.getString("url");
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web_url));
								startActivity(browserIntent);
							}
							catch (JSONException e){
								Log.e(TAG, "Exception: " + e.getMessage());
							}

						}
					});
					Button btnClose =  promptView.findViewById(R.id.btnClose);
					btnClose.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertD2.dismiss();
						}
					});

					alertD2.setCanceledOnTouchOutside(false);
					alertD2.show();
				}

   				gps = new GPSTracker(mContext);
   				if(gps.canGetLocation()){
   						String geoLocationEnabled=COUtils.getDefaults("geoLocationEnabled", mContext);
   						if(geoLocationEnabled==null){
								LayoutInflater layoutInflater = LayoutInflater.from(mContext);
								View promptView = layoutInflater.inflate(R.layout.alert_location, null);
								//promptView.setBackgroundColor(Color.TRANSPARENT);
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
								alertDialogBuilder.setView(promptView);
								final AlertDialog alertD = alertDialogBuilder.create();

								final TextView dialogNo =  promptView.findViewById(R.id.btnDontAllow);
								final TextView dialogYes = promptView.findViewById(R.id.btnAllow);
								// if button is clicked, close the custom dialog
								dialogNo.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										COUtils.setDefaults("geoLocationEnabled", "0", mContext);
										alertD.dismiss();
									}
								});
								dialogYes.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										COUtils.setDefaults("geoLocationEnabled", "1", mContext);
										geofencingService(lst_business);
										alertD.dismiss();

										LayoutInflater layoutInflater = LayoutInflater.from(mContext);
										View promptView = layoutInflater.inflate(R.layout.alert_location, null);
										//promptView.setBackgroundColor(Color.TRANSPARENT);
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
										alertDialogBuilder.setView(promptView);
										final AlertDialog alertD2 = alertDialogBuilder.create();

										final TextView dialogNo2 = (TextView) promptView.findViewById(R.id.btnDontAllow);
										final TextView dialogYes2 = (TextView) promptView.findViewById(R.id.btnAllow);
										final TextView coupon_alert = (TextView) promptView.findViewById(R.id.coupon_alert);
										final TextView coupon_alert_sub = (TextView) promptView.findViewById(R.id.coupon_alert_sub);

										dialogNo2.setText(R.string.txt_dont_allow);
										coupon_alert.setText(R.string.location_alert_background);
										coupon_alert_sub.setText(R.string.location_alert_background_sub);

										dialogNo2.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												alertD2.dismiss();
											}
										});

										dialogYes2.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												alertD2.dismiss();
											}
										});
										alertD2.show();
									}
								});
								alertD.show();
								}
								else if(geoLocationEnabled.equalsIgnoreCase("1")){
									geofencingService(lst_business);
								}
   				}
   				else {
   			       	// can't get location
   			       	// GPS or Network is not enabled
   			       	// Ask user to enable GPS/network in settings
   			       	gps.showSettingsAlert();
   			    }
				initializeMap(); // Initialize google map

			}  else{

				if(FOGlobalVariable.IsActivationCodeAlreadyExist.equalsIgnoreCase("1") ){

					LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					View promptView = layoutInflater2.inflate(R.layout.alert_card_expire, null);
					AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					alertDialogBuilder2.setView(promptView);
					final AlertDialog alertD2 = alertDialogBuilder2.create();
					TextView alertTxt = (TextView) promptView.findViewById(R.id.alertTxt);
					TextView topTxt = (TextView) promptView.findViewById(R.id.topTxt);
					topTxt.setText("Expired");
					alertTxt.setText("All card/cards are expired. Please add new card to continue.");

					TextView btnAddNewCard =  promptView.findViewById(R.id.btnAddNewCard);
					btnAddNewCard.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v){
							alertD2.dismiss();
							finish();
							Intent nintent = new Intent(mContext,ActivationCodeScreen.class);
							nintent.putExtra("mode","0");
							startActivity(nintent);
						}
					});

					TextView btnClose =  promptView.findViewById(R.id.btnClose);
					btnClose.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertD2.dismiss();
							System.exit(0);
						}
					});

					alertD2.setCanceledOnTouchOutside(false);
					alertD2.show();

				}else if(FOGlobalVariable.IsActivationCodeAlreadyExist.equalsIgnoreCase("2") ){
					LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
					AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					alertDialogBuilder2.setView(promptView);
					final AlertDialog alertD2 = alertDialogBuilder2.create();
					TextView alertTxt =  promptView.findViewById(R.id.alertTxt);
					TextView topTxt =  promptView.findViewById(R.id.topTxt);
					topTxt.setText("Oops!!!!");
					alertTxt.setText("There is no any merchant available in your area.");
					RelativeLayout relBottominner = (RelativeLayout) promptView.findViewById(R.id.relBottominner);
					relBottominner.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							System.exit(0);
						}
					});
					alertD2.setCanceledOnTouchOutside(false);
					alertD2.show();

				}
				else{
					LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
					AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					alertDialogBuilder2.setView(promptView);
					final AlertDialog alertD2 = alertDialogBuilder2.create();
					TextView alertTxt =  promptView.findViewById(R.id.alertTxt);
					TextView topTxt =  promptView.findViewById(R.id.topTxt);
					topTxt.setText("Oops!!!!");
					alertTxt.setText("There are some problem with the server.");
					RelativeLayout relBottominner = (RelativeLayout) promptView.findViewById(R.id.relBottominner);
					relBottominner.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							System.exit(0);
						}
					});
					alertD2.setCanceledOnTouchOutside(false);
					alertD2.show();
				}
			}
			 
		}   

		@Override 
		protected void onPreExecute() {
			
			pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show(); 
		}
	}

	public class CheckLocationRangeAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Context mFriendLogin;
		boolean bSuccess;

		public CheckLocationRangeAsyncTask(Context context)
		{
			mFriendLogin = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			bSuccess=RestCallManager.getInstance().checkDistanceRange(COUtils.getDefaults("emailID", mContext),user_latitude,user_longitude);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
				if(bSuccess){
					new SelectedMerchantAsyncTask(mFriendLogin).execute("0");
				}
				else{
					LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					View promptView = layoutInflater2.inflate(R.layout.alert_card_expire, null);
					AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					alertDialogBuilder2.setView(promptView);
					final AlertDialog alertD2 = alertDialogBuilder2.create();
					TextView alertTxt =  promptView.findViewById(R.id.alertTxt);
					TextView topTxt =  promptView.findViewById(R.id.topTxt);
					topTxt.setText("Nearby Merchant");
					alertTxt.setText(FOGlobalVariable.IsActivationCodeAlreadyExist);
					TextView btnNO =  promptView.findViewById(R.id.btnAddNewCard);
					btnNO.setText("NO");
					btnNO.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v){
							alertD2.dismiss();
							Date currentDate = Calendar.getInstance().getTime();
							long millis = currentDate.getTime();
							COUtils.setDefaultsLong("myDateKey", millis,mContext);
							COUtils.setDefaults("loadNearByLocation","0" ,mContext);
							new SelectedMerchantAsyncTask(mContext).execute(COUtils.getDefaults("loadNearByLocation",mContext));
						}
					});
					TextView btnYES =  promptView.findViewById(R.id.btnClose);
					btnYES.setText("YES");
					btnYES.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertD2.dismiss();
							Date currentDate = Calendar.getInstance().getTime();
							long millis = currentDate.getTime();
							COUtils.setDefaultsLong("myDateKey", millis,mContext);
							COUtils.setDefaults("loadNearByLocation","1" ,mContext);
							new SelectedMerchantAsyncTask(mContext).execute(COUtils.getDefaults("loadNearByLocation",mContext));
						}
					});
					alertD2.setCanceledOnTouchOutside(false);
					alertD2.show();
			}

		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
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
				
				bSuccess=RestCallManager.getInstance().downloadAllLocationOfBusiness(Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID),buss_id,user_latitude,user_longitude,COUtils.getDefaults("emailID", mContext));
				return null; 
			}

			@Override  
			protected void onPostExecute(Void result) {    
				
				pDialog.dismiss();
				if(bSuccess){  
					Intent intent = new Intent(mContext, RedeemCouponDetails.class);
					
					lst_business_coupon =  DataStore.getInstance().getCouponLocation();
					//Toast.makeText(getApplicationContext(), lst_business_coupon.get(0).all_coupon_location_same, 3000).show();
					buss_name = lst_business_coupon.get(0).buss_name;
					banner_img = lst_business_coupon.get(0).banner_img;
					intent.putExtra("buss_id", lst_business_coupon.get(0).buss_id);
					intent.putExtra("buss_name", buss_name);
					intent.putExtra("banner_img", banner_img);
					intent.putExtra("all_coupon_location_same", "1");
					intent.putExtra("location_id", location_id);
					
					MerchantListHomePage.this.startActivity(intent);
					MerchantListHomePage.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
				} else {
					Toast.makeText(mContext, "No coupons found.", Toast.LENGTH_LONG).show();
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

		if(v==edit){
			Intent czintent = new Intent(MerchantListHomePage.this, AlertSetting.class);
			startActivity(czintent);
		}

		if(v==listRel){
			searchText.setText(""); // set search text to null

			searchMerchantRel.setVisibility(View.GONE);
			relcat.setVisibility(View.VISIBLE);
			bgtransparent.setVisibility(View.GONE);
			listViewMerchant.setVisibility(View.VISIBLE);
			listViewCategory.setVisibility(View.GONE);
			category_down_arrow.setVisibility(View.GONE);
			gmapLin.setVisibility(View.GONE);

			list.setTextColor(Color.WHITE);
			map.setTextColor(Color.BLACK);
			search.setTextColor(Color.BLACK);

			imgList.setImageResource(R.drawable.list);
			imgMap.setImageResource(R.drawable.map_gray);
			imgSearch.setImageResource(R.drawable.search_gray);

			listRel.setBackground( getResources().getDrawable(R.drawable.segment_active));
			mapRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive));
			searchRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive));
		}
		if(v==mapRel){
			searchText.setText("");  // set search text to null

			searchMerchantRel.setVisibility(View.GONE);
			relcat.setVisibility(View.VISIBLE);
			bgtransparent.setVisibility(View.GONE);
			listViewMerchant.setVisibility(View.GONE);
			listViewCategory.setVisibility(View.GONE);
			category_down_arrow.setVisibility(View.GONE);
			gmapLin.setVisibility(View.VISIBLE);

			list.setTextColor(Color.BLACK);
			map.setTextColor(Color.WHITE);
			search.setTextColor(Color.BLACK);

			imgList.setImageResource(R.drawable.list_gray);
			imgMap.setImageResource(R.drawable.map);
			imgSearch.setImageResource(R.drawable.search_gray);

			listRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive));
			mapRel.setBackground( getResources().getDrawable(R.drawable.segment_active));
			searchRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive));
		}
		if(v==searchRel){
			relcat.setVisibility(View.GONE);
			searchMerchantRel.setVisibility(View.VISIBLE);

			bgtransparent.setVisibility(View.GONE);
			listViewMerchant.setVisibility(View.VISIBLE);
			listViewCategory.setVisibility(View.GONE);
			category_down_arrow.setVisibility(View.GONE);
			gmapLin.setVisibility(View.GONE);

			list.setTextColor(Color.BLACK);
			map.setTextColor(Color.BLACK);
			search.setTextColor(Color.WHITE);

			imgList.setImageResource(R.drawable.list_gray);
			imgMap.setImageResource(R.drawable.map_gray);
			imgSearch.setImageResource(R.drawable.search);

			listRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive));
			mapRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive));
			searchRel.setBackground( getResources().getDrawable(R.drawable.segment_active));
		}

		/*if(v == settingsicon){
			Intent intent = new Intent(MerchantListHomePage.this, SettingScreen.class);
			startActivity(intent);  
		}
		if(v == addpostcardicon){
			Intent intent = new Intent(MerchantListHomePage.this, ActivationCodeScreen.class);
			intent.putExtra("mode", "1");
			startActivity(intent);   
		}*/
		if(v == menuicon){

			if(!mDrawerLayout.isDrawerOpen(Gravity.START)){
				mDrawerLayout.openDrawer(Gravity.START);
			}else{
				mDrawerLayout.closeDrawer(Gravity.START);
			}
		}
		if(v == relcat){

			if(listViewCategory.getVisibility() == View.VISIBLE) {
				closeCategoryDropdown();
			}else {
				listViewCategory.setVisibility(View.VISIBLE);
				bgtransparent.setVisibility(View.VISIBLE);
				listViewCategory.setVisibility(View.VISIBLE);
				category_down_arrow.setVisibility(View.VISIBLE);
				catadapter = new CategoryAdapter(MerchantListHomePage.this, lst_business.get(0).cat_details);
				listViewCategory.setAdapter(catadapter);
				listViewCategory.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
											int position, long id) {

						category_down_arrow.setVisibility(View.GONE);
						bgtransparent.setVisibility(View.GONE);
						ViewHolder vholder = (ViewHolder) v.getTag();
						String category_name = vholder.category_name;
						String category_id = vholder.category_id;
						listViewCategory.setVisibility(View.GONE);
						category_textview.setText(category_name);


						if (category_id.equalsIgnoreCase("1")) {
							new_lst_filtered_buss = lst_business;
							selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this, lst_business);
							listViewMerchant.setAdapter(selectedmerchantadapter);
							selectedmerchantadapter.notifyDataSetChanged();
						} else if (category_id.equalsIgnoreCase("8")) {

							lst_filtered_buss.clear();
							for (int i = 0; i < lst_business.size(); i++) {
								if (lst_business.get(i).isNewBusiness.equalsIgnoreCase("yes")) {
									lst_filtered_buss.add(lst_business.get(i));
								}
							}
							new_lst_filtered_buss = lst_filtered_buss;
							selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this, lst_filtered_buss);
							listViewMerchant.setAdapter(selectedmerchantadapter);
							selectedmerchantadapter.notifyDataSetChanged();

						}
						else if (category_id.equalsIgnoreCase("9")) {

							lst_filtered_buss.clear();
							for (int i = 0; i < lst_business.size(); i++) {
								if (lst_business.get(i).isFavourite.equalsIgnoreCase("1")) {
									lst_filtered_buss.add(lst_business.get(i));
								}
							}
							new_lst_filtered_buss = lst_filtered_buss;
							selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this, lst_filtered_buss);
							listViewMerchant.setAdapter(selectedmerchantadapter);
							selectedmerchantadapter.notifyDataSetChanged();

						}

						else {

							lst_filtered_buss.clear();
							for (int i = 0; i < lst_business.size(); i++) {
								if (category_id.equalsIgnoreCase(lst_business.get(i).cat_id)) {
									lst_filtered_buss.add(lst_business.get(i));
								}
							}

							if (lst_filtered_buss.size() > 0) {
								new_lst_filtered_buss = lst_filtered_buss;
								selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this, lst_filtered_buss);
								listViewMerchant.setAdapter(selectedmerchantadapter);
								selectedmerchantadapter.notifyDataSetChanged();
							} else {
								listViewMerchant.setAdapter(null);
								selectedmerchantadapter.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(), "There is no merchant in this category.", Toast.LENGTH_LONG).show();
							}
						}


						populateMarkerOnMap(new_lst_filtered_buss); // filtering map pointing

					}
				});
			}
		}
		/*if(v == category_arrow){

			category_down_arrow.setVisibility(View.VISIBLE);
			bgtransparent.setVisibility(View.VISIBLE);
			listViewCategory.setVisibility(View.VISIBLE);
			listViewMerchant.setVisibility(View.GONE);
			catadapter = new CategoryAdapter(MerchantListHomePage.this,lst_business.get(0).cat_details);
			listViewCategory.setAdapter(catadapter);
			listViewCategory.setOnItemClickListener(new OnItemClickListener() {
			      public void onItemClick(AdapterView<?> parent, View v,
			        int position, long id) {
			       
			       category_down_arrow.setVisibility(View.GONE);
			       bgtransparent.setVisibility(View.GONE);
			       ViewHolder vholder = (ViewHolder) v.getTag();
			       String category_name = vholder.category_name;
			       String category_id = vholder.category_id;
			       listViewCategory.setVisibility(View.GONE);
				   listViewMerchant.setVisibility(View.VISIBLE);
			       category_textview.setText(category_name);
			       
			      
			       if(category_id.equalsIgnoreCase("1")){
			    	   selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_business); 
			    	   listViewMerchant.setAdapter(selectedmerchantadapter);
					   selectedmerchantadapter.notifyDataSetChanged();
		    	   } else if(category_id.equalsIgnoreCase("8")){
		    		   
		    		   lst_filtered_buss.clear();
		    		   for(int i=0;i<lst_business.size();i++){
				    	   if(lst_business.get(i).isNewBusiness.equalsIgnoreCase("yes")){
				    		   lst_filtered_buss.add(lst_business.get(i));
				    	   }
				       }
		    		   
		    		   selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_filtered_buss);
		    		   listViewMerchant.setAdapter(selectedmerchantadapter);
					   selectedmerchantadapter.notifyDataSetChanged();
			       
		    	   } else {
		    		   
		    		   lst_filtered_buss.clear();
				       for(int i=0;i<lst_business.size();i++){
				    	   if(category_id.equalsIgnoreCase(lst_business.get(i).cat_id)){
				    		   lst_filtered_buss.add(lst_business.get(i));
				    	   }
				       }
				       
			    	   if(lst_filtered_buss.size()>0){
			    		   selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_filtered_buss);
			    		   listViewMerchant.setAdapter(selectedmerchantadapter);
						   selectedmerchantadapter.notifyDataSetChanged();
			    	   } else {
			    		   listViewMerchant.setAdapter(null);
						   selectedmerchantadapter.notifyDataSetChanged();
			    		   Toast.makeText(getApplicationContext(), "There are no merchants in this category.", 2000).show();
			    	   }
			       }
		    	   
			       
			       //Toast.makeText(getApplicationContext(), category_id, 3000).show(); 

			      } 
			     });    
			
		}*/
		
		if(v == category_down_arrow){
			
			int firstVisibleItem = listViewCategory.getFirstVisiblePosition();
            listViewCategory.setSelection(firstVisibleItem+1);
		}
		
		
	}
	
	private void geofencingService(List<BusinessMaster> lst_business){ 

		//GeofenceStore storeObj=new GeofenceStore(mContext, mGeofences);
		//storeObj.removeGeofence();
		mGeofences.clear();
		for(int i=0;i<lst_business.size();i++){
			
			List<BusinessLocationMaster> business_detials=lst_business.get(i).geofence_location_details;

			mGeofenceCoordinates.clear();
			for(int j=0;j<business_detials.size();j++){
				
				if(business_detials.get(j).geo_alert_status.equalsIgnoreCase("1")){
				
				mGeofenceCoordinates.add(new LatLng(Double.valueOf(business_detials.get(j).getBuss_loc_lat().toString()),Double.valueOf(business_detials.get(j).getBuss_loc_long().toString())));
				}
				
			}
	 			
			for(int k=0;k<mGeofenceCoordinates.size();k++){
				mGeofences.add(new Geofence.Builder()
			 			.setRequestId(business_detials.get(k).getId().toString()+"|"+business_detials.get(k).getBuss_id().toString()+"|"
			 					+business_detials.get(k).getName().toString())
						// The coordinates of the center of the geofence and the radius in meters.
						.setCircularRegion(mGeofenceCoordinates.get(k).latitude, mGeofenceCoordinates.get(k).longitude, FOGlobalVariable.GEOFENCE_RADIUS_IN_METERS) 
						.setExpirationDuration(Geofence.NEVER_EXPIRE)
						// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
						.setLoiteringDelay(30000) 
						.setTransitionTypes(
								Geofence.GEOFENCE_TRANSITION_ENTER).build());
				
				//Log.e("LatLong","lat:"+mGeofenceCoordinates.get(k).latitude+"long:"+mGeofenceCoordinates.get(k).longitude);
				//Log.e("Details",business_detials.get(k).getId().toString()+"|"+business_detials.get(k).getBuss_id().toString()+"|"+business_detials.get(k).getName().toString());
			}
			
		}
		
		// Add the geofences to the GeofenceStore object.
		mGeofenceStore = new GeofenceStore(this, mGeofences);
		
		
		 
		/*mGeofenceCoordinates.add(new LatLng(22.6192661, 88.3981565));
		mGeofenceCoordinates.add(new LatLng(22.621008, 88.395086));
		mGeofenceCoordinates.add(new LatLng(22.619923, 88.394491));


		mGeofences.add(new Geofence.Builder()
				.setRequestId("172|159|Taco Bell")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(mGeofenceCoordinates.get(0).latitude, mGeofenceCoordinates.get(0).longitude, 50)
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
				.setLoiteringDelay(30000)
				.setTransitionTypes(
						Geofence.GEOFENCE_TRANSITION_ENTER
				).build());
		mGeofences.add(new Geofence.Builder()
				.setRequestId("133|123|Panda Express")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(mGeofenceCoordinates.get(1).latitude, mGeofenceCoordinates.get(1).longitude, 50)
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
				.setLoiteringDelay(30000)
				.setTransitionTypes(
						Geofence.GEOFENCE_TRANSITION_ENTER
				).build());

		mGeofences.add(new Geofence.Builder()
				.setRequestId("209|191|Jamba Juice")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(mGeofenceCoordinates.get(2).latitude, mGeofenceCoordinates.get(2).longitude, 50)
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
				.setLoiteringDelay(30000)
				.setTransitionTypes(
						Geofence.GEOFENCE_TRANSITION_ENTER
				).build());


		Log.e("LatLong","lat:"+mGeofenceCoordinates.get(0).latitude+"long:"+mGeofenceCoordinates.get(0).longitude);
		Log.e("LatLong","lat:"+mGeofenceCoordinates.get(1).latitude+"long:"+mGeofenceCoordinates.get(1).longitude);
		Log.e("LatLong","lat:"+mGeofenceCoordinates.get(2).latitude+"long:"+mGeofenceCoordinates.get(2).longitude);

		// Add the geofences to the GeofenceStore object.
		mGeofenceStore = new GeofenceStore(this, mGeofences);*/


	}
	
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	


	@Override
	protected void onResume() {
		super.onResume();

	}

	private void closeCategoryDropdown(){
		category_down_arrow.setVisibility(View.GONE);
		bgtransparent.setVisibility(View.GONE);
		listViewCategory.setVisibility(View.GONE);
	}
	
	
	@Override
	public void onBackPressed() {
		//closing the drawer if it is opened
		if(mDrawerLayout.isDrawerOpen(Gravity.START)){
			mDrawerLayout.closeDrawer(Gravity.START);
		}else if(listViewCategory.getVisibility() == View.VISIBLE) {
			closeCategoryDropdown();
		}else{
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
				return;
			}

			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					doubleBackToExitPressedOnce = false;
				}
			}, 2000);
		}
	}
	
	
}
