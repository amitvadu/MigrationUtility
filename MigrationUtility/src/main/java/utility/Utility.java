package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author amit.prajapati
 *
 */
public class Utility {
	
	
	private static DateFormat dateFormatNew = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	private static Date date = null;
	
	private static long[] startTime = new long[100];	
	private static int elapsedCounter = 0;
	

	
	public static void printLog(String fileName, String moduleName, String variableName, String variableValue){
		date = new Date();
		String stringToPrint = "";
			
		if((variableValue != null) && (variableValue.trim().equals(""))){		
			stringToPrint = "[" + dateFormatNew.format(date) + "] [" + moduleName + "] " + variableName;			
		}else{
			stringToPrint = "[" + dateFormatNew.format(date) + "] [" + moduleName + "] " + variableName + " = " + variableValue;
		}
		
		//System.out.println(stringToPrint);	
		
		WriteData.writeToFile(fileName, stringToPrint, true);
		
	}
	
	
	


	
	public static void watchTimer(boolean flag){
		 date = new Date();
		
		String stringToPrint = "[" + dateFormatNew.format(date) + "] ";	
		if(flag){
			System.out.println("Started : " + stringToPrint);
		}else{
			System.out.println("Completed : " + stringToPrint);
		}
				
	}
	
	
	public static void elapsedTimeNew(boolean flag,int identifier,String message){
		
		if(flag){
			
			startTime[identifier] = System.currentTimeMillis();
			
		}else{
			long endTime = System.currentTimeMillis();
			long estimatedTime = endTime - startTime[identifier];
			
			String hms = String.format("%02d:%02d:%03d",
				    TimeUnit.MILLISECONDS.toMinutes(estimatedTime) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds(estimatedTime) % TimeUnit.MINUTES.toSeconds(1),			
				    TimeUnit.MILLISECONDS.toMillis(estimatedTime) % TimeUnit.SECONDS.toMillis(1));
			
			
			//System.out.println("\n\n" + message + " : **  Elapsed Time : " + hms + " **");
			
			
			
			
			//long yourmilliseconds = startTime[identifier];
			//SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss:SS");
			//Date resultdate = new Date(startTimeLocal);
			//System.out.println(sdf.format(resultdate));
			long startTimeLocal = startTime[identifier];
			long endTimeLocal = endTime;
			Date startTimeDate = new Date(startTimeLocal);
			Date endTimeDate = new Date(endTimeLocal);
			
			String startTimeExecution = timeFormat.format(startTimeDate);			
			String endTimeExecution = timeFormat.format(endTimeDate);
			
			System.out.println("\n\n" + message + " : [StartTime : " + startTimeExecution + " | EndTime : " + endTimeExecution + " | Elapsed Time : " + hms + "]\n");
		}
	}
	
	
	
	public static int elapsedTime(int identifier,String message,String fileName){
		
		if(identifier == -1){
			
			identifier = elapsedCounter++;
			startTime[identifier] = System.currentTimeMillis();			
			
			//long startTimeLocal = startTime[identifier];			
			//Date startTimeDate = new Date(startTimeLocal);			
			//String startTimeExecution = timeFormat.format(startTimeDate);
			//String timerMessage = message + " : ["+ identifier +"-StartTime: " + startTimeExecution +  "]\n";
			//Utility.printLog("order-execution.log","START-TIMER",timerMessage,"");
		}else{
			long endTime = System.currentTimeMillis();
			long estimatedTime = endTime - startTime[identifier];
			
			String hms = String.format("%02d:%02d:%02d.%03d",
					TimeUnit.MILLISECONDS.toHours(estimatedTime) % TimeUnit.DAYS.toHours(1),
				    TimeUnit.MILLISECONDS.toMinutes(estimatedTime) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds(estimatedTime) % TimeUnit.MINUTES.toSeconds(1),			
				    TimeUnit.MILLISECONDS.toMillis(estimatedTime) % TimeUnit.SECONDS.toMillis(1));
			
			long startTimeLocal = startTime[identifier];
			long endTimeLocal = endTime;
			Date startTimeDate = new Date(startTimeLocal);
			Date endTimeDate = new Date(endTimeLocal);
			
			String startTimeExecution = timeFormat.format(startTimeDate);			
			String endTimeExecution = timeFormat.format(endTimeDate);
			
			String timerMessage = message + " : ["+ identifier +"-StartTime: " + startTimeExecution + " | EndTime: " + endTimeExecution + " | ElapsedTime: " + hms + "]\n";
			elapsedCounter--;
			//System.out.println("END-TIMER" + timerMessage);
						
			Utility.printLog(fileName,"END-TIMER",timerMessage,"");
		}
		
		return identifier;		
	}
	
	
	
	public static int dateTimeDifferenceInSeconds(String date1,String date2){

		   // Custom date format
		    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

		    Date d1 = null;
		    Date d2 = null;
		    try {
		        d1 = format.parse(date1);
		        d2 = format.parse(date2);
		        
		    } catch (java.text.ParseException e) {
		        e.printStackTrace();
		    }
			
			Date startDate = d1;// Set start date
			Date endDate   = d2;// Set end date

			long duration  = endDate.getTime() - startDate.getTime();		
			long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
			 
			//long diffSeconds = duration / 1000 % 60;
			//long diffMinutes = duration / (60 * 1000) % 60;
			//long diffHours = duration / (60 * 60 * 1000);
			
			//long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			//long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
			//long diffInDays= TimeUnit.MILLISECONDS.toHours(duration);
			//long diffInMonths = TimeUnit.MILLISECONDS.toHours(duration);
			//long diffInYears = TimeUnit.MILLISECONDS.toHours(duration);
			
			//System.out.println("Time in seconds: " + diffInSeconds + " seconds.");
		   // System.out.println("Time in minutes: " + diffInMinutes + " minutes.");
		   // System.out.println("Time in hours: " + diffInHours + " hours.");
			
			diffInSeconds = Math.abs(diffInSeconds);
		    return (int)diffInSeconds;
		}
		
	
	
	public static int randomNumber(int min,int max, int interval){
		
		int randomNo=0;
		
		if (interval <= 0) {
			interval = 1;
		}
		
		randomNo = (int)((Math.random() * ((max/interval)-(min/interval)+1) + (min/interval))) * interval;		
		//System.out.println("randomNo = " + randomNo);
		return randomNo;
	
	}			
	
	/**
	 * The method randomStringGenerator will generate the random string.
	 * 
	 * @param length
	 * Length of returned random string (int) <br>
	 * 
	 * @param type
	 * Possible type values (int) <br>
	 * 	 0 - Number,Upper and Lower <br>
	 *	 1 - Number only <br>
	 *	 2 - Upper only <br>
		 3 - Lower only <br>
		 4 - Number and Upper only <br>
		 5 - Number and lower only <br>
		 6 - Upper and lower only <br>
	 * 
	 * @return
	 * 
	 * The randomStringGenerator method will return the generated random string as per length and type.
	 */
	public static String randomStringGenerator(int length,int type){
		/*
		 0 - Number,Upper and Lower
		 1 - Number only
		 2 - Upper only
		 3 - Lower only
		 4 - Number and Upper only
		 5 - Number and lower only
		 6 - Upper and lower only
		 */
		
		String randomString = "";
		
		switch (type){
			
			case 0:				
				for(int i=0;i<length;i++){
					int tno = randomNumber(1,9,1);
					if(tno <= 3){
						randomString = randomString +  String.valueOf(randomNumber(0,9,1));
					}else if(tno >= 4 && tno <= 6){
						int tempno = randomNumber(65,90,1);
						randomString = randomString + Character.toString((char) tempno);						
					}else{ 
						int tempno = randomNumber(97,122,1);
						randomString = randomString + Character.toString((char) tempno);
					}
				}
				break;
			case 1:				
				for(int i=0;i<length;i++){					
					randomString = randomString +  String.valueOf(randomNumber(0,9,1));
				}
				break;
				
			case 2:				
				for(int i=0;i<length;i++){					
					int tempno = randomNumber(65,90,1);
					randomString = randomString + Character.toString((char) tempno);
				}
				break;
				
			case 3:				
				for(int i=0;i<length;i++){
					int tempno = randomNumber(97,122,1);
					randomString = randomString + Character.toString((char) tempno);					
				}
				break;	
			
			case 4:				
				for(int i=0;i<length;i++){					
					int tno = randomNumber(1,10,1);
					if(tno <= 5){
						randomString = randomString +  String.valueOf(randomNumber(0,9,1));
					}else{
						int tempno = randomNumber(65,90,1);
						randomString = randomString + Character.toString((char) tempno);						
					}
				}
				break;
		

			case 5:				
				for(int i=0;i<length;i++){					
					int tno = randomNumber(1,10,1);
					if(tno <= 5){
						randomString = randomString +  String.valueOf(randomNumber(0,9,1));
					}else{
						int tempno = randomNumber(97,122,1);
						randomString = randomString + Character.toString((char) tempno);						
					}
				}
				break;

			case 6:				
				for(int i=0;i<length;i++){
					int tno = randomNumber(1,10,1);
					if(tno <= 5){
						int tempno = randomNumber(65,90,1);
						randomString = randomString + Character.toString((char) tempno);
					}else{
						int tempno = randomNumber(97,122,1);
						randomString = randomString + Character.toString((char) tempno);						
					}					
				}
				break;
		}
		
		//System.out.println("randomString = " + randomString);		
		return randomString;
	}
	
		
	public static String dateTimeOperation(String date,int value, int valueType,int returnDateTimeFormat){
		/*
		 0 - Second 	 
		 1 - Minute
		 2 - Hour
		 3 - Day
		 4 - Month
		 5 - Year		 
		 */
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		if(returnDateTimeFormat == 0){
	    	dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
	    }else if(returnDateTimeFormat == 1){
	    	dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	    }
		
	    Date d1 = null;
		    
	    try {
	        d1 = dateFormat.parse(date);
	        
	    } catch (java.text.ParseException e) {
	        e.printStackTrace();
	    }			
	
	    
	    Calendar cal = Calendar.getInstance();		
		cal.setTime(d1); 		
				
		switch (valueType){
			
		case 0 :
			 cal.add(Calendar.SECOND, value);
			 break;		
			
		case 1 :
			 cal.add(Calendar.MINUTE, value);
			 break;
				 
		case 2 :
			cal.add(Calendar.HOUR, value);
			break;
				
		case 3 :
			cal.add(Calendar.DATE, value);
			break;
				
		case 4 :
			cal.add(Calendar.MONTH, value);
			break;
				
		case 5 :				
			cal.add(Calendar.YEAR, value);
			break;
		}
		
		
		
		String operatedDate = dateFormat.format(cal.getTime());
		return operatedDate;
		
	}
	
	public static String getFormattedCurrentDate(){
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date newdate = new Date();	
		String currentDate = dateFormat.format(newdate);
		return currentDate;
		
	}

	public static String getSystemCurrentTime(){
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	    String currentTimeInString = "";
		    
	    try {
	    	currentTimeInString = format.format(new Date()); // (capturedCreateDate);
	        
	    //} catch (java.text.ParseException e) {
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
	    
		return currentTimeInString;
		
	}
	
	public static String getCurrentDateTime(){
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	    String currentDateTimeInString = "";
		    
	    try {
	    	currentDateTimeInString = format.format(new Date()); // (capturedCreateDate);
	        
	    //} catch (java.text.ParseException e) {
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
	    
		return currentDateTimeInString;
		
	}
	
	public static String getCurrentDateTimeByProvidedFormat(String dateTimeFormat){
		
		SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat);
	    String currentDateTimeInString = "";
		    
	    try {
	    	currentDateTimeInString = format.format(new Date()); // (capturedCreateDate);
	        
	    //} catch (java.text.ParseException e) {
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
	    
		return currentDateTimeInString;
		
	}
	
	public static String getDateTimeInRequiredFormatFromProvidedDateTime(String providedDateTime,String providedFormat,
			String requiredFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(providedFormat);
		SimpleDateFormat sdf1 = new SimpleDateFormat(requiredFormat);
		String currentDateTimeInString = "";

		try {
			Date date = sdf.parse(providedDateTime);			
			currentDateTimeInString = sdf1.format(date);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return currentDateTimeInString;

	}

	public static void waitInMilliseconds(long millisecnds) {

		try {
			Thread.sleep(millisecnds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String formattedDecimalNumber(float number) {
		DecimalFormat df = new DecimalFormat("#######0.00");
		String formattedPrice = df.format(number);

		return formattedPrice;
	}
	
	public static float formattedFloatDecimalNumber(String number) {
		DecimalFormat df = new DecimalFormat("#######0.00");
		String formattedResult = df.format(Float.parseFloat(number));
		float formattedNumber = Float.parseFloat(formattedResult);
		return formattedNumber;
	}
	
	
	public static String getRandomMacAddress() {
		String mac = "";
		Random r = new Random();
		for (int i = 0; i < 6; i++) {
			int n = r.nextInt(255);
			mac += String.format("%02x", n) + ":";
		}
		mac = mac.substring(0, mac.length() - 1);
		return mac.toUpperCase();
	}
	
	
	
	public static String getRandomSerialNumber(String prefix,int length) {	
		String serialNumber = prefix + Utility.randomStringGenerator(length, 1); 
		return serialNumber;
	}
	
	public static Properties loadProperties(){
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getProperty("user.dir") + "/TestData/datafiles/migration.properties");

			// load a properties file
			prop.load(input);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	
}