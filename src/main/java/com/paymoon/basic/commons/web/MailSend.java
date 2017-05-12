package com.paymoon.basic.commons.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.paymoon.basic.commons.core.MyProp;
import com.paymoon.basic.commons.date.DateUtil;

public class MailSend {
	private static Log logger=LogFactory.getLog(MailSend.class);
	private MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	private String username = "";
	private String password = "";
	private Multipart mp;

	public MailSend() {
		createMimeMessage();
	}

	public MailSend(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}

	public void setSmtpHost(String hostName) {
		if (this.props == null) {
			this.props = System.getProperties();
		}

		this.props.put("mail.smtp.host", hostName);
		this.props.put("mail.smtp.localhost", hostName);
		logger.debug(new Object[] { "mail.smtp.host = ", hostName });
	}

	public boolean createMimeMessage() {
		logger.debug("createMimeMessage> get session...");
		try {
			this.session = Session.getDefaultInstance(this.props, null);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}

		logger.debug("createMimeMessage> create MIME object...");
		try {
			this.mimeMsg = new MimeMessage(this.session);
			this.mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public void setNeedAuth(boolean need) {
		if (this.props == null) {
			this.props = System.getProperties();
		}
		if (need)
			this.props.put("mail.smtp.auth", "true");
		else {
			this.props.put("mail.smtp.auth", "false");
		}
		logger.debug(new Object[] { "mail.smtp.auth = ", Boolean.valueOf(need) });
	}

	public void setNamePass(String name, String pass) {
		logger.debug("setNamePass> ...");
		this.username = name;
		this.password = pass;
	}

	public boolean setSubject(String mailSubject) {
		logger.debug(new Object[] { "setSubject> ", mailSubject });
		try {
			this.mimeMsg.setSubject(mailSubject, "UTF-8");
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean setBody(String mailBody) {
		logger.debug(new Object[] { "setBody> ", mailBody });
		try {
			BodyPart bp = new MimeBodyPart();

			bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody,
					"text/html;charset=GBK");
			this.mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean addFileAffix(String filename) {
		logger.debug(new Object[] { "addFileAffix> ", filename });
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			// bp.setFileName(fileds.getName());
			bp.setFileName(MimeUtility.encodeText(fileds.getName())); // 附件中文名的正常显示
			this.mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean addFileAffix(String filename, String fname) {
		logger.debug(new Object[] { "addFileAffix> ", filename });
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			// bp.setFileName(fname);
			bp.setFileName(MimeUtility.encodeText(fname)); // 附件中文名的正常显示
			this.mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean setFrom(String from) {
		logger.debug(new Object[] { "setFrom> ", from });
		try {
			this.mimeMsg.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}
	public boolean setFrom(String from,String alias) {
		logger.debug(new Object[] { "setFrom> ", from });
		try {
			this.mimeMsg.setFrom(new InternetAddress(from,alias,"UTF-8"));
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean setTo(String to) {
		logger.debug(new Object[] { "setTo> ", to });
		if (to == null)
			return false;
		try {
			this.mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean setCopyTo(String copyto) {
		logger.debug(new Object[] { "setCopyTo> ", copyto });
		if (copyto == null)
			return false;
		try {
			this.mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean sendout() {
		try {
			this.mimeMsg.setContent(this.mp);
			this.mimeMsg.saveChanges();

			logger.debug("sendout> sending....");

			Transport transport = this.session.getTransport("smtp");
			transport.connect((String) (String) this.props.get("mail.smtp.host"), this.username, this.password);
			transport.sendMessage(this.mimeMsg, this.mimeMsg.getRecipients(Message.RecipientType.TO));
			transport.close();
			logger.debug("sendout> ok");

			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static void main(String[] args) {
		String startline = DateUtil.getDate(DateUtil.addDay(new Date(), -7));
		String deadline = DateUtil.getDate(DateUtil.addDay(new Date(), -1));
		MailSend themail = new MailSend("smtp.qq.com");
		themail.setNeedAuth(true);
		themail.setSubject("OneAlert注册来源统计" + startline + "至" + deadline);
		themail.setFrom("lizhiqiang@oneapm.com");
		themail.setNamePass("lizhiqiang@oneapm.com", "Lizhiqiang123");

		themail.setTo("lizhiqiang@oneapm.com");
		themail.setBody("Hi:<br/>OneAlert注册来源统计。<br/>统计时间：" + startline + "至" + deadline + "<br>统计结果：见附件。");
		// themail.addFileAffix(OneAlert注册来源统计1023-1029.xls);

		themail.sendout();
	}

	public static void send(String filename) {
		logger.info("begin to MailSend ");
		
		String mailSign = MyProp.getVariable("mailSign");
		String startline = DateUtil.getDate(DateUtil.addDay(new Date(), -7));
		String deadline = DateUtil.getDate(DateUtil.addDay(new Date(), -1));
		MailSend themail = new MailSend("smtp.qq.com");
		themail.setNeedAuth(true);
		themail.setSubject("OneAlert注册来源统计" + startline + "至" + deadline);
		themail.setFrom("lizhiqiang@oneapm.com");
		themail.setNamePass("lizhiqiang@oneapm.com", "Lizhiqiang123");

		//themail.setTo("lizhiqiang@oneapm.com");
		String to[]={"guojiajia@oneapm.com","lizhiqiang@oneapm.com"};
		String toList = getMailList(to);
		themail.setTo(toList);

//		themail.setCopyTo("1098989547@qq.com");//
		String copyTo[]={"chenbolong@oneapm.com","lizhiqiang@oneapm.com"};
		String copyToList = getMailList(copyTo);
		themail.setCopyTo(copyToList);
		
		
//		themail.setTo("wangguoping@oneapm.com");
//		themail.setCopyTo("chenbolong@oneapm.com");
//		logger.info("MailSend setTo {} setCopyTo {}",toList,copyToList);

		themail.setBody("Hi:<br/>OneAlert注册来源统计。<br/>统计时间：" + startline + "至" + deadline + "<br>统计结果：见附件。<br/><br/><br/><br/><br/><br/>"+mailSign);
//		logger.info("mail body:{} ","Hi:<br/>OneAlert注册来源统计。<br/>统计时间：" + startline + "至" + deadline + "<br>统计结果：见附件。<br/><br/><br/>"+mailSign);

		themail.addFileAffix("OneAlert注册来源统计" + startline + "_" + deadline + ".xls");

		boolean isSendous = themail.sendout();
		/**
		if (isSendous) 
		logger.info("MailSend Successful! >> setTo {} setCopyTo {}",toList,copyToList);
		else 
		logger.info("MailSend Failed! >> setTo {} setCopyTo {}",toList,copyToList);
		*/

	}
	public static boolean send(final String fromAlias,final String targetAddr,final String title,final String content) {
		logger.info("begin to MailSend ");
		
		final String user = MyProp.getVariable("mail.username");
		final String password = MyProp.getVariable("mail.password");
		final String host = MyProp.getVariable("mail.host");
		MailSend themail = new MailSend(host);
		themail.setNeedAuth(true);
		themail.setSubject(title);
		themail.setFrom(user,fromAlias);
		themail.setNamePass(user,password);
		
		String to[]=targetAddr.split(",");
		String toList = getMailList(to);
		themail.setTo(toList);
		
		themail.setBody(content);
		
		boolean isSendous = themail.sendout();
		
		logger.info(String.format("MailSend status: %s  setTo: %s title: %s ",isSendous,toList,title));

		return isSendous;
		
	}
	public static boolean sendCloud(final String fromAlias,final String targetAddr,final String title,final String content) {
		final String url = "http://sendcloud.sohu.com/webapi/mail.send.json";
		final String sendCloudApiuser = MyProp.getVariable("sendcloud.mail.apiuser", "onealert");
		final String sendCloudApikey = MyProp.getVariable("sendcloud.mail.apikey", "ird5WXui2GxtCDj9");
		String status = "SUCCESS";
		HttpPost httpPost = new HttpPost(url);
		HttpClient httpClient = HttpClients.createDefault();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("api_user", sendCloudApiuser));
		params.add(new BasicNameValuePair("api_key", sendCloudApikey));
		params.add(new BasicNameValuePair("to", targetAddr));
		params.add(new BasicNameValuePair("from", "OneAlert@push.oneapm.com"));
		params.add(new BasicNameValuePair("fromname", fromAlias));
		params.add(new BasicNameValuePair("subject", title));
		params.add(new BasicNameValuePair("html", content));
		params.add(new BasicNameValuePair("resp_email_id", "true"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			// 处理响应
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				String errorCode = String.valueOf(response.getStatusLine().getStatusCode());
				logger.info("faild to send email , error code is " + errorCode);
				status = "FAILED";
			}
		} catch (Exception e) {
			logger.error("failed to send email id:" + targetAddr + "," + e.getMessage());
			status = "FAILED";
		} finally {
			httpPost.releaseConnection();
		}
		if (status .equalsIgnoreCase("success") ) {
			return true;
		}else {
			return false;
		}
	}
    private static String getMailList(String[] mailArray){  
        
        StringBuffer toList = new StringBuffer();  
    int length = mailArray.length;  
        if(mailArray!=null && length <2){  
             toList.append(mailArray[0]);  
        }else{  
             for(int i=0;i<length;i++){  
                     toList.append(mailArray[i]);  
                     if(i!=(length-1)){  
                         toList.append(",");  
                     }  
  
             }  
         }  
     return toList.toString();  
  
} 
	 
}
	  