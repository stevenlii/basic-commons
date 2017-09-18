package com.paymoon.basic.commons.web;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: POST 测试接口
 * @Description: TODO
 * @author StevenLii
 * @date 2014年11月19日 下午11:01:24
 * 
 */
public class NetUtil {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	public static void main(String[] args) {
//		Map<String,String> map = new HashMap<>();
//		  map.put("alertAppkey", "111111");
//		  map.put("mnsQueue", "myfirstqueue");
//		  map.put("mnsAccountEndpoint", "http://1412580518114250.mns.cn-beijing.aliyuncs.com");
//		  map.put("mnsAccesskeySecret", "noc1dwMsrRY0o3n1vVFczOHd1v6bjG");
//		  map.put("mnsAccesskeyId", "Jkyctl1JLRkQWNgV");
//
//		  //https://api.hubapi.com/contacts/v2/properties?hapikey=ca045806-e87a-4d5d-9320-46ee0c5dae45&portalId=1698991
//
//		post(map,"http://localhost:8080/mongoservice/mongo/saveorupdate");
//		get("http://localhost:8080/mongoservice/mongo/query");
//		post(null, "http://ci1.test.110monitor.com:5080/ucid/api/authorize?user=lizhiqiang&password=lizhiqiang");
//		post(null, "http://ci1.test.110monitor.com:38080/ucid/api/authorize?user=super&password=123456");
//		post(null, "http://ci1.test.110monitor.com:38080/ucid/api/authorize?user=hqadmin&password=Upy0o123");
//		post(null, "http://zd3s.test.110monitor.com:38080/ucid/api/authorize?user=hqadmin&password=Upy0o123");
//		String result = post(null, "http://ci1.test.110monitor.com:9090/ucid/api/authorize?user=hqadmin&password=Upy0o123");
//		post(null, "http://localhost:8080/ucid/api/authorize?user=hqadmin&password=Upy0o123");
		post(null, "http://ci3.test.110monitor.com:38080/ucid/api/authorize?user=admin&password=123456");
//		post(null, " http://10.128.106.160:5080/itsm-workflow/api/activiti/user/1ac3d4f4-d2fd-4793-861e-b690898ddb71?UPYOO_TOKEN=e979a8f1-478c-201b-ff70-071393eb5a56");
		
	}
	
	@SuppressWarnings("deprecation")
	public static String post(Map<String,String> map, String url) {
		long timebefore = System.currentTimeMillis();
		int status = 0;
		PostMethod post =null;
		HttpClient httpClient = null;
		try {
			String surl = url;
			System.out.println("push url is: "+surl);
			/////////////////////
			
//			JSONArray js = JSONArray.fromObject(map);
			ObjectMapper ob = new ObjectMapper();
			String js = ob.writeValueAsString(map);
			System.out.println("data is: "+js.toString());
			httpClient = new HttpClient();
			post = new PostMethod(surl);
			post.setRequestHeader("Content-Type",
					"application/json;charset=utf-8");
			
//				NameValuePair[] param = { new NameValuePair("properties", js.toString()) };
//				post.setRequestBody(param);
			post.setRequestBody(js.toString());
			post.getParams().setContentCharset("utf-8");
			post.releaseConnection();
			status = httpClient.executeMethod(post);
			
			System.out.println("push status " + status);
//			if (status == HttpStatus.SC_OK) {
				
				System.out.println("push response " + post.getResponseBodyAsString());
				return post.getResponseBodyAsString();
//			}
//			long timeafter = System.currentTimeMillis();
//			System.out.println(timeafter - timebefore);
			
			
		} catch (Exception e) {
			System.err.println("post failed, url is: "+url +" Exception message is:" + e.getMessage());
		}finally {  
			if (post != null) {  
				post.releaseConnection();  
				post = null;  
				httpClient = null;  
			}  
		}  
		return String.valueOf(status);
	}
	public static String post(Object o, String url) {
		long timebefore = System.currentTimeMillis();
		int status = 0;
		PostMethod post =null;
		HttpClient httpClient = null;
		try {
			String surl = url;
			System.out.println("executing post request: "+surl);
			
			ObjectMapper ob = new ObjectMapper();
			String js = ob.writeValueAsString(o);
			System.out.println("data is: "+js.toString());
			httpClient = new HttpClient();
			post = new PostMethod(surl);
			post.setRequestHeader("Content-Type",
					"application/json;charset=utf-8");
			
//				NameValuePair[] param = { new NameValuePair("properties", js.toString()) };
//				post.setRequestBody(param);
			post.setRequestBody(js.toString());
			post.getParams().setContentCharset("utf-8");
			post.releaseConnection();
			status = httpClient.executeMethod(post);
			
			System.out.println("push status " + status);
			if (status == HttpStatus.SC_OK) {
				
//				System.out.println("push response " + post.getResponseBodyAsString());
				return post.getResponseBodyAsString();
			}
			long timeafter = System.currentTimeMillis();
//			System.out.println(timeafter - timebefore);
			
			
		} catch (Exception e) {
			System.err.println("post failed, url is: "+url +" Exception message is:" + e.getMessage());
		}finally {  
			if (post != null) {  
				post.releaseConnection();  
				post = null;  
				httpClient = null;  
			}  
		}  
		return String.valueOf(status);
	}
	@Deprecated
	public static String push(Map<String,Object> map, String url) {
		int status = 0;
		PostMethod post =null;
		HttpClient httpClient = null;
		try {
			String surl = url;
			System.out.println("push url is: "+surl);
			/////////////////////
			
//			JSONArray js = JSONArray.fromObject(map);
			ObjectMapper ob = new ObjectMapper();
			String js = ob.writeValueAsString(map);
			System.out.println("data is: "+js.toString());
			httpClient = new HttpClient();
			post = new PostMethod(surl);
			post.setRequestHeader("Content-Type",
					"application/json;charset=utf-8");
			
//				NameValuePair[] param = { new NameValuePair("properties", js.toString()) };
//				post.setRequestBody(param);
			post.setRequestBody(js.toString());
			post.getParams().setContentCharset("utf-8");
			post.releaseConnection();
			status = httpClient.executeMethod(post);
			
			System.out.println("push status " + status);
			if (status == HttpStatus.SC_OK) {
				
				System.out.println("push response " + post.getResponseBodyAsString());
				return post.getResponseBodyAsString();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("发送数据失败!!!", e);
		}finally {  
			if (post != null) {  
				post.releaseConnection();  
				post = null;  
				httpClient = null;  
			}  
		}  
		return String.valueOf(status);
	}
	public static String get(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.  
			HttpGet httpget = new HttpGet(url);
			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.  
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体  
				HttpEntity entity = response.getEntity();
//				System.out.println("--------------------------------------");
				// 打印响应状态  
				System.out.println(response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度  
//					System.out.println("Response content length: " + entity.getContentLength());
					String  responseContent = EntityUtils.toString(entity);
					// 打印响应内容  
//					System.out.println("Response content: " + responseContent);
					return responseContent;
				}
//				System.out.println("------------------------------------");
			} finally {
				response.close();
			}
		} catch (ParseException |IOException e) {
			System.out.println("NETUIIL ParseException |IOException :" + e.getMessage());
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				System.err.println("get failed, url is: "+url +" Exception message is:" + e.getMessage());
			}
		}
		return null;
	}
	 public static String put(Object o,String uri){  
	        HttpClient htpClient = new HttpClient();  
	        PutMethod putMethod = new PutMethod(uri);  
	        putMethod.addRequestHeader( "Content-Type","application/json" );  
	        putMethod.getParams().setContentCharset("utf-8");;  
	        System.out.println("executing put request: "+uri);
	        try{  
	        	ObjectMapper ob = new ObjectMapper();
	        	String js = ob.writeValueAsString(o);
	        	putMethod.setRequestBody( js );
	        	System.out.println("data is: "+js.toString());
	            int status = htpClient.executeMethod( putMethod );  
				
				System.out.println("put status " + status);
				if (status == HttpStatus.SC_OK) {
					return putMethod.getResponseBodyAsString();
				}
	        }catch(Exception e){  
				System.err.println("put failed, url is: "+uri +" Exception message is:" + e.getMessage());
	        }finally{  
	            putMethod.releaseConnection();  
	        }  
	        return null;  
	    }
	public static String delete(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建HttpDelete.  
			HttpDelete delete = new HttpDelete(url);
			System.out.println("executing request " + delete.getURI());
			// 执行delete请求.  
			CloseableHttpResponse response = httpclient.execute(delete);
			try {
				// 获取响应实体  
				HttpEntity entity = response.getEntity();
				System.out.println("--------------------------------------");
				// 打印响应状态  
				System.out.println(response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度  
					System.out.println("Response content length: " + entity.getContentLength());
					String  responseContent = EntityUtils.toString(entity);
					// 打印响应内容  
					System.out.println("Response content: " + responseContent);
					return responseContent;
				}
				System.out.println("------------------------------------");
			} finally {
				delete.releaseConnection();
				response.close();
			}
		} catch (ParseException |IOException e) {
			System.out.println("NETUIIL ParseException |IOException :" + e.getMessage());
			System.err.println("del failed, url is: "+url +" Exception message is:" + e.getMessage());

		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	

}