package commons;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import api.RestExecution;
import utility.Utility;

public class CommonGetAPI extends RestExecution {

	private String logFileName = "common.log";
	private String logModuleName = "CommonGetAPI";
	
	
	public String getPlanDetails(int planId) {

		String apiURL = "postpaidplan/" + planId;
		apiURL = getAPIURL(apiURL);

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("status");

		String ans = "";
		if (status == 200) {

			JSONObject picodeJSONObject = jsonResponse.getJSONObject("postPaidPlan");
			String serviceName = picodeJSONObject.getString("serviceName");
			float offerprice = picodeJSONObject.getFloat("offerprice");
			int validity = picodeJSONObject.getInt("validity");
			String unitsOfValidity = picodeJSONObject.getString("unitsOfValidity");
			float newOfferPrice = picodeJSONObject.getFloat("newOfferPrice");
			
			ans = serviceName +":"+ offerprice +":"+ validity +":"+ unitsOfValidity +":"+ newOfferPrice;
		}

		return ans;
	}

	
	public int getPlanId(String planName) {

		String apiURL = getAPIURL("postpaidplan/all");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		// Fetching the desired value of a parameter
		int status = jsonResponse.getInt("status");
		int planId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("postpaidplanList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedpartnerName = jsonArray.getJSONObject(i).getString("name");
				if (receivedpartnerName.equalsIgnoreCase(planName)) {
					planId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if (planId == 0) {
			System.out.println("Plan details not found - " + planName);
			Utility.printLog(logFileName, logModuleName, "Plan details not found - ", planName);
		}

		return planId;
	}

	
	public int getStaffId(String staffUserName) {

		String apiURL = getAPIURL("staffuser/allActive");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("status");
		int staffId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("staffUserlist");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedStaffUserName = jsonArray.getJSONObject(i).getString("username");
				if (receivedStaffUserName.equalsIgnoreCase(staffUserName)) {
					staffId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if (staffId == 0) {
			System.out.println("Staff Username details not found - " + staffUserName);
			Utility.printLog(logFileName, logModuleName, "Staff Username not found - ", staffUserName);
		}

		return staffId;
	}
	
	public int getPartnerId(String partnerName) {

		String apiURL = getAPIURL("partner/all");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		// Fetching the desired value of a parameter
		int status = jsonResponse.getInt("status");

		int partnerId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("partnerlist");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedpartnerName = jsonArray.getJSONObject(i).getString("name");
				if (receivedpartnerName.equalsIgnoreCase(partnerName)) {
					partnerId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if (partnerId == 0) {
			System.out.println("Partner details not found - " + partnerName);
			Utility.printLog(logFileName, logModuleName, "Partner details not found - ", partnerName);
		}

		return partnerId;
	}
	
	
	public List<Integer> getServiceAreaIdList(String serviceAreaName) {

		String apiURL = getAPIURL("serviceArea/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");

		List<Integer> list = new ArrayList<Integer>();

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			if(serviceAreaName.equalsIgnoreCase("All")) {
				for (int i = 0; i < jsonArray.length(); i++) {
					list.add(jsonArray.getJSONObject(i).getInt("id"));
				}
			}else {
				for (int i = 0; i < jsonArray.length(); i++) {
					String tempServiceAreaName = jsonArray.getJSONObject(i).getString("name");	
					
					String serviceAreaNameList[] = serviceAreaName.split(",");										
					for(int j=0;j<serviceAreaNameList.length;j++) {
						if(tempServiceAreaName.equalsIgnoreCase(serviceAreaNameList[j])) {
							list.add(jsonArray.getJSONObject(i).getInt("id"));
						}
					}
				}
			}
		}
			
		return list;
	}

	
	
	public int getChargeId(String chargeName) {

		String apiURL = getAPIURL("charge/all");

		JSONObject jsonResponse = httpGet(apiURL);
		//String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("status");		
		int chargeId=0;
		
		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("chargelist");
			for (int i = 0; i < jsonArray.length(); i++) {
				String tempChargeName = jsonArray.getJSONObject(i).getString("name");
				if(tempChargeName.equalsIgnoreCase(chargeName)) {
					chargeId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}
		
		if(chargeId == 0) {
			System.out.println("Charge details not found - " + chargeName);
			Utility.printLog(logFileName, logModuleName, "Charge details not found - ", chargeName);
		}
		
		return chargeId;
	}
	
	
	public int getQosPolicyId(String qosPolicyName) {

		String apiURL = getAPIURL("qosPolicy/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		
		int qosPolicyId=0;
		
		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String tempQosPolicyName = jsonArray.getJSONObject(i).getString("name");
				if(tempQosPolicyName.equalsIgnoreCase(qosPolicyName)) {
					qosPolicyId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}
		
		if(qosPolicyId == 0) {
			System.out.println("Qos Policy details not found - " + qosPolicyName);
			Utility.printLog(logFileName, logModuleName, "Qos Policy details not found - ", qosPolicyName);
		}
		
		return qosPolicyId;
	}
	
	public int getTimeBasePolicyId(String timeBasePolicy) {

		String apiURL = getAPIURL( "timebasepolicy/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		int timeBasePolicyId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeType = jsonArray.getJSONObject(i).getString("name");
				if (timeBasePolicy.equalsIgnoreCase(receivedChargeType)) {
					timeBasePolicyId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if(timeBasePolicyId == 0) {
			System.out.println("TimeBasePolicy details not found - " + timeBasePolicy);
			Utility.printLog(logFileName, logModuleName, "TimeBasePolicy details not found - ", timeBasePolicy);
		}

		return timeBasePolicyId;
	}

	public List<Integer> getServiceIdList(String serviceName) {

		String apiURL = getAPIURL("planservice/all");

		JSONObject jsonResponse = httpGet(apiURL);

		int status = jsonResponse.getInt("status");		
		List<Integer> list = new ArrayList<Integer>();

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("serviceList");
			if(serviceName.equalsIgnoreCase("All")) {
				for (int i = 0; i < jsonArray.length(); i++) {
						list.add(jsonArray.getJSONObject(i).getInt("id"));
					}
			}else {
				for (int i = 0; i < jsonArray.length(); i++) {
					String receivedServiceName= jsonArray.getJSONObject(i).getString("name");					
					String serviceAreaNameList[] = serviceName.split(",");						
					for(int j=0;j<serviceAreaNameList.length;j++) {
						if(receivedServiceName.equalsIgnoreCase(serviceAreaNameList[j])) {				
							list.add(jsonArray.getJSONObject(i).getInt("id"));
						}
					}
				}				
			}
		}
		
		if (list.size() == 0) {
			System.out.println("Service details not found - " + serviceName);
			Utility.printLog(logFileName, logModuleName, "Service details not found - ", serviceName);
		}
		
		return list;
	}
	
	public int getTaxId(String taxName) {

		String apiURL = getAPIURL("taxes/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("status");
		int taxId = 0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("taxlist");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedCountryName = jsonArray.getJSONObject(i).getString("name");
				if (taxName.equalsIgnoreCase(receivedCountryName)) {
					taxId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}

		if (taxId == 0) {
			System.out.println("Tax details not found - " + taxName);
			Utility.printLog(logFileName, logModuleName, "Tax details not found - ", taxName);
		}

		return taxId;
	}

	
	
	public List<Integer> getBusinessUnitIdList(String businessUnitNames) {

		String apiURL = getAPIURL("businessUnit/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		
		List<Integer> list = new ArrayList<Integer>();

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			if(businessUnitNames.equalsIgnoreCase("All")) {
				for (int i = 0; i < jsonArray.length(); i++) {
						list.add(jsonArray.getJSONObject(i).getInt("id"));
					}
			}else {
				for (int i = 0; i < jsonArray.length(); i++) {
					String receivedBusinessUnitName= jsonArray.getJSONObject(i).getString("buname");
					if(businessUnitNames.equalsIgnoreCase(receivedBusinessUnitName)) {				
						list.add(jsonArray.getJSONObject(i).getInt("id"));
					}
				}				
			}
		}
		
		if (list.size() == 0) {
			System.out.println("BusinessUnit details not found - " + businessUnitNames);
			Utility.printLog(logFileName, logModuleName, "BusinessUnit details not found - ", businessUnitNames);
		}
			
		return list;
	}
	
	
	public List<Integer> getRegionIdList(String regionNames) {

		String apiURL = getAPIURL("region/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		List<Integer> regionList = new ArrayList<Integer>();
		
		//int branchId=0;

		if (status == 200) {
			
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			if(regionNames.equalsIgnoreCase("All")) {
				for (int i = 0; i < jsonArray.length(); i++) {
					regionList.add(jsonArray.getJSONObject(i).getInt("id"));
					}
			} else {
				for (int i = 0; i < jsonArray.length(); i++) {
					String receivedRegionName= jsonArray.getJSONObject(i).getString("rname");					
					String regionNameList[] = regionNames.split(",");						
					for(int j=0;j<regionNameList.length;j++) {
						if(receivedRegionName.equalsIgnoreCase(regionNameList[j])) {				
							regionList.add(jsonArray.getJSONObject(i).getInt("id"));
						}
					}
				}				
			}
		}
		
		if (regionList.size() == 0) {
			System.out.println("Region details not found - " + regionNames);
			Utility.printLog(logFileName, logModuleName, "Region details not found - ", regionNames);
		}
		
		return regionList;
	}

	
	
	public int getBranchId(String branchName) {

		String apiURL = getAPIURL("branchManagement/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");		
		int branchId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedBranchName = jsonArray.getJSONObject(i).getString("name");
				if(branchName.equalsIgnoreCase(receivedBranchName)) {
					branchId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}
		
		if (branchId == 0) {
			System.out.println("Branch details not found - " + branchName);
			Utility.printLog(logFileName, logModuleName, "Branch details not found - ", branchName);
		}
		
		return branchId;
	}
	
	public List<Integer> getBranchIdList(String branchNames) {

		String apiURL = getAPIURL("branchManagement/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		List<Integer> branchList = new ArrayList<Integer>();
		
		//int branchId=0;

		if (status == 200) {
			
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			if(branchNames.equalsIgnoreCase("All")) {
				for (int i = 0; i < jsonArray.length(); i++) {
					branchList.add(jsonArray.getJSONObject(i).getInt("id"));
					}
			} else {
				for (int i = 0; i < jsonArray.length(); i++) {
					String receivedBranchName= jsonArray.getJSONObject(i).getString("name");					
					String branchNameList[] = branchNames.split(",");						
					for(int j=0;j<branchNameList.length;j++) {
						if(receivedBranchName.equalsIgnoreCase(branchNameList[j])) {				
							branchList.add(jsonArray.getJSONObject(i).getInt("id"));
						}
					}
				}				
			}
			
		}
		
		if (branchList.size() == 0) {
			System.out.println("Branch details not found - " + branchNames);
			Utility.printLog(logFileName, logModuleName, "Branch details not found - ", branchNames);
		}
		
		return branchList;
	}

	public int getMunicipalityId(String municipalityName) {

		String apiURL = getAPIURL("pincode/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");		
		int municipalityId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedMunicipalityName = jsonArray.getJSONObject(i).getString("pincode");
				if(municipalityName.equalsIgnoreCase(receivedMunicipalityName)) {
					municipalityId = jsonArray.getJSONObject(i).getInt("pincodeid");
					break;
				}
			}
		}
		
		if (municipalityId == 0) {
			System.out.println("Municipality details not found - " + municipalityName);
			Utility.printLog(logFileName, logModuleName, "Municipality details not found - ", municipalityName);
		}
		return municipalityId;
	}
	
	
	public String getMasterDetailsFromMunicipalityId(int municipalityId) {

		String apiURL = "pincode/" + municipalityId;
		apiURL = getAPIURL(apiURL);

		JSONObject jsonResponse = httpGet(apiURL);
		//String response = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");

		String ans = null;
		if (status == 200) {
			
			JSONObject picodeJSONObject = jsonResponse.getJSONObject("data");
			int countryId = picodeJSONObject.getInt("countryId");
			int stateId = picodeJSONObject.getInt("stateId");
			int cityId = picodeJSONObject.getInt("cityId");
			
			ans = countryId + ":" + stateId + ":" + cityId;
		}
		
		return ans;
	}
	
	public int getProductId(String productName) {

		String apiURL = getAPIURL("product/getAllActiveProduct");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");		
		int productId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedProductName = jsonArray.getJSONObject(i).getString("name");
				if(productName.equalsIgnoreCase(receivedProductName)) {
					productId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}
		
		if (productId == 0) {
			System.out.println("Product details not found - " + productName);
			Utility.printLog(logFileName, logModuleName, "Product details not found - ", productName);
		}

		return productId;
	}
	
	
	public int getWarehouseId(String warehouseName) {

		String apiURL = getAPIURL("warehouseManagement/getAllActiveWarehouse");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");		
		int warehouseId=0;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedWarehouseName = jsonArray.getJSONObject(i).getString("name");
				if(warehouseName.equalsIgnoreCase(receivedWarehouseName)) {
					warehouseId = jsonArray.getJSONObject(i).getInt("id");
					break;
				}
			}
		}
		
		if (warehouseId == 0) {
			System.out.println("Warehouse details not found - " + warehouseName);
			Utility.printLog(logFileName, logModuleName, "Warehouse details not found - ", warehouseName);
		}

		return warehouseId;
	}
}
