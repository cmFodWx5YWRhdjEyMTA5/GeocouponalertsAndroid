/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ild.geocouponalert;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Listener for geofence transition changes.
 *
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceReceiver extends BroadcastReceiver {
    Context context;
    
    //protected static final String TAG = "geofence-transitions-service";
	protected static final String TAG = "geofence-service";
    
    protected static String CurrentDate;
    
    protected static int flag = 0;

    Intent broadcastIntent = new Intent();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context; 

        
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		Log.v(TAG, "onHandleIntent");
		if(!geofencingEvent.hasError()) {
			int transition = geofencingEvent.getGeofenceTransition();
			String notificationTitle;
			
			switch(transition) {
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				notificationTitle = context.getString(R.string.you_have) /*+ getTriggeringGeofences(intent)*/;
				Log.v(TAG, "Geofence Entered");
				break;
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				notificationTitle = "Geofence Dwell";
				Log.v(TAG, "Dwelling in Geofence");
				break;
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				notificationTitle = "Geofence Exit";
				Log.v(TAG, "Geofence Exited");
				break;
			default:
				notificationTitle = "Geofence Unknown";
			}
						
			getTriggeringGeofences(intent,notificationTitle);
		} 
	
    }
    
    
    private void getTriggeringGeofences(Intent intent,String notificationTitle) {
		GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
		List<Geofence> geofences = geofenceEvent
				.getTriggeringGeofences();
		
		for (int i = 0; i < geofences.size(); i++) {
					
			String[] separated = geofences.get(i).getRequestId().split("\\|");
			flag=sendNotification(context, separated[0],separated[1], separated[2], notificationTitle);
			String curr_date = new SimpleDateFormat("MMM dd, yyyy").format(new Date());
		}
	}
     
    
    private int sendNotification(Context context, String locaionID, String bussID, String notificationText, String notificationTitle) {
		
		Intent notificationIntent = new Intent(context, MerchantListHomePage.class);

		notificationIntent.putExtra("business_id",bussID);
		notificationIntent.putExtra("location_id",locaionID);
		
        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context); 

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MerchantListHomePage.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);
		
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000; 
        
     // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(m, PendingIntent.FLAG_UPDATE_CURRENT);

        
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire(); 
		
		// RemoteViews mContentView = new RemoteViews(getPackageName(), R.layout.xml_header);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setColor(context.getResources().getColor(R.color.light_green))
				.setContentTitle(notificationTitle)
				.setContentText(notificationText)
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(notificationPendingIntent)
				.setAutoCancel(true);
		
		/* NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
			        .setContentIntent(notificationPendingIntent)
			        .setDefaults(Notification.DEFAULT_ALL)
			        .setAutoCancel(true)
			        .setContent(mContentView);

		
		    mContentView.setImageViewResource(R.id.fundraiserLogo, R.drawable.ic_launcher);
		    mContentView.setTextViewText(R.id.app_company_info1, "Custom notification");
		    mContentView.setTextViewText(R.id.app_company_info2, "This is a custom layout");*/
		   
		    
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(m, notificationBuilder.build());

		wakeLock.release();
		
		return 1;
	}
	
	
}	



   
