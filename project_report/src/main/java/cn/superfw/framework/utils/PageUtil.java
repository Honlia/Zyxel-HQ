package cn.superfw.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

/**
 * 分页编辑
 *
 * @author 王阳
 *
 */
public class PageUtil {

    // 分页编辑
    private Map<String, String> pageKeyMap;

    public void setPageKeyMap(Map<String, String> pageKeyMap) {
        this.pageKeyMap = pageKeyMap;
    }

    public PageUtil() {

        pageKeyMap = new HashMap<String, String>();

        this.pageKeyMap.put("pageCountList", "pageCountList");
        this.pageKeyMap.put("pageCount", "pageCount");
        this.pageKeyMap.put("pageMore", "pageMore");
        this.pageKeyMap.put("pageNo", "pageNo");
        this.pageKeyMap.put("pagePreFlg", "pagePreFlg");
        this.pageKeyMap.put("pagePre", "pagePre");
        this.pageKeyMap.put("pageNextFlg", "pageNextFlg");
        this.pageKeyMap.put("pageNext", "pageNext");
    }

    /**
     * 分页编辑
     * @param pageNo 当前选择页码
     * @param totalPages 总页数
     * @param prePage 前一页
     * @param nextPage 后一页
     * @param model
     */
    public void setPageInfo(int pageNo, long totalPages, int prePage,
            int nextPage, ModelMap model) {

        // 画面显示页数list
        List<Long> pageCountList = new ArrayList<Long>();
        for (long i = 0; i < totalPages; i++) {
            if (pageNo > 4 && pageNo + 1 < totalPages) {
                pageCountList.add(pageNo - 3 + i);
            } else if (pageNo > 4 && pageNo + 1 >= totalPages) {
                pageCountList.add(totalPages - 5 + i);
            } else {
                pageCountList.add(i + 1);
            }
            if (i == 4) {
                break;
            }
        }
        model.addAttribute(pageKeyMap.get("pageCountList"), pageCountList);
        // 总页数
        model.addAttribute(pageKeyMap.get("pageCount"), totalPages);

        // ...表示不表示区分
        if (totalPages > 6 && totalPages > pageNo + 2) {
            model.addAttribute(pageKeyMap.get("pageMore"), true);
        } else {
            model.addAttribute(pageKeyMap.get("pageMore"), false);
        }

        // 当前页数
        model.addAttribute(pageKeyMap.get("pageNo"), pageNo);

        // 上一页表示不表示区分
        if (prePage == pageNo) {
            // 上一页不表示
            model.addAttribute(pageKeyMap.get("pagePreFlg"), false);
        } else {
            // 上一页表示
            model.addAttribute(pageKeyMap.get("pagePreFlg"), true);
        }
        // 上一页
        model.addAttribute(pageKeyMap.get("pagePre"), prePage);

        // 下一页表示不表示区分
        if (nextPage == pageNo) {
            // 下一页不表示
            model.addAttribute(pageKeyMap.get("pageNextFlg"), false);
        } else {
            // 下一页表示
            model.addAttribute(pageKeyMap.get("pageNextFlg"), true);
        }
        // 上一页
        model.addAttribute(pageKeyMap.get("pageNext"), nextPage);
    }
}
