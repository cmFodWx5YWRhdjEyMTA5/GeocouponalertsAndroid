package com.ild.geocouponalert;


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
import com.ild.geocouponalert.webmethod.RestCallManager;

import java.util.ArrayList;
import java.util.List;


public class ChangeZipCode extends Activity implements OnClickListener {

	Context mContext;
	TextView btnSubmit,heading;
	EditText pinNumber;
	public String zipcode,user_id,fundraiser_id;
	ImageView back_arrow;

	PinCodeAsyncTask manageActivation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_code_screen);
		mContext=this;
		initView();
	}

	private void initView(){

		pinNumber=(EditText)findViewById(R.id.zipcode);
		btnSubmit=(TextView)findViewById(R.id.btnSubmit);
		heading=(TextView)findViewById(R.id.heading);
		back_arrow = (ImageView)findViewById(R.id.back_arrow);
		back_arrow.setVisibility(View.VISIBLE);

		heading.setText("Chnage Zip Code");
		btnSubmit.setText("Update");
		pinNumber.setHint("ENTER YOUR ZIP CODE HERE");

		btnSubmit.setOnClickListener(this);
		back_arrow.setOnClickListener(this);

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
			String uemail = COUtils.getDefaults("emailID",mFriendLogin);
			bSuccess = RestCallManager.getInstance().updateZipCode(zipcode,uemail);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			pDialog = null;
			LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
			View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
			AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
			alertDialogBuilder2.setView(promptView);
			final AlertDialog alertD2 = alertDialogBuilder2.create();
			TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
			if(bSuccess) {
				topTxt.setText("Success");
			}else {
				topTxt.setText("Oops");
			}
			TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
			alertTxt.setText(FOGlobalVariable.IsActivationCodeAlreadyExist);
			RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
			relBottominner.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(bSuccess) {
						alertD2.dismiss();
						Intent czintent = new Intent(ChangeZipCode.this, MerchantListHomePage.class);
						czintent.putExtra("business_id", "");
						czintent.putExtra("notification_mode", "1");
						czintent.putExtra("new_merchant_notification","");
						startActivity(czintent);
					}else {
						alertD2.dismiss();
					}
				}
			});
			alertD2.show();
			alertD2.setCanceledOnTouchOutside(false);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (manageActivation.getStatus() == Status.RUNNING)
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
				relBottominner.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			}
			else{
				
				manageActivation = new PinCodeAsyncTask(ChangeZipCode.this);
				manageActivation.execute();
	
			}
		
		}

		if(view==back_arrow){
			onBackPressed();
		}
	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		/*super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);*/
	}
	
}
