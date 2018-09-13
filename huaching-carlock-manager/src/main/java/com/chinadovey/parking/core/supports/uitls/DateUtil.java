package com.chinadovey.parking.core.supports.uitls;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.util.Assert;

public class DateUtil {

	public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";

	public static final String PATTERN_DATE = "yyyy-MM-dd";

	/**
	 * 这个方法有问题，避免出错，暂不删除,请使用{@link DateUtil#getCurrentDate()}<br>
	 * 得到当前系统日期 author: YT
	 * @return 当前日期的格式字符串,日期格式为"yyyy-MM-dd HH:mm:ss"
	 */
	@Deprecated
	public static Date getCurrentDateTime() {
//		SimpleDateFormat df = new SimpleDateFormat(PATTERN_STANDARD);
		Date today = new Date();
		return today;
	}

	/**
	 * 这个方法有问题，避免出错，暂不删除
	 * 得到当前系统日期 author: YT
	 * @return 当前日期的格式字符串,日期格式为"yyyy-MM-dd"
	 */
	public static Date getCurrentDate() {
		return new Date();
	}
	
	/**
	 * <p>返回一个格式化的日期
	 * <p>e.g.
	 * <li>yyyy 返回 2014
	 * <li>MM 返回 02
	 * <li>dd 返回 29
	 * <li>HH:mm 返回11:58
	 * <p>and so on.
	 * @param pattern yyyy-MM-dd HH:mm:SSS 日期格式化
	 * @return
	 */
	public static String getCurrentDate(String pattern){
		return new SimpleDateFormat(pattern).format(new Date());
	}

	public static String timestamp2String(Timestamp timestamp, String pattern) {
		if (timestamp == null) {
			throw new java.lang.IllegalArgumentException(
					"timestamp null illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timestamp.getTime()));
	}

	public static String date2String(java.util.Date date, String pattern) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException(
					"timestamp null illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;

		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/*
	 * public static Timestamp currentTimestamp() { return new Timestamp(new
	 * Date().getTime()); }
	 */

	/*
	 * public static String currentTimestamp2String(String pattern) { return
	 * timestamp2String(currentTimestamp(), pattern); }
	 */
	public static Timestamp string2Timestamp(String strDateTime, String pattern) {
		return new Timestamp(string2Date(strDateTime,pattern).getTime());
	}

	public static Date string2Date(String strDate, String pattern) {
		if (strDate == null || strDate.equals("")) {
			throw new RuntimeException("str date null");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = DateUtil.PATTERN_DATE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String stringToYear(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return String.valueOf(c.get(Calendar.YEAR));
	}

	public static String stringToMonth(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// return String.valueOf(c.get(Calendar.MONTH));
		int month = c.get(Calendar.MONTH);
		month = month + 1;
		if (month < 10) {
			return "0" + month;
		}
		return String.valueOf(month);
	}

	public static String stringToDay(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			return "0" + day;
		}
		return "" + day;
	}

	public static Date getFirstDayOfMonth(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = 1;
		c.set(year, month, day, 0, 0, 0);
		return c.getTime();
	}

	public static Date getLastDayOfMonth(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = 1;
		if (month > 11) {
			month = 0;
			year = year + 1;
		}
		c.set(year, month, day - 1, 0, 0, 0);
		return c.getTime();
	}

	public static String date2GregorianCalendarString(Date date) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException("Date is null");
		}
		long tmp = date.getTime();
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTimeInMillis(tmp);
		try {
			XMLGregorianCalendar t_XMLGregorianCalendar = DatatypeFactory
					.newInstance().newXMLGregorianCalendar(ca);
			return t_XMLGregorianCalendar.normalize().toString();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException("Date is null");
		}

	}

	public static boolean compareDate(Date firstDate, Date secondDate) {
		if (firstDate == null || secondDate == null) {
			throw new java.lang.RuntimeException();
		}

		String strFirstDate = date2String(firstDate, "yyyy-MM-dd");
		String strSecondDate = date2String(secondDate, "yyyy-MM-dd");
		if (strFirstDate.equals(strSecondDate)) {
			return true;
		}
		return false;
	}

	public static Date getStartTimeOfDate(Date currentDate) {
		Assert.notNull(currentDate);
		String strDateTime = date2String(currentDate, "yyyy-MM-dd")
				+ " 00:00:00";
		return string2Date(strDateTime, "yyyy-MM-dd hh:mm:ss");
	}

	public static Date getEndTimeOfDate(Date currentDate) {
		Assert.notNull(currentDate);
		String strDateTime = date2String(currentDate, "yyyy-MM-dd")
				+ " 23:59:59";
		return string2Date(strDateTime, "yyyy-MM-dd hh:mm:ss");
	}
//
//	public static void main(String[] args) {
//		String str1 = "2011-01-01" + " 00:00:00";
//		String str2 = "1988-09-09";
//		System.out.println(DateUtil.string2Timestamp(str1, null));
//		System.out.println(DateUtil.string2Timestamp(str1, null).getTime());
//		// Date date1 = DateUtil.string2Date(str1, "yyyy-MM-dd");
//		// Date date2 = DateUtil.string2Date(str2, "yyyy-MM-dd");
//		// Calendar c1 = Calendar.getInstance();
//		// Calendar c2 = Calendar.getInstance();
//		// c1.setTime(date1);
//		// c2.setTime(date2);
//		// c2.add(Calendar.YEAR, 4);
//		// if (c2.before(c1)) {
//		// System.out.println("illegal");
//		// }else {
//		// System.out.println("ok");
//		// }
//	}
}