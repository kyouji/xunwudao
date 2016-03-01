package com.ynyes.xunwudao.controller.front;




import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdOrderGoods;
import com.ynyes.xunwudao.entity.TdProductCategory;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.entity.TdUserCollect;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdOrderGoodsService;
import com.ynyes.xunwudao.service.TdProductCategoryService;
import com.ynyes.xunwudao.service.TdUserCollectService;
import com.ynyes.xunwudao.service.TdUserService;


/**
 * 首页（我的）【需登陆】
 * 角色数：2
 * 会计【唯一界面】、客户。
 *@author Zhangji
 */
@Controller
@RequestMapping("/goods")
public class TdGoodsController {

    
    @Autowired
    private TdCommonService tdCommonService;
    
    @Autowired
    private TdUserService tdUserService;

    @Autowired
    private TdProductCategoryService tdProductCategoryService;
    
    @Autowired
    private TdGoodsService tdGoodsService;
    
    @Autowired
    private TdUserCollectService tdUserCollectService;
    
    @Autowired
    private TdOrderGoodsService tdOrderGoodsService;
    
    //商城首页
    @RequestMapping("/index")
    public String index(HttpServletRequest req, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
    	
    	List<TdProductCategory> category = tdProductCategoryService.findAll();
    	if (category.size() > 0)
    	{
    		map.addAttribute("category", category.get(0));
    	}
    	
    	map.addAttribute("category_list", category);
    	map.addAttribute("showIcon", 2);
    	
        return "/client/goods_index";
    }
    
    //列表页
    @RequestMapping("/list")
    public String goodsList(Long catId, HttpServletRequest req,  ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
    	
    	if(null != catId)
    	{
    		TdProductCategory category = tdProductCategoryService.findOne(catId);
    		List<TdGoods> goodsList = tdGoodsService.findByCategoryIdAndIsOnSaleTrue(catId);
    		
        	map.addAttribute("goods_list", goodsList);
    		map.addAttribute("category", category);
        	map.addAttribute("showIcon", 2);
        	map.addAttribute("catId", catId);
    	}
    	
        return "/client/goods_list";
    }
    
    //列表页
    @RequestMapping("/detail/{id}")
    public String goodsDetail(@PathVariable Long id, String code, HttpServletRequest req,  ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
    	String username = (String) req.getSession().getAttribute("username");
    	if(null != username)
    	{
    		map.addAttribute("user", tdUserService.findByUsername(username));
    		
        	//推荐码
    		TdUser tdUser = tdUserService.findByUsername(username);
        	String userNumber = tdUser.getNumber();
        	Random random = new Random();
    		String randomNumber = random.nextInt(900) + 100 + "";
    		
    		String rfCode = userNumber + randomNumber.toString() + id.toString();
    		map.addAttribute("rfCode", rfCode);
    		
            if (null != code) {
            	TdIndexController index = new TdIndexController();
    			Map<String, String> res = index.getAccessToken(code);
    			System.out.println("openid:"+res.get("openid")); 
    			if (null == tdUserService.findByUsernameAndOpenid(username, res.get("openid"))) {
    				TdUser ever = tdUserService.findByOpenid(res.get("openid"));
    				if(null != ever){
    					ever.setOpenid(null);
    					tdUserService.save(ever);
    				}
    				
    				System.out.println("THIS USER DOESN'T HAVE ANY openid _ZHANGJI");
    				tdUser.setOpenid(res.get("openid"));
    				tdUserService.save(tdUser);
    			}
    		}
    	}
    	if(null != id)
    	{
    		TdGoods tdGoods = tdGoodsService.findOne(id);
    		if(null != tdGoods)
    		{
    			map.addAttribute("tdGoods", tdGoods);
    			
    			if(null != username){
    				TdUserCollect collect = tdUserCollectService.findByUsernameAndGoodsId(username, tdGoods.getId());
    				if(null != collect){
    					map.addAttribute("collected", 1);
    				}
    				else{
    					map.addAttribute("collected", 0);
    				}
    			}
    			
    			
    		}
    		
    		//购买记录
    		List<TdOrderGoods> orderGoodsList = tdOrderGoodsService.findByGoodsIdOrderByIdDesc(id);
    		if(null != orderGoodsList && orderGoodsList.size() > 0){
    			map.addAttribute("order_goods_list", orderGoodsList);
    		}
    		
        	map.addAttribute("showIcon", 2);
    	}
    	
        return "/client/goods_detail";
    }
    

}
