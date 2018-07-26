package com.ild.geocouponalert.fcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationUtils notificationUtils;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final int CONFIRM_BOOKING = 1;
    private static final int DRIVER_ONE_MIN_AWAY = 2;
    private static final int DRIVER_IS_ARRIVED = 3;
    private static final int START_TRIP = 4;
    private static final int CANCEL_TRIP_BY_DRIVER = 5;
    private static final int TRIP_COMPLETE = 6;
    private static final int NO_CAB_FOUND = 7;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
           /* Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();*/
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            //String title = data.getString("title");
            //String message = data.getString("message");
            //JSONObject payload = data.getJSONObject("payload");

            //Log.e(TAG, "title: " + title);
            //Log.e(TAG, "message: " + message);
            //Log.e(TAG, "payload: " + payload.toString());

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Log.e(TAG, "App Fore ground ");
                int notification_mode = Integer.parseInt(data.getString("notification_mode"));
                Intent pushNotification=null;
                if (notification_mode==CONFIRM_BOOKING) {
                    pushNotification = new Intent(Config.PUSH_CONFIRM_BOOKING);
                }
                else if (notification_mode==NO_CAB_FOUND){
                    pushNotification = new Intent(Config.PUSH_NO_CAB_FOUND);
                }
                else if (notification_mode==DRIVER_IS_ARRIVED || notification_mode == START_TRIP){
                    pushNotification = new Intent(Config.PUSH_DRIVER_IS_ARRIVED_AND_START_TRIP);
                }
                else if (notification_mode==CANCEL_TRIP_BY_DRIVER){
                    pushNotification = new Intent(Config.PUSH_CANCEL_TRIP_BY_DRIVER);
                }
                else if (notification_mode==TRIP_COMPLETE){
                    pushNotification = new Intent(Config.PUSH_TRIP_COMPLETE);
                }
                pushNotification.putExtra("jsonValue",data.toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                /*Log.e(TAG, "App background ");
                Intent resultIntent = new Intent(getApplicationContext(), HomePage.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }*/
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
