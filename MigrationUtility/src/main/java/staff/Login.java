package staff;

import org.json.JSONObject;

import api.RestExecution;
import utility.Constant;
import utility.Utility;

public class Login extends RestExecution {

	private void getLogin(String username, String password) {

		String logFileName = "login.log";
		String logModuleName = "Login";

		String apiURL = getAPIURL("login");
		Utility.printLog(logFileName, logModuleName, "Request URL", apiURL);

		// Initializing payload or API body
		String APIBody = getLoginJson(username, password);
		Utility.printLog(logFileName, logModuleName, "Request Body", APIBody);

		JSONObject JSONResponseBody = httpPost(apiURL, APIBody);
		String response = JSONResponseBody.toString(4);
		Utility.printLog(logFileName, logModuleName, "Response", response);

		int status = JSONResponseBody.getInt("status");

		if (status == 200) {
			System.out.println("Login successfully - " + username);			
			String auth_bearer = JSONResponseBody.getString("accessToken");
			RestExecution.auth = auth_bearer;
		}
	}


	public void setAuthBearer() {

		String userName = Constant.STAFF_USERNAME;
		String password = Constant.STAFF_PASSWORD;
		
		getLogin(userName,password);
		
	}

	@SuppressWarnings("unchecked")
	private String getLoginJson(String username, String password) {

		String jsonString = null;

		try {

			org.json.simple.JSONObject loginJsonObject = new org.json.simple.JSONObject();

			loginJsonObject.put("username", username);
			loginJsonObject.put("password", password);

			jsonString = loginJsonObject.toJSONString();

		} catch (Exception e) {
			jsonString = null;
			e.printStackTrace();
		}

		return jsonString;
	}

}
