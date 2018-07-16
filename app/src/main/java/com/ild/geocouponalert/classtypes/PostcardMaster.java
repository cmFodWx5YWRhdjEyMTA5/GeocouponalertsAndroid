package com.ild.geocouponalert.classtypes;

import java.util.List;

public class PostcardMaster {
	
	public String id;
	public String activation_code; 
	public String activation_status;
	public String activation_process_status;
	public String user_pin;
	public String isAlreadyActiveCard;
	public String activation_date;
	public String expiry_date;
	public String agreement_text;
	public String activation_screen;
	
	public String getActivation_screen() {
		return activation_screen;
	}
	public void setActivation_screen(String activation_screen) {
		this.activation_screen = activation_screen;
	}
	public String getId() {  
		return id;
	}
	public void setId(String id) { 
		this.id = id;
	}
	public String getActivation_code() {
		return activation_code;
	}
	public void setActivation_code(String activation_code) {
		this.activation_code = activation_code;
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
	public String getUser_pin() {
		return user_pin;
	}
	public void setUser_pin(String user_pin) {
		this.user_pin = user_pin;
	}
	public String getIsAlreadyActiveCard() {
		return isAlreadyActiveCard;
	}
	public void setIsAlreadyActiveCard(String isAlreadyActiveCard) {
		this.isAlreadyActiveCard = isAlreadyActiveCard;
	}
	public String getActivation_date() {
		return activation_date;
	}
	public void setActivation_date(String activation_date) {
		this.activation_date = activation_date;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getAgreement_text() {
		return agreement_text;
	}
	public void setAgreement_text(String agreement_text) {
		this.agreement_text = agreement_text;
	}	
}