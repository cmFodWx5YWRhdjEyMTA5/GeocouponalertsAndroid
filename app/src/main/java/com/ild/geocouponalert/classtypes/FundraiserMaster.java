package com.ild.geocouponalert.classtypes;

import java.util.List;

public class FundraiserMaster {
	
	 public String id;
	 public  String fundraiser_id; 
	 public String coor_id;
	 public String name; 
	 public String logo_img;
	 public String address1;
	 public String address2; 
	 public String city;
	 public String state_initial;
	 public String state;
	 public String zip;
	 public String phone;
	 public String fax;
	 public String email;
	 public String website;
	 public String colour_code;
	 //public String active_date;
	 public String postcard_code;
	 public String user_id;
	 public String activation_date;
	 public String expiry_date;
	 public String activation_code;
	 public String activation_status;
	 public String activation_process_status;
	 public String user_pin;
	 public String push_notification;
	 public String location_reminders;
	 
	public String isAlreadyActiveCard;
	 public String agreement_text;
	 public String no_redeem_coupon;
	 public String no_of_remaining_coupon;
	 
	 public List<BusinessLocationMaster> buss_details;
	
	 
	 public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	public String getNo_of_remaining_coupon() {
		return no_of_remaining_coupon;
	}
	public void setNo_of_remaining_coupon(String no_of_remaining_coupon) {
		this.no_of_remaining_coupon = no_of_remaining_coupon;
	}
		public String getPush_notification() {
		return push_notification;
	}
	public void setPush_notification(String push_notification) {
		this.push_notification = push_notification;
	}
	public String getNo_redeem_coupon() {
		return no_redeem_coupon;
	}
	public void setNo_redeem_coupon(String no_redeem_coupon) {
		this.no_redeem_coupon = no_redeem_coupon;
	}
	
		
		public String getExpiry_date() {
			return expiry_date;
		}
		public void setExpiry_date(String expiry_date) {
			this.expiry_date = expiry_date;
		}
	 
	public String getUser_pin() {
		return user_pin;
	}
	public void setUser_pin(String user_pin) {
		this.user_pin = user_pin;
	}
	public String getActivation_status() {
		return activation_status;
	}
	public void setActivation_status(String activation_status) {
		this.activation_status = activation_status;
	}
	public String getActivation_process_status() {
		return activation_process_status;
	}
	public void setActivation_process_status(String activation_process_status) {
		this.activation_process_status = activation_process_status;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFundraiser_id() {
		return fundraiser_id;
	}
	public void setFundraiser_id(String fundraiser_id) {
		this.fundraiser_id = fundraiser_id;
	}
	public String getCoor_id() {
		return coor_id;
	}
	public void setCoor_id(String coor_id) {
		this.coor_id = coor_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo_img() {
		return logo_img;
	}
	public void setLogo_img(String logo_img) {
		this.logo_img = logo_img;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState_initial() {
		return state_initial;
	}
	public void setState_initial(String state_initial) {
		this.state_initial = state_initial;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
    public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getColour_code() {
		return colour_code;
	}
	public void setColour_code(String colour_code) {
		this.colour_code = colour_code;
	}
	/*public String getActive_date() {
		return active_date;
	}
	public void setActive_date(String active_date) {
		this.active_date = active_date;
	}*/
	public String getPostcard_code() {
		return postcard_code;
	}
	public void setPostcard_code(String postcard_code) {
		this.postcard_code = postcard_code;
	}
	public String getActivation_date() {
		return activation_date;
	}
	public void setActivation_date(String activation_date) {
		this.activation_date = activation_date;
	}
	public String getActivation_code() {
		return activation_code;
	}
	public void setActivation_code(String activation_code) {
		this.activation_code = activation_code;
	}
	public String getIsAlreadyActiveCard() {
		return isAlreadyActiveCard;
	}
	public void setIsAlreadyActiveCard(String isAlreadyActiveCard) {
		this.isAlreadyActiveCard = isAlreadyActiveCard;
	}
	
	public String getAgreement_text() {
		return agreement_text;
	}
	public void setAgreement_text(String agreement_text) {
		this.agreement_text = agreement_text;
	}
	
	public String getLocation_reminders() {
		return location_reminders;
	}
	public void setLocation_reminders(String location_reminders) {
		this.location_reminders = location_reminders;
	}
	
	public List<BusinessLocationMaster> getBusiness_detail() {
		return buss_details;
	}
	public void setBusiness_detail(List<BusinessLocationMaster> business_detail) {
		this.buss_details = business_detail;
	}

	public List<BusinessLocationMaster> getBuss_details() {
		return buss_details;
	}
	public void setBuss_details(List<BusinessLocationMaster> buss_details) {
		this.buss_details = buss_details;
	}
	
}
