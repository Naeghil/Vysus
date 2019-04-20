package util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import exceptions.InvalidDataException;

/*************************************
 *  Converts data safely	 *
 ************************************/

public class Conv {
	public static Date stringToDate(String date) throws InvalidDataException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try { 
			return new Date(format.parse(date).getTime()); 
		} catch(ParseException e) {
			throw new InvalidDataException(e, "date", "This is an invalid date");
		}
	}
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(new java.util.Date(date.getTime()));
	}
}
