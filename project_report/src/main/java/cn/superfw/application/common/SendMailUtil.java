package cn.superfw.application.common;

import java.text.MessageFormat;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.application.config.SystemConfig;
import cn.superfw.application.domain.Account;
import cn.superfw.application.domain.ProductDetail;
import cn.superfw.application.domain.Project;
import cn.superfw.application.model.m01.controller.SpecialStockController;
import cn.superfw.framework.utils.MailUtil;

public class SendMailUtil {

    private static final Logger log = LoggerFactory.getLogger(SpecialStockController.class);

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void send(String taskName, String processInsId, String processDefName, String startUsername,
            Account account, Project project) {

        String subject = "【ZyCN SPM】" + "【" + AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()) + "】【" + project.getProjectName() + "】";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("{0},您好：");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("您在【{1}】中有新的任务要处理或参与。");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【发起人】[{2}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【任务名称】[{3}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目编号】[{4}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目名称】[{5}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目状态】[{6}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("详细信息请登陆系统平台进行查看。");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("内网登录 http://172.26.1.128");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("外网登录  https://vpn.zyxel.com.tw");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("谢谢！");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("From：项目报备管理平台");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);

        String message = MessageFormat.format(messageBuilder.toString(),
                account.getUsername(), // 参与者
                processDefName + "-" + processInsId, // 流程名-实例ID
                startUsername, // 发起人
                taskName, // 当前任务名
                project.getProjectNumber(), // 项目编号
                project.getProjectName(), // 项目名称
                AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()) // 项目状态
                );

        String from = SystemConfig.SYSTEM_MAIL_FROM.value();
        String to = account.getEmail();

        log.info(subject);
        log.info(message);

        try {

        	// 如果发邮件开关为关闭状态,直接返回
            if (!"true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value())) {
                return;
            }

            MailUtil.sendMailHtml(from, to, subject, message);
        } catch(Exception e) {
            log.error("邮件发送失败！");
            log.error(ExceptionUtils.getStackTrace(e));
            throw e;
        }

    }

    public static void sendHis(String processInsId, String processDefName, String startUsername,
            Account account, Project project) {
        // 如果发邮件开关为关闭状态,直接返回
        /*if (!"true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value())) {
            return;
        }*/
        String subject = "【ZyCN SPM】" + "【查询历史批复】【" + project.getProjectName() + "】";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("{0},您好：");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("此项目需要查询历史批复信息。");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【流程名-实例ID】[{1}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【发起人】[{2}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【任务名称】[{3}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目编号】[{4}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目名称】[{5}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目状态】[{6}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);

        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("详细信息请登陆系统平台进行查看。");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("内网登录 http://172.26.1.128");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("外网登录 https://vpn.zyxel.com.tw");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("谢谢！");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("From：项目报备管理平台");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);

        String message = MessageFormat.format(messageBuilder.toString(),
                account.getUsername(), // 参与者
                processDefName + "-" + processInsId, // 流程名-实例ID
                startUsername, // 发起人
                "查询历史批复信息",// 当前任务名
                project.getProjectNumber(),// 项目编号
                project.getProjectName(),// 项目名称
                AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()) // 项目状态
                );

        String from = SystemConfig.SYSTEM_MAIL_FROM.value();
        String to = account.getEmail();

        log.info(subject);
        log.info(message);

        try {

        	// 如果发邮件开关为关闭状态,直接返回
            if (!"true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value())) {
                return;
            }

            MailUtil.sendMailHtml(from, to, subject, message);
        } catch (Exception e) {
            log.error("邮件发送失败！");
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }


    public static void sendGP(String processInsId, String processDefName, String startUsername,
            Account account, Project project) {

        // 如果发邮件开关为关闭状态,直接返回
        /*if (!"true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value())) {
            return;
        }*/

        String subject = "【ZyCN SPM】" + "【" + AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()) + "】【" + project.getProjectName() + "】";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("{0},您好：");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("您在【{1}】中需要重新计算GP。");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【发起人】[{2}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【任务名称】[{3}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目编号】[{4}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目名称】[{5}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目状态】[{6}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);

        messageBuilder.append("{7}");

        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("详细信息请登陆系统平台进行查看。");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("内网登录 http://172.26.1.128");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("外网登录 https://vpn.zyxel.com.tw");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("谢谢！");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("From：项目报备管理平台");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);

        StringBuilder pdbuilder = new StringBuilder();
        for (ProductDetail pd : project.getProductDetailList()) {
            String productLine = pd.getProductLine();
            String productModel = pd.getProductModel();
            Double price = pd.getExpectedPrice();
            Integer number = pd.getNumber();
            pdbuilder.append("\t产品线\t\t")
                .append(productLine).append(LINE_SEPARATOR)
                .append("\t产品型号\t").append(productModel).append(LINE_SEPARATOR)
                .append("\t申请价格\t").append(price).append(LINE_SEPARATOR)
                .append("\t产品数量\t").append(number).append(LINE_SEPARATOR)
                .append(LINE_SEPARATOR);
        }

        String message = MessageFormat.format(messageBuilder.toString(),
                account.getUsername(), // 参与者
                processDefName + "-" + processInsId, // 流程名-实例ID
                startUsername, // 发起人
                "重新计算GP%",// 当前任务名
                project.getProjectNumber(),// 项目编号
                project.getProjectName(),// 项目名称
                AppContants.PROJECT_STATUS_NAMES.get(project.getStatus()), // 项目状态
                pdbuilder.toString()
                );

        String from = SystemConfig.SYSTEM_MAIL_FROM.value();
        String to = account.getEmail();

        log.info(subject);
        log.info(message);

        try {

        	// 如果发邮件开关为关闭状态,直接返回
            if (!"true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value())) {
                return;
            }

            MailUtil.sendMailHtml(from, to, subject, message);
        } catch (Exception e) {
            log.error("邮件发送失败！");
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void sendUpdateLog(String username, String cooperativePartner, Project project, String editFlag) {
        // 如果发邮件开关为关闭状态,直接返回
        if (!"true".equalsIgnoreCase(SystemConfig.MAIL_SEND_ENABLED.value())) {
            return;
        }

        /*String subject = "【ZyCN SPM】" + "【报备修改日志】【" + project.getProjectName() + "】【" + editFlag + "】";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("【操作者】[{0}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目编号】[{1}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【项目名称】[{2}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【合作伙伴】[{3}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("【预计出货时间】[{4}]");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);

        messageBuilder.append("{5}");

        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("From：项目报备管理平台");
        messageBuilder.append(LINE_SEPARATOR);
        messageBuilder.append("---------------------");
        messageBuilder.append(LINE_SEPARATOR);

        StringBuilder pdbuilder = new StringBuilder();
        for (ProductDetail pd : project.getProductDetailList()) {
            String productLine = pd.getProductLine();
            String productModel = pd.getProductModel();
            Double price = pd.getExpectedPrice();
            Integer number = pd.getNumber();
            pdbuilder.append("\t产品线\t\t")
                .append(productLine).append(LINE_SEPARATOR)
                .append("\t产品型号\t").append(productModel).append(LINE_SEPARATOR)
                .append("\t申请价格\t").append(price).append(LINE_SEPARATOR)
                .append("\t产品数量\t").append(number).append(LINE_SEPARATOR)
                .append(LINE_SEPARATOR);
        }

        String message = MessageFormat.format(messageBuilder.toString(),
                username, // 操作者
                project.getProjectNumber(), // 项目编号
                project.getProjectName(), // 项目名称
                cooperativePartner, // 合作伙伴
                CalendarUtil.formatDate(project.getExpectedShipmentTime()),
                pdbuilder.toString()
                );

        log.info(subject);
        log.info(message);

        String from = SystemConfig.SYSTEM_MAIL_FROM.value();
        String to =  SystemConfig.UPDATE_LOG_MAIL.value();

        try {
            MailUtil.sendMailHtml(from, to, subject, message);
        } catch (Exception e) {
            log.error("邮件发送失败！");
            log.error(ExceptionUtils.getStackTrace(e));
        }*/
    }
}
