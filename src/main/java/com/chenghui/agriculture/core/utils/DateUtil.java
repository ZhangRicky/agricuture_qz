package com.chenghui.agriculture.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	
	/**
	 * 如果当前系统时间=数据库时间往后推迟3个月的时间就触发
	 * @param dateStr
	 * @return
	 */
	public static int getCompareTo(String dateStr){
		
		 SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Calendar c1=Calendar.getInstance();//数据库时间
		 Calendar c2=Calendar.getInstance();//获取当前时间
		 int as = 0;//默认时间相等
		 try {
			Date today = format.parse(dateStr);
			long time=0;
			int day=90;
			time=(today.getTime()/1000)+60*60*24*day;
			Date newDate=new Date();
			newDate.setTime(time*1000);  
			c1.setTime(newDate);
			c2.setTime(new Date());
			as = c1.compareTo(c2);//==0开始触发
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return as;
	}

	/**
	 * 把一个毫秒时间转换成字符形式
	 *
	 * @param mill
	 *            long
	 * @return String
	 */
	public static final String LongToStr(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return SDF.format(new Date(mill));
	}

	public static final String LongToStrDate(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		return SDF.format(new Date(mill));
	}

	public static final Date LongToDate2(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		return getStringToDate(SDF.format(new Date(mill)));
	}

	public static final Date LongToDate(long mill) {
		// SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new Date(mill);
	}

	public static String formatDateTime(Date date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = myformat.format(date);
		return datetime;
	}

	public static String formatDateTime22(Date date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = myformat.format(date);
		return datetime;
	}

	public static Date formatString2Date(String date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return myformat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getDateAdd1(String dd) {
		long ti = getDateLong(dd);
		ti += 1000l * 60 * 60 * 24;
		dd = LongToStr(ti);
		Date da = getStringToDate(dd);
		return da;
	}

	/**
	 * uuid产生随机数
	 * 
	 * @return String
	 */
	public static String createKey() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 返回当前时间的毫秒数
	 *
	 * @return Long
	 */
	public static final long getDateLong() {
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		String datetime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime());
		return getDateLong(datetime1);
	}

	public static Date getPreDate() {
		long da = getDateLong() - (1000l * 60 * 60 * 24);
		return LongToDate2(da);
	}

	public static String getPreNDate(long time) {
		long da = getDateLong() - (time);
		return LongToStrDate(da);
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

	public static List<String> getEvaluateMonth(int num) {
		SimpleDateFormat sdfYYYYMM = new SimpleDateFormat("yyyyMM");

		List<String> monthList = new ArrayList<String>();
		Date currentMonth = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentMonth);
		calendar.add(Calendar.MONTH, num);
		Date planMonthDate = calendar.getTime();
		calendar.setTime(currentMonth);
		if (num > 0) {
			while (!planMonthDate.before(calendar.getTime())) {
				monthList.add(sdfYYYYMM.format(calendar.getTime()));
				calendar.add(Calendar.MONTH, 1);
			}
		} else {
			while (!planMonthDate.after(calendar.getTime())) {
				monthList.add(sdfYYYYMM.format(calendar.getTime()));
				calendar.add(Calendar.MONTH, -1);
			}
			Collections.reverse(monthList);
		}
		return monthList;
	}

	public static List<String> getNextEvaluateMonth(int num) {
		List<String> relist = new ArrayList<String>();
		String da = getCurrentDateMonth();
		String curdaString = da;
		relist.add(da);
		for (int i = 0; i < num; i++) {
			curdaString = getDateNextMonth(curdaString);
			relist.add(curdaString);
		}
		return relist;
	}

	public static List<String> getPreEvaluateMonth(int num) {
		List<String> list = new ArrayList<String>();
		List<String> relist = new ArrayList<String>();
		String da = getCurrentDateMonth();
		String curdaString = da;
		for (int i = 0; i < num; i++) {
			curdaString = getDatePreMonth(curdaString);
			list.add(curdaString);
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			relist.add(list.get(i));
		}
		return relist;
	}

	public static Date getNextDate() {
		long da = getDateLong() + (1000l * 60 * 60 * 24);
		return LongToDate2(da);
	}

	public static String getPreMonthDate() {
		long da = getDateLong() - (1000l * 60 * 60 * 24 * 30);
		return LongToStrDate(da);
	}

	public static Date getNextDate(String date) {
		long da = getDateLong(date) + (1000l * 60 * 60 * 24);
		return LongToDate2(da);
	}

	/**
	 * 返回指定时间的毫秒数 参数格式：2003-06-20 00:00:00
	 *
	 * @return Long
	 */
	/*
	 * public static final long getDateLong(String date) { DateFormat d1 =
	 * DateFormat.getDateTimeInstance(); long time = 0; try { Date date2 =
	 * d1.parse(date); time = date2.getTime(); } catch (Exception e) {
	 * e.printStackTrace(); } return time; }
	 */

	public static final long getDateLong(String date) {
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		try {
			Date date2 = d1.parse(date);
			time = date2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static final long getDateLong2(String date) {
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
		long time = 0;
		try {
			Date date2 = d1.parse(date);
			time = date2.getTime();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			;
		}
		return time;
	}

	/**
	 * 返回两个毫秒时间的相差天数
	 *
	 * @return Long 1毫秒=1000毫秒
	 */
	public static long getDateDays(long date1, long date2) {
		long days = 0;
		long day = 86400000l;
		long dateCha = date1 - date2;
		if (dateCha < 0)
			dateCha = dateCha * -1;
		days = dateCha / day;
		return days;
	}

	/**
	 * 得到两个日期之间相差的天数
	 *
	 * @param newDate
	 *            大的日期
	 * @param oldDate
	 *            小的日期
	 * @return newDate-oldDate相差的天数
	 */
	public static int daysBetweenDates(Date newDate, Date oldDate) {
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(oldDate);
		caln.setTime(newDate);
		int oday = calo.get(Calendar.DAY_OF_YEAR);
		int nyear = caln.get(Calendar.YEAR);
		int oyear = calo.get(Calendar.YEAR);
		while (nyear > oyear) {
			calo.set(Calendar.MONTH, 11);
			calo.set(Calendar.DATE, 31);
			days = days + calo.get(Calendar.DAY_OF_YEAR);
			oyear = oyear + 1;
			calo.set(Calendar.YEAR, oyear);
		}
		int nday = caln.get(Calendar.DAY_OF_YEAR);
		days = days + nday - oday;
		if (days < 0)
			days = days * -1;
		return days;
	}

	/**
	 * 取本地系统时间（日期值 英文）
	 *
	 * @return Date
	 */
	public static java.util.Date getCurrentDate2() {
		java.util.Date date = new java.util.Date();
		// 取本地系统时间：2000-10-27 09:36:58
		return date;// 返回本地系统时间：2000/10/27 09:36:58
	}

	/**
	 * 取本地系统时间（日期值 数字）
	 *
	 * @return Date
	 */
	public static java.sql.Date getCurrentDate() {
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		// 取本地系统时间：2000-10-27 09:36:58
		return date;// 返回本地系统时间：2000/10/27 09:36:58
	}

	public static String getCurrentDateString() {
		TimeZone tz = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(tz);
		String datetime1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());

		return datetime1;
	}

	/**
	 * 取本地系统时间（字符串值）
	 *
	 * @return String
	 */
	public static String getCurrentTime() {
		java.util.Date date = new java.util.Date();
		// 取本地系统时间：2000-10-27 09:36:58
		String currTime = DateFormat.getDateTimeInstance().format(date);
		return currTime;// 返回本地系统时间：2000/10/27 09:36:58
	}

	/**
	 * 格式如1999-12-02的当前时间
	 *
	 * @return String
	 */
	public static String getCurrentDateTime() {
		TimeZone tz = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(tz);
		String datetime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime());

		return datetime1;
	}

	public static String getCurrentDatePreMonth(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.split("-")[0]);
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.split("-")[1]);
			month = Integer.parseInt(mm.format(date2));
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}
			if ((month + "").length() == 1)
				return year + "0" + month;
			return year + "" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getDatePreMonth(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.substring(0, 4));
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.substring(4, 6));
			month = Integer.parseInt(mm.format(date2));
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}
			if ((month + "").length() == 1)
				return year + "0" + month;
			return year + "" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getDateNextMonth(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.substring(0, 4));
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.substring(4, 6));
			month = Integer.parseInt(mm.format(date2));

			if (month == 12) {
				year += 1;
				month = 1;
			} else {
				month += 1;
			}
			if ((month + "").length() == 1)
				return year + "0" + month;
			return year + "" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getCurrentDatePreMonth2(String dateTime) {
		int year = 0;
		int month = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = sdf.parse(dateTime.split("-")[0]);
			year = Integer.parseInt(sdf.format(date));
			DateFormat mm = new SimpleDateFormat("MM");
			Date date2 = mm.parse(dateTime.split("-")[1]);
			month = Integer.parseInt(mm.format(date2));
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}
			if ((month + "").length() == 1)
				return year + "-0" + month;
			return year + "-" + month;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getCurtDate() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		currenttime = dd.format(new java.util.Date());

		return getStringToDate(currenttime);
	}

	public static String getStrDate(String strDate) {

		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat d2 = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			Date date = d2.parse(strDate);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			return d1.format(c.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStrDateSGS(String strDate) {

		SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat d1 = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = d2.parse(strDate);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			return d1.format(c.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据字符串日期返回Date日期
	 *
	 * @param String
	 *            日期
	 * @return Date 日期yyyy-MM-dd
	 */
	public static Date getStringToDate(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getStringToDate2(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			date = sdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getStringToDate4(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String currenttime = "";
		try {
			date = sdf.parse(dateTime);
			DateFormat dd = new SimpleDateFormat("yyyy-MM");
			currenttime = dd.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currenttime;
	}

	public static String getStringToDate5(String dateTime) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String currenttime = "";
		try {
			date = sdf.parse(dateTime);
			DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
			currenttime = dd.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currenttime;
	}

	public static String getCurrentDateTime2(Date d) {
		String currenttime = null;
		try {
			DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			currenttime = dd.format(d);
		} catch (Exception e) {
			DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
			currenttime = dd.format(d);
		}
		return currenttime;
	}

	public static String getCurrentDate4() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateMonth() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMM");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDate5() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMddHH");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDate6() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMdd");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateDay() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMM");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateTime2() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMddHHmmss");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static String getCurrentDateTime3() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("yyyyMMddHHmm");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static final long getDateLong5(String date) {
		DateFormat dd = new SimpleDateFormat("yyyyMMddHH");
		long time = 0;
		try {
			Date date2 = dd.parse(date);
			time = date2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static final String LongToDate3(long mill) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHH");
		return SDF.format(new Date(mill));
	}

	public static String formatDateTime2(Date date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		DateFormat myformat = new SimpleDateFormat("yyyyMMddHH");
		String datetime = myformat.format(date);
		return datetime;
	}

	// 获得当前时间的前一个小时
	public static String getCurrentDatePreHour() {
		String dateString = getCurrentDate5();
		long time = getDateLong5(dateString);
		time = time - (1000l * 60 * 60);
		String datetime = LongToDate3(time);
		return datetime;
	}

	public static String getDayTime() {
		String currenttime = null;
		DateFormat dd = new SimpleDateFormat("HH:mm:ss");
		currenttime = dd.format(new java.util.Date());
		return currenttime;
	}

	public static int getdayHours() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getday() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static Date DateAddMinutes(Date date, int plus) {
		Date rtnDate = date;
		rtnDate.setTime(date.getTime() + 1000 * 60 * plus);
		return rtnDate;
	}

	/**
	 * 获得指定年月
	 * 
	 * @param num
	 *            >0 取num个月 =0当前月
	 * @return yyyyMM
	 */
	public static String getNextYearMonth(int num) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		if (num != 0) {
			// c.add(Calendar.MONTH, num);
			c.set(Calendar.MONTH, num - 1);
		}
		return dd.format(c.getTime());
	}

	/**
	 * 获得当前时间整5分 向前取整
	 * 
	 * @param num
	 *            >0 向后取num*5分 =0当前整五分 <0向前取num*5分
	 * @return yyyy-MM-dd HH:mm:00
	 */
	public static String getNextMinute(int num) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("HHmm");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - c.get(Calendar.MINUTE)
				% 5);
		if (num != 0) {
			c.add(Calendar.MINUTE, num);
		}
		return dd.format(c.getTime());
	}

	/**
	 * 获得当前时间整5分 向前取整
	 * 
	 * @param num
	 *            >0 向后取num*5分 =0当前整五分 <0向前取num*5分
	 * @return yyyy-MM-dd HH:mm:00
	 */
	public static String getNextDate(int num, int minute) {
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(zone);
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - c.get(Calendar.MINUTE)
				% minute);
		c.set(Calendar.SECOND, 0);
		if (num != 0) {
			c.add(Calendar.MINUTE, num);
		}
		return dd.format(c.getTime());
	}

	/**
	 * @author yudq
	 * @param minute
	 *            整数, 可以取到整5分时间值
	 * @return yyyyMMddHHmm 例如201207071335
	 */
	public static String getCurrentDateTime(int minute) {
		return getCurrentDate6() + getNextMinute(minute);
	}

	/**
	 * 获得本周开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfThisWeek(Date date,
			int firstDayOfThisLocale) {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal.setTime(date);
		cal.add(Calendar.DATE, firstDayOfThisLocale - dayOfWeek);
		return cal.getTime();
	}

	public static Date getLastDateOfThisWeek(Date date, int firstDayOfThisLocale) {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal.setTime(date);
		cal.add(Calendar.DATE, firstDayOfThisLocale - dayOfWeek + 7);
		return cal.getTime();
	}

	public static Date getFirstDateOfThisMonth(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf_long = new SimpleDateFormat("yyyyMMdd");
		String dateString = sdf.format(date) + "01";
		return sdf_long.parse(dateString);
	}

	public static Date getLastDateOfThisMonth(Date date) throws ParseException {
		Date firstDateOfThisMonth = getFirstDateOfThisMonth(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(firstDateOfThisMonth);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}

	
	/**
	 * 判断字符串是不是日期格式
	 * @param IDStr
	 * @author LLJ
	 * @return
	 */
	public static String checkValidDate(String IDStr) throws ParseException{   
		String errorInfo = "";// 记录错误信息   
	    String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4","3", "2" };   
	    String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7","9", "10", "5", "8", "4", "2" };   
	    String Ai = "";
	    // ================ 号码的长度 15位或18位 ================   
        if (IDStr.length() != 15 && IDStr.length() != 18) {   
            errorInfo = "身份证号码长度应该为15位或18位。";   
            return errorInfo;   
        }  
        //================ 数字 除最后以为都为数字 ================   
        if (IDStr.length() == 18) {   
            Ai = IDStr.substring(0, 17);   
        } else if (IDStr.length() == 15) {   
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);   
        }   
        if (isNumeric(Ai) == false) {   
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";   
            return errorInfo;   
        } 
        // ================ 出生年月是否有效 ================   
        String strYear = Ai.substring(6, 10);// 年份   
        String strMonth = Ai.substring(10, 12);// 月份   
        String strDay = Ai.substring(12, 14);// 月份   
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {   
            errorInfo = "身份证生日无效。";   
            return errorInfo;   
        }   
        GregorianCalendar gc = new GregorianCalendar();   
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");   
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150  
                || (gc.getTime().getTime() - s.parse(   
                        strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {   
            errorInfo = "身份证生日不在有效范围。";   
            return errorInfo;   
        }   
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {   
            errorInfo = "身份证月份无效";   
            return errorInfo;   
        }   
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {   
            errorInfo = "身份证日期无效";   
            return errorInfo;   
        } 
        int TotalmulAiWi = 0;   
        for (int i = 0; i < 17; i++) {   
            TotalmulAiWi = TotalmulAiWi   
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))   
                    * Integer.parseInt(Wi[i]);   
        }   
        int modValue = TotalmulAiWi % 11;   
        String strVerifyCode = ValCodeArr[modValue];   
        Ai = Ai + strVerifyCode;   
  
        if (IDStr.length() == 18) {   
             if (Ai.equals(IDStr) == false) {   
                 errorInfo = "身份证无效，不是合法的身份证号码";   
                 return errorInfo;   
             }   
         } else {   
             return "";   
         } 
        return "";
	}
	
	public static boolean isDataFormat(String str){
		  boolean flag=false;
		  String regxStr="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		  Pattern pattern1=Pattern.compile(regxStr);
		  Matcher isNo=pattern1.matcher(str);
		  if(isNo.matches()){
		   flag=true;
		  }
		  return flag;
	}
	
	 /**  
     * 功能：判断字符串是否为数字  
     * @param str  
     * @return  
     */  
    private static boolean isNumeric(String str) {   
        Pattern pattern = Pattern.compile("[0-9]*");   
        Matcher isNum = pattern.matcher(str);   
        if (isNum.matches()) {   
            return true;   
        } else {   
            return false;   
        }   
    }
}
