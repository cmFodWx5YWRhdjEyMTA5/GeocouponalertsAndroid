package com.ild.geocouponalert.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ild.geocouponalert.R;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.imagefile.ImageLoader;

import java.util.ArrayList;
import java.util.List;

//import com.ild.geocouponalert.user.BusinessDetails;

public class AlertMerchantAdapter extends BaseAdapter  implements Filterable {

    private Activity activity;
    public List<BusinessMaster> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    GPSTracker gps;
    String user_latitude,user_longitude,buss_id,buss_name,banner_img;
    List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	private List<BusinessMaster> mDisplayedValues;    // Values to be displayed
	String isCheckAllMerchant;
	public static ArrayList<String> allInAvtiveAlertMerchantId;

    public AlertMerchantAdapter(Activity a, List<BusinessMaster> d, String isCheckAllMerchantAlert, ArrayList<String> alertMerchantIdInActive) {
        activity = a;
        data=d;
		mDisplayedValues = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
		isCheckAllMerchant=isCheckAllMerchantAlert;
		allInAvtiveAlertMerchantId = alertMerchantIdInActive;
    }

    public int getCount() {
    	return mDisplayedValues.size();
    }

    public Object getItem(int position) {
		return mDisplayedValues.get(position);
    }

    public long getItemId(int position) {
        return position;
    } 
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        View vi=convertView; 
        ViewHolder holder = null;
        
        if(vi==null){
            vi = inflater.inflate(R.layout.alert_merchant_row, null);
            holder = new ViewHolder();
			holder.chckboxLin=(LinearLayout)vi.findViewById(R.id.chckboxLin);
			holder.checkUncheck=(ImageView)vi.findViewById(R.id.checkUncheck);
            holder.image_view=(ImageView)vi.findViewById(R.id.businessLogo);
            holder.buss_name=(TextView)vi.findViewById(R.id.txtViewbusinessName);
            holder.view_coupon_btn=(TextView)vi.findViewById(R.id.view_coupon_btn);
            holder.no_of_coupon=(TextView)vi.findViewById(R.id.no_of_coupon);
            holder.view_coupon_btn=(TextView)vi.findViewById(R.id.view_coupon_btn);
            holder.top_info_group = (LinearLayout)vi.findViewById(R.id.top_info_group);
            
            vi.setTag(holder);
        }
        else {
			holder = new ViewHolder(); 
			holder = (ViewHolder) vi.getTag();
		}

        //final int pos = position;
        final BusinessMaster business = mDisplayedValues.get(position);

		if(isCheckAllMerchant.equalsIgnoreCase("Y")){
			holder.checkUncheck.setImageResource(R.drawable.selected_tick);
		}else{
				if (allInAvtiveAlertMerchantId.contains(business.buss_id)) {
					holder.checkUncheck.setImageResource(R.drawable.unselected_tick);
				}else{
					holder.checkUncheck.setImageResource(R.drawable.selected_tick);
				}
		}

		final String merchantid = business.buss_id;
		final ImageView checkbox_holder = holder.checkUncheck;

		holder.chckboxLin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (allInAvtiveAlertMerchantId.contains(merchantid)) {
					checkbox_holder.setImageResource(R.drawable.selected_tick);
					allInAvtiveAlertMerchantId.remove(merchantid);
				}else{
					checkbox_holder.setImageResource(R.drawable.unselected_tick);
					allInAvtiveAlertMerchantId.add(merchantid);
				}
			}
		});
       
        holder.buss_name.setText(business.name.toString());
        holder.business_id=business.buss_id.toString().trim();
        imageLoader.DisplayImage(business.logo_img.toString(), holder.image_view);
        if(business.no_of_coupon.toString().equals("1")){
        	holder.no_of_coupon.setText(business.no_of_coupon.toString()+" Coupon");
        } else {
        	holder.no_of_coupon.setText(business.no_of_coupon.toString()+" Coupons");
        }
        
        return vi;  
    }


	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {

				mDisplayedValues = (List<BusinessMaster>) results.values; // has the filtered values
				notifyDataSetChanged();  // notifies the data with new filtered values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
				List<BusinessMaster> FilteredArrList = new ArrayList<BusinessMaster>();

				if (data == null) {
					data = new ArrayList<BusinessMaster>(mDisplayedValues); // saves the original data in mOriginalValues
				}

				final List<BusinessMaster> list = data;


				if (constraint == null || constraint.length() == 0) {
					// set the Original result to return
					results.count = list.size();
					results.values = list;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < list.size(); i++) {
						BusinessMaster newBussObj = list.get(i);
						if (newBussObj.name.toLowerCase().contains(constraint.toString())) {
							FilteredArrList.add(newBussObj);
						}
					}
					// set the Filtered result to return
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}
		};
		return filter;

	}

   
}