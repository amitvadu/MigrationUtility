package api;

import static com.jayway.restassured.RestAssured.given;

import org.json.JSONObject;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import utility.Constant;

public class RestExecution {
	
	//private static String auth = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJmaXJzdE5hbWVcIjpcImFkbWluXCIsXCJsYXN0TmFtZVwiOlwiYWRtaW5cIixcInVzZXJJZFwiOjIsXCJwYXJ0bmVySWRcIjoxLFwicm9sZXNMaXN0XCI6XCIxXCIsXCJzZXJ2aWNlQXJlYUlkXCI6bnVsbCxcIm12bm9JZFwiOjIsXCJzZXJ2aWNlQXJlYUlkTGlzdFwiOlsxLDUsNywxMSwxMywxOSwyNSwzMSwzMiwzMywzNCwzNSwzNiwzNywzOCwzOSw0MCw0MSw0Miw0Myw0NCw0NSw0Nl0sXCJzdGFmZklkXCI6MixcImJ1SWRzXCI6W10sXCJsY29cIjpmYWxzZX0iLCJleHAiOjE2NzIxNjU1MjN9.OsRNFpVyUlOfEDGaVuMMDBy2_UH1UObE6Mdqaqd3FfY";

	
	public static String auth = "";
	
	public String getAPIURL(String apiName) {
		//Initializing Rest API's URL
		String apiURL =Constant.API_URL + apiName;
		return apiURL;
	}
		
	public JSONObject httpPost(String url,String body) {
		JSONObject JSONResponseBody = null;
		
		 // Building request using requestSpecBuilder
		 RequestSpecBuilder builder = new RequestSpecBuilder();
		  
		 //Setting API's body
		 builder.setBody(body);
		  
		 //Setting content type as application/json or application/xml
		 builder.setContentType("application/json");
		 builder.addHeader("Authorization", auth);
		 
		 RequestSpecification requestSpec = builder.build();
		 try {
			 //Making post request with authentication, leave blank in case there are no credentials- basic("","")
			 //Response response = given().authentication().preemptive().basic({username}, {password}).spec(requestSpec).when().post(apiURL));
			 Response response = given().spec(requestSpec).when().post(url);
			 JSONResponseBody = new JSONObject(response.body().asString());
			 
		 }catch(Exception e) {
			e.printStackTrace(); 
		 }
		 return JSONResponseBody;
	}


	public JSONObject httpGet(String url) {
		JSONObject JSONResponseBody = null;
		
		 // Building request using requestSpecBuilder
		 RequestSpecBuilder builder = new RequestSpecBuilder();
		  
		 //Setting content type as application/json or application/xml
		 builder.setContentType("application/json");
		 builder.addHeader("Authorization", auth);
		 
		 RequestSpecification requestSpec = builder.build();
		 try {
			 //Making post request with authentication, leave blank in case there are no credentials- basic("","")
			 //Response response = given().authentication().preemptive().basic({username}, {password}).spec(requestSpec).when().post(apiURL));
			 Response response = given().spec(requestSpec).when().get(url);
			 JSONResponseBody = new JSONObject(response.body().asString());
			 			 
		 }catch(Exception e) {
			e.printStackTrace(); 
		 }
		 return JSONResponseBody;
	}

	
	public JSONObject httpPut(String url,String body) {
		JSONObject JSONResponseBody = null;
		
		 // Building request using requestSpecBuilder
		 RequestSpecBuilder builder = new RequestSpecBuilder();
		  
		 //Setting API's body
		 builder.setBody(body);
		  
		 //Setting content type as application/json or application/xml
		 builder.setContentType("application/json");
		 builder.addHeader("Authorization", auth);
		 
		 RequestSpecification requestSpec = builder.build();
		 try {
			 //Making post request with authentication, leave blank in case there are no credentials- basic("","")
			 //Response response = given().authentication().preemptive().basic({username}, {password}).spec(requestSpec).when().post(apiURL));
			 Response response = given().spec(requestSpec).when().put(url);
			 JSONResponseBody = new JSONObject(response.body().asString());
			 
		 }catch(Exception e) {
			e.printStackTrace(); 
		 }
		 return JSONResponseBody;
	}
	
}
