package cn.superfw.application.model.m99.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superfw.application.domain.Product;
import cn.superfw.application.web.uvo.MyUVO;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.web.BLogicService;
import cn.superfw.framework.web.BaseController;

/**
 * 产品管理控制器
 *
 * @author zhouli
 *
 */
@Controller
public class ManageProductController extends BaseController {

    @Autowired
    private BLogicService<Product, Long> saveProductService;

    @Autowired
    private BLogicService<Long, Void> deleteProductService;

    @Autowired
    private BLogicService<Product, List<Product>> getProductListService;

    @Autowired
    private BLogicService<Void, List<String>> getProductLineService;

    @Autowired
    private BLogicService<String, List<String>> getProductTypeListService;

    @Autowired
    private BLogicService<Map<String, String>, List<String>> getProductModelService;

    @Autowired
    private BLogicService<String, List<String>> getProductModelListByTypeService;

    @RequestMapping(value="/changeProductLine", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, List<String>> changeProductLine(@RequestBody Map<String, String> map) {

        Map<String, List<String>> productMap = new HashMap<String, List<String>>();

        List<String> productTypeList = getProductTypeListService.execute(map.get("productLine"));
        List<String> productModelList = getProductModelService.execute(map);

        productMap.put("productTypeList", productTypeList);
        productMap.put("productModelList", productModelList);

        return productMap;
    }

    @RequestMapping(value="/changeProductType", method=RequestMethod.POST)
    @ResponseBody
    public List<String> changeProductType(@RequestBody Map<String, String> map) {

        List<String> productModelList = getProductModelListByTypeService.execute(map.get("productType"));

        return productModelList;
    }

    @ModelAttribute("productLineList")
    public List<String> populateProductLineList() {
        List<String> productLineList = getProductLineService.execute(null);
        return productLineList;
    }

    @ModelAttribute("productTypeList")
    public List<String> populateProductTypeList() {
        List<String> productTypeList = getProductTypeListService.execute(null);
        return productTypeList;
    }

    @ModelAttribute("productModelList")
    public List<String> populateProductModelList() {
        List<String> productLineList = getProductModelService.execute(null);
        return productLineList;
    }

    @RequestMapping(value="/m99/initManageProduct")
    public String initManageProduct(ModelMap model, Product product) {

        model.addAttribute("product", product);

        model.addAttribute("productList", getProductListService.execute(product));
        return "07_manageProduct";
    }

    @RequestMapping(value="/m99/initCreatProduct")
    public String initCreatProduct(ModelMap model) {

        model.addAttribute("product", new Product());
        return "08_creatProduct";
    }

    @RequestMapping(value="/m99/creatProduct")
    public String creatProduct(ModelMap model, HttpSession session, Product product) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
        String username = uvo.getUsername();
        product.setAddUser(username);
        product.setUpdUser(username);

        saveProductService.execute(product);

        model.addAttribute("product", new Product());
        model.addAttribute("productList", getProductListService.execute(new Product()));

        return "07_manageProduct";
    }

    @RequestMapping(value="/m99/initModifyProduct")
    public String initModifyProduct(ModelMap model, @RequestParam(required=true) String id) {

        Product product = new Product();
        product.setId(Long.valueOf(id));

        model.addAttribute("product", getProductListService.execute(product).get(0));

        return "09_modifyProduct";
    }

    @RequestMapping(value="/m99/modifyProduct")
    public String modifyProduct(ModelMap model, HttpSession session, Product product) {

        MyUVO uvo = (MyUVO)session.getAttribute(CommonContants.UVO);
        String username = uvo.getUsername();
        product.setUpdUser(username);

        saveProductService.execute(product);

        model.addAttribute("product", new Product());
        model.addAttribute("productList", getProductListService.execute(new Product()));
        return "07_manageProduct";
    }

    @RequestMapping(value="/m99/deleteProduct")
    public String deleteProduct(ModelMap model, @RequestParam(required=true) String id) {

        deleteProductService.execute(Long.valueOf(id));

        model.addAttribute("product", new Product());
        model.addAttribute("productList", getProductListService.execute(new Product()));
        return "07_manageProduct";
    }

}
