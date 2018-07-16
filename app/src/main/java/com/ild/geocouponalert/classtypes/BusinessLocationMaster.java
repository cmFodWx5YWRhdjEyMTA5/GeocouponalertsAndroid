package com.ild.geocouponalert.classtypes;

public class BusinessLocationMaster {
	
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
	 
	 public String user_lat;
	 public String user_long;
	 public String buss_loc_lat;
	 public String buss_loc_long;
	 public String distance;
	 
	 public String geo_alert_status;
	 
	 
	 public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id; 
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
	
	public String getUser_lat() {
		return user_lat;
	}

	public void setUser_lat(String user_lat) {
		this.user_lat = user_lat;
	}

	public String getUser_long() {
		return user_long;
	}

	public void setUser_long(String user_long) {
		this.user_long = user_long;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getBuss_loc_lat() {
		return buss_loc_lat;
	}

	public void setBuss_loc_lat(String buss_loc_lat) {
		this.buss_loc_lat = buss_loc_lat;
	}

	public String getBuss_loc_long() {
		return buss_loc_long;
	}

	public void setBuss_loc_long(String buss_loc_long) {
		this.buss_loc_long = buss_loc_long;
	}

}
