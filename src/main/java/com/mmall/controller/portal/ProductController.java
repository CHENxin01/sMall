package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chen
 */

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    //前台商品详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer prpduct) {
        return iProductService.getProductDetail(prpduct);
    }

    //用户搜索
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false)Integer categoryId,
                                         @RequestParam(value = "pageName",defaultValue = "1") int pageName,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "")String orderBy) {
        return iProductService.getProductByKeywordCategory(keyword, categoryId, pageName, pageSize, orderBy);

    }


}



