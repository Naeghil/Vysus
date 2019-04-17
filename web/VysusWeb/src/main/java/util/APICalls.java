package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.*;

public class APICalls {

	public static void main(String[] args) {
		//System.out.println(fullAddress("cm34qw","Danbury Fryer"));
		//System.out.println(getDistance("cm34rl","ab245dj",1000	));
	}
	
		public static Map<String,String> fullAddress(String postcode, String identifier){
			//System.out.println("fullAddress.identifier: " + identifier);
			//System.out.println("fullAddress.postcode: " + postcode);
			String identifierFix = identifier.replaceAll("\\s","");
			@SuppressWarnings("unused")
			String APIData = APICalls.getData("https://api.getAddress.io/find/"+postcode+"/"+identifierFix+"?api-key=xDg38fqBR02Bgpr1KgDhVw18443");
			Map<String,String> testData = new HashMap<String,String>();
			testData.put("Identifier", "WE RAN OUT OF API CALLS");
			testData.put("Town", "WE RAN OUT OF API CALLS");
			testData.put("City", "WE RAN OUT OF API CALLS");
			testData.put("County", "WE RAN OUT OF API CALLS");
			//return parseAddress(APIData);
			return testData;
		}
		
		public static boolean checkDistance(String start, String destination, float maximumDistance) {
			String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+start+"&destinations="+destination+"&key=AIzaSyDaoTS-UJ0jeuB0kDkGCtBtl7A3KoybsmU";
			String APIData = APICalls.getData(URL);
			int calculatedDistance = parseDistance(APIData);
			if (calculatedDistance < maximumDistance*1000) {
				return true;
			} else {
				return false;
			}
		}
		
		//generic api caller
		public static String getData(String url){
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
			    	return "error";
			    } else {
			    	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    	while((line = reader.readLine()) != null) {
			    		response.append(line);
			    	}
			    	reader.close();
			    	
			    }
			    con.disconnect();
			    //System.out.println(response.toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return response.toString();
		    
		}
		
		public static Map<String, String> parseAddress(String response) {
			Map<String, String> fullAddress = new HashMap<String, String>();
			if (response.equals("error")) {
				fullAddress.put("Identifier", "WE RAN OUT OF API CALLS");
				fullAddress.put("Town", "WE RAN OUT OF API CALLS");
				fullAddress.put("City", "WE RAN OUT OF API CALLS");
				fullAddress.put("County", "OR YOUR DATA IS WRONG");
				System.out.println(fullAddress);
				return fullAddress;
			}
			JSONObject data = new JSONObject(response);
			JSONArray innerData = (JSONArray) data.get("addresses"); 
			System.out.println(data.toString());
			StringTokenizer st = new StringTokenizer(innerData.get(0).toString());
			System.out.println(st);
			String[] result = innerData.get(0).toString().split(",");
			//System.out.println(result[0]);
			for (int i = 0; i < result.length; i++) {
				System.out.println(i + " " + result[i]);
			}
			fullAddress.put("Identifier", result[0]);
			fullAddress.put("Town", result[4]);
			fullAddress.put("City", result[5]);
			fullAddress.put("County", result[6]);
			System.out.println(fullAddress);
			return fullAddress;
		}
		
		public static int parseDistance(String response) {
			JSONObject data = new JSONObject(response);
			JSONArray innerData = (JSONArray) data.get("rows");
			JSONObject data2 = innerData.getJSONObject(0);
			JSONArray data3 = (JSONArray) data2.get("elements");
			JSONObject data4 = data3.getJSONObject(0);
			JSONObject data5 = data4.getJSONObject("distance");
			int distance = data5.getInt("value");
			return distance;
		}
}
