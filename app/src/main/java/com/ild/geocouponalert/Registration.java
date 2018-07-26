package com.ild.geocouponalert;


import com.ild.geocouponalert.webmethod.RestCallManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
public class Registration extends Activity implements OnClickListener {

	Context mContext;
	TextView lblLogin,lblTerms,lblPrivacy;
	EditText txtEmail,txtMobileNo,txtPassword,txtConfirmPassword;
	Button btn_register;
    String str_email, str_mobile_no, str_password, str_confirm_paasword;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        mContext=this;
        initView();    
	}
	
	private void initView(){
		txtEmail = findViewById(R.id.txt_email);
		txtMobileNo = findViewById(R.id.txt_mobile_no);
		txtPassword = findViewById(R.id.txt_password);
		txtConfirmPassword = findViewById(R.id.txt_confirm_password);
		lblLogin = findViewById(R.id.lbl_login);
		lblLogin.setOnClickListener(this);
		btn_register = findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		lblTerms = findViewById(R.id.lbl_terms);
		lblTerms.setOnClickListener(this);
		lblPrivacy = findViewById(R.id.lbl_privacy);
		lblPrivacy.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_register){
			if (checkValidation()) {
				new RegistrationAsyncTask(mContext).execute();
			}
		}
		if(v == lblLogin){
			Intent intent = new Intent(Registration.this,Login.class);
			startActivity(intent);
			Registration.this.finish();
		}
		if(v == lblTerms){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-terms-and-conditions"));
			startActivity(browserIntent);
		}
		if(v == lblPrivacy){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geocouponalerts.com/app-privacy-policy"));
			startActivity(browserIntent);
		}
	}

	public boolean checkValidation(){
		str_email = txtEmail.getText().toString().trim();
		str_mobile_no = txtMobileNo.getText().toString().trim();
		str_password = txtPassword.getText().toString().trim();
		str_confirm_paasword = txtConfirmPassword.getText().toString().trim();
		if (str_email.isEmpty() || !COUtils.isEmailValid(str_email)) {
			alertDilog("Please enter a valid email address");
			return false;
		}else if (str_mobile_no.isEmpty() || !COUtils.isMobileNoValid(str_mobile_no)) {
			alertDilog("Enter valid mobile no");
			return false;
		}else if(str_password.length() == 0) {
			alertDilog("Password can not be blank");
			return false;
		}else if(str_confirm_paasword.length() == 0) {
			alertDilog("Confirm Password can not be blank");
			return false;
		}else if(str_password.length() !=0 && str_confirm_paasword.length() !=0 && !str_password.equals(str_confirm_paasword)){
			alertDilog("Your confirm password does not match. Give it another try");
			return false;
		}
		return true;
	}

	public void alertDilog(String alertText){
		LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
		View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
		alertDialogBuilder2.setView(promptView);
		final AlertDialog alertD2 = alertDialogBuilder2.create();
		TextView alertTxt=promptView.findViewById(R.id.alertTxt);
		alertTxt.setText(alertText);
		RelativeLayout relBottomInner = promptView.findViewById(R.id.relBottominner);
		relBottomInner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertD2.dismiss();
			}
		});
		alertD2.setCanceledOnTouchOutside(false);
		alertD2.show();
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
			
			bSuccess=RestCallManager.getInstance().userRegistration(str_email,str_mobile_no,
					str_password,Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID), COUtils.getDefaults("DeviceTokenFCM", mContext), "Android");
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
				alertDilog(FOGlobalVariable.IsActivationCodeAlreadyExist);
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
