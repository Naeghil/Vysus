package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.*;


public class APICalls {

	public void getLatLong(){}

	public static void main(String[] args) {
	    try {
	        StringBuffer j = APICalls.getData("cm34rl","ab245dj");
	        parseJSON(j);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			   
		public static StringBuffer getData(String start,String destination){
			String testPostcode = "cm34rl";
		    //String url = "https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=47.6044,-122.3345;47.6731,-122.1185;47.6149,-122.1936&destinations=45.5347,-122.6231;47.4747,-122.2057&travelMode=driving&key=AoWX66pYythZ2v4yWfGFEHOhpGYEtVNTq5CX3UfB1skxrWBmWMbQEgP0aPR_tejX";
		    //String url = "http://api.postcodes.io/postcodes/" + testPostcode;
			String url = "https://api.getAddress.io/find/cm34rl/45?api-key=GJUIdYuj6UiW-Atc5lR_uQ18432";
			URL obj;
			BufferedReader reader;
			String line;
			StringBuffer response = new StringBuffer();
			try {
				obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
			    con.setConnectTimeout(5000);
			    con.setReadTimeout(5000);
			    int status = con.getResponseCode();
			    //System.out.println(status);
			    
			    if (status != 200) {
			    	reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			    	while((line = reader.readLine()) != null) {
			    		response.append(line);
			    	}
			    	reader.close();
			    } else {
			    	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    	while((line = reader.readLine()) != null) {
			    		response.append(line);
			    	}
			    	reader.close();
			    }
			    con.disconnect();
			    System.out.println(response.toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return response;
		    
		}
		
		public static String parseJSON(String response) {
			JSONObject data = new JSONObject(response);
			JSONArray innerData = (JSONArray) data.get("addresses"); 
			System.out.println(data.toString());
			StringTokenizer st = new StringTokenizer(innerData.get(0).toString());
			System.out.println(st);
			/*for (int i = 0; i < data.length(); i++) {
				JSONObject dataItem = data.getJSONObject(i);
				String quality = dataItem.getString("quality");
			}*/
			
			return "yes";
		}
}
