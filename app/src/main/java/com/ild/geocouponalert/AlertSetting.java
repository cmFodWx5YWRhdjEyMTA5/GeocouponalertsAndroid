package com.ild.geocouponalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ild.geocouponalert.adapter.AlertMerchantAdapter;
import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.FavoriteMerchantAdapter;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.FavoriteAlertMerchant;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.webmethod.RestCallManager;

import java.util.ArrayList;
import java.util.List;

public class AlertSetting extends Activity implements OnClickListener{

	static Context mContext;
	SelectedMerchantAsyncTask manageActivation;
	ListView listViewAlert,listViewFavourite;
	List<BusinessMaster> lst_business = new ArrayList<BusinessMaster>();
	List<BusinessMaster> lst_filtered_buss = new ArrayList<BusinessMaster>();
	List<BusinessCouponLocation> lst_business_coupon = new ArrayList<BusinessCouponLocation>();
	AlertMerchantAdapter selectedmerchantadapter=null;
	FavoriteMerchantAdapter slecetdFavoriteAdapter = null;
	//Spinner categorySpinner;
    CategoryAdapter catadapter=null;
    ImageView menuicon;
    GPSTracker gps;
    TextView category_textview;
    String buss_id,user_latitude,user_longitude,buss_name,banner_img,location_id;
    RelativeLayout relheading;
    String new_merchant_notification;

 // Alert dialog manager
 	AlertDialogManager alert = new AlertDialogManager();
	// Connection detector
	ConnectionDetector cd;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	String regId;


	RelativeLayout alertRel,favouriteRel;
	ImageView imgAlert,imgFavourite;
	TextView save,alerts,favourite;

	RelativeLayout searchMerchantRel;

	List<BusinessLocationMaster> lst_bus_location = new ArrayList<BusinessLocationMaster>();
	List<BusinessMaster> new_lst_filtered_buss = null;
	EditText searchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_setting);

		mContext=this;
		initView();
	}

	private TextWatcher searchtextAlertWatcher = new TextWatcher() {
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

	private TextWatcher searchtextFavouriteWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		public void afterTextChanged(Editable s) {
		}
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//if (s.length() != 0) {
			//Toast.makeText(mContext,"You have entered : " + searchText.getText(),Toast.LENGTH_SHORT).show();
			slecetdFavoriteAdapter.getFilter().filter(s.toString());
			//}
		}
	};

	private void initView(){

		save = (TextView)findViewById(R.id.save);
		save.setOnClickListener(this);

		alerts      = (TextView)findViewById(R.id.alerts);
		favourite   = (TextView)findViewById(R.id.favourite);

		searchText = (EditText) findViewById(R.id.searchText);
		searchText.addTextChangedListener(searchtextAlertWatcher);

		alertRel   = (RelativeLayout) findViewById(R.id.alertRel);
		alertRel.setOnClickListener(this);

		favouriteRel    = (RelativeLayout) findViewById(R.id.favouriteRel);
		favouriteRel.setOnClickListener(this);

		searchMerchantRel = (RelativeLayout) findViewById(R.id.searchMerchantRel);

		imgAlert     = (ImageView)findViewById(R.id.imgAlert);
		imgFavourite      = (ImageView)findViewById(R.id.imgFavourite);

		category_textview = (TextView)findViewById(R.id.category_textview);

		menuicon = (ImageView)findViewById(R.id.menuicon);
		menuicon.setOnClickListener(this);

		listViewAlert = (ListView)findViewById(R.id.listViewAlert);
		listViewFavourite = (ListView)findViewById(R.id.listViewFavourite);

		relheading = (RelativeLayout)findViewById(R.id.relheading);

		lst_business = DataStore.getInstance().getSelectedBusiness();

		manageActivation = new SelectedMerchantAsyncTask(this);
		manageActivation.execute();
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
			bSuccess=RestCallManager.getInstance().getFavoriteAlertMerchant(COUtils.getDefaults("emailID", mContext));
			return null;
		} 

		@Override
		protected void onPostExecute(Void result) {  
			pDialog.dismiss();
			if(bSuccess){
				List<BusinessMaster> lst_business_alert = new ArrayList<BusinessMaster>();
				List<BusinessMaster> lst_business_favourite = new ArrayList<BusinessMaster>();

				List<FavoriteAlertMerchant> lst_businessalertfavourite = DataStore.getInstance().getFavoriteAlertMerchant();

				ArrayList<String> allAlertMerchantInactiveId = lst_businessalertfavourite.get(0).alert_merchants;
				ArrayList<String> allFavouriteMerchantId = lst_businessalertfavourite.get(0).fav_merchants;


				for(BusinessMaster objBuss : lst_business){
					if(allAlertMerchantInactiveId.contains(objBuss.buss_id)){
						lst_business_alert.add(objBuss);
					}

					/*if(allFavouriteMerchantId.contains(objBuss.buss_id)){
						lst_business_favourite.add(objBuss);
					}*/
				}

				String checkAllMerchantAlert = "Y";
				String checkAllMerchantFavourite = "Y";
				if(lst_business_alert.size()>0){
					checkAllMerchantAlert = "N";
				}

				/*if(lst_business_favourite.size()>0){
					checkAllMerchantFavourite = "N";
				}*/


				selectedmerchantadapter = new AlertMerchantAdapter(AlertSetting.this,lst_business,checkAllMerchantAlert,allAlertMerchantInactiveId);
				listViewAlert.setAdapter(selectedmerchantadapter);
				//selectedmerchantadapter.notifyDataSetChanged();

				slecetdFavoriteAdapter = new FavoriteMerchantAdapter(AlertSetting.this,lst_business,allFavouriteMerchantId);
				listViewFavourite.setAdapter(slecetdFavoriteAdapter);
			}else{
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
				topTxt.setText("Warning!");
				alertTxt.setText("There is a problem with the system");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
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

		@Override 
		protected void onPreExecute() {
			pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show(); 
		}
	}


	public class SetMerchantAlertFavouriteAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Context mFriendLogin;
		boolean bSuccess;
		String alerts_merchant_str;
		String favorite_merchant_str;


		public SetMerchantAlertFavouriteAsyncTask(Context activity,String alerts_merchant, String favorite_merchant) {
			mFriendLogin = activity;
			alerts_merchant_str = alerts_merchant;
			favorite_merchant_str = favorite_merchant;
		}

		@Override
		protected Void doInBackground(Void... params) {
			bSuccess=RestCallManager.getInstance().setMerchantAlertFavourite(COUtils.getDefaults("emailID", mContext),alerts_merchant_str,favorite_merchant_str);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			if(bSuccess){
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
				topTxt.setText("Success");
				alertTxt.setText("Settings saved successfully");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
						Intent czintent = new Intent(AlertSetting.this, MerchantListHomePage.class);
						czintent.putExtra("business_id", "");
						czintent.putExtra("notification_mode", "1");
						czintent.putExtra("new_merchant_notification","");
						startActivity(czintent);
					}
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			}else{
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
				topTxt.setText("Warning!");
				alertTxt.setText("There is a problem with the system");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
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

		@Override
		protected void onPreExecute() {
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

		if(v==save){
			ArrayList<String> allActiveFavoriteMerchantId = FavoriteMerchantAdapter.allAvtiveAlertMerchantId;
			ArrayList<String> allInAvtiveAlertMerchantId  = AlertMerchantAdapter.allInAvtiveAlertMerchantId;

			String activeFavoriteMerchantIdString = TextUtils.join(",", allActiveFavoriteMerchantId);
			String inActiveAlertMerchantIdString = TextUtils.join(",", allInAvtiveAlertMerchantId);

			new SetMerchantAlertFavouriteAsyncTask(mContext,inActiveAlertMerchantIdString,activeFavoriteMerchantIdString).execute();
		}

		if(v==alertRel){
			searchText.setText(""); // set search text to null

			searchText.addTextChangedListener(searchtextAlertWatcher); // adding textwatcher

			listViewAlert.setVisibility(View.VISIBLE);
			listViewFavourite.setVisibility(View.GONE);

			alerts.setTextColor(Color.WHITE);
			favourite.setTextColor(Color.BLACK);

			imgAlert.setImageResource(R.drawable.bell);
			imgFavourite.setImageResource(R.drawable.star_grey);

			alertRel.setBackground( getResources().getDrawable(R.drawable.segment_active_alert_left_click));
			favouriteRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive_alert));

		}
		if(v==favouriteRel){
			searchText.setText("");  // set search text to null

			searchText.addTextChangedListener(searchtextFavouriteWatcher); // adding textwatcher

			listViewAlert.setVisibility(View.GONE);
			listViewFavourite.setVisibility(View.VISIBLE);

			alerts.setTextColor(Color.BLACK);
			favourite.setTextColor(Color.WHITE);

			imgAlert.setImageResource(R.drawable.bell_grey);
			imgFavourite.setImageResource(R.drawable.star);

			alertRel.setBackground( getResources().getDrawable(R.drawable.segment_inactive_alert_click));
			favouriteRel.setBackground( getResources().getDrawable(R.drawable.segment_active_alert_right_click));
		}


		if(v == menuicon){
			onBackPressed();
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		//closing the drawer if it is opened
		finish();
	}
	
}
