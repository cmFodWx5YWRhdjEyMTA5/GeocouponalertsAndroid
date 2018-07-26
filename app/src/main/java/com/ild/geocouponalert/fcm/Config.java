package com.ild.geocouponalert.fcm;

public class Config {

    // broadcast receiver intent filters
    public static final String PUSH_CONFIRM_BOOKING = "pushNotificationForBookingConfirmation";
    public static final String PUSH_DRIVER_ONE_MIN_AWAY = "pushNotificationForDriverOneMinAway";
    public static final String PUSH_DRIVER_IS_ARRIVED_AND_START_TRIP = "pushNotificationForDriverArrivalAndStartTrip";
    public static final String PUSH_CANCEL_TRIP_BY_DRIVER = "pushNotificationForCancelTrip";
    public static final String PUSH_TRIP_COMPLETE = "pushNotificationForTripComplete";
    public static final String PUSH_NO_CAB_FOUND = "pushNotificationForNoCabFound";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
