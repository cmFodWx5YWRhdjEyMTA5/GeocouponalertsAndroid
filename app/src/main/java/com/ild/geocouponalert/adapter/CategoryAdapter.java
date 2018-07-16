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
import com.ild.geocouponalert.classtypes.Category;;

public class CategoryAdapter extends BaseAdapter {
    public List<Category> categoryList;
    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    public CategoryAdapter(Activity a,List<Category> cat_details) {
        activity = a;
        categoryList = cat_details;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
    	return categoryList.size();
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
            vi = inflater.inflate(R.layout.category_row, null);
            holder = new ViewHolder();
            holder.category_img=(ImageView)vi.findViewById(R.id.cat_img);
            holder.category_txt=(TextView)vi.findViewById(R.id.cat_name);
            vi.setTag(holder);
        }
        else {

			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();
		}
        	//final int pos = position;
        final Category catObj = categoryList.get(position);
        //Toast.makeText(activity, ""+position, 1000).show();
        if(position == 0){
	        holder.category_txt.setText(catObj.cat_name.toString());   
	        holder.category_name = catObj.cat_name.toString();
	        holder.category_id = catObj.cat_id.toString();
	        holder.category_img.setVisibility(View.GONE);
	        
        } else {
        	holder.category_txt.setText(catObj.cat_name.toString()); 
        	holder.category_name = catObj.cat_name.toString();
        	holder.category_id = catObj.cat_id.toString();
        	holder.category_img.setVisibility(View.VISIBLE);
        	imageLoader.DisplayImage(catObj.cat_img.toString(), holder.category_img);
        }
        return vi;
    }
   /* public View getDropDownView(final int position, View convertView, ViewGroup parent) {
    	View vi=convertView;
        ViewHolder holder = null;
       
        if(vi==null){
            vi = inflater.inflate(R.layout.category_row, null);
            holder = new ViewHolder();
            holder.category_img=(ImageView)vi.findViewById(R.id.cat_img);
            holder.category_txt=(TextView)vi.findViewById(R.id.cat_name);
            vi.setTag(holder);
        }
        else {
 
			holder = new ViewHolder();
			holder = (ViewHolder) vi.getTag();   
		}
        	//final int pos = position;
        final Category catObj = categoryList.get(position);
        if(position == 0){
	        holder.category_txt.setText(catObj.cat_name.toString());    
	        holder.category_img.setVisibility(View.GONE);
        } else {
	        holder.category_txt.setText(catObj.cat_name.toString());    
	        imageLoader.DisplayImage(catObj.cat_img.toString(), holder.category_img);
        }
      
        return vi;
    }*/
}