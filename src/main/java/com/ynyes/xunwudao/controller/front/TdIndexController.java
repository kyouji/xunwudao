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
    public String index(String code,HttpServletRequest req, Device device, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
        String username = (String) req.getSession().getAttribute("username");
        if(null != username)
        {
        	map.addAttribute("username" , username);
            TdUser user = tdUserService.findByUsername(username);
            if(null != user){
                if (null != code) {
        			Map<String, String> res = getAccessToken(code);
        			if (null == tdUserService.findByUsernameAndUnionid(username, res.get("unionid"))) {
        				TdUser ever = tdUserService.findByUnionid(res.get("unionid"));
        				if(null != ever){
        					ever.setOpenid(null);
        					tdUserService.save(ever);
        				}
        				user.setUnionid(res.get("unionid"));
        				tdUserService.save(user);
        			}
        		}
            }
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
    
    
    /**
	 * @author lc
	 * @注释：通过code 获取 access_token,用户openid, refresh_token,
	 */
	public Map<String, String> getAccessToken(String code){
		
		Map<String, String> res = new HashMap<String, String>();
		
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" 				  
                   + Configure.getAppid()+ "&secret=" + Configure.getSecret()+
                   "&code=" +code + "&grant_type=authorization_code";
		
		String accessToken = null;
		String expiresIn = null;
		String refresh_token = null;
		String openid = null;
		
		//当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
		String unionid = null;
		try {  
			  
            URL urlGet = new URL(url);  

            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  

            http.setRequestMethod("GET"); // 必须是get方式请求  

            http.setRequestProperty("Content-Type",  

                    "application/x-www-form-urlencoded");  

            http.setDoOutput(true);  

            http.setDoInput(true);  

            http.connect();  

            InputStream is = http.getInputStream();  

            int size = is.available();  

            byte[] jsonBytes = new byte[size];  

            is.read(jsonBytes);  

            String message = new String(jsonBytes, "UTF-8");  

            JSONObject demoJson = JSONObject.fromObject(message);  

            accessToken = demoJson.getString("access_token");  
            expiresIn = demoJson.getString("expires_in");  
            refresh_token = demoJson.getString("refresh_token");
            openid = demoJson.getString("openid");
            unionid = demoJson.getString("unionid");
            
            res.put("access_token", accessToken);
            res.put("expires_in", expiresIn);
            res.put("refresh_token", refresh_token);
            res.put("openid", openid);
            res.put("unionid", unionid);
//           System.out.println("accessToken===="+accessToken);  
//            System.out.println("expiresIn==="+expiresIn);  
  
           // System.out.println("====================获取token结束==============================");  

            is.close();  

        } catch (Exception e) {  

            e.printStackTrace(); 
        }  
		return res;
	}
}
