package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.CouponFragmnetAdapter;
import com.ild.geocouponalert.adapter.CouponRedeemFragmnetAdapter;
import com.ild.geocouponalert.adapter.LocationFragmnetAdapter;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.Category;
import com.ild.geocouponalert.classtypes.CouponMaster;
import com.ild.geocouponalert.datastore.DataStore;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CouponRedeemFragment extends Activity {
	CouponRedeemFragmnetAdapter couponadapter=null;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<CouponMaster> coupon = new ArrayList<CouponMaster>();
	ListView couponlistView;
	TextView nocoupon;
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_fragment_listview);
        nocoupon = (TextView)findViewById(R.id.nocoupon);
        couponlistView = (ListView) findViewById(R.id.couponlistView);
        couponLocation = DataStore.getInstance().getCouponLocation();
        coupon = couponLocation.get(0).coupons_detail;
        if(coupon.size() >0){
        	couponadapter = new CouponRedeemFragmnetAdapter(this,coupon);
            couponlistView.setAdapter(couponadapter);
            //couponadapter.notifyDataSetChanged();
        } else {
        	nocoupon.setText("All coupons have either expired or been redeemed for this location.");
       	 	//Toast.makeText(getApplicationContext(), "No Information Found.", 2000).show();
        }
    } 
    @Override
	public void onBackPressed() {
	    // TODO Auto-generated method stub
	    super.onBackPressed();
	    Intent intent = new Intent(CouponRedeemFragment.this, MerchantListHomePage.class);
		intent.putExtra("business_id", "");
		intent.putExtra("location_id", "");
		intent.putExtra("new_merchant_notification", "");
		startActivity(intent); 
	}
}  