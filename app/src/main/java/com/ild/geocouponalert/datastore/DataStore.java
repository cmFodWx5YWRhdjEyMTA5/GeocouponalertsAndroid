package com.ild.geocouponalert.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.ild.geocouponalert.classtypes.BusinessCouponLocation;
import com.ild.geocouponalert.classtypes.BusinessLocationMaster;
import com.ild.geocouponalert.classtypes.BusinessMaster;
import com.ild.geocouponalert.classtypes.BusinessSelectedMaster;
import com.ild.geocouponalert.classtypes.Category;
import com.ild.geocouponalert.classtypes.FavoriteAlertLocationMerchant;
import com.ild.geocouponalert.classtypes.FavoriteAlertMerchant;
import com.ild.geocouponalert.classtypes.FundraiserMaster;
import com.ild.geocouponalert.classtypes.PostcardMaster;
import com.ild.geocouponalert.classtypes.UserMaster;
import com.ild.geocouponalert.classtypes.CouponMaster;

public class DataStore {

	private static DataStore instance; 
	private DataStore() {
		
	}

	/*Instance of DataStore*/
	public static synchronized DataStore getInstance() {
		if (instance == null) {
			instance = new DataStore(); 
		}
		return instance;
	} 
	
	//--------------------------------------------------------------------------------------
	private HashMap<String, FundraiserMaster> m_FundraiserMap = new HashMap<String, FundraiserMaster>();
	private HashMap<String, BusinessMaster> m_BusinessMap = new HashMap<String, BusinessMaster>();
	private HashMap<String, BusinessSelectedMaster> m_BusinessSelectedMap = new HashMap<String, BusinessSelectedMaster>();
	private HashMap<String, BusinessLocationMaster> m_BusinessMultipleLocationMap = new HashMap<String, BusinessLocationMaster>();
	private HashMap<String,CouponMaster> m_CouponMap = new HashMap<String, CouponMaster>();
	private HashMap<String,UserMaster> m_UserMap = new HashMap<String, UserMaster>();
	private HashMap<String,Category> m_CatMap = new HashMap<String, Category>();
	private HashMap<String,BusinessCouponLocation>  m_CouponLocationByBussIDMap = new HashMap<String, BusinessCouponLocation>();
	public Boolean isNotificationLocationReminderEnabled = false;
	public Boolean isNotificationExpirationAlertEnabled = false;
	public Boolean isNotificationEmailEnabled = false;
	public Boolean isNotificationSMSEnabled = false;
	public Boolean isNotificationPushEnabled = false; 
	private HashMap<String, PostcardMaster> m_PostcardMap = new HashMap<String, PostcardMaster>();
	private HashMap<String, FavoriteAlertMerchant> m_FavoriteAlertMerchantMap = new HashMap<String, FavoriteAlertMerchant>();
	private HashMap<String, FavoriteAlertLocationMerchant> m_FavoriteAlertMerchantEditMap = new HashMap<String, FavoriteAlertLocationMerchant>();
	Map<String, BusinessMaster> Sortedm_BusinessMapAsc;
	public static boolean ASC = false;
	//--------------------------------------------------------------------------------------

	public void addFavoriteAlertMerchantEdit(List<FavoriteAlertLocationMerchant> lstFund) {
		m_FavoriteAlertMerchantEditMap.clear();
		if (lstFund != null && lstFund.size() > 0) {
			for (FavoriteAlertLocationMerchant fund : lstFund) {
				m_FavoriteAlertMerchantEditMap.put(fund.getUser_id(), fund);
			}
		}
	}

	public List<FavoriteAlertLocationMerchant> getFavoriteAlertMerchantEdit(){
		List<FavoriteAlertLocationMerchant> lstFund = new ArrayList<FavoriteAlertLocationMerchant>();
		lstFund.addAll(m_FavoriteAlertMerchantEditMap.values());
		return lstFund;
	}

	public void addFavoriteAlertMerchant(List<FavoriteAlertMerchant> lstFund) {
		m_FavoriteAlertMerchantMap.clear();
		if (lstFund != null && lstFund.size() > 0) {
			for (FavoriteAlertMerchant fund : lstFund) {
				m_FavoriteAlertMerchantMap.put(fund.getUser_id(), fund);
			}
		}
	}

	public List<FavoriteAlertMerchant> getFavoriteAlertMerchant(){
		List<FavoriteAlertMerchant> lstFund = new ArrayList<FavoriteAlertMerchant>();
		lstFund.addAll(m_FavoriteAlertMerchantMap.values());
		return lstFund;
	}

	/*Add Fundraiser*/
	public void addFundraiser(List<FundraiserMaster> lstFund) {
		m_FundraiserMap.clear();
		if (lstFund != null && lstFund.size() > 0) {
			for (FundraiserMaster fund : lstFund) {
				m_FundraiserMap.put(fund.getId(), fund);
			}
		}  
	}
	
	/*Get Fundraiser Details*/
	
	public List<FundraiserMaster> getFundraiser(){
		List<FundraiserMaster> lstFund = new ArrayList<FundraiserMaster>();
		lstFund.addAll(m_FundraiserMap.values());
		return lstFund;
	} 
	
	/*Get Fundraiser By Fundraiser ID */
	public FundraiserMaster GetFundraiserByFundraiserID(String id) {
		FundraiserMaster fund = null;
		if (m_FundraiserMap.containsKey(id)) {
			fund = m_FundraiserMap.get(id);
		}

		return fund;
	}
	
	/*Add Business*/
	
	public void addBusiness(List<BusinessMaster> lstBusiness) {
		m_BusinessMap.clear();
		m_BusinessSelectedMap.clear();
		if (lstBusiness != null && lstBusiness.size() > 0) {
			for (BusinessMaster business : lstBusiness) {
				m_BusinessMap.put(business.getBuss_id(), business);
			}
			Sortedm_BusinessMapAsc = sortByComparator2(m_BusinessMap, ASC);
		}  
	} 
		 
	/*Get All Business*/
	public List<BusinessMaster> getBusiness(){
		List<BusinessMaster> lstBusiness = new ArrayList<BusinessMaster>();
		lstBusiness.addAll(Sortedm_BusinessMapAsc.values());
		return lstBusiness;
	} 
	
	
	
	
	/* Get Business Details */
	
	public BusinessMaster GetBusinessdetails(String buss_id) {
		BusinessMaster business = null;
		if (m_BusinessMap.containsKey(buss_id)) {
			business = m_BusinessMap.get(buss_id);
		}

		return business;
	}
	
	
	
	
	
	/*Add Selected Business*/
	
	public void addSelectedBusiness(List<BusinessMaster> lstSelectedBusiness) {
		m_BusinessMap.clear();
		if (lstSelectedBusiness != null && lstSelectedBusiness.size() > 0) {
			for (BusinessMaster sel_business : lstSelectedBusiness) {
				m_BusinessMap.put(sel_business.getBuss_id(), sel_business);
			} 
			Sortedm_BusinessMapAsc = sortByComparator2(m_BusinessMap, ASC);
		}  
	} 
	
	/*Get All Selected Business*/
	
	public List<BusinessMaster> getSelectedBusiness(){
		List<BusinessMaster> lstSelectedBusiness = new ArrayList<BusinessMaster>();
		lstSelectedBusiness.addAll(Sortedm_BusinessMapAsc.values());
		return lstSelectedBusiness;
	} 
	
	
	/*Add User Details*/
	public void addUserDetails(List<UserMaster> lstUser) {
		if (lstUser != null && lstUser.size() > 0) {
			for (UserMaster user : lstUser) {
				m_UserMap.put(user.getId(), user);
			}
		}  
	}
	
	/*Get User Details*/
	
	public List<UserMaster> getUserDetails(){
		List<UserMaster> lstUser = new ArrayList<UserMaster>();
		lstUser.addAll(m_UserMap.values());
		return lstUser;
	} 
	
/*Add Business multiple Location*/
	
	public void addBusinessMultipleLocation(List<BusinessLocationMaster> lstBusinessLocation) {
		m_BusinessMultipleLocationMap.clear();
		if (lstBusinessLocation != null && lstBusinessLocation.size() > 0) {
			for (BusinessLocationMaster business_location : lstBusinessLocation) {
				m_BusinessMultipleLocationMap.put(business_location.getId(), business_location);
			}
		}  
	}
	
	/*Get All Location of a Business*/
	
	public List<BusinessLocationMaster> getBusinessMultipleLocation(){
		List<BusinessLocationMaster> lstBusinessLocation = new ArrayList<BusinessLocationMaster>();
		lstBusinessLocation.addAll(m_BusinessMultipleLocationMap.values());
		return lstBusinessLocation;
	}

	/*Get Location By location ID */
	public BusinessLocationMaster GetLocationByLocationid(String locationid) {
		BusinessLocationMaster locatuion = null;
		if (m_BusinessMultipleLocationMap.containsKey(locationid)) {
			locatuion = m_BusinessMultipleLocationMap.get(locationid);
		}

		return locatuion;
	}
	
	/*Add Single Business*/
	public void addBusiness(BusinessMaster business) {

		if (business != null ) {
			
					if (m_BusinessMap != null) {

					m_BusinessMap.put(business.buss_id, business);
				
			}
				
		}

	}
	
	/*Add single Selected Business*/
	
	public void addselBusiness(BusinessSelectedMaster sel_business) {

		if (sel_business != null ) {
			

				if (m_BusinessSelectedMap != null) {

					m_BusinessSelectedMap.put(sel_business.buss_id, sel_business);
				
			}
				
		}

	}
	
	/*Get Business By Business ID */
	public BusinessMaster GetBusinessByBusinessId(String buss_id) {
		BusinessMaster business = null;
		if (m_BusinessMap.containsKey(buss_id)) {
			business = m_BusinessMap.get(buss_id);
		}

		return business;
	}
	
	/*Get Selected Business By Business ID */
	public BusinessSelectedMaster GetSelBusinessByBusinessId(String sel_buss_id) {
		BusinessSelectedMaster sel_business = null;
		if (m_BusinessSelectedMap.containsKey(sel_buss_id)) {
			sel_business = m_BusinessSelectedMap.get(sel_buss_id);
		}

		return sel_business;
	}
	
	/*Add Coupon*/
	public void addCouponLocation(List<BusinessCouponLocation> lstCouponLocation) {
		m_CouponLocationByBussIDMap.clear();
		if (lstCouponLocation != null && lstCouponLocation.size() > 0) {
			for (BusinessCouponLocation couponLocation : lstCouponLocation) {
				m_CouponLocationByBussIDMap.put(couponLocation.getBuss_id(), couponLocation);
			}
		}  
	}
	
	/*Get Coupon Details*/
	
	public List<BusinessCouponLocation> getCouponLocation(){
		List<BusinessCouponLocation> lstCouponLocation = new ArrayList<BusinessCouponLocation>();
		lstCouponLocation.addAll(m_CouponLocationByBussIDMap.values());
		return lstCouponLocation;
	} 
	
	/*Get Coupon By Coupon ID */
	public CouponMaster GeCouponByCouponID(String coupon_id) {
		CouponMaster coupon = null;
		if (m_CouponMap.containsKey(coupon_id)) {
			coupon = m_CouponMap.get(coupon_id);
		} 

		return coupon;
	}
	
	/*Remove available Business*/
	public void removeBusiness(String buss_id){
		Iterator<Entry<String, BusinessMaster>> iter=m_BusinessMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, BusinessMaster> entry=iter.next();
			
			if(buss_id.equalsIgnoreCase(entry.getValue().buss_id)){
				iter.remove();
				
			}
		}
		
	}
	
	/*Remove Selected Business*/
	public void removeSelBusiness(String sel_buss_id){
		Iterator<Entry<String, BusinessSelectedMaster>> iter=m_BusinessSelectedMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, BusinessSelectedMaster> entry=iter.next();
			
			if(sel_buss_id.equalsIgnoreCase(entry.getValue().buss_id)){
				iter.remove();
				
			}
		}
		
	}
	
	public void addPostcard(List<PostcardMaster> lstFund) {
		m_PostcardMap.clear();
		if (lstFund != null && lstFund.size() > 0) {
			for (PostcardMaster fund : lstFund) {
				m_PostcardMap.put(fund.getId(), fund);
			}
		}  
	}
	
	/*Get Fundraiser Details*/
	
	public List<PostcardMaster> getPostcard(){
		List<PostcardMaster> lstFund = new ArrayList<PostcardMaster>();
		lstFund.addAll(m_PostcardMap.values());
		return lstFund;
	} 
	
	public void clearDataStore(){
					
			m_FundraiserMap.clear();
			m_BusinessMap.clear();
			m_BusinessSelectedMap.clear();
			m_BusinessMultipleLocationMap.clear();
			m_CouponMap.clear();
		
	}
	
	public void clearCouponList(){
		
		m_CouponMap.clear();
	
	}
	
	private static Map<String, BusinessMaster> sortByComparator2(Map<String, BusinessMaster> unsortMap, final boolean order)
    {

        List<Entry<String, BusinessMaster>> list = new LinkedList<Entry<String, BusinessMaster>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, BusinessMaster>>()
        {
            public int compare(Entry<String, BusinessMaster> o1,
                    Entry<String, BusinessMaster> o2)
            {
        		 
            	String reordered1 = o2.getValue().name;
            	String reordered12 = reordered1.replaceAll("\\s+","");
            	String reordered2 = o1.getValue().name;
            	String reordered21 = reordered2.replaceAll("\\s+","");
	       		return reordered21.compareToIgnoreCase(reordered12);
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, BusinessMaster> sortedMap = new LinkedHashMap<String, BusinessMaster>();
        for (Entry<String, BusinessMaster> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        } 

        return sortedMap;
    }
	
	
	

}
