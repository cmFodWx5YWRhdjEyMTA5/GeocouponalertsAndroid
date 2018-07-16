package com.ild.geocouponalert.webmethod;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

import flexjson.JSONDeserializer;
import flexjson.JSONException;
import flexjson.JSONSerializer;

import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.BusinessSelectedMaster;
import com.ild.geocouponalert.classtypes.CouponMaster;
import com.ild.geocouponalert.classtypes.FavoriteAlertLocationMerchant;
import com.ild.geocouponalert.classtypes.FavoriteAlertMerchant;
import com.ild.geocouponalert.classtypes.FundraiserMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.classtypes.UserMaster;
import com.ild.geocouponalert.datastore.DataStore;
import com.ild.geocouponalert.utils.FOGlobalVariable;


public class RestCallManager {

	private String tag = "RestCallHelper";
	public static final String BASE_URL = "http://geocouponalerts.com/coupon-app-v2/coupon-service-v2_1/";
	//public static final String BASE_URL = "http://infologictechnologies.com/projects/mobile-coupon-beta/coupon-service-v2_1/";
	//public static final String BASE_URL = "http://geocouponalerts.com/coupon-app-v2/coupon-service-v2_0/";
	//public static final String BASE_URL = "http://infologictechnologies.com/projects/mobile-coupon-beta/coupon-service-v2_0/";
	//public static final String BASE_URL = "http://geocouponalerts.com/coupon-app-v2/coupon-service/";
	//public static final String BASE_URL = "http://geocouponalerts.com/coupon-app-v2/coupon-service-v1_3/";
	//public static final String BASE_URL = "http://www.foodel.co.in/coupon-service/";
	private static RestCallManager instance;
	public static String response = null;
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	private RestCallManager() {
		
	}
	public static synchronized RestCallManager getInstance() {
		if (instance == null) {
			instance = new RestCallManager();
		}
		return instance; 
	} 
	/**
	 * Returns the Response from the server
	 * @return 
	 */
	public String getResponse(){   
		return response;
		
	}

	public  WebServiceResponse getCallNew(String strURL) {
		WebServiceResponse webresponse = new WebServiceResponse();

		HttpURLConnection conn = null;

		try {
			URL url = new URL(strURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			InputStream in = new BufferedInputStream(conn.getInputStream());
			InputStreamReader inputReader = new InputStreamReader(in,
					"iso-8859-1");
			//Log.i(tag, inputReader.toString());
			webresponse = new JSONDeserializer<WebServiceResponse>()
					.deserialize(inputReader, WebServiceResponse.class);

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			conn.disconnect();
		}
		return webresponse;
	}

	public WebServiceResponse postCallNew(String requestURL,
												  HashMap<String, String> postDataParams) {

		WebServiceResponse webresponse = new WebServiceResponse();
		HttpURLConnection conn = null;

		try {
			URL url = new URL(requestURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);


			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));

			writer.write(getPostDataString(postDataParams));
			writer.flush();
			writer.close();
			os.close();
			conn.connect();

			InputStream in = new BufferedInputStream(conn.getInputStream());
			InputStreamReader inputReader = new InputStreamReader(in,
					"iso-8859-1");
			Log.i(tag, inputReader.toString());
			webresponse = new JSONDeserializer<WebServiceResponse>()
					.deserialize(inputReader, WebServiceResponse.class);

			in.close();


		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			conn.disconnect();
		}
		return webresponse;
	}

	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		/*StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
        return result.toString();*/

		JSONObject jsonParam = new JSONObject();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			try{
				jsonParam.put(entry.getKey(), entry.getValue());
			}catch (Exception e){

			}
		}
		return jsonParam.toString();
	}


	public  WebServiceResponse getCall(String strURL) { 
		WebServiceResponse webresponse = new WebServiceResponse();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet getRequest = new HttpGet(strURL);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {

//				throw new RuntimeException("Failed : HTTP error code : "
//						+ response.getStatusLine().getStatusCode());
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {

				InputStream is = entity.getContent();
				InputStreamReader inputReader = new InputStreamReader(is,
						"iso-8859-1");
				Log.i(tag, inputReader.toString());
				webresponse = new JSONDeserializer<WebServiceResponse>()
						.deserialize(inputReader, WebServiceResponse.class);

				is.close();
			}

			httpClient.getConnectionManager().shutdown();

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			Log.i(tag, "Http Client Closing Connection");
			httpClient.getConnectionManager().shutdown();
		}
		return webresponse;
	}

    public WebServiceResponse postCall(String strURL, String input) {
		
		// ////////////////////////////////////////////////////
		// /RestCall Post
		WebServiceResponse webresponse = new WebServiceResponse();
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(strURL);
			httppost.setHeader("Content-type", "application/json");
			// Class Object to JSON String
			// String input = new JSONSerializer().serialize(obj);
			
			// Add object with the post request
			httppost.setEntity(new StringEntity(input));
			// Execute request and Get the response
			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity entity = httpresponse.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				InputStreamReader inputReader = new InputStreamReader(is,
						"iso-8859-1");
 
				// String str = new JSONDeserializer<String>()
				// .deserialize(inputReader, String.class);
				webresponse = new JSONDeserializer<WebServiceResponse>()
						.deserialize(inputReader, WebServiceResponse.class);

				is.close();
			}
		} catch (Exception e) {
			Log.e(tag, "Error in http connection " + e.toString());
			return null;
		} finally {
			Log.i(tag, "Http Client Closing Connection");
			httpclient.getConnectionManager().shutdown();
		}
		return webresponse;
	}

    public JSONObject getJSONFromUrl(String url) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			/* HttpPost httpPost = new HttpPost(url); */
			HttpGet getRequest = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(getRequest);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			try {
				jObj = new JSONObject(json);
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String

		return jObj;

	}

     
    
  //--------------------------------Get Record of Fundraiser----------------------------------//
    
 public boolean insertActivationCode(String activation_code,String device_id,String email) {
    	
		String url = BASE_URL;
		if(activation_code!=""){
		url = url + "checkPostcardCode?activation_code=" + activation_code + "&device_id=" + device_id + "&email="+email ;
		}
		
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<PostcardMaster> lst_fund = new ArrayList<PostcardMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
				lst_fund = new JSONDeserializer<List<PostcardMaster>>().use("values",PostcardMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addPostcard(lst_fund);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			} 
			return true;     
		}  
		else if(wbResponse != null && !wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null){
				FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
				return false;
			}
		
		return false;   
    }	
 
 //--------------------------------Get Record of Available Business----------------------------------//
 
 public boolean downloadAllBusiness(String zip,String activation_code) { 
    	
		String url = BASE_URL;
		url = url + "getBusiness?zip=" + zip + "&activation_code=" + activation_code + "&device_type=Android" ;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<BusinessMaster> lst_business = new ArrayList<BusinessMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		/*lst_business = new JSONDeserializer<List<BusinessMaster>>().use("values",
						BusinessMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addBusiness(lst_business);*/
				
			} catch (Exception e) {
				
				e.printStackTrace();  
			} 
			return true;  
		} else {
			return false;      
		}  
			   
    }	   
    

  

   
    //---------------------------------------------------------------------------------------------
 

 
 
 //-----------------------------------------------------------------------------------------------------
 
 public boolean downloadAllLocationOfBusiness(String device_id,String buss_id,String latitude,String longitude,String email) {
 	
		String url = BASE_URL;
		url = url + "getCouponRedeemLocationByBusinessId?device_id="+device_id+"&buss_id=" + buss_id + "&user_lat="+latitude +"&user_long="+longitude+"&email="+email;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<BusinessCouponLocation> couponLocation_master = new ArrayList<BusinessCouponLocation>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		couponLocation_master = new JSONDeserializer<List<BusinessCouponLocation>>().use("values",
		 				BusinessCouponLocation.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addCouponLocation(couponLocation_master);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			}  
			return true;   
		} 
		return false;        
 }	 
 
 public boolean downloadAllLocationCouponByLocationId(String device_id,String buss_id,String latitude,String longitude,String email,String loc_id) {
	 	
		String url = BASE_URL;
		url = url + "downloadAllLocationCouponByLocationId?device_id="+device_id+"&buss_id=" + buss_id + "&user_lat="+latitude +"&user_long="+longitude+"&email="+email+"&location_id="+loc_id;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<BusinessCouponLocation> couponLocation_master = new ArrayList<BusinessCouponLocation>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		couponLocation_master = new JSONDeserializer<List<BusinessCouponLocation>>().use("values",
		 				BusinessCouponLocation.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addCouponLocation(couponLocation_master);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			}  
			return true;   
		} 
		return false;        
}	  
 
 
 
 public boolean downloadAllBusinessLocationMap(String zipcode) {   
	 	
		String url = BASE_URL;
		url = url + "getLatLognOfBusinessLoc?zip=" + zipcode;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<BusinessLocationMaster> lstBusinessLocation = new ArrayList<BusinessLocationMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		lstBusinessLocation = new JSONDeserializer<List<BusinessLocationMaster>>().use("values",
		 				BusinessLocationMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addBusinessMultipleLocation(lstBusinessLocation);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			} 
			return true;  
		} 
		return false;  
}



 public boolean setMerchantAlertFavouriteParticularLocation(String user_email,String buss_id
		 ,String alert_flag,String favorite_flag,String inactive_alert_loc_str) {

	String url = BASE_URL;
	url = url + "setMerchantAlertFavouriteParticularLocation?user_email="+user_email+"&buss_id="+buss_id+"&favorite_flag="+favorite_flag+"&alert_flag="+alert_flag+"&alert_location="+inactive_alert_loc_str;
	WebServiceResponse wbResponse = new WebServiceResponse();
	wbResponse = getCall(url);

	if (wbResponse != null && wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null) {
		return true;
	}
	return false;
 }
 
 
 public boolean insertSelectedBusiness(String activation_code,String device_id,String buss_id,String email) {
 	
		String url = BASE_URL;
		url = url + "storeSelectedBusiness?activation_code=" + activation_code + "&device_id=" + device_id + "&buss_id=" + buss_id+"&email="+email;
		WebServiceResponse wbResponse = new WebServiceResponse(); 
		wbResponse = getCall(url);
		
		if (wbResponse != null && wbResponse.StatusCode.equals("0") 
				&& wbResponse.Data != null) {
		 	 
			return true;  
		} 
		return false;  
 }	
 
 public boolean downloadAllCouponLocationByBussID(String activation_code,String buss_id) {
	 	
		String url = BASE_URL;
		url = url + "getCouponLocationDetailsByBusinessId?buss_id=" + buss_id + "&activation_code="+ activation_code;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url); 
		List<BusinessCouponLocation> couponLocation_master = new ArrayList<BusinessCouponLocation>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		couponLocation_master = new JSONDeserializer<List<BusinessCouponLocation>>().use("values",
		 				BusinessCouponLocation.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addCouponLocation(couponLocation_master);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			} 
			return true;  
		} 
		return false;  
}	 
 
 
 public boolean redeemCoupon(String coupon_id,String buss_id,String device_id,String activation_code,String email,String user_lat,String user_long) {
	 	
		String url = BASE_URL;
		url = url + "redeemCoupon?coupon_id=" + coupon_id + "&buss_id="+ buss_id + "&device_id="+ device_id + "&activation_code="+ activation_code+"&email="+email+"&user_lat="+user_lat+"&user_long="+user_long;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<CouponMaster> coupon_master = new ArrayList<CouponMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		return true;
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			} 
			return true;     
		} 
		return false;     
}
 
 
 public String checkAvailablity(String email,String device_id) {
	 	
		String url = BASE_URL; 
		url = url + "checkActivationCodeByDeviceId?email="+email+"&device_id=" + device_id;
		WebServiceResponse wbResponse = new WebServiceResponse(); 
		wbResponse = getCall(url);  

		
		List<PostcardMaster> lst_fund = new ArrayList<PostcardMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try { 
				lst_fund = new JSONDeserializer<List<PostcardMaster>>().use("values",PostcardMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addPostcard(lst_fund);
				
			} catch (Exception e) {
				
				e.printStackTrace();  
			} 
			return "Success";    
		}  
		/*else if(wbResponse != null && wbResponse.StatusCode.equals("1")
				&& wbResponse.Data != null){
			
			try {
				lst_fund = new JSONDeserializer<List<PostcardMaster>>().use("values",PostcardMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addPostcard(lst_fund);
				
			} catch (Exception e) { 
				 
				e.printStackTrace();  
			} 
			return "No Card Available";
		}*/
		else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && !wbResponse.StatusCode.equals("1")){
			Log.i(tag, "Rest call unsuccessfull");
		}
		else{
			Log.i(tag, "Rest call unsuccessfull response is null");
		}

		return null; 
}	  
 
 
 
//--------------------------------Get Record of Fundraiser----------------------------------//
 
public boolean pushNotificationForCouponAvailable(String zipcode,String device_id) {
   	
		String url = BASE_URL;
		
		url = url + "notification_on_location_change?user_curr_zip=" + zipcode + "&device_id=" + device_id ;
		
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		
		 		return true;
							
			} catch (Exception e) {
				
				e.printStackTrace(); 
			} 
			return true;   
		} 
		
		return false;  
   }	

     
  
        
  
   
	public String updateUserSetup(String input) {
		// TODO Auto-generated method stub
		String url = BASE_URL + "updateUserDetails/";
		WebServiceResponse wbResponse = postCall(url,input);
		if(wbResponse != null && wbResponse.StatusCode.equals("0") ){
			//return webResponse.Data;
			return wbResponse.StatusCode;
		}
    	
		else if(wbResponse != null && !wbResponse.StatusCode.equals("0")){
			Log.i(tag, "Rest call unsuccessfull");
		}
		else{
			Log.i(tag, "Rest call unsuccessfull response is null");
		}

		return null;
	}  
	
	public boolean getUserSetup(String device_id) {
		// TODO Auto-generated method stub
		String url = BASE_URL + "getUserDetails?device_id=" + device_id;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<UserMaster> coupon_master = new ArrayList<UserMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		coupon_master = new JSONDeserializer<List<UserMaster>>().use("values",
		 				UserMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addUserDetails(coupon_master);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			} 
			return true;  
		} 
		return false;
	}
	  
	public boolean assignDeliveryBoy(String input) {
		// TODO Auto-generated method stub
		String url = BASE_URL + "updateDeliveryboyStatus/";
		WebServiceResponse wbResponse = postCall(url,input);
    	
    	if(wbResponse != null && wbResponse.StatusCode.equals("0")){
			//return webResponse.Data;
			return true;
		}
		else if(wbResponse != null && !wbResponse.StatusCode.equals("0")){
			Log.i(tag, "Rest call unsuccessfull");
		}
		else{
			Log.i(tag, "Rest call unsuccessfull response is null");
		}

		return false;
	}
	
	
	
	public boolean updateAttandenceStatus(String input) {
		// TODO Auto-generated method stub
		String url = BASE_URL + "setAttendenceStatus/";
		WebServiceResponse wbResponse = postCall(url,input);
    	
    	if(wbResponse != null && wbResponse.StatusCode.equals("0")){
			//return webResponse.Data;
			return true;
		}
		else if(wbResponse != null && !wbResponse.StatusCode.equals("0")){
			Log.i(tag, "Rest call unsuccessfull");
		}
		else{
			Log.i(tag, "Rest call unsuccessfull response is null");
		}

		return false;
	}
	
public boolean getSelectedMerchant(String device_id,String email) {
	 	
		String url = BASE_URL;
		url = url + "getSelectedBusiness?device_id=" + device_id+"&device_type=Android&email="+email+"&deviceOS=android";
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		List<BusinessMaster> business_master = new ArrayList<BusinessMaster>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
		 	try {
		 		business_master = new JSONDeserializer<List<BusinessMaster>>().use("values",
		 				BusinessMaster.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addSelectedBusiness(business_master);
				
			} catch (Exception e) {
				
				e.printStackTrace(); 
			}  
			return true;  
		}else if (wbResponse != null && wbResponse.StatusCode.equals("1")
				&& wbResponse.Data != null) {
			FOGlobalVariable.IsActivationCodeAlreadyExist="1";
			return false;
		}else if(wbResponse != null && !wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null){
			FOGlobalVariable.IsActivationCodeAlreadyExist="0";
			return false;
		}

		return false;       
}



public boolean redeemLocationStatus(String user_latitude,String user_longitude, String buss_id, String device_id, String coupon_id) {
	String url = BASE_URL;
	url = url + "redeemLocationStatus?user_latitude=" + user_latitude+"&user_longitude="+user_longitude+"&buss_id="+buss_id+"&device_id="+device_id+"&coupon_id="+coupon_id;
	WebServiceResponse wbResponse = new WebServiceResponse();
	wbResponse = getCall(url); 
	if (wbResponse != null && wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null) {
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return true;  
	}
	else if(wbResponse != null && !wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null){
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.StatusCode;
		return false;
	}

	return false;    
}

public String userLogin(String email,String password,String device_id){
	
	String url = BASE_URL;
	url = url + "userLogin?email="+email+"&password="+password+"&device_id="+device_id;
	WebServiceResponse wbResponse = new WebServiceResponse();
	wbResponse = getCall(url);
	List<PostcardMaster> lst_fund = new ArrayList<PostcardMaster>();
	if (wbResponse != null && wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null) {
	 	try {
			lst_fund = new JSONDeserializer<List<PostcardMaster>>().use("values",PostcardMaster.class).deserialize(wbResponse.Data);
			DataStore.getInstance().addPostcard(lst_fund);
			
		} catch (Exception e) {
			
			e.printStackTrace();  
		} 
		return "Success";    
	}  
	
	else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && wbResponse.Data != null){
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return "Failure";   
	}
	return null;   
	
}

public Boolean userRegistration(String email,String password,String device_id){
	
	String url = BASE_URL;
	url = url + "userRegistration?email="+email+"&password="+password+"&device_id="+device_id;
	WebServiceResponse wbResponse = new WebServiceResponse();
	wbResponse = getCall(url);
	if (wbResponse != null && wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null) {
		return true;    
	}  
	
	else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && wbResponse.Data != null){
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return false;   
	}
	return false;   
	
}

public Boolean forgotPassword(String email){
	
	String url = BASE_URL;
	url = url + "forgotPassword?email="+email;
	WebServiceResponse wbResponse = new WebServiceResponse();
	wbResponse = getCall(url);
	if (wbResponse != null && wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null) {
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return true;    
	}  
	
	else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && wbResponse.Data != null){
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return false;   
	}
	return false;   
	
}

public Boolean changePassword(String email,String old_password,String new_password){
	
	String url = BASE_URL;
	url = url + "changePassword?email="+email+"&old_password="+old_password+"&new_password="+new_password;
	WebServiceResponse wbResponse = new WebServiceResponse();
	wbResponse = getCall(url);
	if (wbResponse != null && wbResponse.StatusCode.equals("0")
			&& wbResponse.Data != null) {
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return true;    
	}  
	
	else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && wbResponse.Data != null){
		FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
		return false;   
	}
	return false;   
	
}
	
	public void initDataStore(String buss_id,String activation_code){
		
		try
		{
		DataStore.getInstance().clearCouponList();
		downloadAllCouponLocationByBussID(activation_code, buss_id);		
		}
		catch(Exception e)
		{
			Log.e("", "exception"+e.getMessage()); 
		}
		
	}

	public boolean suggestedMerchant(HashMap<String, String> input) {
		String url = BASE_URL + "suggestedMerchant";
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse= postCallNew(url, input);
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
			FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
			return true;
		}

		else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && wbResponse.Data != null){
			FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
			return false;
		}
		return false;
	}

	public Boolean updateZipCode(String zipcode,String uemail){
		String url = BASE_URL;
		url = url + "updateZipCode?email="+uemail+"&zip="+zipcode;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCall(url);
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
			FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
			return true;
		}else if(wbResponse != null && !wbResponse.StatusCode.equals("0") && wbResponse.Data != null){
			FOGlobalVariable.IsActivationCodeAlreadyExist=wbResponse.Data;
			return false;
		}
		return false;
	}

	public boolean getFavoriteAlertMerchant(String email) {

		String url = BASE_URL;
		url = url + "getMerchantAlertAndFavourite?email="+email;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCallNew(url);
		List<FavoriteAlertMerchant> business_master = new ArrayList<FavoriteAlertMerchant>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
			try {
				business_master = new JSONDeserializer<List<FavoriteAlertMerchant>>().use("values",
						FavoriteAlertMerchant.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addFavoriteAlertMerchant(business_master);

			} catch (Exception e) {

				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean setMerchantAlertFavourite(String email,String alerts_merchant, String favorite_merchant) {

		String url = BASE_URL;
		url = url + "setMerchantAlertFavourite?user_email="+email+"&favorite_merchant="+favorite_merchant+"&alerts_merchant="+alerts_merchant;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCallNew(url);
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
			return true;
		}
		return false;
	}

	public boolean getMerchanFavouriteAndParticluarLocationAlert(String email,String bussId) {

		String url = BASE_URL;
		url = url + "getMerchantFavouriteAndParticluarLocationAlert?email="+email+"&buss_id="+bussId;
		WebServiceResponse wbResponse = new WebServiceResponse();
		wbResponse = getCallNew(url);
		List<FavoriteAlertLocationMerchant> business_master = new ArrayList<FavoriteAlertLocationMerchant>();
		if (wbResponse != null && wbResponse.StatusCode.equals("0")
				&& wbResponse.Data != null) {
			try {
				business_master = new JSONDeserializer<List<FavoriteAlertLocationMerchant>>().use("values",
						FavoriteAlertLocationMerchant.class).deserialize(wbResponse.Data);
				DataStore.getInstance().addFavoriteAlertMerchantEdit(business_master);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}


}
