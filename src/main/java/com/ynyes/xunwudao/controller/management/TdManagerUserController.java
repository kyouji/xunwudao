package com.ynyes.xunwudao.controller.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.xunwudao.entity.TdApply;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.entity.TdUserCollect;
import com.ynyes.xunwudao.service.TdApplyService;
import com.ynyes.xunwudao.service.TdApplyTypeService;
import com.ynyes.xunwudao.service.TdAreaService;
import com.ynyes.xunwudao.service.TdEnterTypeService;
import com.ynyes.xunwudao.service.TdManagerLogService;
import com.ynyes.xunwudao.service.TdPhotoService;
import com.ynyes.xunwudao.service.TdUserCollectService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.SiteMagConstant;

/**
 * 后台用户管理控制器
 * 
 * @author Sharon
 */

@Controller
@RequestMapping(value="/Verwalter/user")
public class TdManagerUserController {
    
    @Autowired
    TdUserService tdUserService;
    
    @Autowired
    TdManagerLogService tdManagerLogService;
    
    @Autowired
    TdApplyService tdApplyService;
    
    @Autowired
    TdAreaService tdAreaService;
    
    @Autowired
    TdApplyTypeService tdApplyTypeService;
    
    @Autowired
    TdEnterTypeService tdEnterTypeService;

    @Autowired
    TdUserCollectService tdUserCollectService;
    
    @Autowired
    TdPhotoService tdPhotoService;
    
    @RequestMapping(value="/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> validateForm(String param, Long id) {
        Map<String, String> res = new HashMap<String, String>();
        
        res.put("status", "n");
        
        if (null == param || param.isEmpty())
        {
            res.put("info", "该字段不能为空");
            return res;
        }
        
        if (null == id)
        {
            if (null != tdUserService.findByUsername(param))
            {
                res.put("info", "已存在同名用户");
                return res;
            }
        }
        else
        {
            if (null != tdUserService.findByUsernameAndIdNot(param, id))
            {
                res.put("info", "已存在同名用户");
                return res;
            }
        }
        
        res.put("status", "y");
        
        return res;
    }
    
    
    
    @RequestMapping(value = "/check/mobile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> validateFormMobile(String param, Long id) {
        Map<String, String> res = new HashMap<String, String>();

        res.put("status", "n");

	        if (null == param || param.isEmpty()) {						
	            res.put("info", "该字段不能为空");
	            return res;
	        }
	        
	        TdUser tdUser = tdUserService.findByMobile(param);
	        
	        if (null == id) // 新增
	        {
	            if (null != tdUser) {
	                res.put("info", "该手机号不能使用");
	                return res;
	            }
	        } 
	        else // 修改，查找除当前ID的所有
	        {
	            TdUser thisUser = tdUserService.findOne(id);
	            
	            if (null == thisUser)
	            {
	                if (null != tdUser) {
	                    res.put("info", "该手机号不能使用");
	                    return res;
	                }
	            }
	            else
	            {
	                if (null != tdUser && !tdUser.getMobile().equals(thisUser.getMobile())  ) {
	                    res.put("info", "该手机号已被使用");
	                    return res;
	                }
	            }
	        }

        
        res.put("status", "y");

        return res;
    }
    
    @RequestMapping(value="/list")
    public String setting(Integer page,
                          Integer size,
                          String keywords,
                          Long roleId,
                          String __EVENTTARGET,
                          String __EVENTARGUMENT,
                          String __VIEWSTATE,
                          Long[] listId,
                          Integer[] listChkId,
                          ModelMap map,
                          HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username) {
            return "redirect:/Verwalter/login";
        }
        if (null != __EVENTTARGET)
        {
            if (__EVENTTARGET.equalsIgnoreCase("btnPage"))
            {
                if (null != __EVENTARGUMENT)
                {
                    page = Integer.parseInt(__EVENTARGUMENT);
                } 
            }
            else if (__EVENTTARGET.equalsIgnoreCase("btnDelete"))
            {
                btnDelete("user", listId, listChkId);
                tdManagerLogService.addLog("delete", "删除用户", req);
            }
        }
        
        if (null == page || page < 0)
        {
            page = 0;
        }
        
        if (null == size || size <= 0)
        {
            size = SiteMagConstant.pageSize;;
        }
        
        if (null != keywords)
        {
            keywords = keywords.trim();
        }
        
        map.addAttribute("page", page);
        map.addAttribute("size", size);
        map.addAttribute("keywords", keywords);
        map.addAttribute("roleId", roleId);
        map.addAttribute("__EVENTTARGET", __EVENTTARGET);
        map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);

        Page<TdUser> userPage = null;
        
        if (null == roleId)
        {
            if (null == keywords || "".equalsIgnoreCase(keywords))
            {
                userPage = tdUserService.findAllOrderBySortIdAsc(page, size);
            }
            else
            {
                userPage = tdUserService.searchAndOrderByIdDesc(keywords, page, size);
            }
        }
        else
        {
            if (null == keywords || "".equalsIgnoreCase(keywords))
            {
                userPage = tdUserService.findByRoleIdOrderByIdDesc(roleId, page, size);
            }
            else
            {
                userPage = tdUserService.searchAndFindByRoleIdOrderByIdDesc(keywords, roleId, page, size);
            }
        }
        
        map.addAttribute("user_page", userPage);
        
        return "/site_mag/user_list";
    }
    
    @RequestMapping(value="/edit")
    public String orderEdit(Long id,
    					Long done,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        if(null != done)
        {
        	map.addAttribute("done", done);
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
        
        map.addAttribute("enterType_list", tdEnterTypeService.findByIsEnableTrueOrderBySortIdAsc());
      
        if (null != id)
        {
        	TdUser user = tdUserService.findOne(id);
            map.addAttribute("user",user);
            map.addAttribute("id",id);
            map.addAttribute("roleId",user.getRoleId());
            map.addAttribute("photo_list", tdPhotoService.findByStatusIdAndUserId(2L, user.getId()));
            int lower = tdUserService.findByUpUserOneOrderByLastLoginTimeDesc(id).size();
            map.addAttribute("lower", lower);
        }
        map.addAttribute("roleId",0);
        return "/site_mag/user_edit";
    }
    

    
    @RequestMapping(value="/save")
    public String orderEdit(TdUser tdUser,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
        
        if (null == tdUser.getId())
        {
            tdManagerLogService.addLog("add", "创建用户", req);
        }
        else
        {
            tdManagerLogService.addLog("edit", "修改用户", req);
        }
        
        tdUser.setIsDeal(0L);
        tdUserService.save(tdUser);
        
//        if (tdUser.getRoleId()==1)
//        {
//        	
//        }
        
        return "redirect:/Verwalter/user/list";
    }
 
    
    
    @RequestMapping(value = "/role" , method = RequestMethod.GET)
    public String userAssumingControl(Long id,HttpServletRequest request , ModelMap map){
        String username = (String) request.getSession().getAttribute("manager");
        if (null == username) {
            return "redirect:/Verwalter/login";
        }
    	
        if (null != id )
        {
        	TdUser user = tdUserService.findOne(id);
        	
        	System.err.println(user);
			Integer roleId = user.getRoleId().intValue();
			request.getSession().setMaxInactiveInterval(60 * 60 * 2);
			
			if (null != roleId && roleId == 1)
			{
				request.getSession().setAttribute("enterpriseUsername", user.getUsername());
				request.getSession().setAttribute("enterpriseUsermobile", user.getMobile());
				request.getSession().setAttribute("username", user.getUsername());
				return "redirect:/enterprise/check";
			}
			
        }
        
    	return "redirect:/user/list";
    }
    

    //如果type是apply的话，就是用户的申请表单列表
    @RequestMapping(value="/{type}/list")
    public String list(@PathVariable String type,
                        Integer page,
                        Integer size,
                        Long statusId,
                        String keywords,
                        Long applyTypeId,
                        String __EVENTTARGET,
                        String __EVENTARGUMENT,
                        String __VIEWSTATE,
                        Long[] listId,
                        Integer[] listChkId,
                        Long[] listSortId,
                        ModelMap map,
                        HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        if (null != __EVENTTARGET)
        {
            if (__EVENTTARGET.equalsIgnoreCase("btnPage"))
            {
                if (null != __EVENTARGUMENT)
                {
                    page = Integer.parseInt(__EVENTARGUMENT);
                } 
            }
            else if (__EVENTTARGET.equalsIgnoreCase("btnDelete"))
            {
                btnDelete(type, listId, listChkId);
            }
        }
        
        if (null == page || page < 0)
        {
            page = 0;
        }
        
        if (null == size || size <= 0)
        {
            size = SiteMagConstant.pageSize;;
        }
        
        if (null != keywords)
        {
            keywords = keywords.trim();
        }
        
        map.addAttribute("page", page);
        map.addAttribute("size", size);
        map.addAttribute("statusId", statusId);
        map.addAttribute("keywords", keywords);
        map.addAttribute("__EVENTTARGET", __EVENTTARGET);
        map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
            
        if (null != type)
        {

            if (type.equalsIgnoreCase("apply")) // 业务申请表单
            {
            	Page<TdApply> applyPage =  null;
            	if(null == applyTypeId)
            	{
            		if(null == statusId)
            		{
            			applyPage= tdApplyService.findAllOrderBySortIdAsc(page, size);
            		}
            		else{
            			applyPage= tdApplyService.findByStatusIdOrderBySortIdAsc(statusId, page,size);
            		}
            	}
            	else{
            		if(null == statusId)
            		{
            			applyPage= tdApplyService.findByApplyTypeIdOrderBySortIdAsc(applyTypeId, page, size);
            		}
            		else{
            			applyPage= tdApplyService.findByApplyTypeIdAndStatusIdOrderBySortIdAsc(applyTypeId, statusId ,page, size);
            		}
            	}

            	map.addAttribute("applyTypeId", applyTypeId);
                map.addAttribute("apply_page", applyPage);
                
//                for(TdApply item : applyPage.getContent())
//                {
//                	TdApplyType applyType = tdApplyTypeService.findOne(item.getApplyTypeId());
//                	map.addAttribute("applyType_"+item.getId(), applyType.getTitle());
//                	
//                	TdUser tdUser = tdUserService.findOne(item.getApplyTypeId());
//                	map.addAttribute("user_"+item.getId(), tdUser);
//                }
                
                map.addAttribute("applyType_list", tdApplyTypeService.findByIsEnableTrueOrderBySortIdAsc());
                return "/site_mag/user_apply_list";
            }
        }
        
        return "/site_mag/error_404";
    }
    
    @RequestMapping(value="/apply/edit")
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
        	TdApply tdApply = tdApplyService.findOne(id);
            map.addAttribute("apply",tdApply);
//            map.addAttribute("user",tdUserService.findByUsername(username);
            
//            map.addAttribute("area",tdAreaService.findOne(tdApply.getAreaId()).getTitle());
        }
        return "/site_mag/user_apply_edit";
    }
    
   @RequestMapping(value="/apply/save")
   @ResponseBody
   public Map<String,Object> applySave(Long id,
	    					Long statusId,
	    					String remark,
	    					Long sortId,
	                        ModelMap map,
	                        HttpServletRequest req){
		 Map<String, Object> res = new HashMap<String, Object>();
			res.put("code", 1);
			
	        String username = (String) req.getSession().getAttribute("manager");
	        if (null == username) {
	        	res.put("msg", "请先登录！");
	            return res;
	        }
	        
	        TdApply tdApply = tdApplyService.findOne(id);
	        if(null != statusId)
	        {
	        	tdApply.setStatusId(statusId);
	        	tdApply.setRemark(remark);
	        	tdApply.setSortId(sortId);
	        	tdApply.setFinishTime(new Date());
	        	tdApplyService.save(tdApply);
	        	res.put("msg", "操作成功！");
	        }

			res.put("code", 0);

			return res;
	    }
   
    
    
    @ModelAttribute
    public void getModel(@RequestParam(value = "userId", required = false) Long userId,
                    @RequestParam(value = "userLevelId", required = false) Long userLevelId,
                    @RequestParam(value = "userConsultId", required = false) Long userConsultId,
                    @RequestParam(value = "userCommentId", required = false) Long userCommentId,
                    @RequestParam(value = "userReturnId", required = false) Long userReturnId,
                        Model model) {
        if (null != userId) {
            model.addAttribute("tdUser", tdUserService.findOne(userId));
        }
    }


    
//    private void btnSave(String type, Long[] ids, Long[] sortIds)
//    {
//        if (null == ids || null == sortIds
//                || ids.length < 1 || sortIds.length < 1
//                || null == type || "".equals(type))
//        {
//            return;
//        }
//        
//        for (int i = 0; i < ids.length; i++)
//        {
//            Long id = ids[i];
//            
//            if (type.equalsIgnoreCase("user")) // 用户
//            {
//                TdUser e = tdUserService.findOne(id);
//                
//                if (null != e)
//                {
//                    if (sortIds.length > i)
//                    {
//                        e.setSortId(sortIds[i]);
//                        tdUserService.save(e);
//                    }
//                }
//            }
//        }
//    }
    
    private void btnDelete(String type, Long[] ids, Integer[] chkIds)
    {
        if (null == ids || null == chkIds
                || ids.length < 1 || chkIds.length < 1 
                || null == type || "".equals(type))
        {
            return;
        }
        
        for (int chkId : chkIds)
        {
            if (chkId >=0 && ids.length > chkId)
            {
                Long id = ids[chkId];
                
                if (type.equalsIgnoreCase("user")) // 用户
                {
                    tdUserService.delete(tdUserService.findOne(id));
                    //同时删除用户收藏
                    List<TdUserCollect> collectList = tdUserCollectService.findByUserId(id);
                    if(null != collectList){
                    	tdUserCollectService.delete(collectList);
                    }
                    
                    
                }
                else if (type.equalsIgnoreCase("apply")) // 
                {
                    tdApplyService.delete(tdApplyService.findOne(id));
                }
            }
        }
    }
    
//    private void btnVerify(String type, Long[] ids, Integer[] chkIds)
//    {
//        if (null == ids || null == chkIds
//                || ids.length < 1 || chkIds.length < 1 
//                || null == type || "".equals(type))
//        {
//            return;
//        }
//        
//        for (int chkId : chkIds)
//        {
//            if (chkId >=0 && ids.length > chkId)
//            {
//                Long id = ids[chkId];
//                
//                if (type.equalsIgnoreCase("consult")) // 咨询
//                {
//                    TdUserConsult e = tdUserConsultService.findOne(id);
//                    
//                    if (null != e)
//                    {
//                        e.setStatusId(1L);
//                        tdUserConsultService.save(e);
//                    }
//                }
//                else if (type.equalsIgnoreCase("comment")) // 评论
//                {
//                    TdUserComment e = tdUserCommentService.findOne(id);
//                    
//                    if (null != e)
//                    {
//                        e.setStatusId(1L);
//                        tdUserCommentService.save(e);
//                    }
//                }
//                else if(type.equalsIgnoreCase("demand"))  //团购要求      @zhangji 2015年7月30日11:23:51
//                {
//                	TdDemand e = tdDemandService.findOne(id);
//                	
//                	if (null != e)
//                	{
//                		e.setStatusId(1L);
//                		tdDemandService.save(e);
//                	}
//                		
//                }
//                else if (type.equalsIgnoreCase("return")) // 退换货
//                {
//                    TdUserReturn e = tdUserReturnService.findOne(id);
//                    
//                    if (null != e)
//                    {
//                        e.setStatusId(1L);
//                        tdUserReturnService.save(e);
//                    }
//                }
//            }
//        }
//    }
}
