package com.ild.geocouponalert.classtypes;

import java.util.List;

public class BusinessCouponLocation {
	
	public String buss_id;
	public String buss_name;
	public String banner_img;
	public List<CouponMaster> coupons_detail;
	public List<BusinessLocationMaster> location_details;
	public List<CouponRedeemMaster> redeem_coupons_detail;
	public String all_coupon_location_same;
	
	
	public String getAll_coupon_location_same() {
		return all_coupon_location_same;
	}
	public void setAll_coupon_location_same(String all_coupon_location_same) {
		this.all_coupon_location_same = all_coupon_location_same;
	}
	public String getBuss_name() {
		return buss_name;
	}
	public void setBuss_name(String buss_name) {
		this.buss_name = buss_name;
	}
	public String getBanner_img() {
		return banner_img;
	}
	public void setBanner_img(String banner_img) { 
		this.banner_img = banner_img;
	}
	public List<CouponRedeemMaster> getRedeem_coupons_detail() {
		return redeem_coupons_detail;
	}
	public void setRedeem_coupons_detail(
			List<CouponRedeemMaster> redeem_coupons_detail) {
		this.redeem_coupons_detail = redeem_coupons_detail;
	}
	public String getBuss_id() {
		return buss_id;
	}
	public void setBuss_id(String buss_id) {
		this.buss_id = buss_id;
	}
	public List<CouponMaster> getCoupons_detail() {
		return coupons_detail;
	}
	public void setCoupons_detail(List<CouponMaster> coupons_detail) {
		this.coupons_detail = coupons_detail;
	}
	public List<BusinessLocationMaster> getLocation_details() {
		return location_details;
	}
	public void setLocation_details(List<BusinessLocationMaster> location_details) {
		this.location_details = location_details;
	}	
	 
	 

}
