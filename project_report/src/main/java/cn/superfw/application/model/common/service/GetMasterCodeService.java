package cn.superfw.application.model.common.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superfw.application.dao.CodeDAO;
import cn.superfw.application.domain.Code;
import cn.superfw.framework.web.BaseBLogicService;

/**
 * 根据传入的分类ID获取相关数据
 *
 * @author 王阳
 *
 */
@Service
public class GetMasterCodeService extends BaseBLogicService<List<String>, Map<String, Map<String, String>>> {

    @Autowired
    private CodeDAO codeDAO;

    /**
     * 根据传入的分类ID获取相关数据
     *
     * @param typeList 分类ID列表
     */
    @Override
    protected Map<String, Map<String, String>> doService(List<String> typeList) {

        Map<String, Map<String, String>> typeMap = new LinkedHashMap<String, Map<String, String>>();

        for (String typeId : typeList) {
            Map<String, String> codeMap = new LinkedHashMap<String, String>();

            List<Code> codeList = codeDAO.createCriteria()
                    .add(Restrictions.eq("typeId", typeId))
                    .addOrder(Order.asc("sortitem")).list();

            for (Code item : codeList) {
                codeMap.put(item.getCodeId(), item.getCodeName());
            }
            typeMap.put(typeId, codeMap);
        }
        return typeMap;
    }

}
