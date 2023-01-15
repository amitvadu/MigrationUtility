package api;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import utility.Constant;
import utility.ReadWriteExcelFile;

public class ReadData {
	
	
	public List<Map<String, String>> getInventoryDataSheet(String sheetName) {
		String fileName = Constant.INVENTORY_DATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheetNew(fileName, sheetName);
		return sheetMap;
	}
	
	public List<Map<String, String>> getDemographicDataSheet(String sheetName) {
		String fileName = Constant.DEMOGRAPHIC_DATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheetNew(fileName, sheetName);
		return sheetMap;
	}
	
	
	public List<Map<String, String>> getPlanDataSheet(String sheetName) {
		String fileName = Constant.PLANDATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheetNew(fileName, sheetName);
		return sheetMap;
	}
	
	public List<Map<String, String>> getMaterDataSheet1(String sheetName) {
		
		String fileName = Constant.MASTERDATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheetNew(fileName, sheetName);
		return sheetMap;
	}

	public List<Map<String, String>> getMaterDataSheet(String sheetName, int startRow, int endRow) {
		
		String fileName = Constant.MASTERDATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheet(fileName, sheetName, startRow, endRow);
		return sheetMap;
	}

	public List<Map<String, String>> getTicketDataSheet(String sheetName, int startRow, int endRow) {
		
		String fileName = Constant.TICKETDATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheet(fileName, sheetName, startRow, endRow);
		return sheetMap;
	}
	
	public List<Map<String, String>> getSalesCRMDataSheet(String sheetName, int startRow, int endRow) {
		
		String fileName = Constant.SALES_CRM_DATA_FILE;
		
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		// return sheetMap = ReadWriteExcelFile.getSheet(sheetName,startRow,endRow);
		sheetMap = ReadWriteExcelFile.getSheet(fileName, sheetName, startRow, endRow);
		return sheetMap;
	}
	
	public JSONObject readJSONFile(String jsonFileName) {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;

		try {
			String filePath =  Constant.BASE_PATH + "\\TestData\\datafiles\\sampleJson\\";
			String fileName = jsonFileName;

			Object obj = parser.parse(new FileReader(filePath + fileName));
			jsonObject = (JSONObject) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
