package com.ild.geocouponalert;

import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.imagefile.ImageLoaderFull;
import com.squareup.picasso.Picasso;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;


	public class CouponDetails extends TabActivity  implements OnClickListener{
		RelativeLayout settingBack;
		String buss_name,banner_img;
		TextView starBucksHeading;
		ImageView banner_image;
		Context mContext;
		public ImageLoaderFull imageLoader; 
		
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.coupon_listing);
			mContext=this;
			banner_image = (ImageView)findViewById(R.id.banner_image);
			imageLoader=new ImageLoaderFull(this.getApplicationContext());
			buss_name = getIntent().getExtras().getString("buss_name");
			banner_img = getIntent().getExtras().getString("banner_img");
			//imageLoader.clearCache();
			/*imageLoader.DisplayImage(banner_img.trim().toString(), banner_image);*/
			Picasso.with(mContext)
					.load(banner_img.trim().toString())
					.fit()
					.placeholder(R.drawable.no_image)
					.into(banner_image);
			starBucksHeading = (TextView)findViewById(R.id.starBucksHeading);
			starBucksHeading.setText(buss_name);
			settingBack = (RelativeLayout)findViewById(R.id.settingBack);
			settingBack.setOnClickListener(this);

			Resources ressources = getResources(); 
			TabHost tabHost = getTabHost(); 
			
			// Android tab
			Intent intentCoupons = new Intent().setClass(this, CouponFragment.class);
			TabSpec tabSpecCoupons = tabHost
				.newTabSpec("Coupons")
				.setIndicator("Coupons")
				.setContent(intentCoupons);

			// Apple tab
			Intent intentLocations = new Intent().setClass(this, LocationFragment.class);
			TabSpec tabSpecLocations = tabHost
				.newTabSpec("Locations")
				.setIndicator("Locations")
				.setContent(intentLocations);
			 
			
		
			// add all tabs 
			tabHost.addTab(tabSpecCoupons);
			tabHost.addTab(tabSpecLocations);
			
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setAllCaps(false); 
			tv.setTextSize(14);
			
			TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
			tv2.setTextColor(Color.parseColor("#ffffff"));
			tv2.setAllCaps(false);  
			tv2.setTextSize(14);
			
			
			//set Windows tab as default (zero based)
			tabHost.setCurrentTab(0);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == settingBack){
				finish();
				/*Intent intent = new Intent(CouponDetails.this, MerchantSelectionScreen.class);
				intent.putExtra("mode", "1");
				startActivity(intent); */ 
			}
			
		}

	}