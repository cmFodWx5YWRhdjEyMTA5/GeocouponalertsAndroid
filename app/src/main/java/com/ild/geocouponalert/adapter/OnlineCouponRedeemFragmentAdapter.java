package com.ild.geocouponalert.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ild.geocouponalert.MerchantListHomePage;
import com.ild.geocouponalert.R;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.CouponMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.utils.FOGlobalVariable;
import com.ild.geocouponalert.webmethod.RestCallManager;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OnlineCouponRedeemFragmentAdapter extends BaseAdapter {
	public List<CouponMaster> couponList;
	private Activity activity;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader;
	Animation animation;
	ProgressDialog progressDialog;
	Handler mHandler = new Handler();
	GPSTracker gps;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<BusinessLocationMaster> location = new ArrayList<BusinessLocationMaster>();
	LocationRedeemFragmnetAdapter locationadapter=null;
	String user_latitude,user_longitude,coupon_id,buss_id,activation_code,name,details,expiry_date,disclaimer,coupon_code,location_id;
	String online_flag,online_barcode;
	Bitmap bitmap ;
	private int progressStatus = 0;
	private Handler handler = new Handler();

	boolean modeForValidLocationForCouponRedeem = false;

	public OnlineCouponRedeemFragmentAdapter(Activity a, List<CouponMaster> coupon) {
		activity = a;
		couponList = coupon;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity.getApplicationContext());

	}

	public int getCount() {
		return couponList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi=convertView;
		ViewHolder holder = null;

		if(vi==null){
			vi = inflater.inflate(R.layout.online_coupon_fragment_row, null);
			holder = new ViewHolder();
			holder.starBucksTitle=vi.findViewById(R.id.starBucksTitle);
			holder.starBuckstxt1=vi.findViewById(R.id.starBuckstxt1);
			holder.expirestxt=vi.findViewById(R.id.expirestxt);
			holder.expires=vi.findViewById(R.id.expires);
			holder.no_of_coupons=vi.findViewById(R.id.no_of_coupons);
			holder.settingBacktag = vi.findViewById(R.id.settingBacktag);
			holder.online_redeem_button = vi.findViewById(R.id.onlineBtnRedeem);
			holder.online_bottom_section = vi.findViewById(R.id.linearBottom);
			holder.lbl_online_link = vi.findViewById(R.id.lblOnlineLink);
			holder.lbl_online_contact = vi.findViewById(R.id.lblOnlineContact);
			holder.btn_call = vi.findViewById(R.id.btnCall);
			vi.setTag(holder);
		}
		else {

			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
		//final int pos = position;
		final CouponMaster couponObj = couponList.get(position);

		//holder.starBucksTitle.setText(couponObj.name.toString());
		//textView.setText(new String(textFromDatabase, "UTF-8"));
		if(couponObj.details != null){
			holder.starBuckstxt1.setVisibility(View.VISIBLE);
			holder.starBuckstxt1.setText(couponObj.details.toString());
		} else {
			holder.starBuckstxt1.setVisibility(View.GONE);
			holder.starBuckstxt1.setText("");
		}
		holder.expirestxt.setText("Expires on "+couponObj.expiry_date.toString());
		holder.expires.setText(couponObj.disclaimer.toString());
		if(couponObj.quantity.equalsIgnoreCase("0")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_unlimited);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("e")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_expired);
			holder.online_bottom_section.setVisibility(View.GONE);
		} else if(couponObj.quantity.equalsIgnoreCase("1")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_oneleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("2")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_twoleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("3")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_threeleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("4")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_fourleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("5")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_fiveleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("6")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_sixleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("7")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_sevenleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("8")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_eightleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("9")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_nineleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("10")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_tenleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("11")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_elevenleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		} else if(couponObj.quantity.equalsIgnoreCase("12")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_twelveleft);
			holder.online_bottom_section.setVisibility(View.VISIBLE);
		}

		String couponTitle = couponObj.name.toString()+" a";
		ImageSpan imageSpan;

		if(couponObj.universal.equalsIgnoreCase("1")){
			Drawable myIcon = activity.getResources().getDrawable(R.drawable.universal);
			myIcon.setBounds(0, 0, 25, 25);
			imageSpan = new ImageSpan(myIcon);
			//holder.universal_logo.setBackgroundResource(R.drawable.universal);
		} else {
			Drawable myIcon = activity.getResources().getDrawable(R.drawable.nonuniversal);
			myIcon.setBounds(0, 0, 25, 25);
			imageSpan = new ImageSpan(myIcon);
			//holder.universal_logo.setBackgroundResource(R.drawable.nonuniversal);
		}
		SpannableString spannableString = new SpannableString(couponTitle); //Set text of SpannableString from TextView
		spannableString.setSpan(imageSpan, couponTitle.length() - 1, couponTitle.length(), 0); //Add image at end of string
		holder.starBucksTitle.setText(spannableString);

		if(couponObj.online_contact.equalsIgnoreCase("")){
			holder.lbl_online_contact.setText("N/A");
			holder.btn_call.setVisibility(View.INVISIBLE);
		}
		else{
			holder.lbl_online_contact.setText(couponObj.online_contact);
			holder.btn_call.setVisibility(View.VISIBLE);
		}
		holder.btn_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCall(couponObj.online_contact);
			}
		});
		holder.lbl_online_link.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(couponObj.online_url));
				activity.startActivity(browserIntent);
			}
		});

		holder.online_redeem_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gps = new GPSTracker(activity);
				if(gps.canGetLocation()){

					user_latitude = String.valueOf(gps.getLatitude());
					user_longitude = String.valueOf(gps.getLongitude());
				} else {
					user_latitude = "0";
					user_longitude = "0";
				}
				coupon_id = couponObj.coupon_id;
				buss_id = couponObj.buss_id;
				location_id = couponObj.location_id;
				activation_code = couponObj.activation_code;
				name = couponObj.name;
				details = couponObj.details;
				expiry_date = couponObj.expiry_date;
				disclaimer = couponObj.disclaimer;
				coupon_code = couponObj.coupon_code;
				online_flag = couponObj.online_flag;
				online_barcode = couponObj.online_barcode;

				LayoutInflater layoutInflater = LayoutInflater.from(activity);
				View promptView = layoutInflater.inflate(R.layout.alert_redeem, null);

				//promptView.setBackgroundColor(Color.TRANSPARENT);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
				alertDialogBuilder.setView(promptView);
				final AlertDialog alertD = alertDialogBuilder.create();

				final TextView coupon_alert = (TextView) promptView.findViewById(R.id.coupon_alert);
				final TextView dialogNo = (TextView) promptView.findViewById(R.id.btnCancel);
				final TextView dialogYes = (TextView) promptView.findViewById(R.id.btnContinue);
				final TextView topTxt = (TextView) promptView.findViewById(R.id.topTxt);

				dialogNo.setText("No");
				dialogYes.setText("Yes");
				topTxt.setText("Continue");
				coupon_alert.setText("Are you sure you want to\nredeem this coupon?");

				// if button is clicked, close the custom dialog
				dialogNo.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD.dismiss();
					}
				});
				dialogYes.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new Thread(){
							public void run(){
								final boolean status =RestCallManager.getInstance().redeemCoupon(coupon_id,buss_id,Secure.getString(activity.getContentResolver(),Secure.ANDROID_ID),activation_code,COUtils.getDefaults("emailID", activity),user_latitude,user_longitude);
								mHandler.post(new Runnable(){
									@SuppressLint("SimpleDateFormat") public void run() {
										if (progressDialog != null) {
											progressDialog.dismiss();
										}
										if (status) {

											Toast.makeText(activity,"Coupon Redeemed",
													Toast.LENGTH_SHORT).show();

											LayoutInflater layoutInflater2 = LayoutInflater.from(activity);
											View promptView = layoutInflater2.inflate(R.layout.redeemed, null);
											//promptView.setBackgroundColor(Color.TRANSPARENT);
											final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(activity);
											alertDialogBuilder2.setView(promptView);
											final AlertDialog alertD2 = alertDialogBuilder2.create();
											TextView starBucksTitle=(TextView)promptView.findViewById(R.id.starBucksTitle);
											starBucksTitle.setText(name);
											TextView starBuckstxt=(TextView)promptView.findViewById(R.id.starBuckstxt);
											starBuckstxt.setText(details);
											TextView expirestxt=(TextView)promptView.findViewById(R.id.expirestxt);
											expirestxt.setText("Expires on "+expiry_date);
											TextView expires=(TextView)promptView.findViewById(R.id.expires);
											expires.setText(disclaimer);
											TextView redemptionlabel=(TextView)promptView.findViewById(R.id.redemptionlabel);
											TextView redemptioncode=(TextView)promptView.findViewById(R.id.redemptioncode);
											LinearLayout linearBarcode = promptView.findViewById(R.id.linearBarcode);
											ImageView imgBardCode = promptView.findViewById(R.id.imgBardCode);
											TextView lblBarCode = promptView.findViewById(R.id.lblBarCode);
											if (online_flag.equalsIgnoreCase("P")){//P->Physical Location, O->Online Location
												if (online_barcode.equalsIgnoreCase("0")){//0->Coupon Code, 1->Barcode
													redemptionlabel.setVisibility(View.VISIBLE);
													redemptioncode.setVisibility(View.VISIBLE);
													linearBarcode.setVisibility(View.GONE);
													if(coupon_code.equalsIgnoreCase("") || coupon_code.equalsIgnoreCase("N/A")){
														redemptioncode.setText("N/A");
													}
													else{
														redemptioncode.setText(coupon_code);
													}
												}
												else{
													redemptionlabel.setVisibility(View.VISIBLE);
													redemptioncode.setVisibility(View.VISIBLE);
													if(coupon_code.equalsIgnoreCase("") || coupon_code.equalsIgnoreCase("N/A")){
														redemptioncode.setText("N/A");
													}
													else{
														redemptioncode.setVisibility(View.GONE);
														linearBarcode.setVisibility(View.VISIBLE);
														try {
															bitmap = TextToSmoothImageEncode(coupon_code);
															imgBardCode.setImageBitmap(bitmap);
															lblBarCode.setText(coupon_code);
														} catch (WriterException e) {
															e.printStackTrace();
														}
													}
												}
											}
											else{
												redemptioncode.setVisibility(View.VISIBLE);
												linearBarcode.setVisibility(View.GONE);
												if(coupon_code.equalsIgnoreCase("") || coupon_code.equalsIgnoreCase("N/A")){
													redemptioncode.setText("N/A");
												}
												else{
													redemptioncode.setText(coupon_code);
												}
											}

											Button dialogclose = (Button) promptView.findViewById(R.id.close);
											// if button is clicked, close the custom dialog
											dialogclose.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {

													Dialog dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
													dialog.setContentView(R.layout.progressbar);

													final ProgressBar progress=(ProgressBar)dialog.findViewById(R.id.progressBar1);
													final TextView v_coupon=(TextView)dialog.findViewById(R.id.v_coupon);
													final ImageView tick_img=(ImageView)dialog.findViewById(R.id.tick_img);
													final Button close_screen_btn=(Button)dialog.findViewById(R.id.close_screen_btn);
													final LinearLayout linearLocation = (LinearLayout)dialog.findViewById(R.id.linearLocation);
													linearLocation.setVisibility(View.INVISIBLE);
													tick_img.setVisibility(View.INVISIBLE);
													close_screen_btn.setVisibility(View.GONE);


													close_screen_btn.setOnClickListener(new View.OnClickListener() {
														@Override
														public void onClick(View v) {
															Intent intent = new Intent(activity, MerchantListHomePage.class);
															intent.putExtra("business_id", "");
															intent.putExtra("location_id", "");
															intent.putExtra("notification_mode", "2");
															intent.putExtra("new_merchant_notification", "");
															activity.startActivity(intent);
														}

													});

													// Set the progress status zero on each button click
													progressStatus = 0;
													// Start the lengthy operation in a background thread
													new Thread(new Runnable() {
														@Override
														public void run() {
															while(progressStatus < 100){
																// Update the progress status
																progressStatus +=1;
																// Try to sleep the thread for 20 milliseconds
																try{
																	Thread.sleep(30);
																}catch(InterruptedException e){
																	e.printStackTrace();
																}
																// Update the progress bar
																handler.post(new Runnable() {
																	@Override
																	public void run() {
																		progress.setProgress(progressStatus);
																		// If task execution completed
																		if(progressStatus == 100){
																			// Hide the progress bar from layout after finishing task
																			progress.setVisibility(View.GONE);
																			v_coupon.setVisibility(View.VISIBLE);
																			v_coupon.setText("Coupon is valid");
																			tick_img.setVisibility(View.VISIBLE);
																			close_screen_btn.setVisibility(View.VISIBLE);
																		}
																	}
																});
															}
														}
													}).start(); // Start the operation
													dialog.show();
												}
											});
											alertD2.show();


											final Handler handler  = new Handler();
											final Runnable runnable = new Runnable() {
												@Override
												public void run() {
													if (alertD2.isShowing()) {
														alertD2.dismiss();
													}

												}
											};

											alertD2.setOnDismissListener(new DialogInterface.OnDismissListener() {
												@Override
												public void onDismiss(DialogInterface dialog) {


													handler.removeCallbacks(runnable);
													Intent intent = new Intent(activity, MerchantListHomePage.class);
													intent.putExtra("business_id", "");
													intent.putExtra("notification_mode", "1");
													intent.putExtra("new_merchant_notification", "");
													activity.startActivity(intent);
												}
											});

											handler.postDelayed(runnable, 60000*60*3);



										}
										else{
											Toast.makeText(activity," Redeemtion failed",
													Toast.LENGTH_SHORT).show();
										}
									}
								});
							}
						}.start();
						processThread();
						alertD.dismiss();
					}
				});
				alertD.show();
			}
		});
		return vi;
	}
	private void processThread() {


		if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else
		{
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}


		progressDialog = ProgressDialog.show(activity, "", "Updating...");
		progressDialog.setMax(10000);
		new Thread() {

			public void run() {

				int i = 0;
				while (true) {
					longTimeMethod();

				}

			}

		}.start();

	}

	private void longTimeMethod() {

		try {

			Thread.sleep(10000);

		} catch (InterruptedException e) {


			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	class DistanceComp1 implements Comparator<BusinessLocationMaster> {

		@Override
		public int compare(BusinessLocationMaster e1, BusinessLocationMaster e2) {

			if( Float.parseFloat(e1.distance) >  Float.parseFloat(e2.distance)){
				return 1;
			} else {
				return -1;
			}
		}
	}

	private Bitmap TextToSmoothImageEncode(String Value) throws WriterException {

		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		String myStringToEncode = Value;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix bitMatrix = multiFormatWriter.encode(
				myStringToEncode,
				BarcodeFormat.CODE_128,//For QR code
				190,80);
		BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
		Bitmap bitmpapp = barcodeEncoder.createBitmap(bitMatrix);
		return bitmpapp;
	}

	public void onCall(String onlineContact) {
		int permissionCheck = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					activity,
					new String[]{android.Manifest.permission.CALL_PHONE},
					123);
		} else {
			activity.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+onlineContact)));
		}
	}
}