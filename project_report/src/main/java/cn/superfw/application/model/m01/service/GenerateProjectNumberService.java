package cn.superfw.application.model.m01.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.common.AppContants;
import cn.superfw.application.dao.ProjectNumberSubfixDAO;
import cn.superfw.application.domain.ProjectNumberSubfix;
import cn.superfw.framework.utils.StringUtil;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GenerateProjectNumberService extends BaseBLogicService<String, String> {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private ProjectNumberSubfixDAO projectNumberSubfixDAO;

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    protected String doService(String dept) {

        String ymd = df.format(new Date());

        ProjectNumberSubfix command = new ProjectNumberSubfix();

        String deptValue = "";
        int numValue = 0;
        if (AppContants.DEPT_BIZ_1.equals(dept)) {
            deptValue = "S01";
            command.setDept("S01");
        } else if (AppContants.DEPT_BIZ_2.equals(dept)) {
            deptValue = "S02";
            command.setDept("S02");
        }
        command.setYmd(ymd);

        ProjectNumberSubfix projectNumberSubfix = (ProjectNumberSubfix)projectNumberSubfixDAO.createCriteria(
                Restrictions.eq("dept", deptValue), Restrictions.eq("ymd", ymd)).uniqueResult();

        if (projectNumberSubfix == null) {
            command.setNum(1);
            projectNumberSubfixDAO.save(command);
            numValue = 1;
        } else {
            projectNumberSubfix.setNum(projectNumberSubfix.getNum().intValue() + 1);
            projectNumberSubfixDAO.save(projectNumberSubfix);
            numValue = projectNumberSubfix.getNum().intValue();
        }

        StringBuilder sb = new StringBuilder();

        //sb.append("ZYSPM-");
        sb.append(deptValue);
        sb.append("-");
        sb.append(ymd);
        sb.append("-P");
        sb.append(StringUtil.leftPad(String.valueOf(numValue), 2, '0'));

        return sb.toString();
    }

}
