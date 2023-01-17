package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadWriteExcelFile {
	
	public static List <Map<String, String>> getSheet(String fileName,String sheetName,int startRow,int endRow){		
		String filePath =  Constant.BASE_PATH + "\\TestData\\input\\";
		
		//Map<String, Map<String, String>> ContainerMap = new HashMap<String,Map<String, String>>(); //Create map
		List<Map<String, String>> containerList = new ArrayList<Map<String, String>>(); //Create map
		
		try { 
	
			File file = new File(filePath +fileName);    		 
		    FileInputStream fis = new FileInputStream(file); 
		       		
		    XSSFWorkbook workbook = new XSSFWorkbook(fis);
		    XSSFSheet  sheet = workbook.getSheet(sheetName);
		    Cell cell = null;
		    
		    
		    
		    for(int nrow=startRow;nrow<=endRow;nrow++){
		        
			    Map<String, String> map = new HashMap<String,String>(); //Create map
		    	
		    	Row row =  sheet.getRow(0); //Get first row
		    	
		    	short minColIx = row.getFirstCellNum(); //get the first column index for a row
		    	short maxColIx = row.getLastCellNum(); //get the last column index for a row
		    	for(short colIx=minColIx; colIx<maxColIx; colIx++) { //loop from first to last index
		    		
		    		cell = sheet.getRow(0).getCell(colIx);
					String firstHeaderRow = String.valueOf(cell);
					
					cell = sheet.getRow(nrow - 1).getCell(colIx);
					String rowValue = String.valueOf(cell);
					
					map.put(firstHeaderRow,rowValue);
					
		    		//Cell cell = row.getCell(colIx); //get the cell
		    		//map.put(cell.getStringCellValue(),cell.getColumnIndex()); //add the cell contents (name of column) and cell index to the map
		    	}
			    
		    	//System.out.println("map : " + map);
			    
		    	//cell = sheet.getRow(nrow).getCell(0);
				//String primaryKey = String.valueOf(cell);
				//System.out.println("primaryKey : " + primaryKey);
		    	
		    	//containerList.put(String.valueOf(nrow), map);
		    	containerList.add(map);
		    
		    }
		    
		    //System.out.println("containerList : " + containerList.toString());
			workbook.close();
			fis.close();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Specifed file not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return containerList;
	}


	public static List <Map<String, String>> getSheetNew(String fileName,String sheetName){		
		
		String filePath =  Constant.BASE_PATH + "\\TestData\\input\\";		
		List<Map<String, String>> containerList = new ArrayList<Map<String, String>>(); //Create map
		DataFormatter dataFormatter = new DataFormatter();
		
		try { 
	
			File file = new File(filePath +fileName);    		 
		    FileInputStream fis = new FileInputStream(file); 
		       		
		    XSSFWorkbook workbook = new XSSFWorkbook(fis);
		    XSSFSheet  sheet = workbook.getSheet(sheetName);
		    
		 // Get iterator to all the rows in current sheet 
			Iterator<Row> rowIterator = sheet.iterator();
						
			// Traversing over each row of XLSX file 
			while (rowIterator.hasNext()) {
				
				Map<String, String> map = new HashMap<String,String>(); //Create map
				Row row = rowIterator.next();
								
				// For each row, iterate through each columns 
				Iterator<Cell> cellIterator = row.cellIterator();
				int columnIndex=0;
				
				while (cellIterator.hasNext()) {
					
					Cell firstRowcell = null;
					firstRowcell = sheet.getRow(0).getCell(columnIndex);
					String firstHeaderRow = String.valueOf(firstRowcell);
					columnIndex++;
					
					Cell cell = cellIterator.next();					
					//String rowValue = String.valueOf(cell);
					String rowValue = dataFormatter.formatCellValue(cell);
					
					map.put(firstHeaderRow,rowValue.trim());
				}
				if(!map.isEmpty()) {
					containerList.add(map);
				}
			}
			
			containerList.remove(0);			
		    //System.out.println("containerList : " + containerList.toString());
			workbook.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Specifed file not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return containerList;
	}

	
}
