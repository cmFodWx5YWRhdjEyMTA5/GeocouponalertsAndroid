package com.ild.geocouponalert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ild.geocouponalert.adapter.CouponFragmnetAdapter;
import com.ild.geocouponalert.adapter.LocationFragmnetAdapter;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.CouponMaster;
import com.ild.geocouponalert.datastore.DataStore;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationFragment extends Activity {
	LocationFragmnetAdapter locationadapter=null;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<BusinessLocationMaster> location = new ArrayList<BusinessLocationMaster>();
	ListView locationlistView;
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_fragment_listview);
        locationlistView = (ListView) findViewById(R.id.locationlistView);
        couponLocation = DataStore.getInstance().getCouponLocation();
        location = couponLocation.get(0).location_details;
        Collections.sort(location,new DistanceComp());
        locationadapter = new LocationFragmnetAdapter(this,location);
        locationlistView.setAdapter(locationadapter);
    }
} 


class DistanceComp implements Comparator<BusinessLocationMaster>{
	 
    @Override
    public int compare(BusinessLocationMaster e1, BusinessLocationMaster e2) {
    	
        if( Float.parseFloat(e1.distance) >  Float.parseFloat(e2.distance)){
            return 1;
        } else {
            return -1;
        }
    }
}