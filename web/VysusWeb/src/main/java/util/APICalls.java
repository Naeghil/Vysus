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

	/*public static void main(String[] args) {
		//System.out.println(fullAddress("cm34rl","45"));
		System.out.println(getDistance("cm34rl","ab245dj",1000	));
	}*/
	
		public static Map<String,String> fullAddress(String postcode, String identifier){
			String APIData = APICalls.getData("https://api.getAddress.io/find/"+postcode+"/"+identifier+"?api-key=GJUIdYuj6UiW-Atc5lR_uQ18432");
			return parseAddress(APIData);
		}
		
		public static boolean getDistance(String start, String destination, int minimumDistance) {
			String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+start+"&destinations="+destination+"&key=AIzaSyDaoTS-UJ0jeuB0kDkGCtBtl7A3KoybsmU";
			String APIData = APICalls.getData(URL);
			int calculatedDistance = parseDistance(APIData);
			if (calculatedDistance < minimumDistance*1000) {
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
			Map<String, String> fullAddress = new HashMap<String, String>();
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