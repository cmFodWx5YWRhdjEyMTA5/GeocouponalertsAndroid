package com.ild.geocouponalert.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.webmethod.RestCallManager;
import com.ild.geocouponalert.ActivationCodeScreen;
import com.ild.geocouponalert.CouponDetails;
import com.ild.geocouponalert.MerchantCouponDetails;
//import com.ild.geocouponalert.user.BusinessDetails;
import com.ild.geocouponalert.R;

public class ListviewBusinessAdapter extends BaseAdapter {
    
    private Activity activity;
    public List<BusinessMaster> data;
    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    public TextView business_count;
    public String activationCode;
    private static int checkBoxCounter = 0;
    String buss_id,activate_code,buss_name,banner_img;
    public static  HashMap<String,String> checkedmap; 
    
    public ListviewBusinessAdapter(Activity a, List<BusinessMaster> d,TextView sel_business_count,String activation_code) {
        activity = a;
        data=d;
        checkBoxCounter = 0;
        activationCode=activation_code;
        this.business_count = sel_business_count;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        if(checkedmap == null){
        	checkedmap = new HashMap<String, String>();
        }
        for (int i = 0; i < this.getCount(); i++) {
            itemChecked.add(i, false); // initializes all items value with false
        }
    }

    public int getCount() {
    	return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    } 
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
    	Toast.makeText(activity, ""+position, 1000).show();
        View vi=convertView; 
        ViewHolder holder = null;
        
        if(vi==null){
            vi = inflater.inflate(R.layout.merchant_row, null);
            holder = new ViewHolder();
            holder.chkTick = (CheckBox) vi.findViewById(R.id.checkboxBusiness);
            holder.image_view=(ImageView)vi.findViewById(R.id.businessLogo);
            holder.buss_name=(TextView)vi.findViewById(R.id.txtViewbusinessName);
            holder.view_coupon_btn=(TextView)vi.findViewById(R.id.view_coupon_btn);
            holder.no_of_coupon=(TextView)vi.findViewById(R.id.no_of_coupon);
            holder.coupon_details=(LinearLayout)vi.findViewById(R.id.linder);
            
            vi.setTag(holder);
        }
        else {

			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
        	//final int pos = position;
            final BusinessMaster business = data.get(position);
       
        holder.buss_name.setText(business.name.toString());    
        holder.business_id=business.buss_id.toString().trim();
        imageLoader.DisplayImage(business.logo_img.toString(), holder.image_view);
        if(business.no_of_coupon.toString().equals("1")){
        	holder.no_of_coupon.setText(business.no_of_coupon.toString()+" Coupon");
        } else {
        	holder.no_of_coupon.setText(business.no_of_coupon.toString()+" Coupons");
        }
        //Toast.makeText(activity, "Test", 1000).show();
       /*if(checkedmap.size() >0){
        	//Toast.makeText(activity, checkedmap.size(), 1000).show();
        	Iterator myVeryOwnIterator = checkedmap.keySet().iterator();
    		while(myVeryOwnIterator.hasNext()) {
    			String key=(String)myVeryOwnIterator.next();
    			Toast.makeText(activity, key, 1000).show();
    		} 
        }*/
         
        
               
        holder.chkTick.setOnClickListener(new View.OnClickListener() { 
 			
 			@Override 
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				if(((CompoundButton) v).isChecked()==true){
 					if(checkBoxCounter<=25){
	 					checkBoxCounter++;
	 					//itemChecked.set(position, true);
	 					//business.setSelected(true);
	 					checkedmap.put(business.buss_id.toString().trim(), "1");
	 					//Toast.makeText(activity, business.buss_id.toString().trim(), 1000).show();
 					} 
 				}
 				else{
 					checkBoxCounter--;
 					//itemChecked.set(position, false);
 					//business.setSelected(false);
 					checkedmap.remove(business.buss_id.toString().trim());
 					
 				}
 				//checkBoxCounter--;
 				//Toast.makeText(activity, ""+checkBoxCounter, Toast.LENGTH_SHORT).show();
 				if(checkBoxCounter<=25){
 				Toast.makeText(activity, "You have selected " + checkBoxCounter + " merchants", Toast.LENGTH_SHORT).show();
 				business_count.setText(checkBoxCounter + "/25 Selected");
 				}
 				else{
 					Toast.makeText(activity, "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
 					//itemChecked.set(position, false);
 					//business.setSelected(false);
 					//((CompoundButton) v).setChecked(false);
 					//checkBoxCounter--;
 				}
 			}
 		});  
        
        if (checkedmap.containsKey(business.buss_id.toString().trim())) {
        	Toast.makeText(activity, business.buss_id.toString().trim(), 1000).show();
        	holder.chkTick.setChecked(true);
        	//holder.chkTick.setChecked(itemChecked.get(position)); 
        } 
         
        //holder.chkTick.setChecked(itemChecked.get(position)); // this will Check or Uncheck the
                
        holder.coupon_details.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				
   				// TODO Auto-generated method stub
   				ViewcouponAsyncTask manageActivation = new ViewcouponAsyncTask(activity);
				manageActivation.execute();
				buss_id = business.buss_id.toString().trim();
				buss_name = business.name.toString().trim();
				banner_img = business.banner_img.toString().trim(); 
				activate_code = activationCode;
   				
   			}
   		});
        return vi;
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
			
			bSuccess=RestCallManager.getInstance().downloadAllCouponLocationByBussID(activate_code,buss_id);
			return null;
		}  
		
		@Override 
		protected void onPostExecute(Void result) { 
			
			pDialog.dismiss();
			if(bSuccess){ 
				Intent intent = new Intent(activity, CouponDetails.class);
				intent.putExtra("business_id", buss_id);
				intent.putExtra("activation_code", activate_code);
				intent.putExtra("buss_name", buss_name);
				intent.putExtra("banner_img", banner_img);
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