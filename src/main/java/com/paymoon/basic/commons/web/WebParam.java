package com.paymoon.basic.commons.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebParam{

	//get request headers
	public Map<String, String> getHeadersInfo(HttpServletRequest request) {
	    Map<String, String> map = new HashMap<String, String>();
	    Enumeration<String> headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        map.put(key, value);
	    }
	    return map;
	  }
	  
	  /**
	   * 根据名字获取cookie
	   * @param request
	   * @param name cookie名字
	   * @return
	   */
	  public static Cookie getCookieByName(HttpServletRequest request,String name){
	      Map<String,Cookie> cookieMap = ReadCookieMap(request);
	      if(cookieMap.containsKey(name)){
	          Cookie cookie = (Cookie)cookieMap.get(name);
	          return cookie;
	      }else{
	          return null;
	      }   
	  }
	   
	   
	   
	  /**
	   * 将cookie值封装到Map里面
	   * @param request
	   * @return
	   */
	  private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	      Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	      Cookie[] cookies = request.getCookies();
	      if(null!=cookies){
	          for(Cookie cookie : cookies){
	              cookieMap.put(cookie.getName(), cookie);
	          }
	      }
	      return cookieMap;
	  }
	  public static Map<String,String> getCookieValueMap(HttpServletRequest request){  
		  Map<String,String> cookieMap = new HashMap<>();
		  Cookie[] cookies = request.getCookies();
		  if(null!=cookies){
			  for(Cookie cookie : cookies){
				  cookieMap.put(cookie.getName(), cookie.getValue());
			  }
		  }
		  return cookieMap;
	  }
	  
	  /**
	   * 从request中获得参数Map，并返回可读的Map
	   * 
	   * @param request
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	public static Map<String,String> getParameterMap(HttpServletRequest request) {
	      // 参数Map
	      Map properties = request.getParameterMap();
	      // 返回值Map
	      Map<String,String> returnMap = new HashMap<>();
		Iterator entries = properties.entrySet().iterator();
	      Map.Entry entry;
	      String name = "";
	      String value = "";
	      while (entries.hasNext()) {
	          entry = (Map.Entry) entries.next();
	          name = (String) entry.getKey();
	          Object valueObj = entry.getValue();
	          if(null == valueObj){
	              value = "";
	          }else if(valueObj instanceof String[]){
	              String[] values = (String[])valueObj;
	              for(int i=0;i<values.length;i++){
	                  value += values[i] + ",";
	              }
	              value = value.substring(0, value.length()-1);
	          }else{
	              value = valueObj.toString();
	          }
	          returnMap.put(name, value);
	      }
	      return returnMap;
	  }
}
