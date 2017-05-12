package com.paymoon.basic.commons.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WebUtil {
	private static Logger logger = LogManager.getLogger();

	public static void print(HttpServletRequest request,HttpServletResponse response, String result) throws IOException {
		 response = getResponse(request, response);
		PrintWriter out = response.getWriter();
		out.print(result);
	}
	public static HttpServletResponse getResponse(HttpServletRequest request,HttpServletResponse response)  {
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (request.getHeader("Access-Control-Request-Method") != null
				&& "OPTIONS".equals(request.getMethod())) {
			// CORS "pre-flight" request
			response.addHeader("Access-Control-Allow-Methods",
					"GET, POST, PUT, DELETE");
			response.addHeader("Access-Control-Allow-Headers",
					"X-Requested-With,Origin,Content-Type, Accept");
		}
		
		response.setContentType("application/json;charset=utf8");
		return response;
	}

}
