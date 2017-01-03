package cn.superfw.framework.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import cn.superfw.framework.exception.SystemException;
import cn.superfw.framework.spring.ApplicationContextHolder;
import cn.superfw.framework.spring.JavaMailSenderImplEx;


public class MailUtil {

	public static boolean checkMailServer() {

		JavaMailSenderImplEx sender = ApplicationContextHolder.getBean("mailSender", JavaMailSenderImplEx.class);
		return sender.checkMailServer();

	}

	/**
	 * 发送邮件
	 * @param from 发件人
	 * @param to 收件人
	 * @param subject 主题
	 * @param text 文本内容
	 */
	public static void sendMail(String from, String to, String cc, String subject, String text) {
		JavaMailSender sender = ApplicationContextHolder.getBean("mailSender", JavaMailSender.class);
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(from);
		mail.setTo(to);
		mail.setCc(cc);
		mail.setSubject(subject);
		mail.setText(text);
		sender.send(mail);
	}

	/**
	 * 发送邮件
	 * @param from 发件人
	 * @param to 收件人
	 * @param subject 主题
	 * @param text 文本内容
	 */
	public static void sendMail(String from, String[] to, String[] cc, String subject, String text) {
		JavaMailSender sender = ApplicationContextHolder.getBean("mailSender", JavaMailSender.class);
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(from);
		mail.setTo(to);
		mail.setCc(cc);
		mail.setSubject(subject);
		mail.setText(text);
		sender.send(mail);
	}

	/**
	 * 发送邮件
	 * @param from 发件人
	 * @param to 收件人
	 * @param subject 主题
	 * @param text 文本内容
	 */
	public static void sendMailHtml(String from, String to, String subject, String text) {
		JavaMailSender sender = ApplicationContextHolder.getBean("mailSender", JavaMailSender.class);
		MimeMessage mail = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail);
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
		} catch (MessagingException e) {
			throw new SystemException("邮件格式异常");
		}
		sender.send(mail);
	}

	/**
	 * 发送邮件
	 * @param from 发件人
	 * @param to 收件人
	 * @param subject 主题
	 * @param text 文本内容
	 */
	public static void sendMailHtml(String from, String[] to, String subject, String text) {
		JavaMailSender sender = ApplicationContextHolder.getBean("mailSender", JavaMailSender.class);
		MimeMessage mail = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail);
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
		} catch (MessagingException e) {
			throw new SystemException("邮件格式异常");
		}
		sender.send(mail);
	}



}
