package com.ild.geocouponalert.classtypes;

import java.util.List;

public class BusinessMaster {
	
	 public String id;
	 public String buss_id;
	 public String name;
	 public String logo_img; 
	 public String contact_name;
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
	 public String cat_id;
	 public String banner_img;
	 private boolean selected;
	 public String no_of_coupon;
	 public List<Category> cat_details;
	 public List<BusinessLocationMaster> geofence_location_details;
	 public String isNewBusiness;
	 public String isFavourite;
	 
	 public String getIsNewBusiness() {
		return isNewBusiness;
	}

	public void setIsNewBusiness(String isNewBusiness) {
		this.isNewBusiness = isNewBusiness;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
	 
	 public String getNo_of_coupon() {
		return no_of_coupon;
	}

	public void setNo_of_coupon(String no_of_coupon) {
		this.no_of_coupon = no_of_coupon;
	}

	public List<Category> getCategory_details() {
		return cat_details;
	}

	public void setCategory_details(List<Category> cat_details) {
		this.cat_details = cat_details;
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

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
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

	

	public String getBuss_id() {
		return buss_id;
	}

	public void setBuss_id(String buss_id) {
		this.buss_id = buss_id;
	}
	
	public boolean isSelected() {
		  return selected;
		 }
		 
		 public void setSelected(boolean selected) {
		  this.selected = selected;
		 }


	public String getIsFavorite() {
		return isFavourite;
	}

	public void setIsFavorite(String isFavorite) {
		this.isFavourite = isFavorite;
	}


}
