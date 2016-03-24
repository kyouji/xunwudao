package com.ynyes.xunwudao.controller.front;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.tencent.common.Configure;
import com.ynyes.xunwudao.entity.TdArticle;
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdArticleService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
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
    
    @Autowired
    TdGoodsService tdGoodsService;

    @RequestMapping("/")
    public String index(String code,String rfCode, HttpServletRequest req, Device device, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
                if (null != code) {
        			Map<String, String> res = getAccessToken(code);
        			String unionid = res.get("unionid");
        			String openid = res.get("openid");
        			String access_token = res.get("access_token");
        			
        			if(null != unionid && null != openid){
        				TdUser tdUser = tdUserService.findByUnionid(unionid);
        				if(null == tdUser){
        					TdWeixinController weixin = new TdWeixinController();
        					Map<String, Object> userInfo = weixin.getWeixinInfo(access_token, openid);
        					
        					System.out.println("****RES"+res);
        					System.out.println("****openid"+openid);
        					
        					TdUser newUser = new TdUser();
        					newUser.setUsername("weixin_"+openid.substring(0, 8));
        					newUser.setPassword("123456");
        					newUser.setAddress(userInfo.get("province").toString()+userInfo.get("city").toString());
        					newUser.setNickname(userInfo.get("nickname").toString());
        					newUser.setSex((boolean)userInfo.get("sex"));
        					newUser.setHeadImageUrl(userInfo.get("headimgurl").toString());
        					newUser.setTotalPoints(0L);
        					newUser.setStatusId(1L);
        					newUser.setRegisterTime(new Date());
        					newUser.setLastLoginTime(new Date());
        					newUser.setOpenid(openid);
        					newUser.setUnionid(unionid);
        					tdUserService.save(newUser);
        					
        					Long id = newUser.getId();
        			        String number = String.format("%04d", id);
        					newUser.setNumber(number);
        					tdUserService.save(newUser);
        					
        					map.addAttribute("user", newUser);
        					req.getSession().setMaxInactiveInterval(60 * 60 * 2);
        					req.getSession().setAttribute("username", newUser.getUsername());
        					
        					 if(null != rfCode && !rfCode.equals("")){
        							//第一级推荐人id
        							Long userId = Long.parseLong(rfCode.substring(0, 4));
        							String url = null;
        							//商品id 
        							if(rfCode.length() > 7){
        								Long goodsId = Long.parseLong(rfCode.substring(7));
        								TdGoods goods = tdGoodsService.findOne(goodsId);
        								
        								//让新用户登陆后跳转到分享的商品详情页面
        								if( null != goods){
        									 url = "/goods/detail?id="+goods.getId();
        								
        								}
        							}
        							TdUser userOne = tdUserService.findOne(userId);
        							
        							
        							if(null == userOne){
        								System.out.println("userOne is NULL");
        							}
        							if(null != userOne){
        								//第一级推荐人
        								newUser.setUpUserOne(userOne.getId());
        								//第二级推荐人
        								Long userTwoUpId = userOne.getUpUserOne();
        								if(null != userTwoUpId){
        									TdUser userTwo = tdUserService.findOne(userTwoUpId);
        									if(null != userTwo){
        										newUser.setUpUserTwo(userTwo.getId());
        									}
        								}
        							}
        			        
        				        if(null != url && !url.equals("")){
        				        	return "redirect:"+url;
        				        }else{
        				        	return "redirect:/user/center?WX=1";
        				        }
        			        }
        				}
        				else{
        					tdUser.setLastLoginTime(new Date());
        					tdUserService.save(tdUser);
        					map.addAttribute("user", tdUser);
        					req.getSession().setMaxInactiveInterval(60 * 60 * 2);
        					req.getSession().setAttribute("username", tdUser.getUsername());
        					return "redirect:/user/center";
        				}
        			}
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
