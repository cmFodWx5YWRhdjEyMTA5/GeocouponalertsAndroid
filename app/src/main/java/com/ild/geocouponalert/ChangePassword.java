package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.Login.LoginAsyncTask;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ild.geocouponalert.MerchantListHomePage;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
public class ChangePassword extends Activity implements OnClickListener {

	Context mContext;
	TextView old_password,new_password,confirm_password;
	Button change_password_button;
	ChangePasswordAsyncTask manageActivation;
	ImageView backbtn;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password);
        mContext=this;
        initView();    
	}
	
	private void initView(){ 
		
		old_password = (TextView)findViewById(R.id.old_password);
		new_password = (TextView)findViewById(R.id.new_password);
		confirm_password = (TextView)findViewById(R.id.confirm_password);
		change_password_button = (Button)findViewById(R.id.change_password_button);
		change_password_button.setOnClickListener(this);
		backbtn = (ImageView)findViewById(R.id.backbtn);
		backbtn.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == change_password_button){
			
			if(old_password.getText().toString().trim().length() == 0) {
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Please provide old Password");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			} else if(new_password.getText().toString().trim().length() == 0) {
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Please provide new Password");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			} else if(confirm_password.getText().toString().trim().length() == 0) {
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Please provide confirm New Password");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			} else if(old_password.getText().toString().trim().length() !=0 && new_password.getText().toString().trim().length() !=0 && confirm_password.getText().toString().trim().length() !=0 && !new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())) {
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Your confirm password does not match. Give it another try");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
			} else {
				manageActivation = new ChangePasswordAsyncTask(mContext);
				manageActivation.execute();
			}
		}
		
		if(v == backbtn){
			onBackPressed();
		}
		
	}
	
	public class ChangePasswordAsyncTask extends AsyncTask<Void, Void, Void> {
		Boolean bSuccess;
		ProgressDialog pDialog;
		Context mFriendLogin;
		
		public ChangePasswordAsyncTask(Context mContext) {
			mFriendLogin = mContext;
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			bSuccess=RestCallManager.getInstance().changePassword(COUtils.getDefaults("emailID", mContext),old_password.getText().toString().trim(),new_password.getText().toString().trim());
			return null;
		}  
		
		@Override 
		protected void onPostExecute(Void result) { 
			
			pDialog.dismiss();
			if(bSuccess) {    
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
				topTxt.setText("Thank you");
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText(FOGlobalVariable.IsActivationCodeAlreadyExist);
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					Intent intent = new Intent(ChangePassword.this,SettingScreen.class);
						startActivity(intent);
						ChangePassword.this.finish();
					} 
				});
				alertD2.show();
				alertD2.setCanceledOnTouchOutside(false);
				
			} else {
				
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
				alertD2.show();
				alertD2.setCanceledOnTouchOutside(false);
				
			}
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
