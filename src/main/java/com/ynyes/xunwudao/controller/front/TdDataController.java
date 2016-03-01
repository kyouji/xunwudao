package com.ynyes.xunwudao.controller.front;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.SiteMagConstant;

@Controller
public class TdDataController {
	
	@Autowired
	private TdUserService tdUserService;
	
	String filepath = SiteMagConstant.imagePath;

	 @RequestMapping(value="/data" , method=RequestMethod.GET)
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
	        
//	        //该客户待确认上传的图片列表
//	        List<TdData> dataList = tdDataService.findByUsernameAndDataTypeIdOrderBySortIdAsc(username, dataTypeId);
//	        map.addAttribute("data_list", dataList);
	        /*
	         * TODO
	         * 想办法按类型、月份查找数据
	         */

	        
	        return "/client/data";
	    }
	 
	 @RequestMapping(value="/download/data", method = RequestMethod.GET)
	    @ResponseBody
	    public void download(String name,Long id,
	                HttpServletResponse resp,
	                HttpServletRequest req) throws IOException {
	        if (null == name)
	        {
	            return;
	        }
	        
	        OutputStream os = resp.getOutputStream();  
	        
	        File file = new File(filepath +"/" + name);
	        
	        if (file.exists())
	        {
	            try {
	                resp.reset();
	                resp.setHeader("Content-Disposition", "attachment; filename="
	                		+URLEncoder.encode(name, "UTF-8"));
	                resp.setContentType("application/octet-stream; charset=utf-8");
	                os.write(FileUtils.readFileToByteArray(file));
	                os.flush();
	            } finally {
	                if (os != null) {
	                    os.close();
	                }
	            }
	        }
	        else 
	        {
	        	return;
	        }
	    }
	
	 
	 @RequestMapping(value="/download/app", method = RequestMethod.GET)
	    @ResponseBody
	    public void downloadApp(String name,Long id,
	                HttpServletResponse resp,
	                HttpServletRequest req) throws IOException {
	        if (null == name)
	        {
	            return;
	        }
	        
	        OutputStream os = resp.getOutputStream();  
	        name = name.substring(7);
	        System.out.println(name);
	        File file = new File(filepath +"/" + name);
	        
	        if (file.exists())
	        {
	            try {
	                resp.reset();
	                resp.setHeader("Content-Disposition", "attachment; filename="
	                		+URLEncoder.encode(name, "UTF-8"));
	                resp.setContentType("application/octet-stream; charset=utf-8");
	                os.write(FileUtils.readFileToByteArray(file));
	                os.flush();
	            } finally {
	                if (os != null) {
	                    os.close();
	                }
	            }
	        }
	        else 
	        {
	        	return;
	        }
	    }
}
