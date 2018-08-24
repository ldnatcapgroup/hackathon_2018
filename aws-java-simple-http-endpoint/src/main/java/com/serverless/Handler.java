package com.serverless;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class Handler implements RequestHandler<RegistrationInfo, String> {

	// private static final Logger LOG = Logger.getLogger(Handler.class);
	private static String accessToken = "OAuth 00D0x0000000QNK!ARcAQJLKTMkFzNFQYcy1T0GfJf14AXJrBua14ZsJPYRY2No7c5QXZC47FTcB6ciaHF3AN6AwhCtgt1VemriK6HD6L_5LBFRc";
	private static String sfdcUrl = "https://xn--capgroupfull-r19f.cs95.my.salesforce.com/services/data/v36.0/sobjects/Notification__c";
	// private static String aamUrl = "http://capitalgroup.demdex.net/event?d_uuid=48515798915548649800232462535070166586&d_sid=11940189&d_rtbd=json";
	private static String ldn_uuid = "d_uuid=48515798915548649800232462535070166586";
	private static String tsy_uuid = "d_uuid=";
	private static String sid = "&d_sid=11940189";
	private static String sma = "11953713";
	private static String fid = "11953717"; 
	private static String aamBaseUrl = "http://capitalgroup.demdex.net/event?";
	private static String sfdcStr = " has attended our booth in the Morningstart Investment conference requesting additional information about ";
	
	private String invokeSFDC(String name, String topic) throws IOException {
		String noticeStr = name + sfdcStr + topic;
		JSONObject json = new JSONObject();
		json.put("Active__c", "true");
		json.put("Description__c", noticeStr);
		json.put("Start_Date__c", "2018-08-23");
		String jsonStr = json.toString();
		
		System.out.println("JSON=" + json.toJSONString());
		// String json = "{\"Active__c\" : \"true\",\"Description__c\" : \"Test6\",\"Start_Date__c\" : \"2018-08-23\"}";
		
		URL url = new URL(sfdcUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestProperty("Authorization", accessToken);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        
        OutputStream os = conn.getOutputStream();
        os.write(jsonStr.getBytes("UTF-8"));
        os.close();
        	       
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}

	private String invokeAAM(String topic) throws IOException {
		String sidStr = sid;
		if (topic.equalsIgnoreCase("SMA")) sidStr = sid + "," + sma;
		if (topic.equalsIgnoreCase("FI")) sidStr = sid + "," + fid;
		String aamUrl = aamBaseUrl + ldn_uuid + sidStr;
		
		System.out.println("aamUrl=" + aamUrl);
		
		URL url = new URL(aamUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		int responseCode = conn.getResponseCode();
		System.out.println("Response Code = " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		return response.toString();		
	}
	
	
	@Override
	public String handleRequest(RegistrationInfo input, Context context) {	
		try {
			
			// invokeAAM("SMA");
	        // invokeSFDC("Lam Nguyen", "SMA");
			LambdaLogger logger = context.getLogger();
			// logger.log("email = " + input.email + "filename=" + input.photoName);
			
			String url = "jdbc:mysql://mysqlhackathon2018.cglflvz1bvvd.us-west-2.rds.amazonaws.com:3306";
		    String username = "hackathon";
		    String password = "inyourface";
		    String lastName = "";

		      Connection conn = DriverManager.getConnection(url, username, password);
		      Statement stmt = conn.createStatement();
		      String photoLocation = "ldn.jpg";
		      ResultSet resultSet = stmt.executeQuery("SELECT * FROM inyourface.Contact WHERE Photo='"+photoLocation+"'");
		      
		      
		      if (resultSet.next()) {
		    	  // resultSet.getObject("LName");
		          lastName = resultSet.getObject("LName").toString();
		          System.out.println("Name = " + lastName);
		        }
	        
		      if (lastName.length() > 0) {
		    	  invokeAAM("FI");
		    	  invokeSFDC(lastName, "FI");
		      }
	        // return String.valueOf(conn.getResponseCode()) +" : "+ conn.getResponseMessage();
			return "{id:'abc123', status:'new'}";
		}

		catch (Exception e) {

		        return e.toString();
		     } 
	    
	}
}
