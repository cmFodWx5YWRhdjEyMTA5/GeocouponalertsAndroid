package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.CouponFragmnetAdapter;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.Category;
import com.ild.geocouponalert.classtypes.CouponMaster;
import com.ild.geocouponalert.datastore.DataStore;


import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class CouponFragment extends Activity {
	CouponFragmnetAdapter couponadapter=null;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<CouponMaster> coupon = new ArrayList<CouponMaster>();
	ListView couponlistView;
	TextView no_of_coupons;
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_fragment_listview);
        couponlistView = (ListView) findViewById(R.id.couponlistView);
        couponLocation = DataStore.getInstance().getCouponLocation();
        coupon = couponLocation.get(0).coupons_detail;
        couponadapter = new CouponFragmnetAdapter(this,coupon);
        couponlistView.setAdapter(couponadapter); 
		 
    }  
}  