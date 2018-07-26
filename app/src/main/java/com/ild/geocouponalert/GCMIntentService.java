package com.ild.geocouponalert;

import static com.ild.geocouponalert.CommonUtilities.SENDER_ID;
import static com.ild.geocouponalert.CommonUtilities.displayMessage;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.ild.geocouponalert.MerchantListHomePage;
import com.ild.geocouponalert.R;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        ServerUtilities.register(context, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        /*int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context, MerchantListHomePage.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);  */ 
    	
    	String title = context.getString(R.string.app_name);
    	Intent notificationIntent = new Intent(context, MerchantListHomePage.class);

		notificationIntent.putExtra("new_merchant_notification","1");
		notificationIntent.putExtra("business_id","");
		
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
				.setContentTitle(title)
				.setContentText(message)
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


    }

}
