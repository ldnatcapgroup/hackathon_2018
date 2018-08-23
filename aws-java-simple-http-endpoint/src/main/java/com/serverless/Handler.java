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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class Handler implements RequestHandler<Map<String, Object>, String> {

	private static final Logger LOG = Logger.getLogger(Handler.class);
	private static String accessToken = "OAuth 00D0x0000000QNK!ARcAQJLKTMkFzNFQYcy1T0GfJf14AXJrBua14ZsJPYRY2No7c5QXZC47FTcB6ciaHF3AN6AwhCtgt1VemriK6HD6L_5LBFRc";
	private static String vAddress = "https://xn--capgroupfull-r19f.cs95.my.salesforce.com/services/data/v36.0/sobjects/Notification__c";

	@Override
	public String handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: " + input);
		HttpURLConnection conn;
		String json = "{\"Active__c\" : \"true\",\"Description__c\" : \"Test5\",\"Start_Date__c\" : \"2018-08-23\"}";
		
		try {
			
	        URL url = new URL(vAddress);
	        conn = (HttpURLConnection) url.openConnection();
            
	        conn.setRequestProperty("Authorization", accessToken);
	        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
	        conn.setDoOutput(true);
	        
	        OutputStream os = conn.getOutputStream();
	        os.write(json.getBytes("UTF-8"));
	        os.close();
	        	       
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return response.toString();
	        
	        // return String.valueOf(conn.getResponseCode()) +" : "+ conn.getResponseMessage();
			// return "{id:'abc123', status:'new'}";

	      } catch (IOException e) {

	        return e.toString();
	     }

	    
	}
}
