package com.ild.geocouponalert;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    //static final String SERVER_URL = "http://infologictechnologies.com/projects/geocouponalertGcmTest/register.php"; 
    static final String SERVER_URL = "http://geocouponalerts.com/coupon-app-v2/coupon_gcm_service/register.php";
    // Google project id
    public static final String SENDER_ID = "430649700976"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.ild.geocouponalert.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "price";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent); 
    }
}
