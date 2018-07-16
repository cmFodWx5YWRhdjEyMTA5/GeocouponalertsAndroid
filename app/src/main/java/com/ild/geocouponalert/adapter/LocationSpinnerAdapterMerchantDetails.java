package com.ild.geocouponalert.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ild.geocouponalert.R;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;

import java.util.ArrayList;
import java.util.List;

//import com.ild.geocouponalert.MapBusinessLocation;
//import com.ild.geocouponalert.user.BusinessDetails;

public class LocationSpinnerAdapterMerchantDetails extends BaseAdapter {
	public List<BusinessCouponLocation> couponlocationList;
    public List<BusinessLocationMaster> locationList;
    public Activity activity;
    public static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;
    public static ArrayList<String> all_alert_location_inactive;

    public LocationSpinnerAdapterMerchantDetails(Activity a, List<BusinessCouponLocation> couponLocation, ArrayList<String> alert_location_inactive) {
        activity = a;
        couponlocationList = couponLocation;
        locationList = couponLocation.get(0).location_details;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
        all_alert_location_inactive = alert_location_inactive;
    }

    public int getCount() {
    	/*final int count = locationList.size();
        return count+1;*/
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
            vi = inflater.inflate(R.layout.location_fragment_row_merchant_details, null);
            holder = new ViewHolder();
            holder.chckboxLin=(LinearLayout)vi.findViewById(R.id.chckboxLin);
            holder.checkUncheck=(ImageView)vi.findViewById(R.id.checkUncheck);
            holder.address1=(TextView)vi.findViewById(R.id.address1);
            holder.address2=(TextView)vi.findViewById(R.id.address2);
            holder.phone=(TextView)vi.findViewById(R.id.phone);
            holder.distance=(TextView)vi.findViewById(R.id.distance);
            holder.parentRelativeLayout = (RelativeLayout)vi.findViewById(R.id.parentRelativeLayout);
            vi.setTag(holder);
        }else {
			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
        	//final int pos = position;
        	holder.address2.setVisibility(View.VISIBLE);
        	holder.phone.setVisibility(View.VISIBLE); 
        	holder.distance.setVisibility(View.VISIBLE);

        	final BusinessLocationMaster locationObj = locationList.get(position);

            if (all_alert_location_inactive.contains(locationObj.id.toString())) {
                holder.checkUncheck.setImageResource(R.drawable.unselected_tick);
            }else{
                holder.checkUncheck.setImageResource(R.drawable.selected_tick);
            }

	        holder.loc_name = locationObj.address1.toString();
	        holder.loc_id = locationObj.id.toString();
	        holder.buss_id = couponlocationList.get(0).buss_id.toString();
	        holder.bus_name = couponlocationList.get(0).buss_name.toString();
	        holder.bus_banner = couponlocationList.get(0).banner_img.toString();
	        
	        holder.address1.setText(locationObj.address1.toString()+" "+locationObj.address2.toString());
	        holder.address2.setText(locationObj.city.toString()+", "+locationObj.state_initial.toString().toUpperCase()+" "+locationObj.zip.toString());
	        if(holder.phone.length()<7){
	        	holder.phone.setText(locationObj.phone.toString());
			}else{
				String first_three=locationObj.phone.toString().substring(0,3);
				String second_three=locationObj.phone.toString().substring(3,6);
				String last=locationObj.phone.toString().substring(6);
				holder.phone.setText(first_three + Html.fromHtml("<font color='#ff0000'>-</font>")+second_three+Html.fromHtml("<font color='#ff0000'>-</font>")+last);
			}
	        holder.distance.setText(locationObj.distance.toString() + " Miles");
	        final Float dist=Float.parseFloat(locationObj.distance);

            final String merchantid = locationObj.id.toString();
            final ImageView checkbox_holder = holder.checkUncheck;

            holder.chckboxLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (all_alert_location_inactive.contains(merchantid)) {
                        checkbox_holder.setImageResource(R.drawable.selected_tick);
                        all_alert_location_inactive.remove(merchantid);
                    }else{
                        checkbox_holder.setImageResource(R.drawable.unselected_tick);
                        all_alert_location_inactive.add(merchantid);
                    }
                }
            });

        return vi;
    }
}