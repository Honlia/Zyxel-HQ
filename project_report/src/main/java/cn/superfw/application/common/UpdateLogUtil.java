package cn.superfw.application.common;

import java.text.MessageFormat;
import java.util.Date;

import cn.superfw.application.domain.ProductDetail;
import cn.superfw.application.domain.Project;
import cn.superfw.application.domain.UpdateLog;
import cn.superfw.framework.utils.CalendarUtil;

public class UpdateLogUtil {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static UpdateLog editUpdateLog(String username, String cooperativePartner, Project project) {

        Date sysTime = CalendarUtil.getSystemTime();

        String title = "[" +
                CalendarUtil.formatDateTime(sysTime) + "][" +
                username + "]" + "修改了报备信息";

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

        StringBuilder pdbuilder = new StringBuilder();
        for (ProductDetail pd : project.getProductDetailList()) {
            String productLine = pd.getProductLine();
            String productModel = pd.getProductModel();
            Double price = pd.getExpectedPrice();
            Integer number = pd.getNumber();
            pdbuilder.append("\t产品线\t")
                .append(productLine).append(LINE_SEPARATOR)
                .append("\t产品型号\t").append(productModel).append(LINE_SEPARATOR)
                .append("\t申请价格\t").append(price).append(LINE_SEPARATOR)
                .append("\t产品数量\t").append(number).append(LINE_SEPARATOR)
                .append(LINE_SEPARATOR);
        }

        String content = MessageFormat.format(messageBuilder.toString(),
                username, // 操作者
                project.getProjectNumber(), // 项目编号
                project.getProjectName(), // 项目名称
                cooperativePartner, // 合作伙伴
                CalendarUtil.formatDate(project.getExpectedShipmentTime()),
                pdbuilder.toString()
                );

        UpdateLog updateLog = new UpdateLog(String.valueOf(project.getId()),
                project.getUpdateCount(),
                title,
                content);
        updateLog.setAddTime(sysTime);
        updateLog.setUpdTime(sysTime);

        return updateLog;
    }

}
