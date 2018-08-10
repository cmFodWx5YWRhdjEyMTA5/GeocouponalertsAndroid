package com.ild.geocouponalert.classtypes;

import java.util.List;

public class CouponMaster {
	
	 public String id;
	 public String buss_id;
	 public String coupon_id;
	 public String location_id;
	 public String coupon_code;
	 public String name;
	 public String details;
	 public String disclaimer; 
	 public String quantity;
	 public String universal;
	public String online_flag;
	public String online_url;
	public String online_contact;
	public String online_barcode;

	 public String getUniversal() {
		return universal;
	}
	public void setUniversal(String universal) {
		this.universal = universal;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String expiry_date;
	 public String activation_code;
	 
	 public String getId() {
		return id;
	}
	public void setId(String id) { 
		this.id = id;
	}

	public String getBuss_id() {
		return buss_id;
	}
	public void setBuss_id(String buss_id) {
		this.buss_id = buss_id;
	}
	 
	public String getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
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
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	public String getOnline_flag() {
		return online_flag;
	}

	public void setOnline_flag(String online_flag) {
		this.online_flag = online_flag;
	}

	public String getOnline_url() {
		return online_url;
	}

	public void setOnline_url(String online_url) {
		this.online_url = online_url;
	}

	public String getOnline_contact() {
		return online_contact;
	}

	public void setOnline_contact(String online_contact) {
		this.online_contact = online_contact;
	}

	public String getOnline_barcode() {
		return online_barcode;
	}

	public void setOnline_barcode(String online_barcode) {
		this.online_barcode = online_barcode;
	}
}
