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
import com.ild.geocouponalert.classtypes.CouponRedeemMaster;

import com.ild.geocouponalert.classtypes.CouponMaster;

public class RedeemFragmnetAdapter extends BaseAdapter {
    public List<CouponRedeemMaster> couponList;
    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    Animation animation;  
    public RedeemFragmnetAdapter(Activity a,List<CouponRedeemMaster> coupon) {
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
            vi = inflater.inflate(R.layout.redeem_fragment_row, null);
            holder = new ViewHolder();
            holder.starBucksTitle=(TextView)vi.findViewById(R.id.starBucksTitle);
            holder.starBuckstxt1=(TextView)vi.findViewById(R.id.starBuckstxt1);
            holder.expirestxt=(TextView)vi.findViewById(R.id.expirestxt);
            holder.expires=(TextView)vi.findViewById(R.id.expires);
            holder.no_of_coupons=(TextView)vi.findViewById(R.id.no_of_coupons);
            
            
            vi.setTag(holder);
        }
        else {

			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
        	//final int pos = position;
        final CouponRedeemMaster couponObj = couponList.get(position);
        holder.starBucksTitle.setText(couponObj.name.toString());    
        holder.starBuckstxt1.setText(couponObj.details.toString());    
        holder.expirestxt.setText("Redeemed on "+couponObj.redeem_date.toString());    
        holder.expires.setText(couponObj.disclaimer.toString()); 
        
        return vi;
    }
}