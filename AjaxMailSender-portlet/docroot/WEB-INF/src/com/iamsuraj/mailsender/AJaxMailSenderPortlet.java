package com.iamsuraj.mailsender;
import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.internet.InternetAddress;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;
public class AJaxMailSenderPortlet extends MVCPortlet {
	@Override
	public void serveResource(ResourceRequest resourceRequest,ResourceResponse resourceResponse)throws  IOException, PortletException {
	System.out.println("====serveResource========");
	
	String receiverName=ParamUtil.getString(resourceRequest,"receiverName");
	String receiverEmail=ParamUtil.getString(resourceRequest,"receiverEmail");
	String senderName=ParamUtil.getString(resourceRequest,"senderName");
	String senderMail=ParamUtil.getString(resourceRequest,"senderMail");
	String mailSubject=ParamUtil.getString(resourceRequest,"mailSubject");
	String mailBody=ParamUtil.getString(resourceRequest,"mailBody");
	logger.info("receiverName:"+receiverName);
	logger.info("receiverEmail:"+receiverEmail);
	logger.info("senderName:"+senderName);
	logger.info("senderMail:"+senderMail);
	logger.info("mailSubject:"+mailSubject);
	logger.info("mailBody:"+mailBody);
	JSONObject responseOject=JSONFactoryUtil.createJSONObject();
	boolean success=false;
	if(Validator.isEmailAddress(receiverEmail)&&Validator.isEmailAddress(senderMail)){
		   try {
		MailMessage mailMessage=new MailMessage();
		mailMessage.setBody(mailBody);
        mailMessage.setSubject(mailSubject);
		mailMessage.setFrom(new InternetAddress(senderMail,senderName));
        mailMessage.setTo(new InternetAddress(receiverEmail,receiverName));
		MailServiceUtil.sendEmail(mailMessage);
		success=true;
		   }catch (Exception e) {
			e.printStackTrace();
			responseOject.put("errorMessage",e.getLocalizedMessage());	
			success=false;
		}
		
	}else{
		responseOject.put("errorMessage","One of Email Adresess is not valid");
		success=false;
	}
	if(success){
		responseOject.put("successMessage","Your mail has been send successfully");
	}
	PrintWriter out=resourceResponse.getWriter();
	logger.info(responseOject.toString());
	out.print(responseOject.toString());
	}
	private static final Log logger = LogFactoryUtil.getLog(AJaxMailSenderPortlet.class);
}
