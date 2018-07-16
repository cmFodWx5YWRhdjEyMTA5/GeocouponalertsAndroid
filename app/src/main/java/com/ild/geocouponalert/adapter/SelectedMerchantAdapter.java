package com.ild.geocouponalert.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.adapter.ListviewBusinessAdapter.ViewcouponAsyncTask;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.datastore.DataStore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.webmethod.RestCallManager;
import com.ild.geocouponalert.ActivationCodeScreen;
import com.ild.geocouponalert.CouponDetails;
import com.ild.geocouponalert.MerchantCouponDetails;
import com.ild.geocouponalert.RedeemCouponDetails;
//import com.ild.geocouponalert.user.BusinessDetails;
import com.ild.geocouponalert.R;

public class SelectedMerchantAdapter extends BaseAdapter  implements Filterable {
    
    private Activity activity;
    public List<BusinessMaster> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    GPSTracker gps;
    String user_latitude,user_longitude,buss_id,buss_name,banner_img;
    List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
	private List<BusinessMaster> mDisplayedValues;    // Values to be displayed
    
    
    public SelectedMerchantAdapter(Activity a, List<BusinessMaster> d) {
        activity = a;
        data=d;
		mDisplayedValues = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
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
            vi = inflater.inflate(R.layout.selected_merchant_row, null);
            holder = new ViewHolder();
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
       
        holder.buss_name.setText(business.name.toString());    
        holder.business_id=business.buss_id.toString().trim();
        imageLoader.DisplayImage(business.logo_img.toString(), holder.image_view);
        if(business.no_of_coupon.toString().equals("1")){
        	holder.no_of_coupon.setText(business.no_of_coupon.toString()+" Coupon");
        } else {
        	holder.no_of_coupon.setText(business.no_of_coupon.toString()+" Coupons");
        }
        
        holder.top_info_group.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				
   			// TODO Auto-generated method stub
   				gps = new GPSTracker(activity);
   				if(gps.canGetLocation()){
   					
   					user_latitude = String.valueOf(gps.getLatitude());
   					user_longitude = String.valueOf(gps.getLongitude());
   					ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(activity);
   					manageActivation.execute();
   					buss_id = business.buss_id.toString().trim();
   					buss_name = business.name.toString().trim();
   					banner_img = business.banner_img.toString().trim(); 
   					
   				} else {
   			       	// can't get location
   			       	// GPS or Network is not enabled
   			       	// Ask user to enable GPS/network in settings
   					user_latitude = "0";
   					user_longitude = "0";
   					ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(activity);
   					manageActivation.execute();
   					buss_id = business.buss_id.toString().trim();
   					buss_name = business.name.toString().trim(); 
   					banner_img = business.banner_img.toString().trim(); 
   			    }
   				
   			}
   		});
        
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

	public class ViewcouponAsyncTask extends AsyncTask<Void, Void, Void> {
		boolean bSuccess;
		ProgressDialog pDialog;
		Activity mFriendLogin;
		
		public ViewcouponAsyncTask(Activity activity) { 
			mFriendLogin = activity;
		}
		@Override
		protected Void doInBackground(Void... params) {   
			
			bSuccess=RestCallManager.getInstance().downloadAllLocationOfBusiness(Secure.getString(activity.getContentResolver(),Secure.ANDROID_ID),buss_id,user_latitude,user_longitude,COUtils.getDefaults("emailID", activity));
			return null; 
		}  
		
		@Override  
		protected void onPostExecute(Void result) {   
			
			pDialog.dismiss();
			if(bSuccess){ 
				
				couponLocation = DataStore.getInstance().getCouponLocation();
				//Toast.makeText(activity, couponLocation.get(0).all_coupon_location_same, 3000).show();
				Intent intent = new Intent(activity, RedeemCouponDetails.class);
				intent.putExtra("buss_id", buss_id);
				intent.putExtra("buss_name", buss_name);
				intent.putExtra("banner_img", banner_img);
				intent.putExtra("all_coupon_location_same", couponLocation.get(0).all_coupon_location_same);
				intent.putExtra("location_id", "");
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			} 
			
			/*if(bSuccess){ 
				Toast.makeText(activity, "Item edited successfully.", 3000).show();
			} else {
				Toast.makeText(activity, "Edit failed..", 3000).show();
			}*/
			
 
		}  
  
		@Override
		protected void onPreExecute() { 
			super.onPreExecute();
			pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}
		
		
	}
   
}