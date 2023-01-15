package utility;

public class DBOperations {
	
	
	
	public String getSingleData(String query) {
		
		DBConnect db = new DBConnect();
		String data = db.selectData(query);
		return data;
	}
	
	public void setAPIData(String entityType,String entityName,int id,int status) {
		
	/*	System.out.println("entityType = " + entityType);
		System.out.println("entityName = " + entityName);
		System.out.println("id = " + id);
		System.out.println("status = " + status);
	*/	
		String logtime = Utility.getCurrentDateTime();
		
		DBConnect db = new DBConnect();
		//System.out.println("orderNumber:" + orderNumber);
		String query = "insert into status (entitytype,name,id,status,logtime) values ('" + entityType + "','" + entityName + "'," + id + "," + status + ",'" + logtime + "')" ;
		
		//System.out.println("query = " + query);
		db.executeQuery(query);
		
	}
}
