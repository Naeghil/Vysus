package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
			   
		public static void call_me(String start,String destination){
			String testPostcode = "cm34rl";
		    //String url = "https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=47.6044,-122.3345;47.6731,-122.1185;47.6149,-122.1936&destinations=45.5347,-122.6231;47.4747,-122.2057&travelMode=driving&key=AoWX66pYythZ2v4yWfGFEHOhpGYEtVNTq5CX3UfB1skxrWBmWMbQEgP0aPR_tejX";
		    String url = "http://api.postcodes.io/postcodes/" + testPostcode;
			URL obj;
			try {
				obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			    // optional default is GET
				con.openConnection();
			    con.setRequestMethod("GET");
			    con.setConnectionTimeout();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		}
}
