package com.ild.geocouponalert;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.ild.geocouponalert.adapter.LocationSpinnerAdapter;
import com.ild.geocouponalert.adapter.LocationSpinnerAdapterMerchantDetails;
import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.FavoriteAlertLocationMerchant;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.gpstracker.GPSTracker;
import com.ild.geocouponalert.imagefile.ImageLoaderFull;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.webmethod.RestCallManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EditMerchantDetails extends Activity implements OnClickListener{
    RelativeLayout settingBack;
    String buss_name,banner_img,buss_id,location_id;
    TextView starBucksHeading,edit,all,custom;
    ImageView banner_image;
    public ImageLoaderFull imageLoader;
    Context mContext;
    Switch switchFavorite,switchAlert;
    List<FavoriteAlertLocationMerchant> lstFALmerchant;
    List<BusinessCouponLocation> couponLocation = new ArrayList<BusinessCouponLocation>();
    List<BusinessLocationMaster> location = new ArrayList<BusinessLocationMaster>();
    ListView listViewLocation;
    LocationSpinnerAdapterMerchantDetails locationadapter=null;
    RelativeLayout allRel,customRel;
    ArrayList<String> alert_location_inactive;
    String allCustomVal="all";

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_merchant_details);
        mContext = this;

        imageLoader=new ImageLoaderFull(this.getApplicationContext());

        buss_id =  getIntent().getExtras().getString("buss_id");
        buss_name = getIntent().getExtras().getString("buss_name");
        banner_img = getIntent().getExtras().getString("banner_img");

        lstFALmerchant = DataStore.getInstance().getFavoriteAlertMerchantEdit();
        alert_location_inactive = lstFALmerchant.get(0).getAlert_location_inactive();

        couponLocation = DataStore.getInstance().getCouponLocation();
        location = couponLocation.get(0).location_details;
        Collections.sort(location,new DistanceComp1());

        settingBack = (RelativeLayout)findViewById(R.id.settingBack);
        settingBack.setOnClickListener(this);

        all = (TextView)findViewById(R.id.all);
        custom = (TextView)findViewById(R.id.custom);

        all.setTextColor(Color.WHITE);
        custom.setTextColor(Color.BLACK);

        edit = (TextView)findViewById(R.id.levelText);
        edit.setOnClickListener(this);

        starBucksHeading = (TextView)findViewById(R.id.starBucksHeading);
        starBucksHeading.setText(buss_name);

        banner_image = (ImageView)findViewById(R.id.banner_image);
        //imageLoader.DisplayImage(banner_img.trim().toString(), banner_image);
        Picasso.with(mContext)
                .load(banner_img.trim().toString())
                .fit()
                .placeholder(R.drawable.no_image_full)
                .into(banner_image);

        allRel      = (RelativeLayout) findViewById(R.id.allRel);
        customRel   = (RelativeLayout) findViewById(R.id.customRel);

        allRel.setOnClickListener(this);
        customRel.setOnClickListener(this);

        switchAlert = (Switch)findViewById(R.id.switchAlert);
        switchFavorite = (Switch)findViewById(R.id.switchFavorite);

        listViewLocation = (ListView) findViewById(R.id.listViewLocation);
        listViewLocation.setVisibility(View.GONE);

        if(lstFALmerchant.get(0).getIs_alert().equalsIgnoreCase("1")){
            switchAlert.setChecked(true);
            if(lstFALmerchant.get(0).getAlert_location_inactive().size()>0){
                allCustomVal = "custom";
                activeCustomTab();
            }else{
                allCustomVal = "all";
                activeAllTab();
            }
        }else{
            switchAlert.setChecked(false);
        }

        if(lstFALmerchant.get(0).getIs_fav().equalsIgnoreCase("1")){
            switchFavorite.setChecked(true);
        }else{
            switchFavorite.setChecked(false);
        }

        switchAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(allCustomVal.equalsIgnoreCase("all")){
                        activeAllTab();
                    }else{
                        activeCustomTab();
                    }
                   /*all.setTextColor(Color.WHITE);
                    custom.setTextColor(Color.BLACK);
                    allRel.setBackground(getResources().getDrawable(R.drawable.segment_active_click_merchant_details));
                    customRel.setBackground(getResources().getDrawable(R.drawable.segment_inactive_click_merchant_details));*/
                }else{
                    listViewLocation.setVisibility(View.GONE);
                    all.setTextColor(Color.WHITE);
                    custom.setTextColor(Color.BLACK);
                    allRel.setBackground(getResources().getDrawable(R.drawable.segment_active_merchant_details));
                    customRel.setBackground(getResources().getDrawable(R.drawable.segment_inactive_merchant_details));
                }
            }
        });


        if(location.size() >0){
            locationadapter = new LocationSpinnerAdapterMerchantDetails(this,couponLocation,alert_location_inactive);
            listViewLocation.setAdapter(locationadapter);
            /*listViewLocation.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                }
            });*/
        }

    }

    public void activeAllTab(){
        all.setTextColor(Color.WHITE);
        custom.setTextColor(Color.BLACK);
        listViewLocation.setVisibility(View.GONE);
        allRel.setBackground(getResources().getDrawable(R.drawable.segment_active_click_merchant_details));
        customRel.setBackground(getResources().getDrawable(R.drawable.segment_inactive_click_merchant_details));
    }

    public void activeCustomTab(){
        custom.setTextColor(Color.WHITE);
        all.setTextColor(Color.BLACK);
        listViewLocation.setVisibility(View.VISIBLE);
        allRel.setBackground(getResources().getDrawable(R.drawable.segment_inactive_click_left_merchant_details));
        customRel.setBackground(getResources().getDrawable(R.drawable.segment_active_click_right_merchant_details));
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if(v==allRel){
            if(switchAlert.isChecked()) {
                allCustomVal = "all";
                activeAllTab();
            }
        }

        if(v==customRel){
            if(switchAlert.isChecked()) {
                allCustomVal = "custom";
                activeCustomTab();
            }
        }

        if(v==edit){
            //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
            String inActiveAlertLocationString = "";
            String favoriteFlag = "0";
            String alertFlag = "0";
            ArrayList<String> all_alert_location_inactive = LocationSpinnerAdapterMerchantDetails.all_alert_location_inactive;
            if(switchAlert.isChecked()) {
                alertFlag = "1";
                if(allCustomVal.equalsIgnoreCase("custom")){
                    inActiveAlertLocationString = TextUtils.join(",", all_alert_location_inactive);
                }
            }
            if(switchFavorite.isChecked()) {
                favoriteFlag = "1";
            }

            new SetMerchantAlertFavouriteParticularLocationAsyncTask(mContext,favoriteFlag,alertFlag,inActiveAlertLocationString).execute();
        }

        if(v == settingBack){
            onBackPressed();
        }

    }

    public class SetMerchantAlertFavouriteParticularLocationAsyncTask extends AsyncTask<Void, Void, Void> {
        boolean bSuccess;
        ProgressDialog pDialog;
        Context mFriendLogin;
        String favorite_flag,alert_flag,inactive_alert_loc_str;

        public SetMerchantAlertFavouriteParticularLocationAsyncTask(Context activity,String favoriteFlag,
                                                                    String alertFlag,String inActiveAlertLocationString) {
            mFriendLogin            = activity;
            favorite_flag           = favoriteFlag;
            alert_flag              = alertFlag;
            inactive_alert_loc_str  = inActiveAlertLocationString;
        }

        @Override
        protected Void doInBackground(Void... params) {

            bSuccess=RestCallManager.getInstance().setMerchantAlertFavouriteParticularLocation(COUtils.getDefaults("emailID", mFriendLogin),buss_id
                    ,alert_flag,favorite_flag,inactive_alert_loc_str);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pDialog.dismiss();
            if(bSuccess){
                LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
                View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
                alertDialogBuilder2.setView(promptView);
                final AlertDialog alertD2 = alertDialogBuilder2.create();
                TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
                TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
                topTxt.setText("Success");
                alertTxt.setText("Settings saved successfully");
                RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
                relBottominner.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertD2.dismiss();
                        onBackPressed();
                    }
                });
                alertD2.setCanceledOnTouchOutside(false);
                alertD2.show();
            }else{
                LayoutInflater layoutInflater2 = LayoutInflater.from(mContext);
                View promptView = layoutInflater2.inflate(R.layout.alert_validation, null);
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mContext);
                alertDialogBuilder2.setView(promptView);
                final AlertDialog alertD2 = alertDialogBuilder2.create();
                TextView alertTxt=(TextView)promptView.findViewById(R.id.alertTxt);
                TextView topTxt=(TextView)promptView.findViewById(R.id.topTxt);
                topTxt.setText("Warning!");
                alertTxt.setText("There is a problem with the system");
                RelativeLayout relBottominner = (RelativeLayout)promptView.findViewById(R.id.relBottominner);
                relBottominner.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertD2.dismiss();
                    }
                });
                alertD2.setCanceledOnTouchOutside(false);
                alertD2.show();
            }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}