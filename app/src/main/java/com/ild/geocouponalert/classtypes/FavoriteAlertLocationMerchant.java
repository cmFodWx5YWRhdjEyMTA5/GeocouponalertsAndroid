package com.ild.geocouponalert.classtypes;

import java.util.ArrayList;

public class FavoriteAlertLocationMerchant {

	String user_id;
	String is_fav;
	String is_alert;
	ArrayList<String> alert_location_inactive;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getIs_fav() {
		return is_fav;
	}

	public void setIs_fav(String is_fav) {
		this.is_fav = is_fav;
	}

	public String getIs_alert() {
		return is_alert;
	}

	public void setIs_alert(String is_alert) {
		this.is_alert = is_alert;
	}

	public ArrayList<String> getAlert_location_inactive() {
		return alert_location_inactive;
	}

	public void setAlert_location_inactive(ArrayList<String> alert_location_inactive) {
		this.alert_location_inactive = alert_location_inactive;
	}
}
