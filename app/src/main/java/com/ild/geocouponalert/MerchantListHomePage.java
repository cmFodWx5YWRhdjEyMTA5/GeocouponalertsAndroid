package com.ild.geocouponalert;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ild.geocouponalert.CommonUtilities;

import static com.ild.geocouponalert.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.ild.geocouponalert.CommonUtilities.EXTRA_MESSAGE;
import static com.ild.geocouponalert.CommonUtilities.SENDER_ID;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.ild.geocouponalert.ActivationCodeScreen.ActivationCodeAsyncTask;
import com.ild.geocouponalert.MerchantSelectionScreen.SelectionAsyncTask;
import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.NavDrawerListAdapter;
import com.ild.geocouponalert.adapter.SelectedMerchantAdapter;
import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.adapter.SelectedMerchantAdapter.ViewcouponAsyncTask;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.Category;
import com.ild.geocouponalert.classtypes.NavDrawerItem;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.AlertDialogManager;
import com.ild.geocouponalert.ConnectionDetector;
import com.ild.geocouponalert.ServerUtilities;
import com.ild.geocouponalert.WakeLocker;
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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantListHomePage extends Activity implements OnClickListener{
	
	static Context mContext;
	SelectedMerchantAsyncTask manageActivation;
	ListView listViewMerchant;
	List<BusinessMaster> lst_business = new ArrayList<BusinessMaster>();
	List<BusinessMaster> lst_filtered_buss = new ArrayList<BusinessMaster>();
	List<BusinessCouponLocation> lst_business_coupon = new ArrayList<BusinessCouponLocation>();
	SelectedMerchantAdapter selectedmerchantadapter=null;
	//Spinner categorySpinner;
    CategoryAdapter catadapter=null;
    ImageView bgtransparent,category_arrow,category_down_arrow,menuicon;
    GPSTracker gps;
    TextView category_textview;
    ListView listViewCategory;
    String buss_id,user_latitude,user_longitude,buss_name,banner_img,location_id;
    RelativeLayout relheading;
    String new_merchant_notification;
 // LogCat tag
  	private static final String TAG = MerchantListHomePage.class.getSimpleName();

  	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
  	/**
 	 * Geofence Data
 	 */ 

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

	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;
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

	LinearLayout gmapLin;
	RelativeLayout relcat,searchMerchantRel,catDropdownRel;

	List<BusinessLocationMaster> lst_bus_location = new ArrayList<BusinessLocationMaster>();
	List<BusinessMaster> new_lst_filtered_buss = null;
	EditText searchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_list_home_page_main);
		
		mContext=this;
		mGeofences = new ArrayList<Geofence>();
		mGeofenceCoordinates = new ArrayList<LatLng>();
		initView();
	}


	private void initilizeMap(List<BusinessMaster> businessList) {

		gps = new GPSTracker(this);
		if(gps.canGetLocation()){

			user_latitude = String.valueOf(gps.getLatitude());
			user_longitude = String.valueOf(gps.getLongitude());
			/*user_latitude = "38.7073";
			user_longitude = "-121.2708";*/
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.gmap)).getMap();
			}else{
				googleMap.clear();
			}

				try {

					if(businessList.size()>0){
						int count = 0;
						for(int i=0;i<businessList.size();i++){

							lst_bus_location = businessList.get(i).geofence_location_details;
							for(int j=0;j<lst_bus_location.size();j++){

								double M_PI = 3.1415926535897932385;

								int nRadius = 6371; // Earth's radius in Kilometers
								double latDiff = (Double.valueOf(lst_bus_location.get(j).buss_loc_lat) - Double.valueOf(user_latitude)) * (M_PI/180);
								double lonDiff = (Double.valueOf(lst_bus_location.get(j).buss_loc_long) - Double.valueOf(user_longitude)) * (M_PI/180);
								double lat1InRadians = Double.valueOf(user_latitude) * (M_PI/180);
								double lat2InRadians = Double.valueOf(lst_bus_location.get(j).buss_loc_lat)  * (M_PI/180);
								double nA = Math.pow ( Math.sin(latDiff/2), 2 ) + Math.cos(lat1InRadians)*  Math.cos(lat2InRadians)*  Math.pow ( Math.sin(lonDiff/2), 2 );
								double nC = 2 * Math.atan2( Math.sqrt(nA), Math.sqrt( 1 - nA ));
								double nD = nRadius * nC;
								//double distance_in_meters = nD*1000;
								//if(nD <= 16.0934){
									count=count+1;
									Location dest_location = new Location("");

									final Double latt = Double.valueOf(lst_bus_location.get(j).buss_loc_lat);
									final Double longtt = Double.valueOf(lst_bus_location.get(j).buss_loc_long);

									dest_location.setLatitude(latt);
									dest_location.setLongitude(longtt);

									String address;
									if (lst_bus_location.get(j).address2.toString().equalsIgnoreCase("") ){
										address = lst_bus_location.get(j).address1+", " + lst_bus_location.get(j).city;
									}
									else{
										address = lst_bus_location.get(j).address1+", " +
												lst_bus_location.get(j).address2 +", " + lst_bus_location.get(j).city;
									}

									MarkerOptions marker = new MarkerOptions().
											position(new LatLng(latt, longtt)).
											title(businessList.get(i).name)
											.snippet(address);
									//snippet(address).
									//icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));

									// adding marker

									CameraUpdate center=
											CameraUpdateFactory.newLatLng(new LatLng(latt,longtt));
									CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

									googleMap.moveCamera(center);
									googleMap.animateCamera(zoom);
									googleMap.addMarker(marker);

									googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
									{
										@Override
										public void onInfoWindowClick(Marker arg0) {
											LatLng coordinate2=arg0.getPosition();

											Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
													Uri.parse("http://maps.google.com/maps?daddr="+coordinate2.latitude+","+coordinate2.longitude));
											startActivity(intent);
										}
									});
								//}
							}
							// create marker
						}


					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Changing marker icon
				//marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plusone_medium_off_client));
				//Moving camera with location
           /* CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(8).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

     */       //Showing Current Location
				googleMap.setMyLocationEnabled(true); // false to disable
				//Zooming button
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				//Zooming Functionality.You can disable zooming gesture functionality
				// googleMap.getUiSettings().setZoomGesturesEnabled(false);
				//Compass Functionality
				googleMap.getUiSettings().setCompassEnabled(true);
				//Location Button
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);

				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}

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

		edit = (TextView)findViewById(R.id.levelText);
		edit.setOnClickListener(this);

		list      = (TextView)findViewById(R.id.list);
		map       = (TextView)findViewById(R.id.map);
		search    = (TextView)findViewById(R.id.search);

		searchText = (EditText) findViewById(R.id.searchText);
		searchText.addTextChangedListener(searchtextWatcher);

		relcat = (RelativeLayout) findViewById(R.id.relcat);
		relcat.setOnClickListener(this);

		listRel   = (RelativeLayout) findViewById(R.id.listRel);
		listRel.setOnClickListener(this);

		mapRel    = (RelativeLayout) findViewById(R.id.mapRel);
		mapRel.setOnClickListener(this);

		searchRel = (RelativeLayout) findViewById(R.id.searchRel);
		searchRel.setOnClickListener(this);

		searchMerchantRel = (RelativeLayout) findViewById(R.id.searchMerchantRel);
		catDropdownRel = (RelativeLayout) findViewById(R.id.catDropdownRel);

		imgList     = (ImageView)findViewById(R.id.imgList);
		imgMap      = (ImageView)findViewById(R.id.imgMap);
		imgSearch   = (ImageView)findViewById(R.id.imgSearch);

		gmapLin = (LinearLayout) findViewById(R.id.gmapLin);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_after_login);
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons_after_login);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList   = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();
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
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
		// Pages

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


		
		category_down_arrow = (ImageView)findViewById(R.id.category_down_arrow);
		category_down_arrow.setOnClickListener(this);
		//categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
		bgtransparent = (ImageView)findViewById(R.id.bgtransparent);
		category_textview = (TextView)findViewById(R.id.category_textview);
		//category_textview.setOnClickListener(this);
		//category_textview.setVisibility(View.GONE);
		category_arrow = (ImageView)findViewById(R.id.category_arrow);
		//category_arrow.setOnClickListener(this);
		
		menuicon = (ImageView)findViewById(R.id.menuicon);
		menuicon.setOnClickListener(this);

		listViewMerchant = (ListView)findViewById(R.id.listViewMerchant);
		listViewCategory = (ListView)findViewById(R.id.listViewCategory); 
		relheading = (RelativeLayout)findViewById(R.id.relheading);
		
	if(!getIntent().getExtras().getString("business_id").equalsIgnoreCase("")
			&& !getIntent().getExtras().getString("location_id").equalsIgnoreCase("")){ // call when click on notification
			

			relheading.setVisibility(View.GONE);
			buss_id = getIntent().getExtras().getString("business_id");
			location_id = getIntent().getExtras().getString("location_id");
			gps = new GPSTracker(mContext);
			if(gps.canGetLocation()){
				
				user_latitude = String.valueOf(gps.getLatitude());
				user_longitude = String.valueOf(gps.getLongitude());
			}
			ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(this);
			manageActivation.execute();
			
		} else {
			manageActivation = new SelectedMerchantAsyncTask(this);
			manageActivation.execute();
			 
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
	
	public class SelectedMerchantAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Activity mFriendLogin;
		boolean bSuccess;
		
		
		public SelectedMerchantAsyncTask(Activity activity) { 
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {    
			
			bSuccess=RestCallManager.getInstance().getSelectedMerchant(Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID),COUtils.getDefaults("emailID", mContext));
			gcmService();
			return null;
		} 

		@Override
		protected void onPostExecute(Void result) {  
			pDialog.dismiss();
			if(bSuccess){
				
				lst_business = DataStore.getInstance().getSelectedBusiness();
				new_merchant_notification = getIntent().getExtras().getString("new_merchant_notification");
				if(new_merchant_notification.equalsIgnoreCase("1")){
					
					category_textview.setText("New Merchants");
					
					for(int i=0;i<lst_business.size();i++){
				    	   if(lst_business.get(i).isNewBusiness.equalsIgnoreCase("yes")){
				    		   lst_filtered_buss.add(lst_business.get(i));
				    	   }
				       }
		    		   selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_filtered_buss);
		    		   listViewMerchant.setAdapter(selectedmerchantadapter);
					   selectedmerchantadapter.notifyDataSetChanged();
				} else {
				
					selectedmerchantadapter = new SelectedMerchantAdapter(MerchantListHomePage.this,lst_business);
					listViewMerchant.setAdapter(selectedmerchantadapter);
					selectedmerchantadapter.notifyDataSetChanged();
				}
				
				/*catadapter = new CategoryAdapter(MerchantListHomePage.this,lst_business.get(0).cat_details);
				categorySpinner.setAdapter(catadapter);	*/
				
				
				// TODO Auto-generated method stub
   				gps = new GPSTracker(mContext);
   				if(gps.canGetLocation()){
   					if(getIntent().getExtras().getString("notification_mode").equalsIgnoreCase("1")){
   						
   						
   						String geoLocationEnabled=COUtils.getDefaults("geoLocationEnabled", mContext);
   						
   						if(geoLocationEnabled==null){
   						
   						LayoutInflater layoutInflater = LayoutInflater.from(mContext);
   						View promptView = layoutInflater.inflate(R.layout.alert_location, null);
   						//promptView.setBackgroundColor(Color.TRANSPARENT);
   						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
   						alertDialogBuilder.setView(promptView);
   						final AlertDialog alertD = alertDialogBuilder.create();
   						
   						final TextView dialogNo = (TextView) promptView.findViewById(R.id.btnDontAllow);
   						final TextView dialogYes = (TextView) promptView.findViewById(R.id.btnAllow);
   						 
   						
   						// if button is clicked, close the custom dialog
   						dialogNo.setOnClickListener(new OnClickListener() {
   							@Override
   							public void onClick(View v) {
   								COUtils.setDefaults("geoLocationEnabled", "0", mContext);
   								alertD.dismiss();
   							}
   						});
   						
   						
   						// if button is clicked, close the custom dialog
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
   		   						
   		   						dialogNo2.setText(R.string.txt_cancel);
   		   						coupon_alert.setText(R.string.location_alert_background);
   		   						coupon_alert_sub.setText(R.string.location_alert_background_sub);
   		   						
   		   						dialogNo2.setOnClickListener(new OnClickListener() {
   		   							@Override
   		   							public void onClick(View v) {
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
   								
   				}
   				else {
   			       	// can't get location
   			       	// GPS or Network is not enabled
   			       	// Ask user to enable GPS/network in settings
   			       	gps.showSettingsAlert(); 
   			    }


				initilizeMap(lst_business);
   				
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
					alertTxt.setText("All cards are expired. Please add new card to continue.");

					TextView btnAddNewCard = (TextView) promptView.findViewById(R.id.btnAddNewCard);
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

					TextView btnClose = (TextView) promptView.findViewById(R.id.btnClose);
					btnClose.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertD2.dismiss();
							System.exit(0);
						}
					});

					alertD2.setCanceledOnTouchOutside(false);
					alertD2.show();

				}else {

					LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
					AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					alertDialogBuilder2.setView(promptView);
					final AlertDialog alertD2 = alertDialogBuilder2.create();
					TextView alertTxt = (TextView) promptView.findViewById(R.id.alertTxt);
					TextView topTxt = (TextView) promptView.findViewById(R.id.topTxt);
					topTxt.setText("Warning!");
					alertTxt.setText("There is a problem with the system");
					RelativeLayout relBottominner = (RelativeLayout) promptView.findViewById(R.id.relBottominner);
					relBottominner.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertD2.dismiss();
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
								Toast.makeText(getApplicationContext(), "There is no merchant in this category.", 2000).show();
							}
						}


						initilizeMap(new_lst_filtered_buss); // filtering map pointing

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
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			//Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	private void gcmService(){
		
		cd = new ConnectionDetector(mContext); 
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MerchantListHomePage.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(mContext);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(mContext);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(mContext);
		
		// Check if regid already presents
				if (regId.equals("")) {
					// Registration is not present, register now with GCM			
					GCMRegistrar.register(mContext, SENDER_ID);
				} else {
					// Device is already registered on GCM
					if (GCMRegistrar.isRegisteredOnServer(this)) {
						// Skips registration.				
						//Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
					} else {
						// Try to register again, but not in the UI thread.
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.
						final Context context = this;
						mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								// Register on our server
								// On server creates a new user
								ServerUtilities.register(context,regId);
								return null;
							}
 
							@Override
							protected void onPostExecute(Void result) { 
								mRegisterTask = null;
							}

						};
						mRegisterTask.execute(null, null, null);
					}
				}
	}

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
