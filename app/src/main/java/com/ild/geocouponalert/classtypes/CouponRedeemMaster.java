package com.ild.geocouponalert.classtypes;

import java.util.List;

public class CouponRedeemMaster {
	
	public String coupon_id;
	public String buss_id; 
	public String name;
	public String coupon_code; 
	public String activation_code;
	public String details;
	public String disclaimer;
	public String redeem_date;
	public String getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	public String getBuss_id() {
		return buss_id;
	}
	public void setBuss_id(String buss_id) {
		this.buss_id = buss_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoupon_code() {
		return coupon_code;
	}
	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}
	public String getActivation_code() {
		return activation_code;
	}
	public void setActivation_code(String activation_code) {
		this.activation_code = activation_code;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	public String getRedeem_date() {
		return redeem_date;
	}
	public void setRedeem_date(String redeem_date) {
		this.redeem_date = redeem_date;
	}
	
}
