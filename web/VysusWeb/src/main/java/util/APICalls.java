package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class APICalls {

	public void getLatLong(){}

	public static void main(String[] args) {
	    try {
	        APICalls.call_me("cm34rl","ab245dj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			   
		public static void call_me(String start,String destination) throws Exception {
			String testPostcode = "cm34rl";
		     //String url = "https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=47.6044,-122.3345;47.6731,-122.1185;47.6149,-122.1936&destinations=45.5347,-122.6231;47.4747,-122.2057&travelMode=driving&key=AoWX66pYythZ2v4yWfGFEHOhpGYEtVNTq5CX3UfB1skxrWBmWMbQEgP0aPR_tejX";
		     String url = "http://api.postcodes.io/postcodes/" + testPostcode;
			 URL obj = new URL(url);
		     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		     // optional default is GET
		     con.setRequestMethod("GET");
		     //add request header
		     con.setRequestProperty("User-Agent", "Mozilla/5.0");
		     int responseCode = con.getResponseCode();
		     System.out.println(con.getContent());
		     String inline = null;
		     if(responseCode != 200)
		    	 System.out.println("Didn't work sorry");
		    	 else
		    	 {

					Scanner sc = new Scanner(obj.openStream());
					while(sc.hasNext()) {
						inline+=sc.nextLine();
					}
					System.out.println("\nJSON data in string format");
					System.out.println(inline);
					sc.close();
		    	 }
		     
		     JSONParser parse = new JSONParser(); 
		     
		     JSONObject jobj = (JSONObject)parse.parse(inline); 
		     
		     JSONArray jsonarr_1 = (JSONArray) jobj.get("results"); 
		     for(int i=0;i<jsonarr_1.size();i++)
				{
					//Store the JSON objects in an array
					//Get the index of the JSON object and print the values as per the index
					JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
					//Store the JSON object in JSON array as objects (For level 2 array element i.e Address Components)
					JSONArray jsonarr_2 = (JSONArray) jsonobj_1.get("address_components");
					System.out.println("Elements under results array");
					System.out.println("\nPlace id: " +jsonobj_1.get("place_id"));
					System.out.println("Types: " +jsonobj_1.get("types"));
					//Get data for the Address Components array
					System.out.println("Elements under address_components array");
					System.out.println("The long names, short names and types are:");
					for(int j=0;j<jsonarr_2.size();j++)
					{
						//Same just store the JSON objects in an array
						//Get the index of the JSON objects and print the values as per the index
						JSONObject jsonobj_2 = (JSONObject) jsonarr_2.get(j);
						//Store the data as String objects
						String str_data1 = (String) jsonobj_2.get("long_name");
						System.out.println(str_data1);
						String str_data2 = (String) jsonobj_2.get("short_name");
						System.out.println(str_data2);
						System.out.println(jsonobj_2.get("types"));
						System.out.println("\n");
					}
					
				}
					//Disconnect the HttpURLConnection stream
					con.disconnect();
		}
}
