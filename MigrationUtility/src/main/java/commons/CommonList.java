package commons;

import org.json.JSONArray;
import org.json.JSONObject;

import api.RestExecution;
import utility.Utility;

public class CommonList extends RestExecution {

	private String logFileName = "common.log";
	private String logModuleName = "CommonList";
	
	
	public String getCommonActualTime(String paymentMode) {

		String apiURL = getAPIURL("commonList/actual_time");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonPaymentMode = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeType = jsonArray.getJSONObject(i).getString("text");
				if (paymentMode.equalsIgnoreCase(receivedChargeType)) {
					commonPaymentMode = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonPaymentMode == null) {
			System.out.println("Common PaymentMode details not found - " + paymentMode);
			Utility.printLog(logFileName, logModuleName, "Common PaymentMode details not found - ", paymentMode);
		}
		return commonPaymentMode;
	}
	
	public String getCommonChargeType(String chargeType) {

		String apiURL = getAPIURL("commonList/generic/chargetype");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		String commonChargeType = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeType = jsonArray.getJSONObject(i).getString("text");
				if (chargeType.equalsIgnoreCase(receivedChargeType)) {
					commonChargeType = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonChargeType == null) {
			System.out.println("Common Chargetype details not found - " + chargeType);
			Utility.printLog(logFileName, logModuleName, "Common Chargetype details not found - ", chargeType);
		}

		return commonChargeType;
	}
	
	public String getCommonChargeCategory(String chargeCategory) {

		String apiURL = getAPIURL("commonList/chargeCategory");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonChargeCategory = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeCategory = jsonArray.getJSONObject(i).getString("text");
				if (chargeCategory.equalsIgnoreCase(receivedChargeCategory)) {
					commonChargeCategory = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonChargeCategory == null) {
			System.out.println("Common ChargeCategory details not found - " + chargeCategory);
			Utility.printLog(logFileName, logModuleName, "Common ChargeCategory details not found - ", chargeCategory);
		}

		return commonChargeCategory;
	}
	
	public String getCommonPlanGroup(String planGroup) {

		String apiURL = getAPIURL("commonList/planGroup");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonPlanGroup = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedPlanCategory = jsonArray.getJSONObject(i).getString("text");
				if (planGroup.equalsIgnoreCase(receivedPlanCategory)) {
					commonPlanGroup = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonPlanGroup == null) {
			System.out.println("Common Plan-Group details not found - " + planGroup);
			Utility.printLog(logFileName, logModuleName, "Common Plan-Group details not found - ", planGroup);
		}
		return commonPlanGroup;
	}
	
	public String getCommonPlanAccessibility(String accessibility) {

		String apiURL = getAPIURL("commonList/accessibility");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonPlanGroup = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedPlanCategory = jsonArray.getJSONObject(i).getString("text");
				if (accessibility.equalsIgnoreCase(receivedPlanCategory)) {
					commonPlanGroup = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonPlanGroup == null) {
			System.out.println("Common Plan-Accessibility details not found - " + accessibility);
			Utility.printLog(logFileName, logModuleName, "Common Plan-Accessibility details not found - ", accessibility);
		}
		return commonPlanGroup;
	}
	
	public String getCommonPaymentMode(String paymentMode) {

		String apiURL = getAPIURL("commonList/paymentMode");

		JSONObject jsonResponse = httpGet(apiURL);
		// String ans = jsonResponse.toString(4);

		int status = jsonResponse.getInt("responseCode");
		String commonPaymentMode = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedChargeType = jsonArray.getJSONObject(i).getString("text");
				if (paymentMode.equalsIgnoreCase(receivedChargeType)) {
					commonPaymentMode = jsonArray.getJSONObject(i).getString("value");
				}
			}
		}

		if (commonPaymentMode == null) {
			System.out.println("Common PaymentMode details not found - " + paymentMode);
			Utility.printLog(logFileName, logModuleName, "Common PaymentMode details not found - ", paymentMode);
		}
		return commonPaymentMode;
	}
	
	
}
