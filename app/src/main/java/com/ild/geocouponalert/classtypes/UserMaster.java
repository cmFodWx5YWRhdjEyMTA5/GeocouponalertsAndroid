package com.ild.geocouponalert.classtypes;

public class UserMaster {
	
	public String id; 
	public String device_id;
	public String user_pin;
	public String activation_code;
	public String activation_date;
	public String unused_coupon;
	public String expiring_soon_coupon;
		 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnused_coupon() {
		return unused_coupon;
	}
	public void setUnused_coupon(String unused_coupon) {
		this.unused_coupon = unused_coupon;
	}
	public String getExpiring_soon_coupon() {
		return expiring_soon_coupon;
	}
	public void setExpiring_soon_coupon(String expiring_soon_coupon) {
		this.expiring_soon_coupon = expiring_soon_coupon;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getUser_pin() {
		return user_pin;
	}
	public void setUser_pin(String user_pin) {
		this.user_pin = user_pin;
	}
	public String getActivation_code() {
		return activation_code;
	}
	public void setActivation_code(String activation_code) {
		this.activation_code = activation_code;
	}
	public String getActivation_date() {
		return activation_date;
	}
	public void setActivation_date(String activation_date) {
		this.activation_date = activation_date;
	}
}
