package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.ild.geocouponalert.classtypes.FundraiserMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
import com.ild.geocouponalert.webmethod.RestCallManager;


public class ActivationCodeScreen extends Activity implements OnClickListener {
	
	Context mContext;
	TextView btnSubmit;
	EditText couponNumber;
	ImageView back_arrow;
	public String activation_code,device_id;
	ActivationCodeAsyncTask manageActivation;
	String condition="";
	
	List<PostcardMaster> postcard_list = new ArrayList<PostcardMaster>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation_code_screen);
		DataStore.getInstance().clearDataStore();
		mContext=this; 
		couponNumber=(EditText)findViewById(R.id.postcardNumber);
		btnSubmit=(TextView)findViewById(R.id.btnSubmit);
		
		if(getIntent().getExtras().getString("mode").equalsIgnoreCase("1")){
		back_arrow=(ImageView)findViewById(R.id.back_arrow);
		back_arrow.setVisibility(View.VISIBLE);
		back_arrow.setOnClickListener(this);
		
		}
				
		btnSubmit.setOnClickListener(this); 
	} 
	
	public class ActivationCodeAsyncTask extends AsyncTask<Void, Void, Void> { 
		boolean bSuccess;
		ProgressDialog pDialog;
		Activity mFriendLogin; 
		
		public ActivationCodeAsyncTask(Activity activity) {
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {    
			
			bSuccess=RestCallManager.getInstance().insertActivationCode(activation_code, device_id,COUtils.getDefaults("emailID", mContext));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);  
			
			
			if(bSuccess){
			postcard_list = DataStore.getInstance().getPostcard();
			if(postcard_list.size() > 0){
				
				Intent intent = new Intent(mContext, PinCodeScreen.class);
				intent.putExtra("activation_code", activation_code);
				intent.putExtra("mode", getIntent().getExtras().getString("mode"));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);	
			    }	
			}
			
			
			else{
				//Toast.makeText(mContext, FOGlobalVariable.IsActivationCodeAlreadyExist, Toast.LENGTH_SHORT).show();
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText(FOGlobalVariable.IsActivationCodeAlreadyExist);
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
			
			pDialog.dismiss();
			pDialog = null;
			
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
			activation_code=couponNumber.getText().toString().trim();
			if(activation_code.equalsIgnoreCase("")){
				
				/*Toast.makeText(mContext, "Activation Code Required", Toast.LENGTH_SHORT).show();
				return;*/
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Activation Code Required");
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
			/*  LayoutInflater layoutInflater = LayoutInflater.from(mContext);
				View promptView = layoutInflater.inflate(R.layout.alert_activation_code, null);
				//promptView.setBackgroundColor(Color.TRANSPARENT);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setView(promptView);
				final AlertDialog alertD = alertDialogBuilder.create();
				
				final TextView dialogNo = (TextView) promptView.findViewById(R.id.btnCancel);
				final TextView dialogYes = (TextView) promptView.findViewById(R.id.btnContinue);
				dialogNo.setText("Cancel");
				dialogYes.setText("Continue");
				
				// if button is clicked, close the custom dialog
				dialogNo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD.dismiss();
					}
				});
				
				
				// if button is clicked, close the custom dialog
				dialogYes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						device_id = Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID);
						manageActivation = new ActivationCodeAsyncTask(ActivationCodeScreen.this);
						manageActivation.execute();
						alertD.dismiss();
					}
				});
				 
				alertD.show();
			*/
				device_id = Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID);
				manageActivation = new ActivationCodeAsyncTask(ActivationCodeScreen.this);
				manageActivation.execute();
			}
		}
		
		if(view == back_arrow){
			onBackPressed();
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	
}
