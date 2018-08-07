package com.ild.geocouponalert.adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.ild.geocouponalert.R;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;

public class LocationSpinnerAdapter extends BaseAdapter {
    public List<BusinessLocationMaster> locationList;
    public Activity activity;
    public static LayoutInflater inflater=null;
    public LocationSpinnerAdapter(Activity a,List<BusinessLocationMaster> couponLocation) {
        activity = a;
        locationList = couponLocation;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder holder;
       
        if(vi==null){
            vi = inflater.inflate(R.layout.location_fragment_row, null);
            holder = new ViewHolder();
            holder.address1=vi.findViewById(R.id.address1);
            holder.address2=vi.findViewById(R.id.address2);
            holder.phone=vi.findViewById(R.id.phone);
            holder.distance=vi.findViewById(R.id.distance);
            holder.parentRelativeLayout = vi.findViewById(R.id.parentRelativeLayout);
            vi.setTag(holder); 
        }
        else {
			holder = (ViewHolder) vi.getTag();
		}
        BusinessLocationMaster locationObj = locationList.get(position);
        if(locationObj.id =="" || locationObj.id =="O"){
        	holder.address2.setVisibility(View.GONE);
        	holder.phone.setVisibility(View.GONE);
        	holder.distance.setVisibility(View.GONE);

            holder.address1.setText(locationObj.address1.toString());
            holder.loc_name = locationObj.address1.toString();
            holder.loc_id = locationObj.id.toString();
        }
        else {
            holder.address2.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);
            holder.distance.setVisibility(View.VISIBLE);
            holder.loc_name = locationObj.address1.toString();
            holder.loc_id = locationObj.id.toString();

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
        }
        return vi;
    }
}