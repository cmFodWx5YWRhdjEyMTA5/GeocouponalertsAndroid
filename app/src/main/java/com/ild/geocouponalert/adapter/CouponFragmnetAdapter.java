package com.ild.geocouponalert.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ild.geocouponalert.adapter.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.ActivationCodeScreen;
import com.ild.geocouponalert.MerchantCouponDetails;
//import com.ild.geocouponalert.user.BusinessDetails;
import com.ild.geocouponalert.R;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;

import com.ild.geocouponalert.classtypes.CouponMaster;

public class CouponFragmnetAdapter extends BaseAdapter {
    public List<CouponMaster> couponList;
    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    Animation animation;  
    public CouponFragmnetAdapter(Activity a,List<CouponMaster> coupon) {
        activity = a;
        couponList = coupon;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
    	return couponList.size();
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
            vi = inflater.inflate(R.layout.coupon_fragment_row, null);
            holder = new ViewHolder();
            holder.starBucksTitle=(TextView)vi.findViewById(R.id.starBucksTitle);
            holder.starBuckstxt1=(TextView)vi.findViewById(R.id.starBuckstxt1);
            holder.expirestxt=(TextView)vi.findViewById(R.id.expirestxt);
            holder.expires=(TextView)vi.findViewById(R.id.expires);
            holder.no_of_coupons=(TextView)vi.findViewById(R.id.no_of_coupons);
            holder.settingBacktag = (ImageView)vi.findViewById(R.id.settingBacktag);
            //holder.universal_logo = (ImageView)vi.findViewById(R.id.universal_logo);
            
            
            
            vi.setTag(holder);
        }
        else {

			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
        	//final int pos = position;
        final CouponMaster couponObj = couponList.get(position);
        holder.starBucksTitle.setText(couponObj.name.toString());
        if(couponObj.details != null){
        	holder.starBuckstxt1.setText(couponObj.details.toString());  
        } else {
        	holder.starBuckstxt1.setText("");  
        }
        //holder.starBuckstxt1.setText(couponObj.details.toString());    
        holder.expirestxt.setText(couponObj.expiry_date.toString());    
        holder.expires.setText(couponObj.disclaimer.toString()); 
        if(couponObj.quantity.equalsIgnoreCase("0")){
        	holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_unlimited);
        } else if(couponObj.quantity.equalsIgnoreCase("e")){
        	holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_expired);
        } else if(couponObj.quantity.equalsIgnoreCase("1")){
        	holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_oneleft); 
        } else if(couponObj.quantity.equalsIgnoreCase("2")){
		    holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_twoleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("3")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_threeleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("4")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_fourleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("5")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_fiveleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("6")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_sixleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("7")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_sevenleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("8")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_eightleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("9")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_nineleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("10")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_tenleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("11")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_elevenleft); 
		} else if(couponObj.quantity.equalsIgnoreCase("12")){
			holder.settingBacktag.setBackgroundResource(R.drawable.ribbon_twelveleft); 
		}
        
        /*if(couponObj.universal.equalsIgnoreCase("1")){
        	holder.universal_logo.setBackgroundResource(R.drawable.universal); 
        } else {
        	holder.universal_logo.setBackgroundResource(R.drawable.nonuniversal); 
        }*/
        
      
        return vi;
    }
}