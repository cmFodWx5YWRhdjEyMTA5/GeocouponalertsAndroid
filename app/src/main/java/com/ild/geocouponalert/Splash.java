package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.classtypes.FundraiserMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.classtypes.UserMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.webmethod.RestCallManager;
import flexjson.JSONSerializer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;
import com.ild.geocouponalert.MerchantListHomePage;
import com.ild.geocouponalert.utils.COUtils;
public class Splash extends Activity {

	private static final int SPLASH_TIME = 2 * 1000;// 3 seconds
	Controller aController;
	Context mContext;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mContext=this;
        String device_id=Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
         try {
			new Handler().postDelayed(new Runnable() {
				@SuppressWarnings("null")
				public void run() { 
					
				}
			}, SPLASH_TIME); 
			
			
			aController = (Controller) getApplicationContext();	
			
			
			
			if(aController.isConnectingToInternet()){
				
				String emailID=COUtils.getDefaults("emailID", mContext);
				if(emailID != null){
					
					CheckPostcardAvailablity manageActivation = new CheckPostcardAvailablity(this);
	     			manageActivation.execute();
					
				} else {
					
					Intent intent = new Intent(Splash.this,Welcome_screen.class);
					startActivity(intent);
					Splash.this.finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					
				}
			}
			else{
				aController.showAlertDialog(this,
						"Internet Connection Error",
						"Please connect to Internet connection", false);
			}
					
		} 
        catch (Exception e) {  
        	
        	e.printStackTrace();
			
		}
     
	}
	
	
	public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
	
	public class CheckPostcardAvailablity extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Context mFriendLogin;
		String bSuccess;
		
		public CheckPostcardAvailablity(Activity activity) {
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {   
					
			
			String device_id=Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
			bSuccess=RestCallManager.getInstance().checkAvailablity(COUtils.getDefaults("emailID", mContext),device_id); 
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
				
				/*else if(lst_fund.get(0).activation_process_status.equalsIgnoreCase("3") && lst_fund.get(0).isAlreadyActiveCard.equalsIgnoreCase("0")){
					Intent intent = new Intent(Splash.this,SettingScreen.class);
					startActivity(intent);
					Splash.this.finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				} */
				/*pDialog.dismiss();
			pDialog = null;*/
			}    
			 
			else{
				
				
				/*String agreement_status=COUtils.getDefaults("agreementStatus", mContext);
								
				if(agreement_status==null){
					
					Intent intent = new Intent(Splash.this,Agreement.class);
					startActivity(intent);
					Splash.this.finish();
				}
				else if(agreement_status.equalsIgnoreCase("1")){
					
					Intent intent = new Intent(Splash.this,ActivationCodeScreen.class);
					intent.putExtra("mode","0");
					startActivity(intent);
					Splash.this.finish();
				}*/
				 
				
			}
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();*/
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
}
