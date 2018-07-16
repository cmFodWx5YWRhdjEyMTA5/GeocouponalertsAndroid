package com.ild.geocouponalert;


import java.util.ArrayList;
import java.util.List;
import com.ild.geocouponalert.classtypes.FundraiserMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.webmethod.RestCallManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ild.geocouponalert.datastore.DataStore;


public class PinCodeScreen extends Activity implements OnClickListener {
	
	Context mContext;
	TextView btnSubmit,heading;
	EditText pinNumber;
	public String zipcode,user_id,fundraiser_id;
	List<PostcardMaster> lst_postcard = new ArrayList<PostcardMaster>();
		
	PinCodeAsyncTask manageActivation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_code_screen);
		mContext=this;
		
		initView(); 
	} 
	
	private void initView(){ 
		
		lst_postcard = DataStore.getInstance().getPostcard(); 
		pinNumber=(EditText)findViewById(R.id.zipcode);
		btnSubmit=(TextView)findViewById(R.id.btnSubmit);
		heading=(TextView)findViewById(R.id.heading);
		
		//String mode = getIntent().getExtras().getString("mode");
		
		
		if(lst_postcard.get(0).user_pin != ""){
			heading.setText("Your Zip code is");
			pinNumber.setText(lst_postcard.get(0).user_pin);
					
			pinNumber.setEnabled(false);
		}
				
		btnSubmit.setOnClickListener(this);   
	
	} 
	
	public class PinCodeAsyncTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		Activity mFriendLogin;
		boolean bSuccess;
		
		public PinCodeAsyncTask(Activity activity) {
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {    
			zipcode=pinNumber.getText().toString().trim();
			bSuccess = RestCallManager.getInstance().downloadAllBusiness(zipcode,lst_postcard.get(0).activation_code.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) { 
			super.onPostExecute(result);
			pDialog.dismiss();
			pDialog = null;
			if(bSuccess){		
				
				Intent intent = new Intent(mContext, MerchantListHomePage.class);
				intent.putExtra("business_id", "");
				intent.putExtra("location_id", "");
				intent.putExtra("notification_mode", "1");
				intent.putExtra("new_merchant_notification","");
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			} else {
				Toast.makeText(getApplicationContext(), "No merchants found.", 3000).show();
			}
		} 
 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if (manageActivation.getStatus() == AsyncTask.Status.RUNNING)
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
	}
	
	

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == btnSubmit){
			
			
			/*String activation_code = getIntent().getExtras().getString("activation_code");*/
			zipcode=pinNumber.getText().toString().trim(); 
			if(zipcode.equalsIgnoreCase("")){
				/*Toast.makeText(mContext, "Enter Zip Code", Toast.LENGTH_SHORT).show();
				return;*/
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Enter Zip Code");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			}
			else{
				
				manageActivation = new PinCodeAsyncTask(PinCodeScreen.this);
				manageActivation.execute();
	
			}
		
		} 
	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		/*super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);*/
	}
	
}
