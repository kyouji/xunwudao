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

import com.ynyes.xunwudao.entity.TdFinance;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdFinanceService;
import com.ynyes.xunwudao.service.TdUserService;

/**
 * 票据管理
 *@author Zhangji
 */
@Controller
@RequestMapping("/finance")
public class TdFinanceController {

    
    @Autowired
    TdCommonService tdCommonService;
    
    @Autowired
    TdUserService tdUserService;
    
    @Autowired
    TdFinanceService tdFinanceService;
    


    @RequestMapping(value="/detail" , method=RequestMethod.GET)
    public String FinanceDetail(Long id,HttpServletRequest req,  ModelMap map) {        
        
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
        
        List<TdFinance> FinanceList = tdFinanceService.findByUserId(user.getId());
        
        if(FinanceList.size() > 0)
        {
        	map.addAttribute("finance_list", FinanceList);
        	if (null == id)
        	{
        		map.addAttribute("finance", FinanceList.get(0));
        	}
        	else{
        		map.addAttribute("finance", tdFinanceService.findOne(id));
        	}
        }

        
        return "/client/finance_detail";
    }
    
    
    
    
}
