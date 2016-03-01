package com.ynyes.xunwudao.controller.front;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.xunwudao.entity.TdGather;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGatherService;
import com.ynyes.xunwudao.service.TdUserService;

/**
 * 票据管理
 *@author Zhangji
 */
@Controller
@RequestMapping("/gather")
public class TdGatherController {

    
    @Autowired
    TdCommonService tdCommonService;
    
    @Autowired
    TdUserService tdUserService;
    
    @Autowired
    TdGatherService tdGatherService;
    


    @RequestMapping(value="/confirm" , method=RequestMethod.GET)
    public String GatherUpload(Long id,HttpServletRequest req,  ModelMap map) {        
        
        String username = (String) req.getSession().getAttribute("username");
        if(null == username)
        {
        	return "redirect:/login";
        }
        map.addAttribute("username" , username);
        
        TdUser user = tdUserService.findByUsername(username);
        if (null != user)
        {
            map.addAttribute("user", user);
        }
        
        List<TdGather> gatherList = tdGatherService.findByUserId(user.getId());
        
        if(gatherList.size() > 0)
        {
        	map.addAttribute("gather_list", gatherList);
        	if (null == id)
        	{
        		map.addAttribute("gather", gatherList.get(0));
        	}
        	else{
        		map.addAttribute("gather", tdGatherService.findOne(id));
        	}
        }

        
        return "/client/gather_confirm";
    }
    
    
    
    
}
