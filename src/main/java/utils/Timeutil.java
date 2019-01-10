package utils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Timeutil {
	public static String currentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}
	public static String currentTime2() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	public static String thisYearTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		return df.format(new Date());
	}
	public static String currentDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	public static Date stringToDateTime(String value) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String datestringToDateTime(String value) {
		long deadline = Long.parseLong(value);
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		return df2.format(new Date(deadline));
	}
	public static String stringToString(String value) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
		Date date=df2.parse(value);
		return df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String HowLongTime(String value) {
		Date date=stringToDateTime(value);
		long endTime = date.getTime();  
		long Time = new Date().getTime();
		int minutes = (int) ((Time - endTime) / (1000 * 60));
		if(minutes<60) {
			if(minutes<1) {
				minutes=1;
			}
			return minutes+"分钟前";
		}else if(minutes<1440) {
			minutes =(int) ((Time - endTime) / (1000 * 60 * 60));
			return minutes+"小时前";
		}else {
			 minutes=(int) ((Time - endTime) / (1000 * 60 * 60 * 24));
			return minutes+"天前";
		}
	}
	
	
	public static int getMonthDiff(Date d1, Date d2) {
		System.out.println("d1:"+new SimpleDateFormat("yyyy-MM-dd").format(d1)+",d2:"+new SimpleDateFormat("yyyy-MM-dd").format(d2));
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int month2 = c2.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
        // 获取月数差值
        int monthInterval =  (month1 + 12) - month2  ;
        if(day1 < day2) monthInterval --;
        monthInterval %= 12;
        if(day2 < 15) {monthInterval++;}
        return yearInterval * 12 + monthInterval;
	}
	
	public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
	
	public static String getDateDay(String value,String day) {
		Date date=stringToDateTime(value+" 00:00:00");
		long endTime = date.getTime()+(1000 * 60 * 60 * 24*Long.parseLong(day));
		return datestringToDateTime(String.valueOf(endTime));
	}
	public static long getDateTime(String value) {
		Date date=stringToDateTime(value);
		return	date.getTime();
	}
	
	
	
	/**
	 * 获取某时间的当月的最后一天
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static String getLast(String day) throws ParseException {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date date =sdf.parse(day);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
	     String last = sdf.format(calendar.getTime());
	     return last;
	}
	
	
	
	/**
	 * 获取某日期的星期，0为星期日，其他的对应1~6
	 * @param date
	 * @return
	 */
	public static int getDayofweek(String date){
	  Calendar cal = Calendar.getInstance();
	  if (date.equals("")) {
	   cal.setTime(new Date(System.currentTimeMillis()));
	  }else {
	   cal.setTime(new Date(getDateByStr2(date).getTime()));
	  }
	   return cal.get(Calendar.DAY_OF_WEEK)-1;
	}
	
	
	
	public static Date getDateByStr2(String dd){
	 
	  SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	  Date date;
	  try {
	   date = sd.parse(dd);
	  } catch (ParseException e) {
	   date = null;
	   e.printStackTrace();
	  }
	  return date;
	 }
	
	public static String getStringOfDateNotLine() {
		String time = currentTime();
		time = time.replace("-", "");
		time = time.replace(":", "");
		time = time.replace(" ", "");
		return time;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getStringOfDateNotLine());
	}
	
	/**
	 * 获取日期，例如num为1则获取的为明天，num为-1则为昨天
	 * @param num
	 * @return
	 */
	public static String getTomorrow(Integer num) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(calendar.DATE,num);
		String date2= sdf.format(calendar.getTime());
		System.out.println(date2);
		return date2;
	}
	
	
	
	
	
	
}
