package com.ild.geocouponalert.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class FOGlobalVariable {

	public static String M_CardAvailable;
	public static String IsActivationCodeAlreadyExist;
	public static String M_LOGIN_ID;
	public static String pushNotificationJsonString;
	
	
	public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";
    public static final float GEOFENCE_RADIUS_IN_METERS = 50; // 1 mile, 1.6 km
    public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();
}
