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
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.ild.geocouponalert.MerchantListHomePage;
import com.ild.geocouponalert.utils.COUtils;
public class Welcome_screen extends Activity implements OnClickListener{

	Context mContext;
	TextView txtView1;
	Button login,register;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_screen);
        mContext=this;
        initView();    
	}
	
	private void initView(){ 
		
		txtView1 = (TextView)findViewById(R.id.txtView1);
		String formattedText = getString(R.string.welcome_txtView1);
		Spanned result = Html.fromHtml(formattedText);
		txtView1.setText(result);
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(this);
		register = (Button)findViewById(R.id.register);
		register.setOnClickListener(this);
		
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == login){
			
			Intent intent = new Intent(mContext, Login.class);
			startActivity(intent);
			Welcome_screen.this.finish();
		}
		if(v == register){
			
			Intent intent = new Intent(mContext, Registration.class);
			startActivity(intent);
			Welcome_screen.this.finish();
		}
		
	}
}
