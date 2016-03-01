package com.ynyes.xunwudao.controller.front;

import java.util.Date;
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

import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdUserCollect;
import com.ynyes.xunwudao.entity.TdApply;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdApplyService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdUserCollectService;
import com.ynyes.xunwudao.service.TdUserService;


/**
 * 用户中心
 * 
 * @author Sharon
 *
 */
@Controller
public class TdUserCollectController {
	@Autowired
	private TdUserCollectService tdUserCollectService;
	
	@Autowired
	private TdGoodsService tdGoodsService;
	
	@Autowired
	private TdCommonService tdCommonService;
	
	@Autowired
	private TdUserService tdUserService;
	
	@Autowired
	private TdApplyService tdApplyService;
	
	//添加收藏
	@RequestMapping(value = "/user/collect/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> collectAdd(HttpServletRequest req, Long goodsId,
	          ModelMap map) {
	
	      Map<String, Object> res = new HashMap<String, Object>();
	      res.put("code", 1);
	
	      if (null == goodsId) {
	          res.put("msg", "参数错误");
	          return res;
	      }
	
	      String username = (String) req.getSession().getAttribute("username");
	
	      if (null == username) {
	          res.put("msg", "请先登录");
	          res.put("login", 1);
	          return res;
	      }
	
	      res.put("code", 0);
	
	      // 没有收藏
	      if (null == tdUserCollectService.findByUsernameAndGoodsId(username,
	              goodsId)) {
	          TdGoods goods = tdGoodsService.findOne(goodsId);
	
	          if (null == goods) {
	              res.put("msg", "商品不存在");
	              return res;
	          }
	          
	          if (null == goods.getTotalCollects())
	          {
	              goods.setTotalCollects(0L);
	          }
	          
	          goods.setTotalCollects(goods.getTotalCollects() + 1L);
	          
	          tdGoodsService.save(goods);
	
	          TdUserCollect collect = new TdUserCollect();
	
	          collect.setUsername(username);
	          collect.setGoodsId(goods.getId());
	          collect.setGoodsCoverImageUri(goods.getCoverImageUri());
	          collect.setGoodsTitle(goods.getTitle());
	          collect.setGoodsSalePrice(goods.getSalePrice());
	          collect.setCollectTime(new Date());
	          collect.setType(1L);
	
	          tdUserCollectService.save(collect);
	
	          res.put("msg", "添加成功");
	
	          return res;
	      }
	
	      res.put("msg", "您已收藏了该商品");
	
	      return res;
	}
	
	//删除收藏商品
	@RequestMapping(value = "/user/collect/remove", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> collectRemove(HttpServletRequest req, Long goodsId,
	          ModelMap map) {
	
	      Map<String, Object> res = new HashMap<String, Object>();
	      res.put("code", 1);
	
	      if (null == goodsId) {
	          res.put("msg", "参数错误");
	          return res;
	      }
	
	      String username = (String) req.getSession().getAttribute("username");
	
	      if (null == username) {
	          res.put("msg", "请先登录");
	          res.put("login", 1);
	          return res;
	      }
	
	      res.put("code", 0);
	
	      // 没有收藏
	      if (null != tdUserCollectService.findByUsernameAndGoodsId(username,
	              goodsId)) {
	          TdGoods goods = tdGoodsService.findOne(goodsId);
	
	          if (null == goods) {
	              res.put("msg", "商品不存在");
	              return res;
	          }
	          
	          if (null == goods.getTotalCollects())
	          {
	              goods.setTotalCollects(0L);
	          }
	          
	          goods.setTotalCollects(goods.getTotalCollects() - 1L);
	          
	          tdGoodsService.save(goods);
	
	          TdUserCollect collect = tdUserCollectService.findByUsernameAndGoodsId(username, goodsId);
	
	          tdUserCollectService.delete(collect);
	
	          res.put("msg", "取消成功");
	
	          return res;
	      }
	
	      res.put("msg", "您已取消了该商品");
	
	      return res;
	}
	
	//个人中心-我的收藏-删除收藏商品
		@RequestMapping(value = "/user/collect/delete", method = RequestMethod.POST)
		@ResponseBody
		public Map<String, Object> collectdelete(HttpServletRequest req, Long[] goodsIds,
		          ModelMap map) {
		
		      Map<String, Object> res = new HashMap<String, Object>();
		      res.put("code", 1);
		
		      if (null == goodsIds || goodsIds.length == 0) {
		          res.put("msg", "请选择至少一件商品！");
		          return res;
		      }
		
		      String username = (String) req.getSession().getAttribute("username");
		
		      if (null == username) {
		          res.put("msg", "请先登录");
		          res.put("login", 1);
		          return res;
		      }
		
		      res.put("code", 0);
		
		      // 没有收藏
		      for(Long goodsId : goodsIds){
		    	  if (null != tdUserCollectService.findByUsernameAndGoodsId(username,
			              goodsId)) {
			          TdGoods goods = tdGoodsService.findOne(goodsId);
			
			          if (null == goods) {
			              res.put("msg", "商品不存在");
			              return res;
			          }
			          
			          if (null == goods.getTotalCollects())
			          {
			              goods.setTotalCollects(0L);
			          }
			          
			          goods.setTotalCollects(goods.getTotalCollects() - 1L);
			          
			          tdGoodsService.save(goods);
			
			          TdUserCollect collect = tdUserCollectService.findByUsernameAndGoodsId(username, goodsId);
			
			          tdUserCollectService.delete(collect);
			          
			          return res;
			      }
		      }
		      res.put("msg", "您已取消了该商品");
		
		      return res;
		}
	
	//个人中心-我的收藏
	@RequestMapping(value = "/user/collect")
    public String collectList(
            String keywords,  HttpServletRequest req,
            ModelMap map) {
        String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
            return "redirect:/login";
        }

        tdCommonService.setHeader(map, req);

        TdUser tdUser = tdUserService.findByUsernameAndIsEnabled(username);

        map.addAttribute("user", tdUser);
    	List<TdUserCollect> collectList = tdUserCollectService.findByUsername(username);
    	map.addAttribute("collect_list", collectList);
    	map.addAttribute("showIcon", 4);
    	
        return "/client/user_collect";
    }
	
	
	//意见或建议
	@RequestMapping(value = "/suggestion/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> suggestion(HttpServletRequest req, String content,
	          ModelMap map) {
	
	      Map<String, Object> res = new HashMap<String, Object>();
	      res.put("code", 1);
	
	      if (null == content || content.equals("")) {
	          res.put("msg", "请输入内容");
	          return res;
	      }
	
	      String username = (String) req.getSession().getAttribute("username");
	
	      if (null == username) {
	    	  username="游客";
	      }
	
	      TdApply apply = new TdApply();
	      apply.setContent(content);
	      apply.setSortId(99L);
	      apply.setStatusId(0L);
	      apply.setRealName(username);
	      apply.setTime(new Date());
	      tdApplyService.save(apply);
	      
	      res.put("code", 0);
	      res.put("msg", "提交成功！");
	      return res;
	}
	
	
}
