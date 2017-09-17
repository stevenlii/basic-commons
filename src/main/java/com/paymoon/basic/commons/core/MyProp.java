package com.paymoon.basic.commons.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import redis.clients.jedis.Jedis;

public class MyProp {
	public static final String CONF = "/conf/";
	public static final String BIN = "/bin/";
	public static final String DATA = "/data/";
	public static final String LIB = "/lib/";
	public static String userDir = "GLOBAL_127.0.0.1_";
	private static Map propsCache = new HashMap();

	private static String[] fileArray = { "version.properties",
			"db.properties", "runtime.properties", "sysconf.properties",
			"common.properties" };
	static {
		userDir = System.getProperty("user.dir");
		userDir += "_";
		userDir += GetLocalIp.getLocalHostIP();
		userDir += "_";

	}

	public static String getDir() {
		String bindir = System.getProperty("user.dir");
		if (bindir.indexOf("/bin") != -1)
			bindir = bindir.substring(0, bindir.indexOf("/bin"));
		else {
			bindir = bindir + "/target/classes";
		}
		return bindir;
	}

	public static void write(String filename, String key, String value)
			throws IOException {
		boolean exsitKey = false;

		List<String> t = readFileByLines(filename);
		StringBuffer sb = new StringBuffer();
		for (String str : t) {
			if ((!(str.startsWith("#"))) && (str.startsWith(key))) {
				exsitKey = true;
				str = str.substring(0, str.indexOf("=") + 1) + value;
			}
			sb.append(str).append("\n");
		}
		if (!(exsitKey)) {
			sb.append(key).append("=").append(value).append("\n");
		}

		appendContent(filename, sb.toString());
	}

	private static void appendContent(String fileName, String content)
			throws IOException {
		FileWriter writer = new FileWriter(fileName, false);
		writer.write(content);
		writer.close();
	}

	private static List<String> readFileByLines(String resourceName) {
		List<String> strs = new ArrayList<>();

		File file = new File(resourceName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;

			while ((tempString = reader.readLine()) != null) {
				strs.add(tempString);

				++line;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException2) {
				}
		}
		return strs;
	}

	private static String getProperty(String resourceName, String key,
			String defaultValue, Locale locale) {
		String prefix = resourceName
				.substring(0, resourceName.lastIndexOf("."));
		String after = resourceName.substring(
				resourceName.lastIndexOf(".") + 1, resourceName.length());
		String newResourceName = prefix + "_" + locale.getDisplayName() + "."
				+ after;
		return getProperty(newResourceName, key, defaultValue);
	}

	private static String getProperty(String resourceName, String key,
			String defaultValue) {
		try {
			String value = getPropertyInFile(resourceName, key);
			if ((value != null) && (!("".equalsIgnoreCase(value))))
				return value;
			return defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}

	private static String getPropertyInFile(String resourceName, String key)
			throws Exception {
		Properties props = (Properties) propsCache.get(resourceName);
		MyProp utilProperties = new MyProp();
		if (props == null) {
			props = new Properties();
			ClassLoader loader = null;
			if (loader == null) {
				try {
					loader = Thread.currentThread().getContextClassLoader();
				} catch (SecurityException e) {
					loader = utilProperties.getClass().getClassLoader();
				}
			}

			String url = getDir();
			InputStream in = null;
			Reader reader = null;
			try {
				reader = new InputStreamReader(in, "UTF-8");
				props.load(reader);
			} catch (Exception e) {
				try {
					in = utilProperties.getClass().getResourceAsStream(
							"/" + resourceName);
					reader = new InputStreamReader(in, "UTF-8");
					props.load(reader);
				} catch (Exception e1) {
					throw e1;
				}
			}
		}
		propsCache.put(resourceName, props);
		return props.getProperty(key);
	}

	private static String getProperty(String resourceName, String key) {
		return getProperty(resourceName, key, "");
	}

	public static String getVariable(String key) {
		for (int i = 0; i < fileArray.length; ++i) {
			String value = null;
			value = RedisUtilSimple.get(userDir + key,2);
			if (StringUtils.isNotBlank(value)) {
				return value;
			}
			try {
				value = getPropertyInFile(fileArray[i], key);
				if (StringUtils.isNotBlank(value)) {
					RedisUtilSimple.set(userDir + key, value,2);
				}
				return StringUtils.trim(value);
			} catch (Exception e) {
				if ((e.getMessage() != null)
						&& (!(e.getMessage().equals("null")))) {
					System.out.println(e.getMessage());
				}
			}
		}
		return null;
	}

	public static String getVariable(String key, String defaultValue) {
		for (int i = 0; i < fileArray.length; ++i) {
			String value = null;
			try {
				value = getPropertyInFile(fileArray[i], key);
				if (StringUtils.isBlank(value))
					return StringUtils.trim(defaultValue);
				return StringUtils.trim(value);
			} catch (Exception e) {
				if (!(e instanceof NullPointerException)) {
					System.out.println(e.getMessage());
				}
			}
		}
		return StringUtils.trim(defaultValue);
	}

	public static String getVariable(String file, String key,
			String defaultValue) {
		String value = null;
		try {
			value = getPropertyInFile(file, key);
			if ((value == null) || ("".equalsIgnoreCase(value)))
				return StringUtils.trim(defaultValue);
			return StringUtils.trim(value);
		} catch (Exception e) {
			if (e instanceof NullPointerException)
				System.out.println("file is not exit " + file);
			else {
				System.out.println("MyProp Exception:" + e.getMessage());
			}
		}
		return StringUtils.trim(defaultValue);
	}
}