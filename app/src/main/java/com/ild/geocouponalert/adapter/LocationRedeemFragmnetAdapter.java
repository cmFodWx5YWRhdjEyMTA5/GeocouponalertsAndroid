package com.ild.geocouponalert.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.adapter.ViewHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.ActivationCodeScreen;
import com.ild.geocouponalert.MapBusinessLocation;
import com.ild.geocouponalert.MapRedeemBusinessLocation;
//import com.ild.geocouponalert.MapBusinessLocation;
import com.ild.geocouponalert.MerchantCouponDetails;
import com.ild.geocouponalert.PinCodeScreen;
//import com.ild.geocouponalert.user.BusinessDetails;
import com.ild.geocouponalert.R;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;

import com.ild.geocouponalert.classtypes.CouponMaster;

public class LocationRedeemFragmnetAdapter extends BaseAdapter {
    public List<BusinessLocationMaster> locationList;
    public Activity activity;
    public static LayoutInflater inflater=null;
    //public ImageLoader imageLoader; 
    public LocationRedeemFragmnetAdapter(Activity a,List<BusinessLocationMaster> location) {
        activity = a;
        locationList = location;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
    	return locationList.size();
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
            vi = inflater.inflate(R.layout.location_fragment_row, null);
            holder = new ViewHolder();
            holder.address1=(TextView)vi.findViewById(R.id.address1);
            holder.address2=(TextView)vi.findViewById(R.id.address2);
            holder.phone=(TextView)vi.findViewById(R.id.phone);
            holder.distance=(TextView)vi.findViewById(R.id.distance);
            holder.distanceIcon = (ImageView)vi.findViewById(R.id.distanceIcon);
            holder.parentRelativeLayout = (RelativeLayout)vi.findViewById(R.id.parentRelativeLayout);
            
            
            vi.setTag(holder); 
        }
        else {

			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
        	//final int pos = position;
        holder.distanceIcon.setVisibility(View.VISIBLE);
        final BusinessLocationMaster locationObj = locationList.get(position);
        holder.address1.setText(locationObj.address1.toString()+" "+locationObj.address2.toString());
        holder.address2.setText(locationObj.city.toString()+", "+locationObj.state_initial.toString().toUpperCase()+" "+locationObj.zip.toString());
        if(holder.phone.length()<7){
        	holder.phone.setText(locationObj.phone.toString());
		}
		else{
		
			String first_three=locationObj.phone.toString().substring(0,3);
			String second_three=locationObj.phone.toString().substring(3,6);
			String last=locationObj.phone.toString().substring(6);
			holder.phone.setText(first_three + Html.fromHtml("<font color='#ff0000'>-</font>")+second_three+Html.fromHtml("<font color='#ff0000'>-</font>")+last);
		}
        holder.distance.setText(locationObj.distance.toString() + " Miles");
        final Float dist=Float.parseFloat(locationObj.distance);
        holder.parentRelativeLayout.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   			    //Toast.makeText(activity, "Test", 2000).show();
   				Intent intent = new Intent(activity, MapRedeemBusinessLocation.class);
				intent.putExtra("buss_loc_lat", locationObj.buss_loc_lat);
				intent.putExtra("buss_loc_long", locationObj.buss_loc_long);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);	
   			}
   		});
        return vi;
    }
}