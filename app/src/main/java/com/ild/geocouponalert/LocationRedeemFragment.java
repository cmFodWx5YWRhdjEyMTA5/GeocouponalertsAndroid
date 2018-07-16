package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.ild.geocouponalert.adapter.CouponFragmnetAdapter;
import com.ild.geocouponalert.adapter.LocationFragmnetAdapter;
import com.ild.geocouponalert.adapter.LocationRedeemFragmnetAdapter;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.CouponMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.webmethod.RestCallManager;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationRedeemFragment extends Activity {
	
	static Context mContext;
	LocationRedeemFragmnetAdapter locationadapter=null;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<BusinessLocationMaster> location = new ArrayList<BusinessLocationMaster>();
	ListView locationlistView;
    public void onCreate(Bundle savedInstanceState) { 
    	 super.onCreate(savedInstanceState);
         setContentView(R.layout.location_fragment_listview);
         locationlistView = (ListView) findViewById(R.id.locationlistView);
         couponLocation = DataStore.getInstance().getCouponLocation();
         location = couponLocation.get(0).location_details;
         Collections.sort(location,new DistanceComp1());
         if(location.size() >0){
        	 locationadapter = new LocationRedeemFragmnetAdapter(this,location);
        	 locationlistView.setAdapter(locationadapter);
         } else {
        	 Toast.makeText(getApplicationContext(), "No Information Found.", 2000).show();
         }
    } 
    
    @Override
   	public void onBackPressed() {
   	    // TODO Auto-generated method stub
   	    super.onBackPressed();
   	    Intent intent = new Intent(LocationRedeemFragment.this, MerchantListHomePage.class);
   		intent.putExtra("business_id", "");
   		intent.putExtra("location_id", "");
   		intent.putExtra("notification_mode", "2");
   		intent.putExtra("new_merchant_notification", "");
   		startActivity(intent); 
   	}
	
}

class DistanceComp1 implements Comparator<BusinessLocationMaster>{
	 
    @Override
    public int compare(BusinessLocationMaster e1, BusinessLocationMaster e2) {
    	
        if( Float.parseFloat(e1.distance) >  Float.parseFloat(e2.distance)){
            return 1;
        } else {
            return -1;
        }
    }
}

