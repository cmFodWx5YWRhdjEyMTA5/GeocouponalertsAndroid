package com.ild.geocouponalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
import com.ild.geocouponalert.webmethod.RestCallManager;

import java.util.HashMap;

public class SuggestMerchant extends Activity implements OnClickListener {

	Context mContext;
	TextView name,email,phone,address,city,state,zip;
	String mname,memail,mphone,maddress,mcity,mstate,mzip;
	Button btnSubmit;
	SuggestMerchantAsyncTask manageActivation;
	ImageView backbtn;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.suggest_merchant);
        mContext=this;
        initView();
	}

	private void initView(){
		name = (TextView)findViewById(R.id.name);
		email = (TextView)findViewById(R.id.email);
		phone = (TextView)findViewById(R.id.phone);
		address = (TextView)findViewById(R.id.address);
		city = (TextView)findViewById(R.id.city);
		state = (TextView)findViewById(R.id.state);
		zip = (TextView)findViewById(R.id.zip);
		//save = (TextView) findViewById(R.id.save);
		//save.setOnClickListener(this);
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		backbtn = (ImageView)findViewById(R.id.backbtn);
		backbtn.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private void showAlertMsg(String msg){
		LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
		View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
		alertDialogBuilder2.setView(promptView);
		final AlertDialog alertD2 = alertDialogBuilder2.create();
		TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
		alertTxt.setText(msg);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btnSubmit){

			mname = name.getText().toString().trim();
			memail = email.getText().toString().trim();
			mphone = phone.getText().toString().trim();
			maddress = address.getText().toString().trim();
			mcity = city.getText().toString().trim();
			mstate = state.getText().toString().trim();
			mzip = zip.getText().toString().trim();

			if(mname.length() == 0) {
				showAlertMsg("Please enter name.");
			}else if(memail.length() == 0){
				showAlertMsg("Please enter email.");
			}else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(memail).matches()){
				showAlertMsg("Not a valid email.");
			}else {
				manageActivation = new SuggestMerchantAsyncTask(mContext);
				manageActivation.execute();
			}
		}

		if(v == backbtn){
			onBackPressed();
		}

	}

	public class SuggestMerchantAsyncTask extends AsyncTask<Void, Void, Void> {
		Boolean bSuccess;
		ProgressDialog pDialog;
		Context mFriendLogin;

		public SuggestMerchantAsyncTask(Context mContext) {
			mFriendLogin = mContext;
		}
		@Override
		protected Void doInBackground(Void... params) {

			String uemail = COUtils.getDefaults("emailID",mFriendLogin);

			HashMap<String,String> data = new HashMap<String,String>();

			data.put("user_email",uemail);
			data.put("s_merchant_name",mname);
			data.put("s_merchant_email",memail);
			data.put("s_merchant_phone",mphone);
			data.put("s_merchant_address",maddress);
			data.put("s_merchant_city",mcity);
			data.put("s_merchant_state",mstate);
			data.put("s_merchant_zip",mzip);

			bSuccess=RestCallManager.getInstance().suggestedMerchant(data);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			pDialog.dismiss();
			//if(bSuccess) {

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
							onBackPressed();
						}else {
							alertD2.dismiss();
						}
					}
				});
				alertD2.show();
				alertD2.setCanceledOnTouchOutside(false);

			/*} else {

				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText(FOGlobalVariable.IsActivationCodeAlreadyExist);
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.show();
				alertD2.setCanceledOnTouchOutside(false);
				
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
}
