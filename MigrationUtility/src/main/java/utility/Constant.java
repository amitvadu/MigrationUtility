package utility;

import java.util.Properties;

public class Constant {
	
	static Properties prop = Utility.loadProperties();
	
	public static final String BASE_PATH = System.getProperty("user.dir");
	public static final String MASTERDATA_FILE = prop.getProperty("MASTERDATA_FILE");
	public static final String TICKETDATA_FILE = prop.getProperty("TICKETDATA_FILE");
	public static final String SALES_CRM_DATA_FILE = prop.getProperty("SALESCRMDATA_FILE");
	public static final String PLANDATA_FILE = prop.getProperty("PLANDATA_FILE");
	public static final String DEMOGRAPHIC_DATA_FILE = prop.getProperty("DEMOGRAPHIC_DATA_FILE");
	public static final String INVENTORY_DATA_FILE = prop.getProperty("INVENTORY_DATA_FILE");
	
	public static final boolean MASTERDATA_MIGRATION = Boolean.valueOf(prop.getProperty("MASTERDATA_MIGRATION"));
	public static final boolean PLANDATA_MIGRATION = Boolean.valueOf(prop.getProperty("PLANDATA_MIGRATION"));
	public static final boolean STAFFDATA_MIGRATION = Boolean.valueOf(prop.getProperty("STAFFDATA_MIGRATION"));
	public static final boolean CUSTOMERDATA_MIGRATION = Boolean.valueOf(prop.getProperty("CUSTOMERDATA_MIGRATION"));
	public static final boolean INVENTORYDATA_MIGRATION = Boolean.valueOf(prop.getProperty("INVENTORYDATA_MIGRATION"));
	
	
	//public static String AUTHENTICATION ="";
	
	public static final String API_URL = prop.getProperty("API_URL");
	public static final String STAFF_USERNAME = prop.getProperty("STAFF_USERNAME");
	public static final String STAFF_PASSWORD = prop.getProperty("STAFF_PASSWORD");
	
}
