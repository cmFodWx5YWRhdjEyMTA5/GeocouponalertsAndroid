package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.CouponFragmnetAdapter;
import com.ild.geocouponalert.adapter.CouponRedeemFragmnetAdapter;
import com.ild.geocouponalert.adapter.LocationFragmnetAdapter;
import com.ild.geocouponalert.adapter.OnlineCouponRedeemFragmentAdapter;
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
	CouponRedeemFragmnetAdapter couponAdapter=null;
	OnlineCouponRedeemFragmentAdapter onlineCouponAdapter=null;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<CouponMaster> couponArray = new ArrayList<CouponMaster>();
	List<CouponMaster> onlineCouponArray = new ArrayList<CouponMaster>();
	List<CouponMaster> physicalCouponArray = new ArrayList<CouponMaster>();
	ListView couponlistView;
	TextView nocoupon;
	String locID;
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_fragment_listview);
        nocoupon = (TextView)findViewById(R.id.nocoupon);
        couponlistView = (ListView) findViewById(R.id.couponlistView);
        couponLocation = DataStore.getInstance().getCouponLocation();
		couponArray = couponLocation.get(0).coupons_detail;
        locID = getIntent().getStringExtra("locID");
        for (CouponMaster couponObj : couponArray) {
        	if (couponObj.online_flag.equalsIgnoreCase("O")){
        		onlineCouponArray.add(couponObj);
			}
			else{
        		physicalCouponArray.add(couponObj);
			}
		}
		if (locID.equalsIgnoreCase("O")){
			if (onlineCouponArray.size() > 0) {
				onlineCouponAdapter = new OnlineCouponRedeemFragmentAdapter(this, onlineCouponArray);
				couponlistView.setAdapter(onlineCouponAdapter);
			} else {
				nocoupon.setText("All coupons have been expired");
			}
		}
		else {
			if (physicalCouponArray.size() > 0) {
				couponAdapter = new CouponRedeemFragmnetAdapter(this, physicalCouponArray);
				couponlistView.setAdapter(couponAdapter);
			} else {
				nocoupon.setText("All coupons have either expired or been redeemed for this location.");
			}
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