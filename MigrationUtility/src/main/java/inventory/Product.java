package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import commons.CommonGetAPI;
import utility.Utility;

public class Product extends RestExecution {

	private String logFileName = "inventory.log";
	private String logModuleName = "CreateProduct";

	private void createProduct(Map<String, String> productDetails) {

		String apiURL = getAPIURL("product/save");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getProductJson(productDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		if (!apiBody.equals(null)) {
			JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			int status = JSONResponseBody.getInt("responseCode");

			if (status == 200) {
				String message = "New Product is added successfully - " + productDetails.get("Name");
				System.out.println(message);
				Utility.printLog("execution.log", logModuleName, "Success", message);

			} else if (status == 406) {
				String error = JSONResponseBody.getString("responseMessage") + " - " + productDetails.get("Name");
				System.out.println(error);
				Utility.printLog("execution.log", logModuleName, "Already Exist", error);
			}
		}
	}

	public void createProduct(List<Map<String, String>> productMapList) {

		for (int i = 0; i < productMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = productMapList.get(i);
			Utility.printLog(logFileName, logModuleName, "Sheet Raw Data", map.toString());
			createProduct(map);
		}
	}

	public List<Map<String, String>> readUniqueProductList() {

		String sheetName = "Product";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getInventoryDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> productMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);
			
			String name = cellValue.get("Name");
			if ((!"".equals(name)) && (name != null)) {

				valuemap.put("Name", cellValue.get("Name"));
				valuemap.put("ProductId", cellValue.get("ProductId"));
				valuemap.put("LedgerId", cellValue.get("LedgerId"));
				valuemap.put("Description", cellValue.get("Description"));
				valuemap.put("Category", cellValue.get("Category"));
				valuemap.put("TotalInPorts", cellValue.get("TotalInPorts"));
				valuemap.put("TotalOutPorts", cellValue.get("TotalOutPorts"));
				valuemap.put("Charge", cellValue.get("Charge"));
				valuemap.put("RefundAmount", cellValue.get("RefundAmount"));
				valuemap.put("WarrentyTime", cellValue.get("WarrentyTime"));
				valuemap.put("WarrentyTimeUnit", cellValue.get("WarrentyTimeUnit"));
				valuemap.put("Status", cellValue.get("Status"));				
				productMapList.add(valuemap);
			}
		}
		return productMapList;
	}

	@SuppressWarnings("unchecked")
	private String getProductJson(Map<String, String> productDetails) {

		String jsonString = "";

		try {

			org.json.simple.JSONObject productJsonObject = new org.json.simple.JSONObject();

			// ReadData readData = new ReadData();
			// productJsonObject = readData.readJSONFile("CreateProduct.json");

			productJsonObject.put("id", null);
			productJsonObject.put("name", productDetails.get("Name"));
			productJsonObject.put("productId", productDetails.get("ProductId"));
			productJsonObject.put("navLedgerId", productDetails.get("LedgerId"));
			productJsonObject.put("description", productDetails.get("Description"));

			String result = getProductCategoryIdAndType(productDetails.get("Category"));
			if (result != null) {

				String ans[] = result.split(":");
				int productCategoryId = Integer.parseInt(ans[0]);
				String type = ans[1];

				productJsonObject.put("productCategory", productCategoryId);
				if (type.equalsIgnoreCase("NetworkBind") || type.equalsIgnoreCase("CustomerBind")) {

					productJsonObject.put("totalInPorts", Integer.parseInt(productDetails.get("TotalInPorts")));
					productJsonObject.put("totalOutPorts", Integer.parseInt(productDetails.get("TotalOutPorts")));
					productJsonObject.put("availableInPorts", Integer.parseInt(productDetails.get("TotalInPorts")));
					productJsonObject.put("availableOutPorts", Integer.parseInt(productDetails.get("TotalOutPorts")));
				}
			}

			CommonGetAPI commonGetAPI = new CommonGetAPI();
			String charge = productDetails.get("Charge");
			if ("".equals(charge)) {
				productJsonObject.put("chargeId", charge);
			} else {
				int chargeId = commonGetAPI.getChargeId(charge);
				if (chargeId != 0) {
					productJsonObject.put("chargeId", chargeId);
				}
			}

			float refundAmount = Utility.formattedFloatDecimalNumber(productDetails.get("RefundAmount"));
			productJsonObject.put("refundAmount", refundAmount);
			productJsonObject.put("expiryTime", Integer.parseInt(productDetails.get("WarrentyTime")));
			productJsonObject.put("expiryTimeUnit", productDetails.get("WarrentyTimeUnit"));
			productJsonObject.put("status", productDetails.get("Status").toUpperCase());

			jsonString = productJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	public String getProductCategoryIdAndType(String productCategoryName) {

		String apiURL = getAPIURL("productCategory/all");

		JSONObject jsonResponse = httpGet(apiURL);
		int status = jsonResponse.getInt("responseCode");
		String result = null;

		if (status == 200) {
			JSONArray jsonArray = jsonResponse.getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				String receivedProductCategoryName = jsonArray.getJSONObject(i).getString("name");
				if (receivedProductCategoryName.equalsIgnoreCase(productCategoryName)) {
					int Id = jsonArray.getJSONObject(i).getInt("id");
					String type = jsonArray.getJSONObject(i).getString("type");
					result = Id + ":" + type;
					break;
				}
			}
		}

		if (result == null) {
			System.out.println("ProductCategory details not found - " + productCategoryName);
			Utility.printLog(logFileName, logModuleName, "ProductCategory details not found - ", productCategoryName);
		}
		return result;
	}

}
