package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;


import com.ild.geocouponalert.classtypes.UserMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.webmethod.RestCallManager;

import flexjson.JSONSerializer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingScreen extends Activity implements OnClickListener {
	
	static Context mContext;
	RelativeLayout settingBack;
	//CheckBox unusedcoupon,expiringsooncoupon;
	String unusedcouponstatus ="0",expiringsooncouponstatus ="0";
	//Button save;
	List<UserMaster> lst_user=new ArrayList<UserMaster>();
	SettingAsyncTask manageSettingAsyncTask;
	TextView unusedCoupons1,expiringSoonCoupons1,changePassword;
	//GetSettingAsyncTask getSettingAsyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_screen);
		mContext=this;
		initView();
	}
	
	private void initView(){
		settingBack = (RelativeLayout)findViewById(R.id.settingBack); 
		settingBack.setOnClickListener(this);
		unusedCoupons1 = (TextView)findViewById(R.id.unusedCoupons1);
		unusedCoupons1.setOnClickListener(this);
		expiringSoonCoupons1 = (TextView)findViewById(R.id.expiringSoonCoupons1);
		expiringSoonCoupons1.setOnClickListener(this);
		changePassword = (TextView)findViewById(R.id.changePassword);
		changePassword.setOnClickListener(this);
		
		/*unusedcoupon = (CheckBox)findViewById(R.id.unusedcoupon);
		expiringsooncoupon = (CheckBox)findViewById(R.id.expiringsooncoupon);
		unusedcoupon.setOnClickListener(this);
		expiringsooncoupon.setOnClickListener(this);
		save = (Button)findViewById(R.id.save);
		save.setOnClickListener(this);
		getSettingAsyncTask = new GetSettingAsyncTask(this);
		getSettingAsyncTask.execute();*/
	}
	
	/*public class GetSettingAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Activity mFriendLogin;
		boolean response;
		
		public GetSettingAsyncTask(Activity activity) {  
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {    
			
			response=RestCallManager.getInstance().getUserSetup(Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(response){
				lst_user=DataStore.getInstance().getUserDetails();
				if(lst_user.get(0).unused_coupon.equalsIgnoreCase("1")){
					unusedcoupon.setChecked(true);
				} else {
					unusedcoupon.setChecked(false);
				}
				if(lst_user.get(0).expiring_soon_coupon.equalsIgnoreCase("1")){
					expiringsooncoupon.setChecked(true);
				} else {
					expiringsooncoupon.setChecked(false); 
				}
			}
		} 

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if (getSettingAsyncTask.getStatus() == AsyncTask.Status.RUNNING)
			{
			    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			    {
			        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			    }
			    else
			    {
			        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			    }
			}
			pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}
	} */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == settingBack){
			/*Intent intent = new Intent(SettingScreen.this, MerchantListHomePage.class);
			intent.putExtra("mode", "1");
			startActivity(intent);  */
			finish();
		}
		/*if(v == unusedcoupon){
			 if (unusedcoupon.isChecked()) {
	        	  unusedcouponstatus = "1"; 
	          } else {
	        	  unusedcouponstatus = "0"; 
	          }
		}
		if(v == expiringsooncoupon){
			 if (expiringsooncoupon.isChecked()) {
				 expiringsooncouponstatus = "1"; 
	          } else {
	        	  expiringsooncouponstatus = "0"; 
	          }
		} 
		if(v == save){
			
			//Toast.makeText(mContext, unusedcouponstatus + " - " + expiringsooncouponstatus, 1000).show();
			manageSettingAsyncTask = new SettingAsyncTask(this);
			manageSettingAsyncTask.execute();
		}*/
		if(v == unusedCoupons1){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-terms-and-conditions"));
			startActivity(browserIntent);
		}
		if(v == expiringSoonCoupons1){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-privacy-policy"));
			startActivity(browserIntent);
		}
		if(v == changePassword){

			Intent intent = new Intent(mContext, ChangePassword.class);
			startActivity(intent);
			SettingScreen.this.finish();
			//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
		
		
	}
	
	public class SettingAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Activity mFriendLogin;
		String response;
		
		public SettingAsyncTask(Activity activity) {
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {    
			
			UserMaster user_master=CreateNotificationObject();
			
			String input = new JSONSerializer().serialize(user_master);
			response= RestCallManager.getInstance().updateUserSetup(input);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			Toast.makeText(getApplicationContext(),"Settings Updated Successfully!!!", 
		                Toast.LENGTH_SHORT).show();
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
	
	public UserMaster CreateNotificationObject() {
		UserMaster user_master = new UserMaster();
		user_master.unused_coupon=unusedcouponstatus;
		user_master.expiring_soon_coupon=expiringsooncouponstatus;
		user_master.device_id=Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID);
		return user_master;
	} 
}
