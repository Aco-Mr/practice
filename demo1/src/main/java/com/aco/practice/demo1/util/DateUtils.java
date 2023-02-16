package com.aco.practice.demo1.util;

import com.aco.practice.demo1.exception.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	public static final long FILTER_TIME_STAMP = 1604145600000L;

	public static final String DATE10 = "yyyy-MM-dd";

	private static final FastDateFormat DATE10_DF = FastDateFormat.getInstance(DATE10);

	public static final String DATE8 = "yyyyMMdd";

	private static final FastDateFormat DATE8_DF = FastDateFormat.getInstance(DATE8);

	public static final String DATE8_LINE = "yyyy/MM/dd";

	public static final String YEARMONTH = "yyyyMM";

	private static final FastDateFormat YEARMONTH_DF = FastDateFormat.getInstance(YEARMONTH);

	public static final String YEARMONTH10 = "yyyy-MM";

	private static final FastDateFormat YEARMONTH10_DF = FastDateFormat.getInstance(YEARMONTH10);

	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

	private static final FastDateFormat DATETIME_DF = FastDateFormat.getInstance(DATETIME);

	public static final String DATETIME_ISO = "yyyy-MM-dd'T'HH:mm:ss";

	private static final FastDateFormat DATETIME_ISO_DF = FastDateFormat.getInstance(DATETIME_ISO);

	public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final FastDateFormat TIMESTAMP_DF = FastDateFormat.getInstance(TIMESTAMP);

	public static final String[] ACCEPT_DATE_FORMATS = { "yyyy/MM/dd", DATETIME, DATETIME_ISO, DATE10 };

	public static final String[] DATE_FORMATS = { DATE8_LINE, DATETIME, DATETIME_ISO, DATE10, DATE8, YEARMONTH, YEARMONTH10, };

	/**
	 * 每天毫秒数
	 */
	private static final long ND = 1000 * 24 * 60 * 60;
	/**
	 * 每小时毫秒数
	 */
	private static final long NH = 1000 * 60 * 60;
	/**
	 * 每分钟毫秒数
	 */
	private static final long NM = 1000 * 60;

	public static String formatYearMonth10(Date date) {
		return YEARMONTH10_DF.format(date);
	}
	public static String formatDate10(Date date) {
		return DATE10_DF.format(date);
	}

	public static String formatDate8(Date date) {
		return DATE8_DF.format(date);
	}

	public static String formatTimestamp(Date date) {
		return TIMESTAMP_DF.format(date);
	}

	public static String formatDatetime(Date date) {
		return DATETIME_DF.format(date);
	}

	public static String formatDatetimeISO(Date date) {
		return DATETIME_ISO_DF.format(date);
	}

	public static String formatYearMonth(Date date) {
		return YEARMONTH_DF.format(date);
	}

	public static String format(Date date, String pattern) {
		return FastDateFormat.getInstance(pattern).format(date);
	}

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTime();
	}

	public static int getIntervalDays(Date startDate, Date endDate) {
		startDate = beginOfDay(startDate);
		endDate = beginOfDay(endDate);
		return (int) TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
	}

	public static boolean isSpanLeapDay(Date startDate, Date endDate) {
		return !endDate.before(nextLeapDay(startDate));
	}

	public static Date nextLeapDay(Date since) {
		since = beginOfDay(since);
		Calendar cal = Calendar.getInstance();
		cal.setTime(since);
		int year = cal.get(Calendar.YEAR);
		if (isLeapYear(year)) {
			Date leapDay = parseDate8(year + "0229");
			if (!since.after(leapDay))
				return leapDay;
		}
		while (!isLeapYear(++year))
			;
		return parseDate8(year + "0229");
	}

	public static boolean isLeapYear(int year) {
		boolean result = false;

		if (((year % 4) == 0) &&			// must be divisible by 4...
				((year < 1582) ||				// and either before reform year...
						((year % 100) != 0) ||		// or not a century...
						((year % 400) == 0))) {		// or a multiple of 400...
			result = true;			// for leap year.
		}
		return result;
	}

	public static Date parseDate10(String string) {
		try {
			return DATE10_DF.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate8(String string) {
		try {
			return DATE8_DF.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDatetime(String string) {
		try {
			return DATETIME_DF.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseTimestamp(String string) {
		try {
			return TIMESTAMP_DF.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDatetimeISO(String string) {
		try {
			return DATETIME_ISO_DF.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parse(String string, String pattern) {
		try {
			return FastDateFormat.getInstance(pattern).parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parse(String string) {
		if (StringUtils.isNumeric(string) || string.length() >= 13)
			try {
				Long timestamp = Long.valueOf(string);
				return new Date(timestamp);
			} catch (NumberFormatException nfe) {
			}
		for (String format : ACCEPT_DATE_FORMATS) {
			try {
				return FastDateFormat.getInstance(format).parse(string);
			} catch (ParseException e) {
				continue;
			}
		}
		return null;
	}

	public static boolean isToday(Date date) {
		return isSameDay(date, new Date());
	}


	public static Date beginOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date endOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}

	public static Date beginOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date endOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MILLISECOND, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(date2);
		return year == cal.get(Calendar.YEAR) && month == cal.get(Calendar.MONTH)
				&& day == cal.get(Calendar.DAY_OF_MONTH);
	}

	public static boolean isSameDate(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		cal.setTime(date2);
		return year == cal.get(Calendar.YEAR) && month == cal.get(Calendar.MONTH)
				&& day == cal.get(Calendar.DAY_OF_MONTH) && hour == cal.get(Calendar.HOUR_OF_DAY)
				&& minute == cal.get(Calendar.MINUTE) && second == cal.get(Calendar.SECOND);
	}

	public static boolean isBeginOfDay(Date date) {
		return date.getTime() == beginOfDay(date).getTime();
	}

	public static boolean isEndOfDay(Date date) {
		return date.getTime() == endOfDay(date).getTime();
	}

	public static boolean isEndOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static boolean isEndOfYear(Date date) {
		return formatDate10(date).endsWith("12-31");
	}

	public static boolean isBeginOfMonth(Date date) {
		return formatDate10(date).endsWith("01");
	}

	public static boolean isBeginOfYear(Date date) {
		return formatDate10(date).endsWith("01-01");
	}

	public static String humanRead(Date date) {
		Date now = new Date();
		long delta = now.getTime() - date.getTime();
		boolean before = (delta >= 0);
		delta = delta < 0 ? -delta : delta;
		delta /= 1000;
		String s;
		if (delta <= 60) {
			return "1分钟内";
		} else if (delta < 3600) {
			delta /= 60;
			if (delta == 30)
				s = "半个小时";
			else
				s = delta + "分钟";
		} else if (delta < 86400) {
			double d = delta / 3600d;
			long h = (long) d;
			long m = (long) ((d - h) * 3600);
			m /= 60;
			if (m == 0)
				s = h + "个小时";
			else if (m == 30)
				s = h + "个半小时";
			else
				s = h + "个小时" + m + "分钟";
		} else if (delta < 2592000) {
			s = delta / 86400 + "天";
		} else if (delta < 31104000) {
			s = delta / 2592000 + "个月";
		} else {
			s = delta / 31104000 + "年";
		}
		return s + (before ? "前" : "后");
	}

	public static boolean isBirthday(Date date, boolean isLunar) {
		if (date == null)
			return false;
		if (!isLunar) {
			Date today = new Date();
			return formatDate8(today).substring(4).equals(formatDate8(date).substring(4));
		} else {
			Calendar cal = Calendar.getInstance();
			Lunar lunar = new Lunar(cal);
			Date today = lunar.toDate();
			return formatDate8(today).substring(4).equals(formatDate8(date).substring(4));
		}
	}

	public static Date localDateTime2Date(LocalDateTime time) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = time.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

	/**
	 * @url http://www.blogjava.net/soddabao/archive/2007/01/04/91729.html
	 * @author soddabao
	 * 
	 */
	public static class Lunar {
		private int year;
		private int month;
		private int day;
		private boolean leap;
		final static String chineseNumber[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
		static FastDateFormat chineseDateFormat = FastDateFormat.getInstance("yyyy年MM月dd日");
		final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554,
				0x056a0, 0x09ad0, 0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2,
				0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2,
				0x04970, 0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
				0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
				0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50,
				0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
				0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0,
				0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50,
				0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0,
				0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0,
				0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
				0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520,
				0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

		// ====== 传回农历 y年的总天数
		final private static int yearDays(int y) {
			int i, sum = 348;
			for (i = 0x8000; i > 0x8; i >>= 1) {
				if ((lunarInfo[y - 1900] & i) != 0)
					sum += 1;
			}
			return (sum + leapDays(y));
		}

		// ====== 传回农历 y年闰月的天数
		final private static int leapDays(int y) {
			if (leapMonth(y) != 0) {
				if ((lunarInfo[y - 1900] & 0x10000) != 0)
					return 30;
				else
					return 29;
			} else
				return 0;
		}

		// ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
		final private static int leapMonth(int y) {
			return (int) (lunarInfo[y - 1900] & 0xf);
		}

		// ====== 传回农历 y年m月的总天数
		final private static int monthDays(int y, int m) {
			if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
				return 29;
			else
				return 30;
		}

		// ====== 传回农历 y年的生肖
		final public String animalsYear() {
			final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };
			return Animals[(year - 4) % 12];
		}

		// ====== 传入 月日的offset 传回干支, 0=甲子
		final private static String cyclicalm(int num) {
			final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
			final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
			return (Gan[num % 10] + Zhi[num % 12]);
		}

		// ====== 传入 offset 传回干支, 0=甲子
		final public String cyclical() {
			int num = year - 1900 + 36;
			return (cyclicalm(num));
		}

		/**
		 * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
		 * dayCyl5:与1900年1月31日相差的天数,再加40 ?
		 * 
		 * @param cal
		 * @return
		 */
		public Lunar(Calendar cal) {
			@SuppressWarnings("unused")
			int yearCyl, monCyl, dayCyl;
			int leapMonth = 0;
			Calendar baseCalendar = Calendar.getInstance();
			baseCalendar.set(Calendar.YEAR, 1900);
			baseCalendar.set(Calendar.MONTH, 0);
			baseCalendar.set(Calendar.DAY_OF_MONTH, 31);
			baseCalendar.set(Calendar.HOUR, 0);
			baseCalendar.set(Calendar.MINUTE, 0);
			baseCalendar.set(Calendar.SECOND, 0);
			// 求出和1900年1月31日相差的天数
			int offset = (int) ((cal.getTime().getTime() - baseCalendar.getTimeInMillis()) / 86400000L);
			dayCyl = offset + 40;
			monCyl = 14;

			// 用offset减去每农历年的天数
			// 计算当天是农历第几天
			// i最终结果是农历的年份
			// offset是当年的第几天
			int iYear, daysOfYear = 0;
			for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
				daysOfYear = yearDays(iYear);
				offset -= daysOfYear;
				monCyl += 12;
			}
			if (offset < 0) {
				offset += daysOfYear;
				iYear--;
				monCyl -= 12;
			}
			// 农历年份
			year = iYear;

			yearCyl = iYear - 1864;
			leapMonth = leapMonth(iYear); // 闰哪个月,1-12
			leap = false;

			// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
			int iMonth, daysOfMonth = 0;
			for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
				// 闰月
				if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
					--iMonth;
					leap = true;
					daysOfMonth = leapDays(year);
				} else
					daysOfMonth = monthDays(year, iMonth);

				offset -= daysOfMonth;
				// 解除闰月
				if (leap && iMonth == (leapMonth + 1))
					leap = false;
				if (!leap)
					monCyl++;
			}
			// offset为0时，并且刚才计算的月份是闰月，要校正
			if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
				if (leap) {
					leap = false;
				} else {
					leap = true;
					--iMonth;
					--monCyl;
				}
			}
			// offset小于0时，也要校正
			if (offset < 0) {
				offset += daysOfMonth;
				--iMonth;
				--monCyl;
			}
			month = iMonth;
			day = offset + 1;
		}

		public static String getChinaDayString(int day) {
			String chineseTen[] = { "初", "十", "廿", "卅" };
			int n = day % 10 == 0 ? 9 : day % 10 - 1;
			if (day > 30)
				return "";
			if (day == 10)
				return "初十";
			else
				return chineseTen[day / 10] + chineseNumber[n];
		}

		public Date toDate() {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			return cal.getTime();
		}

		@Override
		public String toString() {
			return year + "年" + (leap ? "闰" : "") + chineseNumber[month - 1] + "月" + getChinaDayString(day);
		}

	}




	/**
	 * 获取当月开始时间戳
	 *
	 * @param timeStamp 毫秒级时间戳
	 * @param timeZone  如 GMT+8:00
	 * @return
	 */
	public static Long getMonthStartTime(Long timeStamp, String timeZone) {
		Calendar calendar = Calendar.getInstance();// 获取当前日期
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.setTimeInMillis(timeStamp);
		calendar.add(Calendar.YEAR, 0);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}


	/**
	 * 获取当月的结束时间戳
	 *
	 * @param timeStamp 毫秒级时间戳
	 * @param timeZone  如 GMT+8:00
	 * @return
	 */
	public static Long getMonthEndTime(Long timeStamp, String timeZone) {
		Calendar calendar = Calendar.getInstance();// 获取当前日期
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.setTimeInMillis(timeStamp);
		calendar.add(Calendar.YEAR, 0);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 		获取当前月最后一天
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取指定年、月的结束时间戳(获取月份最后一天的最后时间戳)
	 *
	 * @param timeStamp 毫秒级时间戳
	 * @param timeZone  如 GMT+8:00
	 * @return
	 */
	public static Long getYearAndMonthEndTime(int year,int month, String timeZone) {
		if (StringUtils.isBlank(timeZone)){
			timeZone = "GMT+8:00";
		}
		Calendar calendar = Calendar.getInstance();// 获取当前日期
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 		获取当前月最后一天
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}


	/**
	 * 获取指定某一天的开始时间戳
	 *
	 * @param timeStamp 毫秒级时间戳
	 * @param timeZone  如 GMT+8:00
	 * @return
	 */
	public static Long getDailyStartTime(Long timeStamp, String timeZone) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.setTimeInMillis(timeStamp);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取指定某一天的开始时间戳
	 *
	 * @param hour 毫秒级时间戳
	 * @return
	 */
	public static Long getDailyHourTime(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取指定某一天的结束时间戳
	 *
	 * @param timeStamp 毫秒级时间戳
	 * @param timeZone  如 GMT+8:00
	 * @return
	 */
	public static Long getDailyEndTime(Long timeStamp, String timeZone) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.setTimeInMillis(timeStamp);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	public static int convert(Date endDate, Date nowDate) {

		long nd = 1000 * 24 * 60 * 60;
		//long nh = 1000 * 60 * 60;
		//long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		//long hour = diff % nd / nh;
		// 计算差多少分钟
		//long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		//return day+ "天" + hour + "小时" + min + "分钟";
		return (int) day;
	}

	/**
	 *   获取2个时间之间的时间差
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String dateDiff(Date startTime, Date endTime,Long usedTime) {
        // 获得两个时间的毫秒时间差异
		long diff = endTime.getTime() - startTime.getTime()-usedTime;
		boolean flag = diff > 0 ;
		diff = Math.abs(diff);
		// 计算差多少天
		long day = diff / ND;
		// 计算差多少小时
		long hour = diff % ND / NH;
		// 计算差多少分钟
		long min = diff % ND % NH / NM;
		StringBuilder sb = new StringBuilder();
		if(!flag){
			sb.append("-");
		}
		if(day != 0){
			sb.append(day);
			sb.append("工作日");
		}
		if(day == 0 && hour == 0){
		}else {
			sb.append(hour);
			sb.append("小时");
		}
		sb.append(min);
		sb.append("分钟");
		return sb.toString();
	}

	/**
	 * 获取两个时间差的天数（只获取天数）
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long dateDiffDay(long startTime,long endTime){
		// 获得两个时间的毫秒时间差异
		long diff = endTime - startTime;
		diff = Math.abs(diff);
		// 计算差多少天
		return diff / ND;
	}

	/**
	 * 获取两个时间差的小时数（只获取剩余的小时）
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long dateDiffHour(long startTime,long endTime){
		// 获得两个时间的毫秒时间差异
		long diff = endTime - startTime;
		diff = Math.abs(diff);
		// 计算差多少小时
		return diff % ND / NH;
	}

	/**
	 * 获取两个时间差的分钟数（只获取剩余的分钟）
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long dateDiffMinute(long startTime,long endTime){
		// 获得两个时间的毫秒时间差异
		long diff = endTime - startTime;
		diff = Math.abs(diff);
		// 计算差多少分钟
		return diff % ND % NH / NM;
	}



	/**
	 *  获取2个时间之间的毫秒数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long msDiff(Date startTime, Date endTime,long usedTime) {
		return endTime.getTime() - startTime.getTime() + usedTime;
	}


	public static String dateDiff(long ms) {
		boolean flag = ms > 0 ;
		ms = Math.abs(ms);
		// 计算差多少天
		long day = ms / ND;
		// 计算差多少小时
		long hour = ms % ND / NH;
		// 计算差多少分钟
		long min = ms % ND % NH / NM;
		StringBuilder sb = new StringBuilder();
		if(!flag){
			sb.append("-");
		}
		if(day != 0){
			sb.append(day);
			sb.append("工作日");
		}
		if(day == 0 && hour == 0){
		}else {
			sb.append(hour);
			sb.append("小时");
		}
		sb.append(min);
		sb.append("分钟");
		return sb.toString();
	}


	/**
	 * 时间戳转LocalDate
	 * @param timestamp
	 * @return
	 */
	public static LocalDate timestampToLocalDate(long timestamp){
		return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
	}

	/**
	 * 时间戳转LocalDateTime
	 * @param timestamp
	 * @return
	 */
	public static LocalDateTime timestampToLocalDateTime(long timestamp){
		return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
	}

	/**
	 * LocalDate转Date
	 * @param localDate
	 * @return
	 */
	public static Date localDateToDate(LocalDate localDate){
		return Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
	}

	/**
	 * LocalDateTime转Date
	 * @param localDateTime
	 * @return
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime){
		return Date.from(localDateTime.atZone(ZoneOffset.ofHours(8)).toInstant());
	}

	/**
	 * LocalDate转时间戳
	 * @param localDate
	 * @return
	 */
	public static long localDateToTimestamp(LocalDate localDate){
		return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
	}

	/**
	 * LocalDateTime转时间戳
	 * @param localDateTime
	 * @return
	 */
	public static long localDateTimeToTimestamp(LocalDateTime localDateTime){
		return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
	}

	/**
	 * LocalDate转LocalDate
	 * @param date
	 * @return
	 */
	public static LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * LocalDate转LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * 分钟装换成小时
	 * @param min
	 * @param decimalPoint 小数点：0.00
	 * @return
	 */
	public static String minuteToHour(long min,String decimalPoint){
		if (StringUtils.isBlank(decimalPoint)){
			decimalPoint = "0.00";
		}
		DecimalFormat decimalFormat = new DecimalFormat(decimalPoint);
		return decimalFormat.format((float) min/60);
	}

	/**
	 * 指定日期格式，格式化日期
	 * @param localDate
	 * @param formatter
	 * @return
	 */
	public static String localDateFormat(LocalDate localDate,String formatter){
		if (StringUtils.isBlank(formatter) || ObjectUtils.isEmpty(localDate)){
			return "";
		}
		List<String> strings = Arrays.asList(DATE_FORMATS);
		if (DATETIME.equals(formatter) || DATETIME_ISO.equals(formatter) || !strings.contains(formatter)){
			throw new CustomException("The current date format is not supported");
		}
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(formatter);
		return pattern.format(localDate);
	}

	/**
	 * 指定日期格式，格式化日期
	 * @param localDateTime
	 * @param formatter
	 * @return
	 */
	public static String localDateTimeFormat(LocalDateTime localDateTime,String formatter){
		if (StringUtils.isBlank(formatter) || ObjectUtils.isEmpty(localDateTime)){
			return "";
		}
		List<String> strings = Arrays.asList(DATE_FORMATS);
		if (!strings.contains(formatter)){
			throw new CustomException("The current date format is not supported");
		}
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(formatter);
		return pattern.format(localDateTime);
	}

	public static void main(String[] args) {
//		long start = 1597289202127L;
//		long end = 1597462002000L;
//		System.out.println(dateDiffDay(start,end) + "天" + dateDiffHour(start,end) + "小时" + dateDiffMinute(start,end) + "分钟");
//		System.out.println(minuteToHour(dateDiffMinute(start, end),null) + "小时");
//		double date = dateDiffHour(start, end);
//		System.out.println(date + "小时");
//		System.out.println(dateDiffDay(start,end) + "天" + ((double) dateDiffHour(start, end) + Double.valueOf(minuteToHour(dateDiffMinute(start, end),null)) + "小时"));
//		System.out.println(DoubleUtil.mul(Double.valueOf(minuteToHour(dateDiffMinute(start, end),null)),3.0));
//		System.out.println(DoubleUtil.sub(37.5,37.4));
//		System.out.println(Math.round(34.4));
//		Long yearAndMonthEndTime = getYearAndMonthEndTime(2019, 3, null);
//		System.out.println(yearAndMonthEndTime);
		LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(new Date(1598954760000L));
		System.out.println(localDateTime.toLocalDate().atStartOfDay().plusDays(1));
		System.out.println(LocalDateTime.now().getMonthValue() + ":"+LocalDateTime.now().getDayOfMonth());
		System.out.println(localDateTime.getMonthValue()+":"+localDateTime.getDayOfMonth());
		System.out.println(localDateTime.getDayOfYear());
	}
}
