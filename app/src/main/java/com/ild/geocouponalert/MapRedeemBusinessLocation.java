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
import com.ild.geocouponalert.datastore.DataStore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class MapRedeemBusinessLocation extends Activity implements OnClickListener{

	Context mContext;
	ImageView btnBack;
	ImageView fundraiser_logo,back_arrow,settingBack;
	TextView comp_name,address1,address2,phone;
	String latitude,longitude,buss_name,banner_img;
	Double lat,longt;
	TextView heading;
	LatLng latLng;
	List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	List<BusinessLocationMaster> location = new ArrayList<BusinessLocationMaster>();
	// Google Map
    private GoogleMap googleMap;
	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_location);
        heading = (TextView)findViewById(R.id.heading);
        couponLocation = DataStore.getInstance().getCouponLocation();
        location = couponLocation.get(0).location_details;
        buss_name = couponLocation.get(0).buss_name;
        banner_img = couponLocation.get(0).banner_img;
        heading.setText(couponLocation.get(0).buss_name);
        settingBack = (ImageView)findViewById(R.id.settingBack);    
        settingBack.setOnClickListener(this);
        mContext=this;  
		initView();	
		 try {
	            // Loading map
	            //initilizeMap();
	 
	        } catch (Exception e) {  
	            e.printStackTrace(); 
	        }
               
    } 
	
	/*private void initilizeMap() {
		
		latitude = getIntent().getExtras().getString("buss_loc_lat");
		longitude = getIntent().getExtras().getString("buss_loc_long");
		//Toast.makeText(getApplicationContext(), latitude+ " "+longitude, 3000).show();
		lat = Double.parseDouble(latitude); 
		longt = Double.parseDouble(longitude);
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            
            //User Location by Pincode
           
            final Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(lat, longt, 1); 
			  if (addresses != null && !addresses.isEmpty()) {
			    Address address = addresses.get(0);
			    //latLng = new LatLng(lat, longt);
			    googleMap.addMarker(new MarkerOptions()
			    //.center(new LatLng(address.getLatitude(), address.getLongitude()))
	            .position(new LatLng(lat,longt))
	            .title(couponLocation.get(0).buss_name)
	            .snippet(address.getAddressLine(0)+", "+address.getLocality()+", "+address.getCountryName()+", "+address.getPostalCode())
	            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
			    LatLng coordinate = new LatLng(lat, longt);
			    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 11.0f);
			    googleMap.animateCamera(yourLocation);
			    //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			    googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() 
	            {
	                @Override 
	                public void onInfoWindowClick(Marker arg0) {
	                    // call an activity(xml file)
	                	LatLng coordinate2=arg0.getPosition();
	                	
	                	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
	    				Uri.parse("http://maps.google.com/maps?daddr="+coordinate2.latitude+","+coordinate2.longitude));
	    				startActivity(intent);
	                }
	            });
			  } else { 
			    Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show(); 
			  }
			} catch (IOException e) {
			  // handle exception
			}        
            
         // latitude and longitude
            double latitude =0;
            double longitude =0;
          
            Location mylocation = new Location("");
            mylocation.setLatitude(addresses.get(0).getLatitude());
            mylocation.setLongitude(addresses.get(0).getLongitude());
            
            try {
				for(int i=0;i<location.size();i++){
					
					Location dest_location = new Location("");
					latitude=Double.valueOf(location.get(i).getBuss_loc_lat());
					longitude=Double.valueOf(location.get(i).getBuss_loc_long());
					
					final Double latt = latitude;
					final Double longtt = longitude;
					
					dest_location.setLatitude(latitude);
				    dest_location.setLongitude(longitude);
				   
					String address;
					if(latitude != lat && longitude != longt) {
					if(location.get(i).getAddress2().equalsIgnoreCase("")){
					address=location.get(i).getAddress1()+", "+location.get(i).getCity()+", "+location.get(i).getState_initial()+", "+location.get(i).getZip()*//*+", "+distance+" Miles"*//*;
					MarkerOptions marker = new MarkerOptions().
					position(new LatLng(latitude, longitude)).
					title(couponLocation.get(0).buss_name).
					snippet(address).
					icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));
	      
					// adding marker
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
					else{
					address=location.get(i).getAddress1()+", "+location.get(i).getAddress2()+", "+location.get(i).getCity()+", "+location.get(i).getState_initial()+", "+location.get(i).getZip()*//*+", "+distance+" Miles"*//*;
					MarkerOptions marker = new MarkerOptions().
					position(new LatLng(latitude, longitude)).
					title(couponLocation.get(0).buss_name).
					snippet(address).
					icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));
	      
					// adding marker
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
         // Changing marker icon
            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plusone_medium_off_client));
         //Moving camera with location
           *//* CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(8).build();
     
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            
     *//*       //Showing Current Location
            
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
    }*/
 
    @Override
    protected void onResume() {
        super.onResume();
        //initilizeMap();
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
		if(v == btnBack){
			finish();
			/*Intent intent = new Intent(mContext, PincodePage.class);
			intent.putExtra("mode", getIntent().getExtras().getString("mode"));
			startActivity(intent);*/
			overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
			} 
		if(v == settingBack){
			finish();
			Intent intent = new Intent(mContext, RedeemCouponDetails.class);
			intent.putExtra("buss_name", buss_name);
			intent.putExtra("banner_img", banner_img);
			intent.putExtra("all_coupon_location_same", "1");
			intent.putExtra("location_id", "");
			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
			} 
		
		
	}
	
	@Override 
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
	}
}

