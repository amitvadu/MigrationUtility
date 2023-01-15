package temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.monitorjbl.xlsx.StreamingReader;

import api.ReadData;
import utility.Constant;

public class TempExcelDataReader {

	public static void main(String args[]) throws IOException {
		
		//read2();
		//readXLS();
		
		String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getMaterDataSheet1(sheetName);
		//sheetMap = readData.getPlanDataSheet(sheetName);
		System.out.println("sheetMap = " + sheetMap.toString());
		
	/*	String sheetName = "Geogaraphical Areas";
		List<Map<String, String>> sheetMap = new ArrayList<Map<String, String>>();
		ReadData readData = new ReadData();
		sheetMap = readData.getMaterDataSheet(sheetName, 1, 19);
		//sheetMap = readData.getPlanDataSheet(sheetName);
		System.out.println("sheetMap = " + sheetMap.toString());
	*/
	}
	
	
	public static void read2() throws IOException {
		
		String filePath =  Constant.BASE_PATH + "\\TestData\\input\\";
		String fileName = "Migration V3.2.xlsx";
		String sheetName = "Geogaraphical Areas";
		DataFormatter dataFormatter = new DataFormatter();
		
		File myFile = new File(filePath + fileName);
		FileInputStream fis = new FileInputStream(myFile);
		
		// Finds the workbook instance for XLSX file 
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		
		// Return first sheet from the XLSX workbook 
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
		
		// Get iterator to all the rows in current sheet 
		Iterator<Row> rowIterator = mySheet.iterator();
		
		// Traversing over each row of XLSX file 
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			// For each row, iterate through each columns 
			Iterator<Cell> cellIterator = row.cellIterator();
			
			while (cellIterator.hasNext()) {
				Cell cell =  cellIterator.next();				
		        String value = dataFormatter.formatCellValue(cell);
				System.out.print(value + "\t");
				
			} System.out.println("");
		}
		
	}
	
	
	
	public static void read1() throws IOException {
		
		String filePath =  Constant.BASE_PATH + "\\TestData\\input\\";
		String fileName = "Migration V3.2.xlsx";
		String sheetName = "Geogaraphical Areas";
		
		File myFile = new File(filePath + fileName);
		FileInputStream fis = new FileInputStream(myFile);
		
		// Finds the workbook instance for XLSX file 
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		
		// Return first sheet from the XLSX workbook 
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
		
		// Get iterator to all the rows in current sheet 
		Iterator<Row> rowIterator = mySheet.iterator();
		
		// Traversing over each row of XLSX file 
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			// For each row, iterate through each columns 
			Iterator<Cell> cellIterator = row.cellIterator();
			
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				
				switch (cell.getCellType()) { 
					case Cell.CELL_TYPE_STRING: 
						System.out.print(cell.getStringCellValue() + "\t");
						break;
					case Cell.CELL_TYPE_NUMERIC: 
						System.out.print("N : "+cell.getNumericCellValue() + "\t");
						break;
					case Cell.CELL_TYPE_BOOLEAN: 
						System.out.print(cell.getBooleanCellValue() + "\t");
						break;
					default:
				} 
			} System.out.println("");
		}
		
	}
	
	public static void readXLS() throws IOException {
		
		String filePath =  Constant.BASE_PATH + "\\TestData\\input\\";
		String fileName = "Migration V3.1.xlsx";
		String sheetName = "Geogaraphical Areas";

		InputStream file = new FileInputStream(new File(filePath + fileName));
		Workbook workbook = StreamingReader.builder().rowCacheSize(100) // number of rows to keep in memory
				.bufferSize(1) // index of sheet to use (defaults to 0)
				.open(file); // InputStream or File for XLSX file (required)

		//Iterator<Row> rowIterator = workbook.getSheetAt(0).rowIterator();
		Iterator<Row> rowIterator = workbook.getSheet(sheetName).rowIterator();
		
		while (rowIterator.hasNext()) {
			
			Row row = rowIterator.next();
			
			// For each row, iterate through each columns 
			Iterator<Cell> cellIterator = row.cellIterator();
			
			while (cellIterator.hasNext()) {
				//Cell cell = cellIterator.next();
				//String cellValue = dataFormatter.formatCellValue(cell);
				
				Cell cell = cellIterator.next();
				
				switch (cell.getCellType()) { 
					case Cell.CELL_TYPE_STRING: 
						System.out.print(cell.getStringCellValue() + "\t");
						break;
					case Cell.CELL_TYPE_NUMERIC: 
						System.out.print(cell.getNumericCellValue() + "\t");
						break;
					case Cell.CELL_TYPE_BOOLEAN: 
						System.out.print(cell.getBooleanCellValue() + "\t");
						break;
					default:
				} 
			} System.out.println("");
		}
		
	}


}
