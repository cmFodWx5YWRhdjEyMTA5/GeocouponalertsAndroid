package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ild.geocouponalert.MerchantSelectionScreen.SelectionAsyncTask;
import com.ild.geocouponalert.MerchantSelectionScreen.ViewcouponAsyncTask;
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
import android.text.InputType;
import android.text.Spanned;
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
public class Login extends Activity implements OnClickListener {

	Context mContext;
	TextView register,email,password,forgot_password,terms,privacy;
	Button login_button;
	LoginAsyncTask manageActivation;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        mContext=this;
        initView();    
	}
	
	private void initView(){ 
		
		email = (TextView)findViewById(R.id.email);
		password = (TextView)findViewById(R.id.password);
		register = (TextView)findViewById(R.id.register);
		register.setOnClickListener(this);
		forgot_password = (TextView)findViewById(R.id.forgot_password);
		forgot_password.setOnClickListener(this);
		login_button = (Button)findViewById(R.id.login_button);
		login_button.setOnClickListener(this);
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
		if(v == login_button){
			
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
				alertTxt.setText("Password fields can not be blank");
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
				
				manageActivation = new LoginAsyncTask(mContext);
				manageActivation.execute();
			}
			
		}
		if(v == register){
			
			Intent intent = new Intent(Login.this,Registration.class);
			startActivity(intent);
			Login.this.finish();
		}
		if(v == forgot_password){
			
			Intent intent = new Intent(Login.this,ForgotPassword.class);
			startActivity(intent);
			Login.this.finish();
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
	
	public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
		String bSuccess;
		ProgressDialog pDialog;
		Context mFriendLogin;
		
		public LoginAsyncTask(Context mContext) {
			mFriendLogin = mContext;
		}
		@Override
		protected Void doInBackground(Void... params) {    
			
			bSuccess=RestCallManager.getInstance().userLogin(email.getText().toString().trim(),password.getText().toString().trim(),Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID));
			return null;
		}  
		
		@Override 
		protected void onPostExecute(Void result) { 
			
			pDialog.dismiss();
			if(bSuccess.equalsIgnoreCase("Success")){ 
				
				COUtils.setDefaults("emailID", email.getText().toString().trim(), mContext);
				List<PostcardMaster> lst_fund = new ArrayList<PostcardMaster>();
				lst_fund = DataStore.getInstance().getPostcard();
			
				if(lst_fund.size()==0){
					
				Intent intent = new Intent(Login.this,MerchantListHomePage.class);
				intent.putExtra("business_id", "");
				intent.putExtra("notification_mode", "1");
				intent.putExtra("new_merchant_notification","");
				startActivity(intent);
				Login.this.finish();
				
				} else if(lst_fund.get(0).activation_process_status.equalsIgnoreCase("1")){
					
					Intent intent = new Intent(Login.this,PinCodeScreen.class);
					startActivity(intent);
					Login.this.finish();
				
				} else if(lst_fund.get(0).activation_screen.equalsIgnoreCase("1")){
					
					Intent intent = new Intent(Login.this,ActivationCodeScreen.class);
					intent.putExtra("mode", "0");
					startActivity(intent);
					Login.this.finish();
					
				}
			} else if(bSuccess.equalsIgnoreCase("Failure")) {
				
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
				
			} else {
				
				LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
				View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
				alertDialogBuilder2.setView(promptView);
				final AlertDialog alertD2 = alertDialogBuilder2.create();
				TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
				alertTxt.setText("There is some problem with server. Please restart the application");
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
