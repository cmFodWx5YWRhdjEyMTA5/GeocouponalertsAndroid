package com.ild.geocouponalert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.utils.FOGlobalVariable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class NearbyMerchants extends Activity implements OnClickListener{

	Context mContext;
	ImageView btnBack;
	ImageView fundraiser_logo,back_arrow,settingBack;
	TextView comp_name,address1,address2,phone;
	String latitude,longitude,buss_name,banner_img;
	Double lat,longt;
	TextView heading;
	LatLng latLng;
	String user_latitude,user_longitude;
	List<BusinessMaster> lst_business = new ArrayList<BusinessMaster>();
	List<BusinessLocationMaster> lst_bus_location = new ArrayList<BusinessLocationMaster>();
	
	GPSTracker gps;
	// Google Map
    private GoogleMap googleMap;
	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_location);
        heading = (TextView)findViewById(R.id.heading);
        heading.setText("Nearby Merchants");
        settingBack = (ImageView)findViewById(R.id.settingBack);    
        settingBack.setOnClickListener(this);
        mContext=this;  
		initView();	
		 try {
	            // Loading map
	            initilizeMap();
	 
	        } catch (Exception e) {  
	            e.printStackTrace(); 
	        }
               
    } 
	
	private void initilizeMap() {
		
		lst_business = DataStore.getInstance().getSelectedBusiness();
		
		gps = new GPSTracker(this);
		if(gps.canGetLocation()){ 
			
			user_latitude = String.valueOf(gps.getLatitude());
			user_longitude = String.valueOf(gps.getLongitude());
			/*user_latitude = "38.7073";
			user_longitude = "-121.2708";*/
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            
            try {
            	
           if(lst_business.size()>0){
        	   int count = 0;
				for(int i=0;i<lst_business.size();i++){
					
					lst_bus_location = lst_business.get(i).geofence_location_details;
					for(int j=0;j<lst_bus_location.size();j++){
						
						double M_PI = 3.1415926535897932385;
	
						int nRadius = 6371; // Earth's radius in Kilometers
					    double latDiff = (Double.valueOf(lst_bus_location.get(j).buss_loc_lat) - Double.valueOf(user_latitude)) * (M_PI/180);
					    double lonDiff = (Double.valueOf(lst_bus_location.get(j).buss_loc_long) - Double.valueOf(user_longitude)) * (M_PI/180);
					    double lat1InRadians = Double.valueOf(user_latitude) * (M_PI/180);
					    double lat2InRadians = Double.valueOf(lst_bus_location.get(j).buss_loc_lat)  * (M_PI/180);
					    double nA = Math.pow ( Math.sin(latDiff/2), 2 ) + Math.cos(lat1InRadians)*  Math.cos(lat2InRadians)*  Math.pow ( Math.sin(lonDiff/2), 2 );
					    double nC = 2 * Math.atan2( Math.sqrt(nA), Math.sqrt( 1 - nA ));
					    double nD = nRadius * nC;
					    //double distance_in_meters = nD*1000;
					    if(nD <= 16.0934){
					    	count=count+1;
							Location dest_location = new Location("");
							
							final Double latt = Double.valueOf(lst_bus_location.get(j).buss_loc_lat);
							final Double longtt = Double.valueOf(lst_bus_location.get(j).buss_loc_long);
							
							dest_location.setLatitude(latt);
						    dest_location.setLongitude(longtt);
						   
							String address;
						
						
							MarkerOptions marker = new MarkerOptions().
							position(new LatLng(latt, longtt)).
							title(lst_business.get(i).name);
							//snippet(address).
							//icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));
			      
							// adding marker
							
							CameraUpdate center=
								        CameraUpdateFactory.newLatLng(new LatLng(latt,longtt));
						    CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);
	
							googleMap.moveCamera(center);
							googleMap.animateCamera(zoom);
							googleMap.addMarker(marker); 
		
							googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
					            { 
					                @Override
					                public void onInfoWindowClick(Marker arg0) {
					                	LatLng coordinate2=arg0.getPosition();
					                	
					                	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					    				Uri.parse("http://maps.google.com/maps?daddr="+coordinate2.latitude+","+coordinate2.longitude));
					    				startActivity(intent);
					                }
					            });
					    }
				  }
				// create marker
				}

			     if(count==0){
					 LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
					 View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
					 AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
					 alertDialogBuilder2.setView(promptView);
					 final AlertDialog alertD2 = alertDialogBuilder2.create();
					 TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
					 topTxt.setText("Oops");

					 TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
					 alertTxt.setText("There is no Merchant available within 10 miles radius");
					 RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
					 relBottominner.setOnClickListener(new OnClickListener() {
						 @Override
						 public void onClick(View v) {
								 alertD2.dismiss();
							 onBackPressed();
						 }
					 });
					 alertD2.show();
					 alertD2.setCanceledOnTouchOutside(false);
				 }

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
         // Changing marker icon
            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plusone_medium_off_client));
         //Moving camera with location
           /* CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(8).build();
     
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            
     */       //Showing Current Location
            
            googleMap.setMyLocationEnabled(true); // false to disable
            
            //Zooming button
            
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            
            //Zooming Functionality.You can disable zooming gesture functionality
            
           // googleMap.getUiSettings().setZoomGesturesEnabled(false);
            
            //Compass Functionality
            
            googleMap.getUiSettings().setCompassEnabled(true);
            
            
            //Location Button
            
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            
            
            
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
	  }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
	
	
	private void initView(){
		
		/*lst_fund = DataStore.getInstance().getFundraiser();
		View sub_view=findViewById(R.id.sub_header);
		
		btnBack=(ImageView)findViewById(R.id.view_back);
		btnBack.setVisibility(View.VISIBLE);
		View hr=(View)findViewById(R.id.view4);
		hr.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
			 
		String image_url = lst_fund.get(0).logo_img.toString();
		fundraiser_logo=(ImageView)findViewById(R.id.fundraiserLogo);
		comp_name=(TextView)findViewById(R.id.app_company_name1);
		address1=(TextView)findViewById(R.id.app_company_info1);
		address2=(TextView)findViewById(R.id.app_company_info2);
		phone=(TextView)findViewById(R.id.app_company_info3);
		comp_name.setText(lst_fund.get(0).name.toString());
		address1.setText(lst_fund.get(0).address1.toString()+" "+lst_fund.get(0).address2.toString());
		address2.setText(lst_fund.get(0).city.toString()+", "+lst_fund.get(0).state_initial.toString().toUpperCase()+" "+lst_fund.get(0).zip.toString());
		if(lst_fund.get(0).phone.length()<7){
			phone.setText(lst_fund.get(0).phone.toString());
		}
		else{
		
		String first_three=lst_fund.get(0).phone.toString().substring(0,3);
		String second_three=lst_fund.get(0).phone.toString().substring(3,6);
		String last=lst_fund.get(0).phone.toString().substring(6);
		phone.setText(first_three + Html.fromHtml("<font color='#ff0000'>-</font>")+second_three+Html.fromHtml("<font color='#ff0000'>-</font>")+last);
		}
		ImageLoader imgLoader = new ImageLoader(getApplicationContext());
		imgLoader.DisplayImage(image_url,fundraiser_logo);*/
		
	}
	

    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*if(v == btnBack){
			onBackPressed();
		}*/
		if(v == settingBack){
			onBackPressed();
		}
		
		
	}
	
	@Override 
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		/*Intent intent = new Intent(mContext, MerchantListHomePage.class);
		intent.putExtra("business_id", "");
		intent.putExtra("location_id", "");
		intent.putExtra("notification_mode", "2");
		intent.putExtra("new_merchant_notification", "");
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);*/
	}
}

