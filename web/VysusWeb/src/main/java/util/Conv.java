package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import storage.InvalidDataException;

/*************************************
 *  Converts data and maps safely	 *
 ************************************/

public class Conv {
	public static Date stringToDate(String date) throws InvalidDataException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try { 
			return new Date(format.parse(date).getTime()); 
		} catch(ParseException e) {
			throw new InvalidDataException(e, "date", "This is an invalid dateAAAAAAAAAAAaa");
		}
	}
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(new java.util.Date(date.getTime()));
	}
	
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
	public static List<Object> getList(Object list) {
		List<?> temp;
		List<Object> converted = new ArrayList<Object>();
		if(list instanceof ArrayList<?>) temp = (ArrayList<?>) list;
		else return null;
		for(Object item : temp) converted.add(item);
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
	//Used for display purposes:
	public static Map<String, String> makeStringMap(Map<String, Object> map) {
		Map<String, String> converted = new HashMap<String, String>();
		for(String key : map.keySet()) converted.put(key, map.get(key).toString());
		return converted;
	}
	//??
	public static String getString(Object string) {
		if(string instanceof String) return (String)string;
		else return null;
	}
}
