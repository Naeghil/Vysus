package util;

import java.util.HashMap;
import java.util.Map;

/*************************************
 *  Converts data and maps safely	 *
 ************************************/

public class DataConv {
	public static Map<String, String> getStringMap(Object map) {
		Map<?, ?> temp;
		Map<String, String> converted = new HashMap<String, String>();
		String key;
		String value;
		if(map instanceof HashMap<?,?>) temp = (HashMap<?, ?>)map;
		else return null;
		for(Object keyO : temp.keySet()) {
			if(keyO instanceof String) key = (String)keyO;
			else return null;
			Object valueO = temp.get(keyO);
			if(valueO instanceof String) value = (String)valueO;
			else return null;
			converted.put(key, value);
		}
		return converted;
	}
	public static Map<String, Object> getObjectMap(Object map){
		Map<?, ?> temp;
		Map<String, Object> converted = new HashMap<String, Object>();
		String key;
		if(map instanceof HashMap<?,?>) temp = (HashMap<?, ?>)map;
		else return null;
		for(Object keyO : temp.keySet()) {
			if(keyO instanceof String) key = (String)keyO;
			else return null;
			converted.put(key, temp.get(keyO));
		}
		return converted;
	}
}
