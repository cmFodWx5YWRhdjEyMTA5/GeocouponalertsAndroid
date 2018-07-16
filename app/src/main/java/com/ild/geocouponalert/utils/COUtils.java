package com.ild.geocouponalert.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;


public class COUtils {

	//---------------------------------------------------------------------
		public static boolean isEmailValid(String email) {
		    boolean isValid = false;
		
		    //String expression = "[A-Za-z0-9._%+-]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,4}";
		    String expression="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		    CharSequence inputStr = email;
		
		    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		    Matcher matcher = pattern.matcher(inputStr);
		    if (matcher.matches()) {
		        isValid = true;
		    }
		    return isValid;
		}
		
		public static boolean isMobileNoValid(String mobile_no) {
		    boolean isValid = false;
		
		    //String expression = "[A-Za-z0-9._%+-]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,4}";
		    //String expression="^\\+?([0-9]{2})?[0-9]+$";
		    //android.util.Patterns.PHONE.matcher(mobile_no).matches();
		    Pattern pattern = Pattern.compile("\\d{10}");
		    Matcher matcher = pattern.matcher(mobile_no);
		    if (matcher.matches()) {
		        isValid = true;
		    }
		    return isValid;
		}
		
		//---------------------------------------------------------------------
		public static boolean isStringNullOrEmpty(String str) {
			boolean result = false;
			if (str == null || str.equals("")) {

				result = true;
			}

			return result;

		}
		
		//------------------------------------------------------------------------------------------------------------------
		public static int getstatusBarHeight(Context context) {
			   
			DisplayMetrics displayMetrics = new DisplayMetrics();
			((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
			int statusBarHeight;
			final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
			final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
			final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
			final int XHIGH_DPI_STATUS_BAR_HEIGHT = 50;    
			
			switch (displayMetrics.densityDpi) {
			    case DisplayMetrics.DENSITY_HIGH:
			        statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
			        break;
			    case DisplayMetrics.DENSITY_MEDIUM:
			        statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			        break;
			    case DisplayMetrics.DENSITY_LOW:
			        statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
			        break;
			    case DisplayMetrics.DENSITY_XHIGH:
			        statusBarHeight = XHIGH_DPI_STATUS_BAR_HEIGHT;
			        break;
			    default:
			        statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			}
			return statusBarHeight;
		}
		
				
		public static void setDefaults(String key, String value, Context context) {
		    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(key, value);
		    editor.commit();
		}

		public static String getDefaults(String key, Context context) {
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		    return preferences.getString(key, null);
		}
		
		
		
		
		public static String pad(int c) {
			if (c >= 10)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}
}
