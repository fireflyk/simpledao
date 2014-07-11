package com.codinghero.sql.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateUtils {

	public static String format(Date date, String pattern) {
		return new SimpleDateFormat(pattern, Locale.CHINESE).format(date);
	}

	private static Date format(String dateStr, String sdf) {
		try {
			return new SimpleDateFormat(sdf).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	public static Date getCurDate() {
		return new Date();
	}

	public static String getCurDateStr() {
		return getDateStr(new Date());
	}

	public static String getCurDateStr(String format) {
		return getDateStr(new Date(), format);
	}

	public static Date getDate(String dateStr) {
		return format(dateStr, "yyyy-MM-dd");
	}

	public static Date getDate(String dateStr, String format) {
		return format(dateStr, format);
	}

	public static String getDateStr(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	public static String getDateStr(Date date, String format) {
		return format(date, format);
	}

	public static Date getCurTime() {
		return new Date();
	}

	public static String getCurTimeStr() {
		return getTimeStr(new Date());
	}

	public static Date getTime(String dateStr) {
		return format(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getTime(String dateStr, String format) {
		return format(dateStr, format);
	}

	public static String getTimeStr(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getTimeStr(Date date, String format) {
		return format(date, format);
	}

	public static String getYear(Date date) {
		return format(date, "yyyy");
	}

	public static String getMonth(Date date) {
		return format(date, "MM");
	}

	public static String getDay(Date date) {
		return format(date, "dd");
	}

	public static String getHour(Date date) {
		return format(date, "HH");
	}

	public static String getMinute(Date date) {
		return format(date, "mm");
	}

	public static String getSecond(Date date) {
		return format(date, "ss");
	}

	public static String getCurYear() {
		return getYear(new Date());
	}

	public static String getCurMonth() {
		return getMonth(new Date());
	}

	public static String getCurDay() {
		return getDay(new Date());
	}

	public static String getCurHour() {
		return getHour(new Date());
	}

	public static String getCurMinute() {
		return getMinute(new Date());
	}

	public static String getCurSecond() {
		return getSecond(new Date());
	}

	public static Date lastMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR, -12);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date thisMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR, -12);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	public static int compareTo(Date d1, Date d2) {
		if (getTimeStr(d1).equals(getTimeStr(d2)))
			return 0;
		else
			return d1.compareTo(d2);
	}
}
