package com.ild.geocouponalert;

import java.util.ArrayList;  
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.android.gms.plus.model.people.Person.Cover.Layout;
import com.ild.geocouponalert.classtypes.Category;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.imagefile.ImageLoader;
import com.ild.geocouponalert.utils.COUtils;
import com.ild.geocouponalert.webmethod.RestCallManager;
import com.ild.geocouponalert.adapter.CategoryAdapter;
import com.ild.geocouponalert.adapter.ListviewBusinessAdapter;
import com.ild.geocouponalert.adapter.SelectedMerchantAdapter;
import com.ild.geocouponalert.adapter.ViewHolder;
import com.ild.geocouponalert.adapter.ListviewBusinessAdapter.ViewcouponAsyncTask;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MerchantSelectionScreen extends Activity implements OnClickListener {
	
	static Context mContext;
	TextView btnSubmit,btnBack,btnPreview;
	EditText pinNumber;
	TextView count;
	ImageView mapImg,fundraiser_logo,bgtransparent,category_arrow,category_down_arrow;
	ListView listview_available;
	ListviewBusinessAdapter adapter=null;
	private Handler mHandler = new Handler();
    ProgressDialog progressDialog;
    List<PostcardMaster> lst_fund = new ArrayList<PostcardMaster>();
    List<BusinessMaster> lst_business = new ArrayList<BusinessMaster>();
    List<BusinessMaster> lst_filtered_buss = new ArrayList<BusinessMaster>();
    List<String> sel_buss = new ArrayList<String>();
    SelectionAsyncTask manageActivation;
    CategoryAdapter catadapter=null; 
    ListView listViewCategory;
    TextView category_textview;
    LinearLayout listViewMerchant;
    ScrollView scrolllistViewMerchant;
    RelativeLayout relSpinner;
    int count1 = 0;
    public static  HashMap<String,String> checkedmap; 
    LinearLayout lm;
    ViewcouponAsyncTask manageActivation1;
    String buss_id,buss_name,banner_img,activate_code; 
    //List<BusinessSelectedMaster>` lst_sel_business = new ArrayList<BusinessSelectedMaster>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_selection_screen);
		mContext=this;
		initView();    
	}
	
	private void initView(){ 
		
		category_down_arrow = (ImageView)findViewById(R.id.category_down_arrow);
		category_down_arrow.setOnClickListener(this);
		
		relSpinner = (RelativeLayout)findViewById(R.id.relSpinner);
		
		scrolllistViewMerchant = (ScrollView)findViewById(R.id.scrolllistViewMerchant);
		
		if(checkedmap == null){
        	checkedmap = new HashMap<String, String>();
        }
		lst_business = DataStore.getInstance().getBusiness();
		
		btnPreview = (TextView)findViewById(R.id.btnPreview);
		btnPreview.setOnClickListener(this);
		
		count=(TextView)findViewById(R.id.countBusiness);
		//listViewMerchant=(LinearLayout)findViewById(R.id.listViewMerchant);
		lst_fund = DataStore.getInstance().getPostcard();
		lm = (LinearLayout) findViewById(R.id.listViewMerchant);
		//Toast.makeText(getApplicationContext(), ""+lst_business.size(), 1000).show();
		if(lst_business.size()>0){
			lm.removeAllViews();
			for(int i=0;i<lst_business.size();i++){
				
				final int k = i;
				
				final LinearLayout ll = new LinearLayout(this);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
			            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params3.setMargins(0, 8, 0, 8);
				ll.setLayoutParams(params3);
				 
				ll.setOnClickListener(new OnClickListener() {
			        public void onClick(View v) {
			        	//Toast.makeText(getApplicationContext(), "Test", 3000).show();
			        	manageActivation1 = new ViewcouponAsyncTask(mContext);
			        	manageActivation1.execute();
						buss_id = lst_business.get(k).buss_id.toString().trim();
						buss_name = lst_business.get(k).name.toString().trim();
						banner_img = lst_business.get(k).banner_img.toString().trim();
						activate_code = lst_fund.get(0).activation_code.toString();
			        }
				});
				
				final CheckBox checkboxBusiness = new CheckBox(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(10, 0, 10, 0);
				layoutParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
				checkboxBusiness.setLayoutParams(layoutParams);
				/*checkboxBusiness.setScaleX((float) 1.20);
				checkboxBusiness.setScaleY((float) 1.20);*/
				checkboxBusiness.setButtonDrawable(R.drawable.checkbox_selector);
				ll.addView(checkboxBusiness);
				/*if (checkedmap.containsKey(lst_business.get(k).buss_id.toString().trim())) {
					//checkboxBusiness.setChecked(true);
			        	//holder.chkTick.setChecked(itemChecked.get(position)); 
			    }*/
				//Toast.makeText(getApplicationContext(), "" + count1, Toast.LENGTH_SHORT).show();
				/*checkboxBusiness.setOnCheckedChangeListener(
                    new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                            	if(count1<=25){
    			        			count1++;
    				        		checkedmap.put(lst_business.get(k).buss_id.toString().trim(), "1");
    			        		} 
                              

                            } else {

                            	count1--;
    			        		checkedmap.remove(lst_business.get(k).buss_id.toString().trim());
                            }
                            
                            if(count1<=25){
    			        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
    			        		count.setText(count1+"/25 Selected");
    				        } else {  
    		        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
    		        			count1--;
    				        } 

                        }
                    });*/
				
				
				
				checkboxBusiness.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if(((CompoundButton) v).isChecked()==true){
			        		if(count1<25){
			        			count1++;
			        			//Toast.makeText(getApplicationContext(), ""+count1, 1000).show();
				        		checkedmap.put(lst_business.get(k).buss_id.toString().trim(), lst_business.get(k).name.toString().trim());
				        		count.setText(count1+"/25 Selected");
				        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
			        		} else {
			        			checkboxBusiness.setChecked(false);
			        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
			        		}
			        		
			        	} else {
			        		count1--;
			        		count.setText(count1+"/25 Selected");
			        		checkedmap.remove(lst_business.get(k).buss_id.toString().trim());
			        	}
				        /*if(count1<=25){
				        	count.setText(count1+"/25 Selected");
			        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
				        } else {  
		        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
		        			//count1--;
				        }*/
					 } 
			      });
				
				
				final LinearLayout linder = new LinearLayout(this);
				LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				linder.setLayoutParams(layoutParams2);
				linder.setPadding(3, 3, 3, 3);
				linder.setWeightSum(6);
				linder.setOrientation(LinearLayout.HORIZONTAL);
				linder.setBackgroundResource(R.color.white);
				
				ImageView businessLogo = new ImageView(this);
				LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,(float)1.5);
				layoutParams1.setMargins(0, 0, 5, 0);
				layoutParams1.weight = (float)1.5;
				businessLogo.setLayoutParams(layoutParams1);
				
				ImageLoader imageLoader=new ImageLoader(this);  
		        imageLoader.DisplayImage(lst_business.get(i).logo_img, businessLogo);
		        linder.addView(businessLogo);
		        
		        final LinearLayout app_company_info = new LinearLayout(this);
		        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		        layoutParams3.setMargins(5, 0, 5, 0);
		        layoutParams3.weight=4;
		        layoutParams3.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
		        app_company_info.setLayoutParams(layoutParams3);
		        
		        app_company_info.setOrientation(LinearLayout.VERTICAL);
		        
		        TextView txtViewbusinessName = new TextView(this);
		        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		        txtViewbusinessName.setLayoutParams(layoutParams4);
		        txtViewbusinessName.setSingleLine(false);
		        txtViewbusinessName.setTextColor(getResources().getColor(R.color.black));
		        txtViewbusinessName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		        txtViewbusinessName.setText(lst_business.get(i).name);
		        app_company_info.addView(txtViewbusinessName);
		        
		        TextView no_of_coupon = new TextView(this);
		        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		        no_of_coupon.setLayoutParams(layoutParams5);
		        no_of_coupon.setSingleLine(false);
		        no_of_coupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		        if(lst_business.get(i).no_of_coupon.toString().equals("1")){
		        	no_of_coupon.setText(lst_business.get(i).no_of_coupon.toString()+" Coupon");
		        } else {
		        	no_of_coupon.setText(lst_business.get(i).no_of_coupon.toString()+" Coupons");
		        }
		        app_company_info.addView(no_of_coupon);
		        
		        TextView view_coupon_btn = new TextView(this);
		        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		        layoutParams6.setMargins(0, 5, 0, 0);
		        view_coupon_btn.setLayoutParams(layoutParams6);
		        view_coupon_btn.setText("View Coupons & Locations");
		        view_coupon_btn.setTextColor(Color.parseColor("#ff6700"));
		        view_coupon_btn.setTypeface(null, Typeface.BOLD);
		        app_company_info.addView(view_coupon_btn);
		        
		        linder.addView(app_company_info);
		
		        ImageView row = new ImageView(this);
				LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
				layoutParams7.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
				layoutParams7.weight=(float) 0.5;
				row.setLayoutParams(layoutParams7);
				String uri = "@drawable/next";  // where myresource.png is the file
                // extension removed from the String
				int imageResource = getResources().getIdentifier(uri, null, getPackageName());
				Drawable res = getResources().getDrawable(imageResource);
				row.setImageDrawable(res);
		        
		        linder.addView(row);
		        ll.addView(linder);
		        //ll.addView(row);
				lm.addView(ll);  
				
			}  
		} 
		
		//Toast.makeText(getApplicationContext(), ""+checkedmap.size(), 3000).show();
		/*listview_available=(ListView)findViewById(R.id.listViewMerchant);
		
	    adapter=new ListviewBusinessAdapter(this, lst_business,(TextView)findViewById(R.id.countBusiness),lst_fund.get(0).activation_code.toString());
		listview_available.setAdapter(adapter);
		count.setText("0/25 Selected");*/
		
			   
		btnSubmit=(TextView)findViewById(R.id.btnSubmit);  
		btnSubmit.setOnClickListener(this);
		
		listViewCategory = (ListView)findViewById(R.id.listViewCategory);
		bgtransparent = (ImageView)findViewById(R.id.bgtransparent);
		
		relSpinner.setOnClickListener(this);
		
		category_textview = (TextView)findViewById(R.id.category_textview);
		/*category_textview.setVisibility(View.GONE);
		category_textview.setOnClickListener(this);
		
		category_arrow = (ImageView)findViewById(R.id.category_arrow);
		//category_arrow.setVisibility(View.GONE);
		category_arrow.setOnClickListener(this);*/
		
	}
	
	
	public class SelectionAsyncTask extends AsyncTask<Void, Void, Void> { 

		ProgressDialog pDialog;
		Activity mFriendLogin;
		boolean bSuccess;
		
		public SelectionAsyncTask(Activity activity) {
			mFriendLogin = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {   
			
			
			List<String> split = sel_buss;
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < split.size(); j++) {
			    sb.append(split.get(j));
			    if (j != split.size() - 1) {
			        sb.append(",");
			    }
			}
			String buss_join = sb.toString();
			//String imploded=StringUtils.join(new String[] {"Hello", "World"}, " ");
			bSuccess=RestCallManager.getInstance().insertSelectedBusiness(lst_fund.get(0).activation_code,Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID),buss_join,COUtils.getDefaults("emailID", mContext));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(bSuccess){
			pDialog.dismiss();
			pDialog = null;
			if(lst_fund.get(0).isAlreadyActiveCard.equalsIgnoreCase("1")){
				Intent intent = new Intent(mContext, MerchantListHomePage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}
			else{
			Intent intent = new Intent(mContext, MerchantListHomePage.class);
			intent.putExtra("business_id", "");
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}
			}
			else{
				Toast.makeText(mContext, "updation failed", Toast.LENGTH_SHORT).show();
				pDialog.dismiss();
				pDialog = null; 
			}
		} 

		@Override  
		protected void onPreExecute() {
			super.onPreExecute(); 
			
			if (manageActivation.getStatus() == AsyncTask.Status.RUNNING)
			{
			    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			    {
			        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			    }
			    else
			    {
			        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			    }
			}
			
			pDialog = new ProgressDialog(mFriendLogin);
			pDialog.setMessage("Loading Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show(); 
		}
	}
	
	@Override
    public void onDestroy()
    {
		//listview_available.setAdapter(null);
		super.onDestroy();
    }
    
   
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == btnSubmit){ 
			
			if(checkedmap.size() == 0){
				Toast.makeText(mContext, "Please select Merchants", Toast.LENGTH_SHORT).show();
				return;
			} else {
			
				if(checkedmap.size() <=25){
					Iterator myVeryOwnIterator = checkedmap.keySet().iterator();
					while(myVeryOwnIterator.hasNext()) {
						String c = myVeryOwnIterator.next().toString();
						BusinessMaster business=DataStore.getInstance().GetBusinessByBusinessId(c);
						sel_buss.add(c);
					}

					LayoutInflater layoutInflater = LayoutInflater.from(mContext);
					View promptView = layoutInflater.inflate(R.layout.alert_activation_code, null);
					//promptView.setBackgroundColor(Color.TRANSPARENT);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
					alertDialogBuilder.setView(promptView);
					final AlertDialog alertD = alertDialogBuilder.create();
					TextView warning_text=(TextView)promptView.findViewById(R.id.coupon_alert);
					if(checkedmap.size() == 25){
						warning_text.setText("You have reached the maximum number of Merchants. Select \"Add Merchant\" to make changes or select \"Continue\" to lock in your selections");
					} else {
						warning_text.setText("You can still select up to "+ String.valueOf(25-checkedmap.size()) +" additional Merchants. You will not able to add Merchants after this step.");
					}
					
					final TextView dialogNo = (TextView) promptView.findViewById(R.id.btnCancel);
					final TextView dialogYes = (TextView) promptView.findViewById(R.id.btnContinue);
					 
					
					// if button is clicked, close the custom dialog
					dialogNo.setOnClickListener(new OnClickListener() { 
						@Override
						public void onClick(View v) {
							alertD.dismiss();
						}
					});
					
					
					// if button is clicked, close the custom dialog
					dialogYes.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							manageActivation = new SelectionAsyncTask(MerchantSelectionScreen.this);
							manageActivation.execute();
							alertD.dismiss();
						}
					});
					 
					alertD.show();  
				
				} else{
					manageActivation = new SelectionAsyncTask(this);
					manageActivation.execute();
				}
			}
		}
		if(view == relSpinner){  
			
			bgtransparent.setVisibility(View.VISIBLE);
			listViewCategory.setVisibility(View.VISIBLE);
			category_down_arrow.setVisibility(View.VISIBLE);
			scrolllistViewMerchant.setVisibility(View.GONE);
			catadapter = new CategoryAdapter(MerchantSelectionScreen.this,lst_business.get(0).cat_details);
			listViewCategory.setAdapter(catadapter);
			listViewCategory.setOnItemClickListener(new OnItemClickListener() {
			      public void onItemClick(AdapterView<?> parent, View v,
			        int position, long id) {
			       
			       bgtransparent.setVisibility(View.GONE); 
			       category_down_arrow.setVisibility(View.GONE);
			       ViewHolder vholder = (ViewHolder) v.getTag();
			       String category_name = vholder.category_name;
			       String category_id = vholder.category_id;
			       listViewCategory.setVisibility(View.GONE);
			       scrolllistViewMerchant.setVisibility(View.VISIBLE);
			       if(category_id.equalsIgnoreCase("1")){
			    	   category_textview.setText("All Merchants");
			       } else {
			    	   category_textview.setText(category_name);
			       }
			       lst_filtered_buss.clear();
			       for(int i=0;i<lst_business.size();i++){
			    	   if(category_id.equalsIgnoreCase(lst_business.get(i).cat_id)){
			    		   lst_filtered_buss.add(lst_business.get(i));
			    	   }
			       }
			       
			       if(category_id.equalsIgnoreCase("1")){
			    	   if(lst_business.size()>0){
			    		lm.removeAllViews();
			   			for(int i=0;i<lst_business.size();i++){
							
							final int k = i;
							
							final LinearLayout ll = new LinearLayout(mContext);
							ll.setOrientation(LinearLayout.HORIZONTAL);
							LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
						            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							params3.setMargins(0, 8, 0, 8);
							ll.setLayoutParams(params3);
							
							ll.setOnClickListener(new OnClickListener() {
						        public void onClick(View v) {
						        	//Toast.makeText(getApplicationContext(), "Test", 3000).show();
						        	manageActivation1 = new ViewcouponAsyncTask(mContext);
						        	manageActivation1.execute();
									buss_id = lst_business.get(k).buss_id.toString().trim();
									buss_name = lst_business.get(k).name.toString().trim();
									banner_img = lst_business.get(k).banner_img.toString().trim();
									activate_code = lst_fund.get(0).activation_code.toString();
						        }
							});
							
							final CheckBox checkboxBusiness = new CheckBox(mContext);
							LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							layoutParams.setMargins(10, 0, 10, 0);
							layoutParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
							checkboxBusiness.setLayoutParams(layoutParams);
							/*checkboxBusiness.setScaleX((float) 1.20);
							checkboxBusiness.setScaleY((float) 1.20);*/
							checkboxBusiness.setButtonDrawable(R.drawable.checkbox_selector);
							ll.addView(checkboxBusiness);
							if (checkedmap.containsKey(lst_business.get(k).buss_id.toString().trim())) {
								checkboxBusiness.setChecked(true);
						    } 
							checkboxBusiness.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									if(((CompoundButton) v).isChecked()==true){
						        		if(count1<25){
						        			count1++;
						        			//Toast.makeText(getApplicationContext(), ""+count1, 1000).show();
							        		checkedmap.put(lst_business.get(k).buss_id.toString().trim(), lst_business.get(k).name.toString().trim());
							        		count.setText(count1+"/25 Selected");
							        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
						        		} else {
						        			checkboxBusiness.setChecked(false);
						        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
						        		}
						        		
						        	} else {
						        		count1--;
						        		count.setText(count1+"/25 Selected");
						        		checkedmap.remove(lst_business.get(k).buss_id.toString().trim());
						        	}
							        /*if(count1<=25){
							        	count.setText(count1+"/25 Selected");
						        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
							        } else {  
					        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
					        			//count1--;
							        }*/
								 } 
						      });
							
							final LinearLayout linder = new LinearLayout(mContext);
							LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							linder.setLayoutParams(layoutParams2);
							linder.setPadding(3, 3, 3, 3);
							linder.setWeightSum(6);
							linder.setOrientation(LinearLayout.HORIZONTAL);
							linder.setBackgroundResource(R.color.white);
							
							ImageView businessLogo = new ImageView(mContext);
							LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,(float)1.5);
							layoutParams1.setMargins(0, 0, 5, 0);
							layoutParams1.weight = (float)1.5;
							businessLogo.setLayoutParams(layoutParams1);
							
							ImageLoader imageLoader=new ImageLoader(mContext);  
					        imageLoader.DisplayImage(lst_business.get(i).logo_img, businessLogo);
					        linder.addView(businessLogo);
					        
					        final LinearLayout app_company_info = new LinearLayout(mContext);
					        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
					        layoutParams3.setMargins(5, 0, 5, 0);
					        layoutParams3.weight=4;
					        layoutParams3.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
					        app_company_info.setLayoutParams(layoutParams3);
					        
					        app_company_info.setOrientation(LinearLayout.VERTICAL);
					        
					        TextView txtViewbusinessName = new TextView(mContext);
					        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					        txtViewbusinessName.setLayoutParams(layoutParams4);
					        txtViewbusinessName.setSingleLine(false);
					        txtViewbusinessName.setTextColor(getResources().getColor(R.color.black));
					        txtViewbusinessName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					        txtViewbusinessName.setText(lst_business.get(i).name);
					        app_company_info.addView(txtViewbusinessName);
					        
					        TextView no_of_coupon = new TextView(mContext);
					        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					        no_of_coupon.setLayoutParams(layoutParams5);
					        no_of_coupon.setSingleLine(false);
					        no_of_coupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
					        if(lst_business.get(i).no_of_coupon.toString().equals("1")){
					        	no_of_coupon.setText(lst_business.get(i).no_of_coupon.toString()+" Coupon");
					        } else {
					        	no_of_coupon.setText(lst_business.get(i).no_of_coupon.toString()+" Coupons");
					        }
					        app_company_info.addView(no_of_coupon);
					        
					        TextView view_coupon_btn = new TextView(mContext);
					        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					        layoutParams6.setMargins(0, 5, 0, 0);
					        view_coupon_btn.setLayoutParams(layoutParams6);
					        view_coupon_btn.setText("View Coupons & Locations");
					        view_coupon_btn.setTextColor(Color.parseColor("#ff6700"));
					        view_coupon_btn.setTypeface(null, Typeface.BOLD);
					        app_company_info.addView(view_coupon_btn);
					        
					        linder.addView(app_company_info);
					
					        ImageView row = new ImageView(mContext);
							LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
							layoutParams7.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
							layoutParams7.weight=(float)0.5; 
							row.setLayoutParams(layoutParams7);
							String uri = "@drawable/next";  // where myresource.png is the file
			                // extension removed from the String
							int imageResource = getResources().getIdentifier(uri, null, getPackageName());
							Drawable res = getResources().getDrawable(imageResource);
							row.setImageDrawable(res);
					        
					        linder.addView(row);
					        ll.addView(linder);
							lm.addView(ll);  
							
						}
			   		} else {
			   			lm.removeAllViews();
			   			Toast.makeText(getApplicationContext(), "There are no merchants in this category.", 2000).show();
			   		}
			    	   
			       } else {
			    	   //Toast.makeText(getApplicationContext(), ""+lst_filtered_buss.size(), 3000).show();
			    	  
			    	   if(lst_filtered_buss.size()>0){
			    		   lm.removeAllViews();
				   			for(int i=0;i<lst_filtered_buss.size();i++){
								
								final int k = i;
								
								final LinearLayout ll = new LinearLayout(mContext);
								ll.setOrientation(LinearLayout.HORIZONTAL);
								LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
							            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								params3.setMargins(0, 8, 0, 8);
								ll.setLayoutParams(params3);
								
								ll.setOnClickListener(new OnClickListener() {
							        public void onClick(View v) {
							        	//Toast.makeText(getApplicationContext(), "Test", 3000).show();
							        	manageActivation1 = new ViewcouponAsyncTask(mContext);
							        	manageActivation1.execute();
										buss_id = lst_filtered_buss.get(k).buss_id.toString().trim();
										buss_name = lst_filtered_buss.get(k).name.toString().trim();
										banner_img = lst_filtered_buss.get(k).banner_img.toString().trim();
										activate_code = lst_fund.get(0).activation_code.toString();
							        }
								});
								
								final CheckBox checkboxBusiness = new CheckBox(mContext);
								LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								layoutParams.setMargins(10, 0, 10, 0);
								layoutParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
								checkboxBusiness.setLayoutParams(layoutParams);
								/*checkboxBusiness.setScaleX((float) 1.20);
								checkboxBusiness.setScaleY((float) 1.20);*/
								checkboxBusiness.setButtonDrawable(R.drawable.checkbox_selector);
								ll.addView(checkboxBusiness);
								if (checkedmap.containsKey(lst_filtered_buss.get(k).buss_id.toString().trim())) {
									checkboxBusiness.setChecked(true);
							        	//holder.chkTick.setChecked(itemChecked.get(position)); 
							    } 
								checkboxBusiness.setOnClickListener(new OnClickListener() {
									public void onClick(View v) {
										if(((CompoundButton) v).isChecked()==true){
							        		if(count1<25){
							        			count1++;
							        			//Toast.makeText(getApplicationContext(), ""+count1, 1000).show();
								        		checkedmap.put(lst_business.get(k).buss_id.toString().trim(), lst_business.get(k).name.toString().trim());
								        		count.setText(count1+"/25 Selected");
								        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
							        		} else {
							        			checkboxBusiness.setChecked(false);
							        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
							        		}
							        		
							        	} else {
							        		count1--;
							        		count.setText(count1+"/25 Selected");
							        		checkedmap.remove(lst_business.get(k).buss_id.toString().trim());
							        	}
								        /*if(count1<=25){
								        	count.setText(count1+"/25 Selected");
							        		Toast.makeText(getApplicationContext(), "You have selected " + count1 + " merchants", Toast.LENGTH_SHORT).show();
								        } else {  
						        			Toast.makeText(getApplicationContext(), "The maximum number of merchants have been selected. You must uncheck a merchant to add another.", Toast.LENGTH_SHORT).show();
						        			//count1--;
								        }*/
									 } 
							      });
								
								final LinearLayout linder = new LinearLayout(mContext);
								LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								linder.setLayoutParams(layoutParams2);
								linder.setPadding(3, 3, 3, 3);
								linder.setWeightSum(6);
								linder.setOrientation(LinearLayout.HORIZONTAL);
								linder.setBackgroundResource(R.color.white);
								
								ImageView businessLogo = new ImageView(mContext);
								LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,(float)1.5);
								layoutParams1.setMargins(0, 0, 5, 0);
								layoutParams1.weight = (float)1.5;
								businessLogo.setLayoutParams(layoutParams1);
								
								ImageLoader imageLoader=new ImageLoader(mContext);  
						        imageLoader.DisplayImage(lst_filtered_buss.get(i).logo_img, businessLogo);
						        linder.addView(businessLogo);
						        
						        final LinearLayout app_company_info = new LinearLayout(mContext);
						        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
						        layoutParams3.setMargins(5, 0, 5, 0);
						        layoutParams3.weight=4;
						        layoutParams3.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
						        app_company_info.setLayoutParams(layoutParams3);
						        
						        app_company_info.setOrientation(LinearLayout.VERTICAL);
						        
						        TextView txtViewbusinessName = new TextView(mContext);
						        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						        txtViewbusinessName.setLayoutParams(layoutParams4);
						        txtViewbusinessName.setSingleLine(false);
						        txtViewbusinessName.setTextColor(getResources().getColor(R.color.black));
						        txtViewbusinessName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						        txtViewbusinessName.setText(lst_filtered_buss.get(i).name);
						        app_company_info.addView(txtViewbusinessName);
						        
						        TextView no_of_coupon = new TextView(mContext);
						        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						        no_of_coupon.setLayoutParams(layoutParams5);
						        no_of_coupon.setSingleLine(false);
						        no_of_coupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						        if(lst_filtered_buss.get(i).no_of_coupon.toString().equals("1")){
						        	no_of_coupon.setText(lst_filtered_buss.get(i).no_of_coupon.toString()+" Coupon");
						        } else {
						        	no_of_coupon.setText(lst_filtered_buss.get(i).no_of_coupon.toString()+" Coupons");
						        }
						        app_company_info.addView(no_of_coupon);
						        
						        TextView view_coupon_btn = new TextView(mContext);
						        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						        layoutParams6.setMargins(0, 5, 0, 0);
						        view_coupon_btn.setLayoutParams(layoutParams6);
						        view_coupon_btn.setText("View Coupons & Locations");
						        view_coupon_btn.setTextColor(Color.parseColor("#ff6700"));
						        view_coupon_btn.setTypeface(null, Typeface.BOLD);
						        app_company_info.addView(view_coupon_btn);
						        
						        linder.addView(app_company_info);
						
						        ImageView row = new ImageView(mContext);
								LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
								layoutParams7.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
								layoutParams7.weight=(float) 0.5;
								row.setLayoutParams(layoutParams7);
								String uri = "@drawable/next";  // where myresource.png is the file
				                // extension removed from the String
								int imageResource = getResources().getIdentifier(uri, null, getPackageName());
								Drawable res = getResources().getDrawable(imageResource);
								row.setImageDrawable(res);
						        
						        linder.addView(row);
						        ll.addView(linder);
						        //ll.addView(row);
								lm.addView(ll);  
								
							}
				   		} else {
				   			lm.removeAllViews();
				   			Toast.makeText(getApplicationContext(), "There are no merchants in this category.", 2000).show();
				   		}
			    	   
			       }
			      } 
			     });  
			
		}
		
		if(view == btnPreview){  
			
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			View promptView = layoutInflater.inflate(R.layout.preview_merchant_selection_screen, null);
			//promptView.setBackgroundColor(Color.TRANSPARENT);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
			alertDialogBuilder.setView(promptView);
			final AlertDialog alertD2 = alertDialogBuilder.create();
			final TextView previewheading = (TextView)promptView.findViewById(R.id.previewheading);
			previewheading.setText("Selected Merchant ("+checkedmap.size()+")");
			//final RelativeLayout previewparentrelative = (RelativeLayout)promptView.findViewById(R.id.previewparentrelative);
			final LinearLayout previewlistViewMerchant = (LinearLayout) promptView.findViewById(R.id.previewlistViewMerchant);
			final TextView btnPreviewClose = (TextView)promptView.findViewById(R.id.btnPreviewClose);
			final TextView previewnomerchant = (TextView)promptView.findViewById(R.id.previewnomerchant);
			final RelativeLayout previewrelBottom = (RelativeLayout)promptView.findViewById(R.id.previewrelBottom);
			
			Map<Integer, String> map = sortByValues(checkedmap); 
		      
			
			if(map.size() > 0){
				
				Set set2 = map.entrySet();
			    Iterator iterator2 = set2.iterator();
				while(iterator2.hasNext()) {
					
					Map.Entry me2 = (Map.Entry)iterator2.next();
					//String c = me2.getKey().toString();
					
					BusinessMaster buss_detail = DataStore.getInstance().GetBusinessdetails(me2.getKey().toString());
					final LinearLayout ll = new LinearLayout(mContext);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
				            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					params3.setMargins(0, 8, 0, 8);
					ll.setLayoutParams(params3);
					
					final LinearLayout linder = new LinearLayout(mContext);
					LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					linder.setLayoutParams(layoutParams2);
					linder.setPadding(3, 3, 3, 3);
					linder.setWeightSum(6);
					linder.setOrientation(LinearLayout.HORIZONTAL);
					linder.setBackgroundResource(R.color.white);
					
					ImageView businessLogo = new ImageView(mContext);
					LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,(float)1.5);
					layoutParams1.setMargins(0, 0, 5, 0);
					layoutParams1.weight = (float)1.5;
					businessLogo.setLayoutParams(layoutParams1);
					
					ImageLoader imageLoader=new ImageLoader(mContext);  
			        imageLoader.DisplayImage(buss_detail.logo_img, businessLogo);
			        linder.addView(businessLogo);
			        
			        final LinearLayout app_company_info = new LinearLayout(mContext);
			        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, (float)4.5);
			        layoutParams3.setMargins(5, 0, 10, 0);
			        layoutParams3.weight=(float)4.5;
			        layoutParams3.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
			        app_company_info.setLayoutParams(layoutParams3);
			        
			        app_company_info.setOrientation(LinearLayout.VERTICAL);
			        
			        TextView txtViewbusinessName = new TextView(mContext);
			        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			        txtViewbusinessName.setLayoutParams(layoutParams4);
			        txtViewbusinessName.setSingleLine(false);
			        txtViewbusinessName.setTextColor(getResources().getColor(R.color.black));
			        txtViewbusinessName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			        txtViewbusinessName.setText(buss_detail.name);
			        app_company_info.addView(txtViewbusinessName);
			        
			        TextView no_of_coupon = new TextView(mContext);
			        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			        no_of_coupon.setLayoutParams(layoutParams5);
			        no_of_coupon.setSingleLine(false);
			        no_of_coupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			        if(buss_detail.no_of_coupon.toString().equals("1")){
			        	no_of_coupon.setText(buss_detail.no_of_coupon.toString()+" Coupon");
			        } else {
			        	no_of_coupon.setText(buss_detail.no_of_coupon.toString()+" Coupons");
			        }
			        app_company_info.addView(no_of_coupon);
			        
			        /*TextView view_coupon_btn = new TextView(mContext);
			        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			        layoutParams6.setMargins(0, 5, 0, 0);
			        view_coupon_btn.setLayoutParams(layoutParams6);
			        view_coupon_btn.setText("View Coupons & Locations");
			        view_coupon_btn.setTextColor(Color.parseColor("#ff6700"));
			        view_coupon_btn.setTypeface(null, Typeface.BOLD);
			        app_company_info.addView(view_coupon_btn);*/
			        
			        linder.addView(app_company_info);
			
			       /* ImageView row = new ImageView(mContext);
					LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
					layoutParams7.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
					layoutParams7.weight=1;
					row.setLayoutParams(layoutParams7);
					String uri = "@drawable/next";  // where myresource.png is the file
	                // extension removed from the String
					int imageResource = getResources().getIdentifier(uri, null, getPackageName());
					Drawable res = getResources().getDrawable(imageResource);
					row.setImageDrawable(res);
			        
			        linder.addView(row);*/ 
			        ll.addView(linder);
			        //ll.addView(row);
			        previewlistViewMerchant.addView(ll);  
					
				
				}
			} else { 
				previewnomerchant.setVisibility(View.VISIBLE);
			}
			previewrelBottom.setOnClickListener(new OnClickListener() {
			        public void onClick(View v) {
			        	alertD2.cancel();
			        }
			        });
		
			alertD2.show(); 
		}
		
		if(view == category_down_arrow){
			
			int firstVisibleItem = listViewCategory.getFirstVisiblePosition();
            listViewCategory.setSelection(firstVisibleItem+1);
		}
	}
	
	public class ViewcouponAsyncTask extends AsyncTask<Void, Void, Void> {
		boolean bSuccess;
		ProgressDialog pDialog;
		Context mFriendLogin;
		
		public ViewcouponAsyncTask(Context mContext) {
			mFriendLogin = mContext;
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
				Intent intent = new Intent(mFriendLogin, CouponDetails.class);
				intent.putExtra("business_id", buss_id);
				intent.putExtra("activation_code", activate_code);
				intent.putExtra("buss_name", buss_name);
				intent.putExtra("banner_img", banner_img);
				mContext.startActivity(intent);
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		/*super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);*/
	}
	
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	}
	

}
