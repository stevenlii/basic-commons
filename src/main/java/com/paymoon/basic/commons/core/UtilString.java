package com.paymoon.basic.commons.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UtilString {
	private static Logger logger = LogManager.getLogger();
	/**
	 * 
	 */
	public UtilString() {
		super();
	}

	public static boolean isNotEmpty(String str){
		if(str!=null&&!"".equals(str.trim()))
			return true;
		return false;
	}
	public static boolean isEmpty(String str){
		if(str!=null&&!"".equals(str.trim()))
			return false;
		return true;
	}
	public static String getString(String str) {
		return str == null ? "" : str.trim();
	}
	public static String getString(String str,int length){
		String tmp=getString(str);
		if(tmp.getBytes().length>length){
			tmp=splitString(tmp,length);
		}
		if(tmp.getBytes().length>length+4)
			tmp=trim(str,length);
		return tmp;
	}

	public static String convertDateToStr(Date date){
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        String temp=df.format(date);
	        return df.format(date);
	}
	/**
	 * 
	 * @param java.util.Date
	 * @param String format "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String convertDateToStr(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat switchDate = new SimpleDateFormat(format);
		try {
			return switchDate.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public static String convertTimeToStr(Time time){
		return convertTimeToStr(time,false);
	}
	public static String convertTimeToStr(Time time,boolean includeSecond){
		String result="";
		try{
			SimpleDateFormat switchDate =null;
			if(includeSecond)
				switchDate=new SimpleDateFormat("HH:mm:ss");
			else
				switchDate=new SimpleDateFormat("HH:mm");
			try {
				result= switchDate.format(time);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @param java.util.Date
	 * @param String format "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String convertTimestampToStr(Timestamp date) {
		return convertTimestampToStr(date,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * @param java.util.Date
	 * @param String format "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String convertTimestampToStr(Timestamp date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat switchDate = new SimpleDateFormat(format);
		try {
			return switchDate.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param strDate yyyy-MM-dd h24:mi:ss
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp convertStrToTimestamp(String strTimestamp) {
		if (strTimestamp == null || "".equals(strTimestamp.trim()))
			return null;
		SimpleDateFormat switchDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date;
		Timestamp ts = null;
		try {
			date = switchDate.parse(strTimestamp);
			Calendar   cal   =   Calendar.getInstance(); 
			cal.setTime(date);
			ts=new Timestamp(cal.getTimeInMillis());
		} catch (ParseException e) {
			switchDate = new SimpleDateFormat(
			"yyyy-MM-dd");
			try {
				date = switchDate.parse(strTimestamp);
				Calendar   cal   =   Calendar.getInstance(); 
				cal.setTime(date);
				ts=new Timestamp(cal.getTimeInMillis());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return ts;
	}
	public static Date convertStrToDate(String strDate) {
		Date date=convertStrToDate(strDate,"yyyy-MM-dd");
		if(date==null)
			date=convertStrToDate(strDate,"yyyy/MM/dd");
		return date;
	}
	public static Date convertStrToDate(String strDate,String format) {
		if (strDate == null || "".equals(strDate.trim()))
			return null;
		if(format==null)
			format="yyyy-MM-dd";
		SimpleDateFormat switchDate = new SimpleDateFormat(format);
		Date date=null;
		try {
			return date = switchDate.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static Time convertStrToTime(String strTime) {
		if (strTime == null || "".equals(strTime.trim()))
			return null;
		Time time=null;
		try{
			String strArr[]=strTime.split(":");
			int hour=0;
			int minute=0;
			int second=0;
			for(int i=0;i<strArr.length;i++){
				if(i==0)
				{
					hour=Integer.valueOf(strArr[i]);
				}
				if(i==1)
				{
					minute=Integer.valueOf(strArr[i]);
				}
				if(i==2)
				{
					second=Integer.valueOf(strArr[i]);
				}
			}
			Calendar   cal   =   Calendar.getInstance();
			cal.set(1970, 0, 1, hour, minute, second);
			time=new Time(cal.getTimeInMillis());
		}catch(Exception e){
			e.printStackTrace();
		}
		return time;
	}
	public static List<String> convertStrToList(String str){
		List<String> list=null;
		if(checkIsNull(str)){
			return list;
		}
		list=new ArrayList<String>();
		String strArray[]=str.split(",");
		for(String tmpStr:strArray){
			list.add(getString(tmpStr));
		}
		return list;
	}
	public static String convertListToDbStr(List<String> idList){
		StringBuffer sb=new StringBuffer("");
		boolean blnFirst=true;
		if(idList!=null&&idList.size()>0){
			for(String tmpId:idList){
				if(!blnFirst){
					sb.append(",");
				}else
					blnFirst=false;
				sb.append("'"+tmpId+"'");
			}
		}
		return sb.toString();
	}
	public static String convertToGBK(String s) {
		if (s == null || s.length() == 0)
			return s;
		byte[] b;
		try {
			b = s.getBytes("ISO8859_1");
			for (int i = 0; i < b.length; i++)
				if (b[i] + 0 < 0)
					return new String(b, "GBK");
			b = s.getBytes("GBK");
			for (int i = 0; i < b.length; i++)
				if (b[i] + 0 < 0)
					return new String(b, "GBK");
		} catch (Exception e) {
		}
		return s;
	}
	public static String convertToUTF8(String s) {
		if (s == null || s.length() == 0)
			return s;
		byte[] b;
		try {
			b = s.getBytes("ISO8859_1");
			for (int i = 0; i < b.length; i++)
				if (b[i] + 0 < 0)
					return new String(b, "UTF-8");
			b = s.getBytes("UTF-8");
			for (int i = 0; i < b.length; i++)
				if (b[i] + 0 < 0)
					return new String(b, "UTF-8");
		} catch (Exception e) {
		}
		return s;
	}

	public static String encodingURL(String url){
		try {
			return java.net.URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		} catch(Exception e){
			return "";
		}
	}
	public static String decodingURL(String url){
		try {
			return java.net.URLDecoder.decode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		} catch(Exception e){
			return "";
		}
	}
	
	public static boolean checkIsNull(String str) {
		boolean result = true;
		if (str != null && !"".equals(str.trim()))
			result = false;
		return result;
	}

	public static String getStatusCn(int status) {
		String cntxt = "";
		if (status == 1)
			cntxt = "valid";
		else
			cntxt = "unvalid";
		return cntxt;
	}

	public final static String convertUrl(String str) {
		if (str.indexOf("%") != -1)
			str = repbfhao(str);
		if (str.indexOf("#") != -1)
			str = rep(str, "#", "%23");
		if (str.indexOf("&") != -1)
			str = rep(str, "&", "%26");
		if (str.indexOf("+") != -1)
			str = rep(str, "+", "%2B");
		if (str.indexOf("\\") != -1)
			str = rep(str, "\\", "%2F");
		if (str.indexOf("=") != -1)
			str = rep(str, "=", "%3D");
		if (str.indexOf("*") != -1)
			str = rep(str, "*", "%2A");
		if (str.indexOf("'") != -1)
			str = rep(str, "'", "%27");
		if (str.indexOf("?") != -1)
			str = rep(str, "?", "%3F");
		if (str.indexOf(" ") != -1)
			str = rep(str, " ", "%20");
		return str;

	}

	private static String repbfhao(String str) {
		String tmpStr = str;
		int pos = -1;
		String newStr = "";

		while ((pos = tmpStr.indexOf("%")) != -1) {
			newStr += tmpStr.substring(0, pos);
			if ((pos + 3) <= tmpStr.length()) {
				String checkStr = tmpStr.substring(pos, pos + 3);
				if (isSpecChar(checkStr)) {
					newStr += checkStr;
					tmpStr = tmpStr.substring(pos + 3, tmpStr.length());
				} else {
					newStr += "%25";
					tmpStr = tmpStr.substring(pos + 1, tmpStr.length());
				}
			} else {
				try {
					newStr += "%25";
					tmpStr = tmpStr.substring(pos + 1, tmpStr.length());
				} catch (Exception ex) {
					System.out.println("++" + ex);
				}
			}
		}
		newStr += tmpStr;
		return newStr;
	}

	/** is specal char */
	private static boolean isSpecChar(String str) {
		if ("%23".equals(str) || "%26".equals(str) || "%2B".equals(str)
				|| "%2F".equals(str) || "%3D".equals(str) || "%2A".equals(str)
				|| "%27".equals(str) || "%3F".equals(str) || "%20".equals(str)
				|| "%25".equals(str)) {
			return true;
		}
		return false;
	}

	public final static String rep(String str, String r, String n) {
		return StringUtils.replace(str, r, n);
	}

	public static String getValidHql(String hql) {
		if (hql == null)
			return "";
		return rep(hql, "'", "''");
	}

	public static String getDateFromStr(String date) {
		try {
			if (date != null && !"".equals(date.trim())) {
				int pos = date.indexOf(" ");
				String beginDate = date.substring(0, pos).trim();
				return beginDate;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getHourFromStr(String date) {
		try {
			if (date != null && !"".equals(date.trim())) {
				int pos = date.indexOf(" ");
				if (pos == -1)
					return "";
				String beginTime = date.substring(pos + 1, date.length())
						.trim();
				pos = beginTime.indexOf(":");
				if (pos == -1)
					return "";
				String beginHour = beginTime.substring(0, pos);
				return beginHour;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getMinuteFromStr(String date) {
		try {
			if (date != null && !"".equals(date.trim())) {
				int pos = date.indexOf(" ");
				if (pos == -1)
					return "";
				String beginTime = date.substring(pos + 1, date.length())
						.trim();
				pos = beginTime.indexOf(":");
				int secPos = beginTime.lastIndexOf(":");
				if (pos == -1 || secPos == -1)
					return "";
				String beginMin = "";
				if (pos != secPos) {
					beginMin = beginTime.substring(pos + 1, secPos).trim();
				} else
					beginMin = beginTime.substring(pos + 1, beginTime.length())
							.trim();
				return beginMin;
			}
		} catch (Exception ex) {
		}
		return "";
	}
	public static String encodingMD5(String password)
	{
		byte[] passBytes = password.getBytes();
		byte[] hash;
		MessageDigest digest=getDigest();
		if(digest!=null)
			hash = getDigest().digest(passBytes);
		else
			hash = passBytes;
		String passwordHash = encodeBase16(hash);
		return passwordHash;
		/*
		char hexDigits[] = "chenbolong8upyoo".toUpperCase().toCharArray();

		try {
			byte[] strTemp = str1.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}*/
	}
	public static String md5(String str1){
		char hexDigits[] = "chenbolong8upyoo".toUpperCase().toCharArray();

		try {
			byte[] strTemp = str1.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	private static String encodeBase16(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            // top 4 bits
            char c = (char) ((b >> 4) & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
            // bottom 4 bits
            c = (char) (b & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
        }
        return sb.toString();
	}

	private static MessageDigest getDigest() {
		String _hashAlgorithm="MD5";
		try {
			return MessageDigest.getInstance(_hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Algorithm not supported : " + _hashAlgorithm, e);
		}
		return null;
	}
	

	protected static String splitString(String str, int len) {
		return splitString(str, len, "...");
	}

	protected static String splitString(String str, int len, String elide) {
		if (str == null) {
			return "";
		}
		byte[] strByte;
		try {
			strByte = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			strByte = str.getBytes();
		}
		int strLen = strByte.length;
		int elideLen = (elide.trim().length() == 0) ? 0
				: elide.getBytes().length;
		if (len >= strLen || len < 1) {
			return str;
		}
		if (len - elideLen > 0) {
			len = len - elideLen;
		}
		int count = 0;
		for (int i = 0; i < len; i++) {
			int value = (int) strByte[i];
			if (value < 0) {
				count++;
			}
		}
		if (count % 3 ==1) {
			len+=2;
		}else if(count%3==2){
			len+=1;
		}
		if(len>=strByte.length)
			return str;
		return new String(strByte, 0, len) + elide.trim();
	}
	public static String convertList2Str(List<String> list){
		if(list==null||list.size()==0)
			return null;
		StringBuffer sb=new StringBuffer("");
		for(int i=0;i<list.size();i++){
			if(i>0)
				sb.append(",");
			sb.append(list.get(i));
		}
		return sb.toString();
	}
	public static List<String> convertStr2List(String str){
		if(str==null||str.trim().length()==0)
			return null;
		List<String> list=new ArrayList<String>();
		String[] strArr=str.split(",");
		for(String s:strArr){
			list.add(s.trim());
		}
		return list;
	}
	public static String convertStr2SQL(String str){
		if(str==null||str.trim().length()==0)
			return null;
		StringBuffer sb=new StringBuffer("");
		String[] strArr=str.split(",");
		for(int i=0;i<strArr.length;i++){
			if(i>0)
				sb.append(",");
			sb.append("'"+strArr[i]+"'");
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		String a="1,2,3";
		System.out.println(convertStr2SQL(a));
	}
	

    public static String trim(String str, int specialCharsLength) {  
        if (str == null || "".equals(str) || specialCharsLength < 1) {  
            return "";  
        }  
        char[] chars = str.toCharArray();  
        int charsLength = getCharsLength(chars, specialCharsLength);  
        return new String(chars, 0, charsLength);  
    }  
  
    private static int getCharsLength(char[] chars, int specialCharsLength) {  
        int count = 0;  
        int normalCharsLength = 0;  
        for (int i = 0; i < chars.length; i++) {  
            int specialCharLength = getSpecialCharLength(chars[i]);  
            if (count <= specialCharsLength - specialCharLength) {  
                count += specialCharLength;  
                normalCharsLength++;  
            } else {  
                break;  
            }  
        }  
        return normalCharsLength;  
    }  
  
    private static int getSpecialCharLength(char c) {  
        if (isLetter(c)) {  
            return 1;  
        } else {  
            return 2;  
        }  
    }  
  
    private static boolean isLetter(char c) {  
        int k = 0x80;  
        return c / k == 0 ? true : false;  
    }
    
}