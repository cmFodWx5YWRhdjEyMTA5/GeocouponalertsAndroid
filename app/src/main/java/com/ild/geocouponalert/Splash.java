package com.ild.geocouponalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.InternetConnectionDetector;
import com.ild.geocouponalert.webmethod.RestCallManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class Splash extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	InternetConnectionDetector cd;
	Context mContext;
	String currentVersion;
	private static final int REQUEST_CHECK_SETTINGS = 0x1;
	private static GoogleApiClient mGoogleApiClient;
	private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
	private static final String BROADCAST_GPS_LOCATION = "android.location.PROVIDERS_CHANGED";
	/*private ProgressBar progressBar;*/
	TextView lblNoInternetConnection;
	int oneTimeRestCall1 = 0, oneTimeRestCall2=0, welcomeScreen = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		mContext = this;
		initGoogleAPIClient();//Init Google API Client
		checkPermissions();//Check Permission

		RelativeLayout rootView = (RelativeLayout)findViewById(R.id.relMain);
		RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lblNoInternetConnection = new TextView(mContext);
		lblNoInternetConnection.setPadding(6, 6, 6, 6);
		lblNoInternetConnection.setLayoutParams(lay);
		lblNoInternetConnection.setGravity(Gravity.CENTER_HORIZONTAL);
		lblNoInternetConnection.setTextSize(13);
		lblNoInternetConnection.setText("Connectivity problem. Please check your internet connection.");
		lblNoInternetConnection.setBackgroundResource(R.color.black);
		lblNoInternetConnection.setTextColor(Color.parseColor("#ffffff"));
		rootView.addView(lblNoInternetConnection);
		lblNoInternetConnection.setVisibility(View.INVISIBLE);

		/*try {
			currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		new GetVersionCode().execute();*/
	}

	/* Broadcast receiver to check status of Internet Connection */
	private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			//If Action is Internet connection
			if (intent.getAction().matches(CONNECTIVITY_ACTION)) {


					if (Build.VERSION.SDK_INT >= 23 && COUtils.getDefaults("permission", mContext)
							!=null) {
						Log.d("app", "Network connectivity change");
						if (intent.getExtras() != null) {

							LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
							boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
							if (COUtils.getDefaults("emailID", mContext) == null) {
								if (checkInternetConnection() && GpsStatus == true) {
									if (welcomeScreen == 0) {
										welcomeScreen();
									}
								}
							} else {
								if (checkInternetConnection() && GpsStatus == true) {
									if (oneTimeRestCall1 == 0)
										new CheckPostcardAvailablity(mContext).execute();
								}
							}
						}
					}
					else if (Build.VERSION.SDK_INT < 23) {

						Log.d("app", "Network connectivity change");
						if (intent.getExtras() != null) {

							LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
							boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
							if (checkInternetConnection() && GpsStatus == true) {
								if (COUtils.getDefaults("emailID", mContext) == null) {
									if (welcomeScreen == 0) {
										welcomeScreen();
									}
								} else {
									if (oneTimeRestCall1 == 0) {
										new CheckPostcardAvailablity(mContext).execute();
									}
								}
							}
						}
					}
			}

			//If Action is Location
			if (intent.getAction().matches(BROADCAST_GPS_LOCATION)) {
				LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
				//Check if GPS is turned ON or OFF
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					if (checkInternetConnection()) {
						if (COUtils.getDefaults("emailID", mContext) == null) {
							if (welcomeScreen == 0) {
								welcomeScreen();
							}
						} else {
							if (oneTimeRestCall1 == 0) {
								new CheckPostcardAvailablity(mContext).execute();
							}
						}
					}
				} else {
					//If GPS turned OFF show Location Dialog
					new Handler().postDelayed(sendUpdatesToUI, 10);
				}

			}
		}
	};

	//Run on UI
	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			showSettingDialog();
		}
	};

	/* Initiate Google API Client  */
	private void initGoogleAPIClient() {
		//Without Google API Client Auto Location Dialog will not work
		mGoogleApiClient = new GoogleApiClient.Builder(Splash.this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	/* Check Location Permission for Marshmallow Devices */
	private void checkPermissions() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (ContextCompat.checkSelfPermission(Splash.this,
					android.Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED)
				requestLocationPermission();
			else
				showSettingDialog();
		} else
			showSettingDialog();

	}
	/*  Show Popup to access User Permission  */
	private void requestLocationPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
			ActivityCompat.requestPermissions(Splash.this,
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
					ACCESS_FINE_LOCATION_INTENT_ID);

		} else {
			ActivityCompat.requestPermissions(Splash.this,
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
					ACCESS_FINE_LOCATION_INTENT_ID);
		}
	}

	/* Show Location Access Dialog */
	private void showSettingDialog() {
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
		locationRequest.setInterval(30 * 1000);
		locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);
		builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				final LocationSettingsStates state = result.getLocationSettingsStates();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:

							// All location settings are satisfied. The client can initialize location
							// requests here.
							if (COUtils.getDefaults("emailID",mContext)==null) {
								if (checkInternetConnection()) {
									Timer timer = new Timer();
									timer.schedule(new TimerTask() {
										public void run() {
											if (welcomeScreen==0) {
												welcomeScreen();
											}
										}

									}, 2000);
								}
							}
							else{
								if (checkInternetConnection()) {
									if (oneTimeRestCall1==0)
										new CheckPostcardAvailablity(mContext).execute();
								}
							}
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						// Location settings are not satisfied. But could be fixed by showing the user
						// a dialog.
						try {
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult(Splash.this, REQUEST_CHECK_SETTINGS);
						} catch (IntentSender.SendIntentException e) {
							e.printStackTrace();
							// Ignore the error.
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						// Location settings are not satisfied. However, we have no way to fix the
						// settings so we won't show the dialog.
						break;
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			// Check for the integer request code originally supplied to startResolutionForResult().
			case REQUEST_CHECK_SETTINGS:
				switch (resultCode) {
					case RESULT_OK:
						Log.e("Settings", "Result OK");

							if (COUtils.getDefaults("emailID",mContext)==null){
								if (checkInternetConnection()) {
									Timer timer = new Timer();
									timer.schedule(new TimerTask() {

										public void run() {
											if (welcomeScreen==0) {
												welcomeScreen();
											}
										}
									}, 2000);
								}
							}
							else{

								if (checkInternetConnection()) {
									if (oneTimeRestCall1==0)
										new CheckPostcardAvailablity(mContext).execute();
								}
							}

						break;
					case RESULT_CANCELED:
						Log.e("Settings", "Result Cancel");
						Timer timer2 = new Timer();
						timer2.schedule(new TimerTask() {
							public void run() {
								finish();
								System.exit(0);
							}
						}, 2000);
						break;
				}
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(connectivityReceiver, new IntentFilter(CONNECTIVITY_ACTION));//Register broadcast receiver to check internet connection.
		registerReceiver(connectivityReceiver, new IntentFilter(BROADCAST_GPS_LOCATION));//Register broadcast receiver to check the status of GPS
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Unregister receiver on destroy
		if (connectivityReceiver != null)
			unregisterReceiver(connectivityReceiver);
	}

	/* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case ACCESS_FINE_LOCATION_INTENT_ID: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					COUtils.setDefaults("permission","1", mContext);
					//If permission granted show location dialog if APIClient is not null
					if (mGoogleApiClient == null) {
						initGoogleAPIClient();
						showSettingDialog();

					} else
						showSettingDialog();


				} else {
					Toast.makeText(Splash.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}
		}
	}

	public void welcomeScreen(){
		welcomeScreen=1;
		Intent intent = new Intent(Splash.this,Welcome_screen.class);
		startActivity(intent);
		Splash.this.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public class CheckPostcardAvailablity extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Context mFriendLogin;
		String bSuccess;

		public CheckPostcardAvailablity(Context context) {
			mFriendLogin = context;
		}

		@Override
		protected Void doInBackground(Void... params) {


			String device_id= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
			bSuccess= RestCallManager.getInstance().checkAvailablity(COUtils.getDefaults("emailID", mContext),device_id);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(bSuccess.equalsIgnoreCase("Success")){

				List<PostcardMaster> lst_fund = new ArrayList<PostcardMaster>();
				lst_fund = DataStore.getInstance().getPostcard();

				if(lst_fund.size()==0){

					Intent intent = new Intent(Splash.this,MerchantListHomePage.class);
					intent.putExtra("business_id", "");
					intent.putExtra("notification_mode", "1");
					intent.putExtra("new_merchant_notification","");
					startActivity(intent);
					Splash.this.finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

				} else if(lst_fund.get(0).activation_process_status.equalsIgnoreCase("1")){
					Intent intent = new Intent(Splash.this,PinCodeScreen.class);
					startActivity(intent);
					Splash.this.finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

				} else if(lst_fund.get(0).activation_screen.equalsIgnoreCase("1")){

					Intent intent = new Intent(Splash.this,ActivationCodeScreen.class);
					intent.putExtra("mode", "0");
					startActivity(intent);
					Splash.this.finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

				}

			}

			else{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setTitle("Oops!!");
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setMessage("We are having an issue connecting to server. Please try again.");
				alertDialogBuilder.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
								new CheckPostcardAvailablity(mContext).execute();
							}
						});
				alertDialogBuilder.setPositiveButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								new Timer().schedule(new TimerTask() {
									public void run() {
										finish();
										System.exit(0);
									}
								}, 2000);
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.MAGENTA);
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	public boolean checkInternetConnection(){
		cd = new InternetConnectionDetector(mContext);
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent){
			lblNoInternetConnection.setVisibility(View.VISIBLE);
			return false;
		}
		else{
			lblNoInternetConnection.setVisibility(View.INVISIBLE);
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}


	/*class GetVersionCode extends AsyncTask<Void, String, String> {

		@Override

		protected String doInBackground(Void... voids) {

			String newVersion = null;

			try {
				Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Splash.this.getPackageName()  + "&hl=en")
						.timeout(30000)
						.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.referrer("http://www.google.com")
						.get();
				if (document != null) {
					Elements element = document.getElementsContainingOwnText("Current Version");
					for (Element ele : element) {
						if (ele.siblingElements() != null) {
							Elements sibElemets = ele.siblingElements();
							for (Element sibElemet : sibElemets) {
								newVersion = sibElemet.text();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return newVersion;

		}


		@Override

		protected void onPostExecute(String onlineVersion) {

			super.onPostExecute(onlineVersion);

			if (onlineVersion != null && !onlineVersion.isEmpty()) {

				if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
					//show anything
					new AlertDialog.Builder(mContext)
							.setTitle("Update is available!")
							.setMessage("An update for this application is available.")
							.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// continue with delete
									final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
									try {
										//Toast.makeText(getApplicationContext(), "App is in BETA version cannot update", Toast.LENGTH_SHORT).show();
										startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
									} catch (ActivityNotFoundException anfe) {
										startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
									}
								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();
				}

			}

			Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

		}
	}*/


}
