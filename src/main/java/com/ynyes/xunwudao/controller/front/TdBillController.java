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

import com.ynyes.xunwudao.entity.TdBill;
import com.ynyes.xunwudao.entity.TdPhoto;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdBillService;
import com.ynyes.xunwudao.service.TdBillTypeService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdEnterTypeService;
import com.ynyes.xunwudao.service.TdPhotoService;
import com.ynyes.xunwudao.service.TdUserService;

/**
 * 票据管理
 *@author Zhangji
 */
@Controller
@RequestMapping("/bill")
public class TdBillController {

    
    @Autowired
    TdCommonService tdCommonService;
    
    @Autowired
    TdUserService tdUserService;
    
    @Autowired
    TdBillService tdBillService;
    
    @Autowired
    TdBillTypeService tdBillTypeService;
    
    @Autowired
    TdPhotoService tdPhotoService;
    
    @Autowired
    TdEnterTypeService tdEnterTypeService;

    @RequestMapping(value="/upload" , method=RequestMethod.GET)
    public String billUpload(Long id,HttpServletRequest req,  ModelMap map) {        
        
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
        
        //该客户待确认上传的图片列表
        List<TdBill> billList = tdBillService.findByStatusIdAndUserId(1L, user.getId());
        map.addAttribute("bill_list", billList);
        
        //票据类别列表
        map.addAttribute("billType_list", tdBillTypeService.findByIsEnableTrueOrderBySortIdAsc());
        
        return "/client/bill_upload";
    }
    
    
    @RequestMapping(value="/upload/add" )
    public String billUploadAdd(Long billId, Long photoId, HttpServletRequest req,  ModelMap map) {        
        
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
        if(null != billId)
        {
            TdBill tdBill = tdBillService.findOne(billId);
            map.addAttribute("bill", tdBill);
            return "/client/bill_upload_add";
        }
        else if(null != photoId)
        {
            TdPhoto tdPhoto = tdPhotoService.findOne(photoId);
            map.addAttribute("photo", tdPhoto);
            return "/client/photo_upload_add";
        }


        return "/client/bill_upload_add";
    }
    
    //单个图片页面，单个确认页面
    @RequestMapping(value="/upload/confirm" )
    public String billUploadConfirm(Long billId,Long photoId, HttpServletRequest req,  ModelMap map) {        
        
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
        if (null != billId)
        {
        	 TdBill tdBill = tdBillService.findOne(billId);
             tdBill.setStatusId(1L); //待确认状态
             tdBillService.save(tdBill);
             map.addAttribute("bill", tdBill);
             
             //该客户待确认上传的图片列表
             List<TdBill> billList = tdBillService.findByStatusIdAndUserId(1L, user.getId());
             map.addAttribute("bill_list", billList);
             
             //票据类别列表
             map.addAttribute("billType_list", tdBillTypeService.findByIsEnableTrueOrderBySortIdAsc());
             
             return "/client/bill_upload";
        }
        else if (null != photoId)
        {
        	TdPhoto tdPhoto = tdPhotoService.findOne(photoId);
        	tdPhoto.setStatusId(1L);
        	tdPhotoService.save(tdPhoto);
            map.addAttribute("photo", tdPhoto);
            
            //该客户待确认上传的图片列表
            List<TdPhoto> photoList2 = tdPhotoService.findByStatusIdAndUserId(2L, user.getId());
            map.addAttribute("photo_list2", photoList2);
            List<TdPhoto> photoList1 = tdPhotoService.findByStatusIdAndUserId(1L, user.getId());
            map.addAttribute("photo_list1", photoList1);
            
    		tdCommonService.setHeader(map, req);
    		
    		map.addAttribute("enterType_list", tdEnterTypeService.findByIsEnableTrueOrderBySortIdAsc());
            
            return "/client/user_info";
        }

        return "/client/bill_upload";
    }
    
    //完成上传 改变状态id
	@RequestMapping(value = "/upload/finish", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> billUploadFinish(Long billTypeId, Long[] billIds, 
			 HttpServletRequest req) {
		Map<String, Object> res = new HashMap<String, Object>();

		res.put("code", 1);

        String username = (String) req.getSession().getAttribute("username");
        if(null == username)
        {
        	res.put("msg", "请先登陆");
        	res.put("login", 1);
        	return res;
        }
		
//        if(null == billTypeId)
//        {
//        	res.put("msg", "请选择票据类别！");
//        	return res;
//        }
        if(null == billIds || billIds.length < 1)
        {
        	res.put("msg", "请先拍摄票据并上传！");
        	return res;
        }
        
        TdUser tdUser = tdUserService.findByUsername(username);
        
        for(Long id : billIds)
        {
        	TdBill tdBill = tdBillService.findOne(id);
        	tdBill.setStatusId(2L); 
        	tdUser.setIsDeal(1L);
        	tdBill.setBillTypeId(billTypeId);
        	tdBill.setTime(new Date());
        	tdBillService.save(tdBill);
        }
        
        TdUser user= tdUserService.findByUsername(username);
        
        //确认页面中取消上传的要删除
        List<TdBill> toDelete = tdBillService.findByStatusIdAndUserId(1L, user.getId());
    	tdBillService.delete(toDelete);
  
        	res.put("code", 0);
			return res;
	
	}
	
    //查询进度
    @RequestMapping(value="/check" )
    public String billCheck(Long billId,HttpServletRequest req,  ModelMap map) {        
        
        String username = (String) req.getSession().getAttribute("username");
        if(null == username)
        {
        	return "redirect:/login";
        }
        map.addAttribute("username" , username);

        TdUser user = tdUserService.findByUsername(username);
        //该客户待确认上传的图片列表
        List<TdBill> billList = tdBillService.findByUserId(user.getId());
        if(billList.size() > 0)
        {
            TdBill bill = billList.get(0);
            map.addAttribute("bill", bill);
        }
        else{
        	map.addAttribute("msg", "对不起，您没有上传票据的记录");
        	return "/client/index";
        }

        return "/client/bill_check";
    }
    
}
