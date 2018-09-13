package com.chinadovey.parking.core.supports.uitls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 * @author Administrator
 *
 */
public final class DateUtils {
	/**
	 * String 转化 Date 0 yyyy-MM-dd 1 yyyy-MM-dd HH 2 yyyy-MM-dd HH:mm 3
	 * yyyy-MM-dd HH:mm:ss 4 yyyy-MM 5 yyyy else yyyy-MM-dd
	 * 
	 * @param time
	 * @return
	 */
	public static Date stringConvertDate(String time, int temp) {
		if (time == null || "".equals(time)) {
			return new Date();
		}

		SimpleDateFormat sdf = null;

		if (temp == 0) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else if (temp == 1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		} else if (temp == 2) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (temp == 3) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (temp == 4) {
			sdf = new SimpleDateFormat("yyyy-MM");
		} else if (temp == 5) {
			sdf = new SimpleDateFormat("yyyy");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}

		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public static Date stringConvertDate(String date, String pattern) {
		if (date == null || date.isEmpty()) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}

	/**
	 * Date 转化 String
	 * 
	 * @param time
	 * @return
	 */
	public static String dateConvertString(Date time, int temp) {
		if (time == null) {
			return null;
		}

		SimpleDateFormat sdf = null;

		if (temp == 0) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.format(time);

		} else if (temp == 1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			sdf.format(time);

		} else if (temp == 2) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			sdf.format(time);
		} else if (temp == 3) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.format(time);
		} else if (temp == 4) {
			sdf = new SimpleDateFormat("yyyy-MM");
			sdf.format(time);
		} else if (temp == 5) {
			sdf = new SimpleDateFormat("yyyy");
			sdf.format(time);
		} else if (temp == 6) {
			sdf = new SimpleDateFormat("MM");
			sdf.format(time);
		} else if (temp == 7) {
			sdf = new SimpleDateFormat("dd");
			sdf.format(time);
		} else if (temp == 8) {
			sdf = new SimpleDateFormat("HH");
			sdf.format(time);
		} else if (temp == 9) {
			sdf = new SimpleDateFormat("mm");
			sdf.format(time);
		} else if (temp == 10) {
			sdf = new SimpleDateFormat("HH:mm");
			sdf.format(time);
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.format(time);
		}

		try {
			return sdf.format(time);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * java.util.Date 转化 java.sql.Date();
	 * 
	 * @param time
	 * @return
	 */
	public static java.sql.Date dateConvertSqlDate(Date time) {
		if (time == null) {
			return null;
		}
		long date = time.getTime();
		return new java.sql.Date(date);
	}

	/**
	 * 获取距离当天的日期 把日期往后增加一天.整数往后推,负数往前移动
	 * 
	 * @param day
	 * @return
	 */
	public static Date getNeedDate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 *
	 * 
	 * @param day
	 * @return
	 */
	public static String getConvertDateString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 根据pattern的格式取日期字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getConvertDateToString(Date date, String pattern) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 
	 * 将参数内时间移动 y年M 月 d 日 h 时 m 分 s 秒个单位。
	 * 
	 * @author T-LUWEN
	 * @param date
	 * @param i
	 */
	@SuppressWarnings("static-access")
	public static Date getNeedDate(Date date, String time, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if ("y".equals(time)) {
			calendar.add(calendar.YEAR, i);
		} else if ("M".equals(time)) {
			calendar.add(calendar.MONTH, i);
		} else if ("d".equals(time)) {
			calendar.add(calendar.DATE, i);
		} else if ("h".equals(time)) {
			calendar.add(calendar.HOUR, i);
		} else if ("m".equals(time)) {
			calendar.add(calendar.MINUTE, i);
		} else if ("s".equals(time)) {
			calendar.add(calendar.SECOND, i);
		} else if("ms".equals(time)){
			calendar.add(calendar.MILLISECOND, i);
		}
		return calendar.getTime();
	}

	/**
	 * 算出两个时间相隔的天数.
	 * 
	 * @author T-LUWEN
	 * @param scInDate
	 * @param scActualInDate
	 * @return
	 */
	public static int getBetweenDays(Date fDate, Date tDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String sdate1 = sdf.format(fDate);
		String sdate2 = sdf.format(tDate);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(sdate1);
			date2 = sdf.parse(sdate2);
			return (int) ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 基于日期算出两个时间相隔的天数.
	 * 
	 * @author T-LUWEN
	 * @param scInDate
	 * @param scActualInDate
	 * @return
	 */
	public static int getBetweenDaysByDay(Date fDate, Date tDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sdate1 = sdf.format(fDate);
		String sdate2 = sdf.format(tDate);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(sdate1);
			date2 = sdf.parse(sdate2);
			return (int) ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取两个时间段之间的分钟数
	 * 
	 * @param fDate
	 * @param tDate
	 * @return
	 */
	public static int getBetweenMinutes(Date fDate, Date tDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sdate1 = sdf.format(fDate);
		String sdate2 = sdf.format(tDate);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(sdate1);
			date2 = sdf.parse(sdate2);
		} catch (ParseException e) {
			return -1;
		}
		return (int) ((date1.getTime() - date2.getTime()) / (60 * 1000l));
	}

	/**
	 * 获取两个时间段之间的分钟数
	 * 
	 * @param fDate
	 * @param tDate
	 * @return
	 */
	public static int getBetweenMinutesByTime(Date fDate, Date tDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sdate1 = sdf.format(fDate);
		String sdate2 = sdf.format(tDate);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(sdate1);
			date2 = sdf.parse(sdate2);
		} catch (ParseException e) {
			return -1;
		}
		return (int) ((date1.getTime() - date2.getTime()) / (60 * 1000l));
	}

	/**
	 * 返回指定日期是哪一年的哪个季度，map.get("年份")，map.get("季度")
	 * 
	 * @param date
	 * @return
	 */
	public static Map<String, Integer> getQuarter(Date date) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String str = dateConvertString(date, "yyyy-MM");
		String[] strs = str.split("-");
		Integer quarter = 0;
		if (Integer.parseInt(strs[1]) < 4) {
			quarter = 1;
		} else if (3 < Integer.parseInt(strs[1]) && Integer.parseInt(strs[1]) < 7) {
			quarter = 2;
		} else if (7 < Integer.parseInt(strs[1]) && Integer.parseInt(strs[1]) < 10) {
			quarter = 3;
		} else {
			quarter = 4;
		}
		map.put("年份", Integer.parseInt(strs[0]));
		map.put("季度", quarter);
		return map;
	}

	/**
	 * 返回指定日期是季度日期 time1 , time2
	 * 
	 * @param date
	 * @return
	 */
	public static String[] getQuarterTime(Date date) {

		Map<String, Integer> map = getQuarter(date);
		String[] str = new String[2];
		Integer quarter = map.get("季度");
		if (quarter == null) {
			quarter = 1;
		}
		switch (quarter) {
		case 1:
			str[0] = "01-01";
			str[1] = "03-31";
			break;
		case 2:
			str[0] = "04-01";
			str[1] = "06-30";
			break;
		case 3:
			str[0] = "07-01";
			str[1] = "09-30";
			break;
		case 4:
			str[0] = "10-01";
			str[1] = "12-31";
			break;
		default:
			break;
		}
		return str;
	}

	/**
	 * 返回指定日期是哪一年的第几个星期，map.get("年份")，map.get("星期")
	 * 
	 * @param date
	 * @return
	 */
	public static Map<String, Integer> getWeek(Date date) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String str = dateConvertString(date, "yyyy");
		Integer week = cal.get(Calendar.WEEK_OF_YEAR);
		map.put("年份", Integer.parseInt(str));
		map.put("星期", week);
		return map;
	}

	/**
	 * 判断两个时间的大小，第一个参数大于等于的二个参数时，返回true
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean toJudge(Date date1, Date date2) {
		long _date1 = 0L;
		long _date2 = 0L;
		if (date1 != null) {
			_date1 = date1.getTime();
		}
		if (date2 != null) {
			_date2 = date2.getTime();
		}
		return (_date1 >= _date2);
	}

	public static void main(String[] args) {
		Date date1 = stringConvertDate("1970-01-01 01:18:00", 3);
		Date date2 = stringConvertDate("1970-01-02 23:07:39", 3);
		System.out.println(getBetweenDaysByDay(date1, date2));
	}

	/**
	 * Date 转化 String
	 * 
	 * @param time
	 * @return
	 */
	public static String dateConvertString(Date time, String temp) {
		if (time == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(temp);
		sdf.format(time);

		try {
			return sdf.format(time);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据日期 格式为 yyyy-MM 返回该月最大天数
	 * 
	 * @param date
	 * @return int
	 */
	public static int getDayNumber(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 根据时间字符串 如 2014-10、2014-01-01返回该时间断的开始（0） 和结束（1） 仅支持 年 月 日
	 * 
	 * @param time
	 * @param x
	 * @return Date
	 */
	public static Date startOrEnd(String time, int x) {
		if (time.length() == 4) {
			if (x == 0) {
				return stringConvertDate(time + "-01-01 00:00:00", 3);
			} else if (x == 1) {
				return stringConvertDate(time + "-12-31 23:59:59", 3);
			}
		} else if (time.length() == 7) {
			if (x == 0) {
				return stringConvertDate(time + "-01 00:00:00", 3);
			} else if (x == 1) {
				String str = time + "-" + getDayNumber(stringConvertDate(time, 4));
				return stringConvertDate(str + " 23:59:59", 3);
			}
		} else if (time.length() == 10) {
			if (x == 0) {
				return stringConvertDate(time + " 00:00:00", 3);
			} else if (x == 1) {
				return stringConvertDate(time + " 23:59:59", 3);
			}
		}
		return null;
	}
	public static Date startOrEnd(Date time, String dateType,int x) {
		int temp = 0;
		if("day".equals(dateType)){
			temp = 0;
		}else if("month".equals(dateType)){
			temp = 4;
		}else if("year".equals(dateType)){
			temp = 5;
		}
		String date = dateConvertString(time,temp);
		return startOrEnd(date,x);
	}
	/**
	 * 将完整日期格式 转换成时间格式 2015-10-10 10:10:10 转成 10:10:10
	 * 
	 * @param date
	 * @return
	 */
	public static Date datetimeInterceptionTime(Date date) {
		String timeStr = dateConvertString(date, "HH:mm:ss");
		return stringConvertDate(timeStr, "HH:mm:ss");
	}

	/**
	 * 将完整日期格式 转换成日期格式 2015-10-10 10:10:10 转成 2015-10-10
	 * 
	 * @param date
	 * @return
	 */
	public static Date datetimeInterceptionDate(Date date) {
		String timeStr = dateConvertString(date, "yyyy-MM-dd");
		return stringConvertDate(timeStr, "yyyy-MM-dd");
	}
	public static Date dateInterceptionDatetime(Date date) {
		String timeStr = dateConvertString(date, "yyyy-MM-dd HH:mm:ss");
		return stringConvertDate(timeStr, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 计算两个时间段的交叉段
	 * 
	 * @param startTime1
	 * @param endTime1
	 * @param startTime2
	 * @param endTime2
	 * @return
	 */
	public static int countCrossTime(Date startTime1, Date endTime1, Date startTime2, Date endTime2) {
		int minute;
		if (startTime1.after(endTime2) && endTime1.before(startTime2)) {
			minute = 0;
		} else {
			if (startTime1.after(startTime2)) {
				if (endTime1.after(endTime2)) {
					minute = DateUtils.getBetweenMinutesByTime(endTime2, startTime1);
				} else {
					minute = DateUtils.getBetweenMinutesByTime(endTime1, startTime1);
				}
			} else {
				if (endTime1.after(endTime2)) {
					minute = DateUtils.getBetweenMinutesByTime(endTime2, startTime2);
				} else {
					minute = DateUtils.getBetweenMinutesByTime(endTime1, startTime2);
				}
			}
		}
		return minute;
	}

	/**
	 * 字符串根据类型和位置进行转换
	 * 
	 * @param timeStr   时间字符串
	 * @param dateType  日期类型
	 * @param position  位置
	 * @return
	 */
	public static Date stringFormatDateByTypeAndPosition(String timeStr, String dateType, int position) {
		int type = typeStrParseTypeInt(dateType);
		Date time = stringConvertDate(timeStr, type);
		return timeChangeByPosition(time, dateType, position);
	}

	private static int typeStrParseTypeInt(String dateType) {
		int type = 0;
		if (dateType.equals("year") || dateType.equals("total")) {
			type = 5;
		} else if (dateType.equals("month") || dateType.equals("season")) {
			type = 4;
		} else if (dateType.equals("day") || dateType.equals("hours")) {
			type = 0;
		} else if (dateType.equals("hour")) {
			type = 1;
		} else if (dateType.equals("minute")) {
			type = 2;
		} else if (dateType.equals("second")) {
			type = 3;
		} else {
			type = 0;
		}
		return type;

	}

	private static Date timeChangeByPosition(Date time, String dateType, int position) {
		if (dateType.equals("year") || dateType.equals("total")) {
			if (position == 1) {
				time = getNeedDate(time, "y", 1);
				time = getNeedDate(time, "s", -1);
			}
		} else if (dateType.equals("month") || dateType.equals("season")) {
			if (position == 1) {
				time = getNeedDate(time, "M", 1);
				time = getNeedDate(time, "s", -1);
			}
		} else if (dateType.equals("day") || dateType.equals("hours")) {
			if (position == 1) {
				time = getNeedDate(time, "d", 1);
				time = getNeedDate(time, "s", -1);
			}
		} else if (dateType.equals("hour")) {
			if (position == 1) {
				time = getNeedDate(time, "h", 1);
				time = getNeedDate(time, "s", -1);
			}
		} else if (dateType.equals("minute")) {
			if (position == 1) {
				time = getNeedDate(time, "m", 1);
				time = getNeedDate(time, "s", -1);
			}
		} else if (dateType.equals("second")) {
			if (position == 1) {
				time = getNeedDate(time, "s", 1);
			}
		} else {
			if (position == 1) {
				time = getNeedDate(time, "d", 1);
				time = getNeedDate(time, "s", -1);
			}
		}
		return time;
	}

	/**
	 * 根据类型得到时间
	 * @param dateType
	 * @return
	 */
	public static Date getNeedDateByDateType(String dateType) {
		Date now = new Date();
		Date now_;
		if (dateType.equals("day")) {
			now_ = getNeedDate(now, "d", -1);
		} else if (dateType.equals("week")) {
			now_ = getNeedDate(now, "d", -7);
		} else if (dateType.equals("month")) {
			now_ = getNeedDate(now, "d", -30);
		} else if (dateType.equals("threeMonth")) {
			now_ = getNeedDate(now, "d", -90);
		} else if (dateType.equals("year")) {
			now_ = getNeedDate(now, "d", -365);
		} else {
			now_ = getNeedDate(now, "d", -7);
		}
		return now_;
	}

	/**
	 * 将long转换成date
	 * @param time
	 */
	public static Date getDateByLong(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return c.getTime();
	}
	//date  转换成  String   yyyy-MM-dd hh:ss
	public static String timeToStr(java.util.Date dateDate) {
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   String dateString = formatter.format(dateDate);
		   return dateString;
		}
	public static String timeToStrTemp(java.util.Date dateDate,String temp) {
		   SimpleDateFormat formatter = new SimpleDateFormat(temp);
		   String dateString = formatter.format(dateDate);
		   return dateString;
		}
	public static Date stringTempDate(String time, int temp) {
		if (time == null || "".equals(time)) {
			return null;
		}

		SimpleDateFormat sdf = null;

		if (temp == 0) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else if (temp == 1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		} else if (temp == 2) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (temp == 3) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (temp == 4) {
			sdf = new SimpleDateFormat("yyyy-MM");
		} else if (temp == 5) {
			sdf = new SimpleDateFormat("yyyy");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}

		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public static Date getDateByType(Date date , int type){
		String dateStr = "";
		if(type==1){
			dateStr = DateUtils.dateConvertString(date, 1);//格式化为：“yyyy-MM-dd HH”
			dateStr+=":00:00";
		}else if(type==2){
			dateStr = DateUtils.dateConvertString(date, 0);//格式化为：“yyyy-MM-dd”
			dateStr+=" 00:00:00";
		}else if(type==3){
			dateStr = DateUtils.dateConvertString(date, 4);//格式化为：“yyyy-MM”
			dateStr+="-01 00:00:00";
		}else if(type==4){
			dateStr = DateUtils.dateConvertString(date, 5);//格式化为：“yyyy”
			dateStr+="-01-01 00:00:00";
		}
		//String dateStr = DateUtils.dateConvertString(date, t);
		Date dateTime = DateUtils.stringConvertDate(dateStr, 3);
		return dateTime;
	}
	
	public static Date changeType(Date date , int type){
		String dateStr = "";
		if(type==1){
			dateStr = dateConvertString(date, 1);//格式化为：“yyyy-MM-dd HH”
		}else if(type==0){
			dateStr = dateConvertString(date, 0);//格式化为：“yyyy-MM-dd”
		}else if(type==3){
			dateStr = dateConvertString(date, 4);//格式化为：“yyyy-MM”
		}else if(type==4){
			dateStr = dateConvertString(date, 5);//格式化为：“yyyy”
		}
		//String dateStr = DateUtils.dateConvertString(date, t);
		Date dateTime = stringConvertDate(dateStr, type);
		return dateTime;
	}
	public static Date getDatesByType(Date date , int type,int type2){
		
		String dateStr = DateUtils.dateConvertString(date, type);
		Date dateTime = DateUtils.startOrEnd(dateStr, type2);
		return dateTime;
	}
}
