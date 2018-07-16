package com.ild.geocouponalert.classtypes;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAlertMerchant {
	
	public String user_id;
	public ArrayList<String> fav_merchants;
	public ArrayList<String> alert_merchants;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public ArrayList<String> getFav_merchants() {
		return fav_merchants;
	}

	public void setFav_merchants(ArrayList<String> fav_merchants) {
		this.fav_merchants = fav_merchants;
	}

	public ArrayList<String> getAlert_merchants() {
		return alert_merchants;
	}

	public void setAlert_merchants(ArrayList<String> alert_merchants) {
		this.alert_merchants = alert_merchants;
	}
}
