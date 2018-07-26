package com.ild.geocouponalert;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GeofenceStore implements ConnectionCallbacks,
		OnConnectionFailedListener, ResultCallback<Status>, LocationListener {

	private final String TAG = this.getClass().getSimpleName();

	/**
	 * Context
	 */
	private Context mContext;
	
	/**
	 * Google API client object.
	 */
	private GoogleApiClient mGoogleApiClient;
	
	/**
	 * Geofencing PendingIntent
	 */
	private PendingIntent mPendingIntent;
	
	/**
	 * List of geofences to monitor.
	 */
	private ArrayList<Geofence> mGeofences;
	
	/**
	 * Geofence request.
	 */
	private GeofencingRequest mGeofencingRequest;
	
	/**
	 * Location Request object.
	 */
	private LocationRequest mLocationRequest;

	/**
	 * Constructs a new GeofenceStore. 
	 * 
	 * @param context The context to use.
	 * @param geofences List of geofences to monitor.
	 */
	public GeofenceStore(Context context, ArrayList<Geofence> geofences) {
		mContext = context;
		mGeofences = new ArrayList<Geofence>(geofences);
		mPendingIntent = null;

		// Build a new GoogleApiClient, specify that we want to use LocationServices
		// by adding the API to the client, specify the connection callbacks are in 
		// this class as well as the OnConnectionFailed method. 
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

		// This is purely optional and has nothing to do with geofencing. 
		// I added this as a way of debugging.
		// Define the LocationRequest.
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mGoogleApiClient.connect();
	} 
 
	public void disconnect() {
		mGoogleApiClient.disconnect();
	}
	
	@Override
	public void onResult(Status result) {
		if (result.isSuccess()) {
			Log.v(TAG, "Success!");
		} else if (result.hasResolution()) {
			// TODO Handle resolution
		} else if (result.isCanceled()) {
			Log.v(TAG, "Canceled");
		} else if (result.isInterrupted()) {
			Log.v(TAG, "Interrupted");
		} else {

		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.v(TAG, "Connection failed.");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// We're connected, now we need to create a GeofencingRequest with
		// the geofences we have stored.
		
		removeGeofence();
		
		mGeofencingRequest = new GeofencingRequest.Builder().addGeofences(
				mGeofences).build();
		
		mPendingIntent = createRequestPendingIntent();
		
		
		
				
		// This is for debugging only and does not affect
		// geofencing.
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
		
		// Submitting the request to monitor geofences.
		PendingResult<Status> pendingResult = LocationServices.GeofencingApi
				.addGeofences(mGoogleApiClient, mGeofencingRequest,
						mPendingIntent);
		
		// Set the result callbacks listener to this class.
		pendingResult.setResultCallback(this);
	}

	@Override
	public void onConnectionSuspended(int cause) {
		Log.v(TAG, "Connection suspended.");
	}

	/**
	 * This creates a PendingIntent that is to be fired when geofence transitions
	 * take place. In this instance, we are using an IntentService to handle the
	 * transitions.
	 * 
	 * @return A PendingIntent that will handle geofence transitions.
	 */
	private PendingIntent createRequestPendingIntent() {
		/*if (mPendingIntent == null) {
			Log.v(TAG, "Creating PendingIntent");
			Intent intent = new Intent(mContext, GeofenceIntentService.class);
			mPendingIntent = PendingIntent.getService(mContext, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
		}

		return mPendingIntent;*/
		
		
		// If the PendingIntent already exists
        if (null != mPendingIntent) {

            // Return the existing intent
            return mPendingIntent;

        // If no PendingIntent exists
        } else { 

            // Create an Intent pointing to the IntentService
            Intent intent = new Intent("com.ild.geocouponalert.ACTION_RECEIVE_GEOFENCE");
//            Intent intent = new Intent(context, ReceiveTransitionsIntentService.class);
            /*
             * Return a PendingIntent to start the IntentService.
             * Always create a PendingIntent sent to Location Services
             * with FLAG_UPDATE_CURRENT, so that sending the PendingIntent
             * again updates the original. Otherwise, Location Services
             * can't match the PendingIntent to requests made with it.
             */
            return PendingIntent.getBroadcast(
                    mContext,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
	}

	@Override
	public void onLocationChanged(Location location) {

		/*Log.v(TAG, "Location Information\n" 
				+ "==========\n"
				+ "Provider:\t" + location.getProvider() + "\n"
				+ "Lat & Long:\t" + location.getLatitude() + ", "
				+ location.getLongitude() + "\n"
				+ "Altitude:\t" + location.getAltitude() + "\n"
				+ "Bearing:\t" + location.getBearing() + "\n"
				+ "Speed:\t\t" + location.getSpeed() + "\n"
				+ "Accuracy:\t" + location.getAccuracy() + "\n");*/
	}
	
	
	public void removeGeofence(){
		LocationServices.GeofencingApi.removeGeofences(
	            mGoogleApiClient,
	            // This is the same pending intent that was used in addGeofences().
	            createRequestPendingIntent()
	    ).setResultCallback(this);
	}
}
