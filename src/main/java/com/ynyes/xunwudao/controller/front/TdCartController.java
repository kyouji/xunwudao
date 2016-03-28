package com.ynyes.xunwudao.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.xunwudao.entity.TdCartGoods;
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdCartGoodsService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdUserService;

/**
 * 购物车
 *
 */
@Controller
public class TdCartController {

    @Autowired
    private TdCartGoodsService tdCartGoodsService;

    @Autowired
    private TdGoodsService tdGoodsService;

    @Autowired
    private TdCommonService tdCommonService;
    
    @Autowired
    private TdGoodsService tdGoodService;
    
    @Autowired
    private TdUserService tdUserService;

    /**
     * 加入购物车
     * @param id 商品ID
     * @param quantity 数量 
     * @param qiang 抢购类型 0：正常销售 >0：促销
     * @param m 是否是触屏 0: 否 1: 是
     * @param req
     * @return
     */
    @RequestMapping(value = "/cart/init")
    @ResponseBody
    public Map<String, Object> addCart(Long id, Long catId, Long quantity, String zhid, HttpServletRequest req,ModelMap map)
    {
    	Map<String, Object>res = new HashMap<String, Object>();
    	res.put("code", 1);
        // 是否已登录
        boolean isLoggedIn = true;

        String username = (String) req.getSession().getAttribute("username");

        if (null == username) 
        {
            isLoggedIn = false;
            username = req.getSession().getId();
        }
        
        if (null == quantity || quantity.compareTo(1L) < 0)
        {
            quantity = 1L;
        }
        
        if (null != id)
        {
            TdGoods goods = tdGoodsService.findOne(id);

            if (null != goods)
            {
                // 购物车项目
                List<TdCartGoods> oldCartGoodsList = null;
                
                // 购物车是否已有该商品
                oldCartGoodsList = tdCartGoodsService.findByGoodsIdAndUsername(id, username);
                
                // 有多项，则在第一项上数量进行相加
                if (null != oldCartGoodsList && oldCartGoodsList.size() > 0) 
                {
                    res.put("msg", "东西已经在购物车了");
                    return res;
                }
                // 新增购物车项
                else
                {
                    TdCartGoods cartGoods = new TdCartGoods();
                    
                    cartGoods.setIsLoggedIn(isLoggedIn);
                    cartGoods.setUsername(username);
                    cartGoods.setGoodsCoverImageUri(goods.getCoverImageUri());
                    cartGoods.setGoodsTitle(goods.getTitle());
                    cartGoods.setPrice(goods.getSalePrice());
                    cartGoods.setIsSelected(false);
                    cartGoods.setGoodsId(goods.getId());
                    cartGoods.setQuantity(quantity);
                    
                    tdCartGoodsService.save(cartGoods);
                }
            }
        }
        res.put("code", 0);
        if(null != catId)
        {
        	res.put("catId", catId);
        }
        res.put("msg", "成功加入购物车");
		return res;
    }

    @RequestMapping(value = "/cart")
    public String cart(HttpServletRequest req, ModelMap map) 
    {

        String username = (String) req.getSession().getAttribute("username");

        // 未登录用户的购物车商品
        List<TdCartGoods> cartSessionGoodsList = tdCartGoodsService.findByUsername(req.getSession().getId());
        if (null == username)
        {
            username = req.getSession().getId();
        }
        else
        {
            // 合并商品
            // 已登录用户的购物车
            List<TdCartGoods> cartUserGoodsList = tdCartGoodsService
                    .findByUserId(tdUserService.findByUsername(username).getId());
            for (TdCartGoods cg : cartSessionGoodsList)
            {
            // 将未登录用户的购物车加入已登录用户购物车中
            	cg.setIsLoggedIn(true);  //zhangji
                cg.setUsername(username);
                cartUserGoodsList.add(cg);
            }

            cartUserGoodsList = tdCartGoodsService.save(cartUserGoodsList);

            for (TdCartGoods cg1 : cartUserGoodsList) 
            {
                // 删除重复的商品
                List<TdCartGoods> findList = tdCartGoodsService.findByGoodsIdAndUserId(cg1.getGoodsId(), tdUserService.findByUsername(username).getId());

                if (null != findList && findList.size() > 1) 
                {
                    tdCartGoodsService.delete(findList.subList(1,findList.size()));
                }
            }
        }
        TdUser user = tdUserService.findByUsername(username);
        List<TdCartGoods> resList = null;
        if(null == user){
        	resList = tdCartGoodsService.findByUsername(username);
        }else{
        	resList = tdCartGoodsService.findByUserId(tdUserService.findByUsername(username).getId());
        }
        
        
        List<TdGoods> tdGoodsList = new ArrayList<>();
        for (int i = 0; i < resList.size() ;i++)
        { 
        	TdCartGoods tdCartGoods = resList.get(i);
			TdGoods tdGoods = tdGoodService.findOne(tdCartGoods.getGoodsId());
			if (null != tdGoods) 
			{
				map.addAttribute("cartId_" + i,	tdCartGoods.getId());
				tdGoodsList.add(tdGoods);
			}			
		}
        
        if (tdGoodsList != null && tdGoodsList.size()>=1)
        {
        	map.addAttribute("goods_list",tdGoodsList);
		}

        
        map.addAttribute("cart_goods_list", tdCartGoodsService.updateGoodsInfo(resList));
        map.addAttribute("showIcon",3);
        tdCommonService.setHeader(map, req);

        
        return "/client/cart";
    }

    @RequestMapping(value = "/cart/toggleSelect", method = RequestMethod.POST)
    public String cartToggle(Long id, HttpServletRequest req, ModelMap map) {

        String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
            username = req.getSession().getId();
        }

        List<TdCartGoods> cartGoodsList = tdCartGoodsService
                .findByUsername(username);

        if (null != id) {
            for (TdCartGoods cartGoods : cartGoodsList) {
                if (cartGoods.getId().equals(id)) {
                    if (null == cartGoods.getIsSelected() || false == cartGoods.getIsSelected())
                    {
                        cartGoods.setIsSelected(true);
                    }
                    else
                    {
                        cartGoods.setIsSelected(false);
                    }
                    cartGoods = tdCartGoodsService.save(cartGoods);
                    break;
                }
            }
        }

        map.addAttribute("cart_goods_list", tdCartGoodsService.updateGoodsInfo(cartGoodsList));

        return "/client/cart_goods";
    }

    @RequestMapping(value = "/cart/toggleAll", method = RequestMethod.POST)
    public String cartToggleAll(Integer sid, HttpServletRequest req,
            ModelMap map) {

        String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
            username = req.getSession().getId();
        }

        List<TdCartGoods> cartGoodsList = tdCartGoodsService
                .findByUsername(username);

        if (null != sid) {
            if (sid.equals(0)) // 全选
            {
                for (TdCartGoods cartGoods : cartGoodsList) {
                    cartGoods.setIsSelected(true);
                }
            } else // 取消全选
            {
                for (TdCartGoods cartGoods : cartGoodsList) {
                    cartGoods.setIsSelected(false);
                }
            }
            tdCartGoodsService.save(cartGoodsList);
        }

        map.addAttribute("cart_goods_list", tdCartGoodsService.updateGoodsInfo(cartGoodsList));

        return "/client/cart_goods";
    }

    @RequestMapping(value = "/cart/numberAdd", method = RequestMethod.POST)
    public String cartNumberAdd(Long id, HttpServletRequest req, ModelMap map) {

        String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
            username = req.getSession().getId();
        }

        if (null != id) {
            TdCartGoods cartGoods = tdCartGoodsService.findOne(id);
            
            if (null != cartGoods)
            {
                // 226 和 1644商品仅限购买一次
                if (!cartGoods.getGoodsId().equals(226L) && !cartGoods.getGoodsId().equals(1644L))
                {
                    if (cartGoods.getUsername().equalsIgnoreCase(username)) {
                        long quantity = cartGoods.getQuantity();
                        cartGoods.setQuantity(quantity + 1);
                        tdCartGoodsService.save(cartGoods);
                    }
                }
            }
        }

        map.addAttribute("cart_goods_list",
                tdCartGoodsService.updateGoodsInfo(tdCartGoodsService.findByUsername(username)));

        return "/client/cart_goods";
    }

    @RequestMapping(value = "/cart/numberMinus", method = RequestMethod.POST)
    public String cartNumberMinus(Long id, HttpServletRequest req, ModelMap map) {

        String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
            username = req.getSession().getId();
        }

        if (null != id) {
            TdCartGoods cartGoods = tdCartGoodsService.findOne(id);

            if (null != cartGoods)
            {
                // 226 和 1644商品仅限购买一次
                if (!cartGoods.getGoodsId().equals(226L) && !cartGoods.getGoodsId().equals(1644L))
                {
                    if (cartGoods.getUsername().equalsIgnoreCase(username)) {
                        long quantity = cartGoods.getQuantity();
        
                        quantity = quantity > 1 ? quantity - 1 : quantity;
        
                        cartGoods.setQuantity(quantity);
                        tdCartGoodsService.save(cartGoods);
                    }
                }
            }
        }

        map.addAttribute("cart_goods_list",
                tdCartGoodsService.updateGoodsInfo(tdCartGoodsService.findByUsername(username)));

        return "/client/cart_goods";
    }

    @RequestMapping(value = "/cart/del")
    public String cartDel(Long id, HttpServletRequest req, ModelMap map) {

        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
            username = req.getSession().getId();
        }
        
        
        if (null != id)
        {
        	TdCartGoods cartGoods = tdCartGoodsService.findOne(id);

            if (cartGoods.getUsername().equalsIgnoreCase(username)) 
            {
                tdCartGoodsService.delete(cartGoods);
            }
        }

        map.addAttribute("cart_goods_list", tdCartGoodsService.updateGoodsInfo(tdCartGoodsService.findByUsername(username)));

        return "redirect:/cart";
    }
}
