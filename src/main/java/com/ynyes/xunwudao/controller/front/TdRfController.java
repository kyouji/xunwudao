package com.ynyes.xunwudao.controller.front;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdUserService;


/**
 * 用户中心
 * 
 * @author Sharon
 *
 */
@Controller
public class TdRfController {

	@Autowired
	private TdUserService tdUserService;

	@Autowired
	private TdCommonService tdCommonService;
	

	@RequestMapping(value = "/user/rf")
	public String user(HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}
		tdCommonService.setHeader(map, req);

		TdUser tdUser = tdUserService.findByUsername(username);
		if (null == tdUser) {
			return "/client/login";
		}
		if(null != tdUser){
			//下属第一层会员
			List<TdUser> oneList = tdUserService.findByUpUserOneOrderByLastLoginTimeDesc(tdUser.getId());
			if(null != oneList){
				map.addAttribute("one_list", oneList);
				
				//各自的下属第二级
				if(null != oneList){
					for(TdUser user : oneList){
						List<TdUser> twoList = tdUserService.findByUpUserOneOrderByLastLoginTimeDesc(user.getId());
						map.addAttribute("two_list_"+user.getId(), twoList);
					}
				}
				
			}
		}

		map.addAttribute("user", tdUser);
		map.addAttribute("showIcon", 4);

		return "/client/user_rf";
	}

	
}
