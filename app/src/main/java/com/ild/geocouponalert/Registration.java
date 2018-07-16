package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ild.geocouponalert.MerchantListHomePage;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
public class Registration extends Activity implements OnClickListener {

	Context mContext;
	TextView login,email,password,confirm_password,terms,privacy;
	Button register_button;
	RegistrationAsyncTask manageActivation;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        mContext=this;
        initView();    
	}
	
	private void initView(){ 
		
		email = (TextView)findViewById(R.id.email);
		password = (TextView)findViewById(R.id.password);
		confirm_password = (TextView)findViewById(R.id.confirm_password);
		login = (TextView)findViewById(R.id.login);
		login.setOnClickListener(this);
		register_button = (Button)findViewById(R.id.register_button);
		register_button.setOnClickListener(this);
		terms = (TextView)findViewById(R.id.terms);
		terms.setOnClickListener(this);
		privacy = (TextView)findViewById(R.id.privacy);
		privacy.setOnClickListener(this);
		
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == register_button){
			
			Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
			Matcher matcher1 = pattern1.matcher(email.getText().toString().trim());
			if(email.getText().toString().trim().length() == 0) {
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Please Enter Email ID");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
				
			} else if (!matcher1.matches() && email.getText().toString().trim().length() != 0) {
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Please enter a valid email address");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
				
			} else if(password.getText().toString().trim().length() == 0) {
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Password field can not be blank");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
				
			}  else if(confirm_password.getText().toString().trim().length() == 0) {
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("Confirm Password can not be blank");
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
				
			} else if(password.getText().toString().trim().length() !=0 && confirm_password.getText().toString().trim().length() !=0 && !password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){
				
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
				
				manageActivation = new RegistrationAsyncTask(mContext);
				manageActivation.execute();
				
			}
			
		}
		
		if(v == login){
			
			Intent intent = new Intent(Registration.this,Login.class);
			startActivity(intent);
			Registration.this.finish();
		}
		if(v == terms){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-terms-and-conditions"));
			startActivity(browserIntent);
		}
		if(v == privacy){ 
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-privacy-policy"));
			startActivity(browserIntent);
		}
	}
	
	public class RegistrationAsyncTask extends AsyncTask<Void, Void, Void> {
		Boolean bSuccess;
		ProgressDialog pDialog;
		Context mFriendLogin;
		
		public RegistrationAsyncTask(Context mContext) {
			mFriendLogin = mContext;
		}
		@Override
		protected Void doInBackground(Void... params) {    
			
			bSuccess=RestCallManager.getInstance().userRegistration(email.getText().toString().trim(),password.getText().toString().trim(),Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID));
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
				TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
				topTxt.setText("Thank you");
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText(Html.fromHtml("You have registered successfully. Click on the &quot;OK&quot; button to go to the login screen"));
				RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
				relBottominner.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD2.dismiss();
						Intent intent = new Intent(Registration.this,Login.class);
						startActivity(intent);
						Registration.this.finish();
					} 
				});
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
				
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
				alertD2.setCanceledOnTouchOutside(false);
				alertD2.show();
				
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
