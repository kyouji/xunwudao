package com.ynyes.xunwudao.controller.front;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tencent.common.Configure;
import com.ynyes.xunwudao.entity.TdArticle;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdArticleService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdUserService;

import net.sf.json.JSONObject;

/**
 * 首页（我的）【需登陆】
 * 角色数：2
 * 会计【唯一界面】、客户。
 *@author Zhangji
 */
@Controller
@RequestMapping
public class TdIndexController {

    
    @Autowired
    TdCommonService tdCommonService;
    
    @Autowired
    TdUserService tdUserService;
    
    @Autowired
    TdArticleService tdArticleService;

    @RequestMapping("/")
    public String index(HttpServletRequest req, Device device, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
        String username = (String) req.getSession().getAttribute("username");
        if(null != username)
        {
        	map.addAttribute("username" , username);
            TdUser user = tdUserService.findByUsername(username);
            map.addAttribute("user", user);
        }
        map.addAttribute("showIcon", 1);
        return "/client/welcome";
    }
    
    @RequestMapping("/index")
    public String index2(HttpServletRequest req, Device device, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
        String username = (String) req.getSession().getAttribute("username");
        if(null != username)
        {
        	map.addAttribute("username" , username);
            TdUser user = tdUserService.findByUsername(username);
            map.addAttribute("user", user);
        }
        
    	List<TdArticle> articleList = tdArticleService.findByMenuIdAndCategoryIdAndIsEnableOrderByCreateTimeDesc(10L, 1L);
    	if(null != articleList)
    	{
    		map.addAttribute("info", articleList.get(0));
    	}
        map.addAttribute("showIcon", 1);
        return "/client/index";
    }
    
    @RequestMapping("/welcome")
    public String welcome(HttpServletRequest req, Device device, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
        String username = (String) req.getSession().getAttribute("username");
        if(null != username)
        {
        	map.addAttribute("username" , username);
            TdUser user = tdUserService.findByUsername(username);
            map.addAttribute("user", user);
        }
        map.addAttribute("showIcon", 1);
        return "/client/welcome";
    }
    
}
