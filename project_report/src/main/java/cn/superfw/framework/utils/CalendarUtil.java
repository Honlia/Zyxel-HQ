package cn.superfw.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	private static final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat df_year = new SimpleDateFormat("yyyy");

	public static String getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return df.format(calendar.getTime());

	}

	public static Date parseDate(String dateStr) {
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatDate(Date date) {
	    if (date == null) {
	        return "";
	    }
		return df.format(date);
	}

	public static String formatDateTime(Date date) {
	    if (date == null) {
            return "";
        }
		return dt.format(date);
	}

	public static String getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return df_year.format(calendar.getTime());
	}

	public static Date getSystemTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/** 获取两个月前的今天 */
	public static Date getPreTwoMonthStartDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		return calendar.getTime();
	}

	public static boolean isEquals(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		return calendar1.equals(calendar2);
	}

	public static boolean isInTwoMonth(Date start, Date end) {
		Calendar calendarStart = getCalendar();
		Calendar calendarEnd = getCalendar();
		Calendar calendarYearFirst = getCalendar();

		calendarStart.setTime(start);
		calendarEnd.setTime(end);
		calendarYearFirst.setTime(getFirstDayOfYear());

		// 如果开始日是去年,则开始日按今年第一天算
		if (calendarStart.before(calendarYearFirst)) {
			calendarStart = calendarYearFirst;
		}

		calendarStart.add(Calendar.MONTH, 2);
		if (calendarStart.after(calendarEnd) || calendarStart.equals(calendarEnd)) {
			return true;
		}
		return false;
	}

	public static boolean isInTwoQuarter(Date start, Date end) {
		Calendar calendarStart = getCalendar();
		Calendar calendarEnd = getCalendar();
		Calendar calendarYearFirst = getCalendar();

		calendarStart.setTime(start);
		calendarEnd.setTime(end);
		calendarYearFirst.setTime(getFirstDayOfYear());

		// 如果开始日是去年,则开始日按今年第一天算
		if (calendarStart.before(calendarYearFirst)) {
			calendarStart = calendarYearFirst;
		}

		calendarStart.add(Calendar.MONTH, 6);
		if (calendarStart.after(calendarEnd) || calendarStart.equals(calendarEnd)) {
			return true;
		}
		return false;
	}


	public static Date getFirstDayOfYear() {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		return calendar.getTime();
	}

	public static Date getLastDayOfYear() {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 31);
		return calendar.getTime();
	}


	/** 获取当前季度的第一天 */
	public static String getFirstDayOfQuarter() {
		Calendar calendar = getCalendar();
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		try {
            if (currentMonth >= 1 && currentMonth <= 3)
            	calendar.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
            	calendar.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
            	calendar.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
            	calendar.set(Calendar.MONTH, 9);
            calendar.set(Calendar.DATE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return df.format(calendar.getTime());
	}

	/** 获取当前季度的最后一天 */
	public static String getLastDayOfQuarter() {
		Calendar calendar = getCalendar();
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
                calendar.set(Calendar.MONTH, 2);
                calendar.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                calendar.set(Calendar.MONTH, 5);
                calendar.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                calendar.set(Calendar.MONTH, 8);
                calendar.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DATE, 31);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return df.format(calendar.getTime());
	}

	/** 获取当前周的第一天 */
	public static String getFirstDayOfWeek(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return df.format(calendar.getTime());
	}

	/** 获取当前周的最后一天 */
	public static String getLastDayOfWeek(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return df.format(calendar.getTime());
	}

	/** 获取当前审核周的第一天 */
	public static String getFirstDayOfExamineWeek(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return df.format(calendar.getTime());
	}

	/** 获取当前审核周的最后一天 */
	public static String getLastDayOfExamineWeek(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return df.format(calendar.getTime());
	}

	/** 获取当前月的第一天 */
	public static String getFirstDayOfMonth(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		return df.format(calendar.getTime());
	}

	/** 获取当前月的最后一天 */
	public static String getLastDayOfMonth(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return df.format(calendar.getTime());
	}

	/** 获取前月的第一天 */
	public static String getFirstDayOfPreMonth(){
		Calendar calendar = getCalendar();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		return df.format(calendar.getTime());
	}

	/** 获取前月的最后一天 */
	public static String getLastDayOfPreMonth(){
		Calendar calendar = getCalendar();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return df.format(calendar.getTime());
	}

	public static boolean isFirstMonthOfYear() {

		Calendar currentDay = getCalendar();
		Calendar firstDayOfYear = getCalendar();

		firstDayOfYear.setTime(getFirstDayOfYear());

		return (currentDay.get(Calendar.MONTH) == firstDayOfYear.get(Calendar.MONTH));

	}



	private static Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance();
		calendar.setLenient(false);
		return calendar;
	}


	public static void main(String[] args){


	    //System.out.println(isInTwoMonth(parseDate("2015-08-16"), parseDate("2015-10-15")));

	    //System.out.println(formatDate(getWeekExamineDay(parseDate("2015-11-14"))));
	    //System.out.println(isMonthBatchExecuteDay(parseDate("2015-11-02")));

/*		System.out.println(u.getFirstDayOfMonth());
		System.out.println(u.getLastDayOfMonth());
		System.out.println(u.getFirstDayOfExamineWeek());
		System.out.println(u.getLastDayOfExamineWeek());
		System.out.println(u.getFirstDayOfQuarter());
		System.out.println(u.getLastDayOfQuarter());
		System.out.println(u.getPreTwoMonthStartDay());
		System.out.println(u.isPreTwoMonthStartDayInCurrentYear());
		System.out.println(u.getFirstDayOfPreMonth());
		System.out.println(u.getLastDayOfPreMonth());
		System.out.println(u.isFirstMonthOfYear());*/
	}
}
