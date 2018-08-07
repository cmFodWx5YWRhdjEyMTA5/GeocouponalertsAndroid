package com.ild.geocouponalert.fcm;

public class Config {

    // broadcast receiver intent filters
    public static final String PUSH_NEW_MERCHANT = "pushNotificationForNewMerchant";
    public static final String PUSH_EXPIRING_CARD_REMINDER = "pushNotificationForExpiringCard";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
