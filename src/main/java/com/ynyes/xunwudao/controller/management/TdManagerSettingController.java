package com.ynyes.xunwudao.controller.management;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ynyes.xunwudao.entity.TdApplyType;
import com.ynyes.xunwudao.entity.TdArea;
import com.ynyes.xunwudao.entity.TdEnterType;
import com.ynyes.xunwudao.entity.TdSetting;
import com.ynyes.xunwudao.service.TdApplyTypeService;
import com.ynyes.xunwudao.service.TdAreaService;
import com.ynyes.xunwudao.service.TdEnterTypeService;
import com.ynyes.xunwudao.service.TdManagerLogService;
import com.ynyes.xunwudao.service.TdSettingService;
import com.ynyes.xunwudao.util.SiteMagConstant;

/**
 * 后台广告管理控制器
 * 
 * @author Sharon
 */

@Controller
@RequestMapping(value="/Verwalter/setting")
public class TdManagerSettingController {
    
    @Autowired
    TdSettingService tdSettingService;
    
    @Autowired
    TdManagerLogService tdManagerLogService;
    
    @Autowired
    TdEnterTypeService tdEnterTypeService;
    
    @Autowired
    TdAreaService tdAreaService;
    
    @Autowired
    TdApplyTypeService tdApplyTypeService;
    
    @RequestMapping
    public String setting(Long status, ModelMap map,
            HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username) {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("setting", tdSettingService.findTopBy());
        map.addAttribute("status", status);
        
        return "/site_mag/setting_edit";
    }
    
    @RequestMapping(value="/save")
    public String orderEdit(TdSetting tdSetting,
                        ModelMap map,
                        HttpServletRequest req){
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        if (null == tdSetting.getId())
        {
            tdManagerLogService.addLog("add", "用户修改系统设置", req);
        }
        else
        {
            tdManagerLogService.addLog("edit", "用户修改系统设置", req);
        }
        
        tdSettingService.save(tdSetting);
        
        return "redirect:/Verwalter/setting?status=1";
    }
    
    
     /* 
     ===========+++++++++++++===============
    --------------------系统类别模块  begin -----------------------
    ============+++++++++++++++===========*/
    /**
     * 列表
     * @author Zhangji
     */
    @RequestMapping(value = "/{type}/list")
    public String settingTypeList(
    								@PathVariable String type,
						            String __EVENTTARGET,
						            String __EVENTARGUMENT,
						            String __VIEWSTATE,
						            Long[] listId,
						            Integer[] listChkId,
						            Long[] listSortId,
						            Boolean[] listIsEnable,
						            ModelMap map,
						            HttpServletRequest req)
    {
    	String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        if (null != __EVENTTARGET)
        {
            if (__EVENTTARGET.equalsIgnoreCase("btnDelete"))
            {
                btnTypeDelete(type, listId, listChkId);
                switch (type)
                {
                	case "billType": tdManagerLogService.addLog("delete", "删除票据类型", req);
                		break;
                	case "enterType": tdManagerLogService.addLog("delete", "删除公司类型", req);
                		break;
                	case "area": tdManagerLogService.addLog("delete", "删除区域类型", req);
                		break;
                	case "applyType": tdManagerLogService.addLog("delete", "删除申请业务类型", req);
                		break;
                	default:tdManagerLogService.addLog("delete", "删除信息", req);
                }
                	
                
            }
            else if (__EVENTTARGET.equalsIgnoreCase("btnSave"))
            {
            	btnTypeSave(type , listId, listSortId, listIsEnable);
                switch (type)
                {
                	case "billType": tdManagerLogService.addLog("save", "修改票据类型", req);
                		break;
                	case "enterType": tdManagerLogService.addLog("save", "修改公司类型", req);
                		break;
                	case "area": tdManagerLogService.addLog("save", "修改区域类型", req);
                		break;
                	case "applyType": tdManagerLogService.addLog("save", "修改申请业务类型", req);
            		break;
                	default:tdManagerLogService.addLog("save", "修改信息", req);
                }
            }

        }

        map.addAttribute("__EVENTTARGET", __EVENTTARGET);
        map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
        switch (type)
        {
	    	
	    	case "enterType": map.addAttribute("enterType_list", tdEnterTypeService.findAllOrderBySortIdAsc());
	    	return "/site_mag/enterType_list";
	    	
	    	case "area": map.addAttribute("area_list", tdAreaService.findAllOrderBySortIdAsc());
	    	return "/site_mag/area_list";
	    	
	    	case "applyType": map.addAttribute("applyType_list", tdApplyTypeService.findAllOrderBySortIdAsc());
	    	return "/site_mag/applyType_list";
	    	
	    	default: map.addAttribute("area_list", tdAreaService.findAllOrderBySortIdAsc());
        }
        
    	return "/site_mag/billType_list";
    }
    
    //编辑内容
    @RequestMapping(value="/{type}/edit")
    public String settingTypeEdit(Long id,
    					@PathVariable String type,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req)
    {
        String username = (String) req.getSession().getAttribute("manager");
        
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
            
        switch (type)
        {
		        
	    	case "enterType": 
		        if (null != id)
		        {
		        	map.addAttribute("enterType", tdEnterTypeService.findOne(id));
		        }
		        return "/site_mag/enterType_edit";
	    	
	    	case "area": 
		        if (null != id)
		        {
		        	map.addAttribute("area", tdAreaService.findOne(id));
		        }
		        return "/site_mag/area_edit";
		        
	    	case "applyType": 
		        if (null != id)
		        {
		        	map.addAttribute("applyType", tdApplyTypeService.findOne(id));
		        }
		        return "/site_mag/applyType_edit";
	    	
	    	default: 
	    		 if (null != id)
		        {
		        	map.addAttribute("area", tdAreaService.findOne(id));
		        }
        }

        
        return "/site_mag/billType_edit";
    }
    
    //公司类型保存
    @RequestMapping(value="/enterType/save", method = RequestMethod.POST)
    public String enterTypeSave(TdEnterType tdEnterType,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req) {
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
        
        tdEnterTypeService.save(tdEnterType);
        
        tdManagerLogService.addLog("edit", "修改公司类型", req);
        
        return "redirect:/Verwalter/setting/enterType/list";
    }
    
    //区域类型保存
    @RequestMapping(value="/area/save", method = RequestMethod.POST)
    public String areaTypeSave(TdArea tdArea,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req) {
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
        
        tdAreaService.save(tdArea);
        
        tdManagerLogService.addLog("edit", "修改区域类型", req);
        
        return "redirect:/Verwalter/setting/area/list";
    }
    
    //申请业务类型保存
    @RequestMapping(value="/applyType/save", method = RequestMethod.POST)
    public String applyTypeSave(TdApplyType tdApplyType,
                        String __VIEWSTATE,
                        ModelMap map,
                        HttpServletRequest req) {
        String username = (String) req.getSession().getAttribute("manager");
        if (null == username)
        {
            return "redirect:/Verwalter/login";
        }
        
        map.addAttribute("__VIEWSTATE", __VIEWSTATE);
        
        tdApplyTypeService.save(tdApplyType);
        
        tdManagerLogService.addLog("edit", "修改申请业务类型", req);
        
        return "redirect:/Verwalter/setting/applyType/list";
    }
    
    /**
     * 批量删除各种信息类别
     * @author Zhangji
     */
    private void btnTypeDelete(String type, Long[] ids, Integer[] chkIds)
    {
        if (null == ids || null == chkIds || null == type || type.equals("")
                || ids.length < 1 || chkIds.length < 1)
        {
            return;
        }
        
        for (int chkId : chkIds)
        {
            if (chkId >=0 && ids.length > chkId)
            {
                Long id = ids[chkId];
                if (type.equals("enterType"))
                {
               	 tdEnterTypeService.delete(id);
               }
                else if (type.equals("area"))
                {
               	 tdAreaService.delete(id);
               }
                else if (type.equals("applyType"))
                {
               	 tdApplyTypeService.delete(id);
               }
            }
        }
    }

    /**
     * 批量保存各种信息类别
     * @author Zhangji
     */
    private void btnTypeSave(String type, Long[] ids, Long[] sortIds, Boolean[] isEnables)
    {
        if (null == ids || null == sortIds || null == isEnables
                || ids.length < 1 || sortIds.length < 1 || isEnables.length < 1)
        {
            return;
        }
        
        for (int i = 0; i < ids.length; i++)
        {
            Long id = ids[i];
           
            if (type.equals("enterType"))
            {
	            TdEnterType e = tdEnterTypeService.findOne(id);
	            if (null != e)
	            {
	                if (sortIds.length > i)
	                {
	                    e.setSortId(sortIds[i]);
	                    tdEnterTypeService.save(e);
	                }
	                if(isEnables.length > i)
	                {
	                	e.setIsEnable(isEnables[i]);
	                	tdEnterTypeService.save(e);
	                }
	            }
            }
            else if (type.equals("area"))
            {
	            TdArea e = tdAreaService.findOne(id);
	            if (null != e)
	            {
	                if (sortIds.length > i)
	                {
	                    e.setSortId(sortIds[i]);
	                    tdAreaService.save(e);
	                }
	                if(isEnables.length > i)
	                {
	                	e.setIsEnable(isEnables[i]);
	                	tdAreaService.save(e);
	                }
	            }
            }
            else if (type.equals("applyType"))
            {
	            TdApplyType e = tdApplyTypeService.findOne(id);
	            if (null != e)
	            {
	                if (sortIds.length > i)
	                {
	                    e.setSortId(sortIds[i]);
	                    tdApplyTypeService.save(e);
	                }
	                if(isEnables.length > i)
	                {
	                	e.setIsEnable(isEnables[i]);
	                	tdApplyTypeService.save(e);
	                }
	            }
            }
        }
    }
    /*--------------------系统类别模块  end ------------------------*/ 
}
