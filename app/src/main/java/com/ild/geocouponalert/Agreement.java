package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.classtypes.FundraiserMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class Agreement extends Activity implements OnClickListener{
	
	Context mContext;
	List<PostcardMaster> lst_postcard = new ArrayList<PostcardMaster>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		mContext=this;
		initView(); 
		
		
		
		
	}
	
	private void initView(){
		
		
		lst_postcard = DataStore.getInstance().getPostcard();
		

		
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View promptView = layoutInflater.inflate(R.layout.agreement_alert, null);
		//promptView.setBackgroundColor(Color.TRANSPARENT);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		alertDialogBuilder.setView(promptView);
		final AlertDialog alertD = alertDialogBuilder.create();
		WebView web_view = new WebView(mContext);
		web_view.setVerticalScrollBarEnabled(false);

	    ((WebView)promptView.findViewById(R.id.webView1)).addView(web_view);
	    

	    //web_view.loadData(getString(R.string.hello), "text/html", "utf-8");
	    
	    String text = "<html><head>"
		          + "<style type=\"text/css\">body{color: #343434;}"
		          + "</style></head>"
		          + "<body>"                          
		          + lst_postcard.get(0).agreement_text.toString()
		          + "</body></html>";
	    
	    web_view.loadData(text, "text/html", "utf-8");
		   
		
		final TextView dialogNo = (TextView) promptView.findViewById(R.id.btnCancel);
		final TextView dialogYes = (TextView) promptView.findViewById(R.id.btnAgree);
			
		dialogNo.setText("Cancel");
				
		// if button is clicked, close the custom dialog
		dialogNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		        alertDialogBuilder.setTitle("Exit Application?");
		        alertDialogBuilder.setIcon(R.drawable.exit);
		        alertDialogBuilder
		                .setMessage("Are you sure you want to exit from this aplication?")
		                .setCancelable(false)
		                .setPositiveButton("Yes",
		                        new DialogInterface.OnClickListener() {
		                            public void onClick(DialogInterface dialog, int id) {
		                            	finish();
		                                moveTaskToBack(true);
		                                android.os.Process.killProcess(android.os.Process.myPid());
		                                System.exit(1);
		                            }
		                        })

		                .setNegativeButton("No", new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {

		                        dialog.cancel();
		                    }
		                });

		        AlertDialog alertDialog = alertDialogBuilder.create();
		        alertDialog.show();
				
			}
		});
		
		/*dialogNo.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});*/
		
		
		// if button is clicked, close the custom dialog
		dialogYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertD.dismiss();
				
				COUtils.setDefaults("agreementStatus", "1", mContext);
				//finish();
				Intent intent=new Intent(mContext, ActivationCodeScreen.class);
				intent.putExtra("mode","0");
				startActivity(intent);
						
			}
		});
		 
		alertD.show();  
	
	
	
	}
	
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            	finish();
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
}
