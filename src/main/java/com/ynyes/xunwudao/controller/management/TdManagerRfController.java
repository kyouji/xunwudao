package com.ynyes.xunwudao.controller.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.xunwudao.entity.TdApply;
import com.ynyes.xunwudao.entity.TdManager;
import com.ynyes.xunwudao.entity.TdManagerRole;
import com.ynyes.xunwudao.entity.TdProductCategory;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdManagerLogService;
import com.ynyes.xunwudao.service.TdManagerRoleService;
import com.ynyes.xunwudao.service.TdManagerService;
import com.ynyes.xunwudao.service.TdProductCategoryService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.SiteMagConstant;

/**
 * 后台产品控制器
 * 
 * @author Sharon
 */

@Controller
@RequestMapping(value = "/Verwalter/user/rf")
public class TdManagerRfController {

    @Autowired
    TdProductCategoryService tdProductCategoryService;

    @Autowired
    TdManagerLogService tdManagerLogService;
    
    @Autowired
    TdManagerRoleService tdManagerRoleService;
    
    @Autowired
    TdManagerService tdManagerService;
    
    @Autowired
    TdUserService tdUserService;
    
//    @RequestMapping(value = "/list")
//    public String categoryList(String __EVENTTARGET, String __EVENTARGUMENT,
//            String __VIEWSTATE, Long[] listId, Integer[] listChkId,Integer page, Integer size, String keywords,
//            Long[] listSortId, ModelMap map, HttpServletRequest req) {
//        String username = (String) req.getSession().getAttribute("manager");
//        if (null == username) {
//            return "redirect:/Verwalter/login";
//        }
//        if (null != __EVENTTARGET)
//        {
//            if (__EVENTTARGET.equalsIgnoreCase("btnPage"))
//            {
//                if (null != __EVENTARGUMENT)
//                {
//                    page = Integer.parseInt(__EVENTARGUMENT);
//                } 
//            }
//        }
//        
//        if (null == page || page < 0)
//        {
//            page = 0;
//        }
//        
//        if (null == size || size <= 0)
//        {
//            size = SiteMagConstant.pageSize;;
//        }
//        
//        if (null != keywords)
//        {
//            keywords = keywords.trim();
//        }
//        
//        map.addAttribute("page", page);
//        map.addAttribute("size", size);
//        map.addAttribute("keywords", keywords);
//        map.addAttribute("__EVENTTARGET", __EVENTTARGET);
//        map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
//        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
//
//        Page<TdUser> userPage = null;
//
//        if (null == keywords || "".equalsIgnoreCase(keywords))
//        {
//            userPage = tdUserService.findAllOrderBySortIdAsc(page, size);
//        }
//        else
//        {
//            userPage = tdUserService.searchAndOrderByIdDesc(keywords, page, size);
//        }
//
//        map.addAttribute("rf_page", userPage);
//
//        // 参数注回
//        map.addAttribute("__EVENTTARGET", __EVENTTARGET);
//        map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
//        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
//
//        return "/site_mag/user_rf_list";
//    }

    @RequestMapping(value = "/list")
    public String categoryEditDialog(Long id,  String __EVENTTARGET,
            String __EVENTARGUMENT, String __VIEWSTATE, ModelMap map,
            HttpServletRequest req) {
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username) {
            return "redirect:/Verwalter/login";
        }

        map.addAttribute("__EVENTTARGET", __EVENTTARGET);
        map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);

        map.addAttribute("category_list", tdProductCategoryService.findAll());

        // 参数类型表
//        map.addAttribute("param_category_list",
//                tdParameterCategoryService.findAll());


            if (null != id) {
                map.addAttribute("user",tdUserService.findOne(id));
                
                //分销下一层列表
                List<TdUser> userListOne = tdUserService.findByUpUserOneOrderByLastLoginTimeDesc(id);
                map.addAttribute("one_list", userListOne);
                
                //分销下两层列表
                if(null != userListOne){
                	for(TdUser item: userListOne){
                		
                		List<TdUser> userListTwo = tdUserService.findByUpUserOneOrderByLastLoginTimeDesc(item.getId());
                		if(null != userListTwo){
                			map.addAttribute("list_two_"+item.getId(), userListTwo);
                		}
                	}
                }
            }
       
        return "/site_mag/user_rf_list";

    }
    
    @RequestMapping(value="/edit")
    public String applyEdit(Long id,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
      
        if (null != id)
        {
        	TdUser tdUser = tdUserService.findOne(id);
            map.addAttribute("user",tdUser);
            if(null != tdUser.getUpUserOne()){
            	 TdUser userOne = tdUserService.findOne(tdUser.getUpUserOne());
            	 map.addAttribute("userOne", userOne);
            }
            if(null != tdUser.getUpUserTwo()){
           	 TdUser userTwo = tdUserService.findOne(tdUser.getUpUserTwo());
           	 map.addAttribute("userTwo", userTwo);
           }
            
        }
        return "/site_mag/user_rf_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(String username, Long id, String __EVENTTARGET,
            String __EVENTARGUMENT, String __VIEWSTATE, ModelMap map,
            HttpServletRequest req) {
        String managerUsername = (String) req.getSession().getAttribute("manager");
        if (null == managerUsername) {
            return "redirect:/Verwalter/login";
        }


        tdManagerLogService.addLog("edit", "修改分销用户", req);
        
        TdUser user  = tdUserService.findOne(id);
        if(null != user){
        	user.setUpUserOne(tdUserService.findByUsername(username).getId());
        }

        tdUserService.save(user);

        return "redirect:/Verwalter/user/list";
    }
    
    //更改用户分销弹窗
	 @RequestMapping(value = "/list/dialog")
		public String ListDialog( String keywords, Long categoryId, Integer page, Long priceId,
				Integer size, Integer total, String __EVENTTARGET, String __EVENTARGUMENT, String __VIEWSTATE, ModelMap map,
				HttpServletRequest req) {
			String username = (String) req.getSession().getAttribute("manager");
			if (null == username) {
				return "redirect:/Verwalter/login";
			}
			if (null != __EVENTTARGET) {
				if (__EVENTTARGET.equalsIgnoreCase("btnPage")) {
					if (null != __EVENTARGUMENT) {
						page = Integer.parseInt(__EVENTARGUMENT);
					}
				} else if (__EVENTTARGET.equalsIgnoreCase("btnSearch")) {

				} else if (__EVENTTARGET.equalsIgnoreCase("categoryId")) {

				}
			}

			if (null == page || page < 0) {
				page = 0;
			}

			if (null == size || size <= 0) {
				size = SiteMagConstant.pageSize;
				;
			}

			if (null != keywords) {
				keywords = keywords.trim();
			}

			Page<TdUser> userPage = null;

			if (null == keywords || "".equalsIgnoreCase(keywords)) {
				userPage = tdUserService.findByRoleIdOrderByIdDesc(0L, page, size);
			} else {
				userPage = tdUserService.searchAndFindByRoleIdOrderByIdDesc(keywords, 0L, page, size);
			}

			map.addAttribute("user_page", userPage);

			// 参数注回
			map.addAttribute("page", page);
			map.addAttribute("size", size);
			map.addAttribute("total", total);
			map.addAttribute("keywords", keywords);
			map.addAttribute("categoryId", categoryId);
			map.addAttribute("__EVENTTARGET", __EVENTTARGET);
			map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
			map.addAttribute("__VIEWSTATE", __VIEWSTATE);

			// 参数注回
//			map.addAttribute("category_list", tdProductCategoryService.findAll());
			map.addAttribute("page", page);
			map.addAttribute("size", size);
			map.addAttribute("total", total);
			map.addAttribute("keywords", keywords);
			map.addAttribute("categoryId", categoryId);
			map.addAttribute("__EVENTTARGET", __EVENTTARGET);
			map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
			map.addAttribute("__VIEWSTATE", __VIEWSTATE);

			return "/site_mag/dialog_rf_list";
		}		 
    
    

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> validateForm(String param, Long id) {
        Map<String, String> res = new HashMap<String, String>();

        res.put("status", "n");

        if (null == param || param.isEmpty()) {
            res.put("info", "该字段不能为空");
            return res;
        }

//        if (null == id) // 新增分类，查找所有
//        {
//            if (null != tdProductCategoryService.findByTitle(param)) {
//                res.put("info", "已存在同名分类");
//                return res;
//            }
//        } else // 修改，查找除当前ID的所有
//        {
//            if (null != tdProductCategoryService.findByTitleAndIdNot(param, id)) {
//                res.put("info", "已存在同名分类");
//                return res;
//            }
//        }

        res.put("status", "y");

        return res;
    }

    private void productCategoryBtnSave(Long[] ids, Long[] sortIds) {
        if (null == ids || null == sortIds || ids.length < 1
                || sortIds.length < 1) {
            return;
        }

        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            TdProductCategory category = tdProductCategoryService.findOne(id);

            if (sortIds.length > i) {
                category.setSortId(sortIds[i]);
                tdProductCategoryService.save(category);
            }
        }
    }

    private void productCategoryBtnDelete(Long[] ids, Integer[] chkIds) {
        if (null == ids || null == chkIds || ids.length < 1
                || chkIds.length < 1) {
            return;
        }

        for (int chkId : chkIds) {
            if (chkId >= 0 && ids.length > chkId) {
                Long id = ids[chkId];

                tdProductCategoryService.delete(id);
            }
        }
    }
}
