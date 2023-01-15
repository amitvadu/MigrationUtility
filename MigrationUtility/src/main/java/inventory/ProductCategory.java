package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import api.ReadData;
import api.RestExecution;
import utility.Utility;

public class ProductCategory extends RestExecution {

	private void createProductCategory(Map<String, String> productCategoryDetails) {

		String logFileName = "inventory.log";
		String logModuleName = "CreateProductCategory";

		String apiURL = "productCategory/save";
		apiURL = getAPIURL(apiURL);
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String apiBody = getProductCategoryJson(productCategoryDetails);
		Utility.printLog(logFileName, logModuleName, "Request Body", apiBody);

		if (!apiBody.equals(null)) {
			JSONObject JSONResponseBody = httpPost(apiURL, apiBody);
			String response = JSONResponseBody.toString(4);
			Utility.printLog(logFileName, logModuleName, "Response", response);

			// Fetching the desired value of a parameter
			int status = JSONResponseBody.getInt("responseCode");

			if (status == 200) {
				String message = "New ProductCategory is added successfully - " + productCategoryDetails.get("Name");
				System.out.println(message);
				Utility.printLog("execution.log", logModuleName, "Success", message);

			} else if (status == 406) {
				String error = JSONResponseBody.getString("responseMessage") + " - " + productCategoryDetails.get("Name");
				System.out.println(error);
				Utility.printLog("execution.log", logModuleName, "Already Exist", error);
			}
		}
	}

	public void createProductCategory(List<Map<String, String>> productCategoryMapList) {

		for (int i = 0; i < productCategoryMapList.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();
			map = productCategoryMapList.get(i);
			createProductCategory(map);
		}
	}

	public List<Map<String, String>> readUniqueProductCategoryList() {

		String sheetName = "ProductCategory";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getInventoryDataSheet(sheetName);

		Map<String, String> cellValue = new HashMap<String, String>();
		List<Map<String, String>> productCategoryMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sheetMap.size(); i++) {

			Map<String, String> valuemap = new HashMap<String, String>();
			cellValue = sheetMap.get(i);

			if (!"".equals(cellValue.get("Name"))) {

				valuemap.put("Name", cellValue.get("Name"));
				valuemap.put("ProductId", cellValue.get("ProductId"));
				valuemap.put("UOM", cellValue.get("UOM"));
				valuemap.put("HasMac", cellValue.get("HasMac"));
				valuemap.put("HasSerial", cellValue.get("HasSerial"));
				valuemap.put("Type", cellValue.get("Type"));
				valuemap.put("Status", cellValue.get("Status"));
				productCategoryMapList.add(valuemap);
			}
		}
		return productCategoryMapList;
	}

	@SuppressWarnings("unchecked")
	private String getProductCategoryJson(Map<String, String> productCategoryDetails) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject productCategoryJsonObject = new org.json.simple.JSONObject();

			// ReadData readData = new ReadData();
			// productCategoryJsonObject =
			// readData.readJSONFile("CreateProductCategory.json");

			productCategoryJsonObject.put("id", null);
			productCategoryJsonObject.put("name", productCategoryDetails.get("Name"));
			productCategoryJsonObject.put("productId", productCategoryDetails.get("ProductId"));
			productCategoryJsonObject.put("unit", productCategoryDetails.get("UOM"));
			productCategoryJsonObject.put("hasMac", Boolean.valueOf(productCategoryDetails.get("HasMac")));
			productCategoryJsonObject.put("hasSerial", Boolean.valueOf(productCategoryDetails.get("HasSerial")));
			productCategoryJsonObject.put("type", productCategoryDetails.get("Type"));
			productCategoryJsonObject.put("status", productCategoryDetails.get("Status").toUpperCase());

			jsonString = productCategoryJsonObject.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}
