package com.ynyes.xunwudao.controller.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.tencent.common.Configure;
import com.ynyes.xunwudao.entity.TdAccessToken;
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdAccessTokenService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.VerifServlet;

import net.sf.json.JSONObject;


/**
 * 登录
 *
 */
@Controller
public class TdWxShareController{
	@Autowired
	private TdAccessTokenService tdAccessTokenService;
	
@RequestMapping(value = "/bargain/friends/help")
	public String freiends(Integer page, String mobile,  HttpServletRequest req, ModelMap map) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		

			
			// 拉取用户信息
//			if (null != mobile) {			
//				if (null != tdBargainRecordService.findByOpenIdAndNotkan(res.get("openid"))) {
//					//return "/activity/bargain_error";
//				}else {					
//					getWxinfo(res.get("access_token"), res.get("openid"), mobile);
//				}
//			}
//			// 基本信息
//			tdCommonService.setHeader(map, req);

					
			if (null == page) {
				page = 0;
			}
//			TdBargainParticipant tdBargainParticipant = tdBargainParticipantService.findByMobile(mobile);
//			if (null != tdBargainParticipant) {
//				map.addAttribute("participant", tdBargainParticipant);
//				map.addAttribute("currentPrice", tdBargainParticipant.getCurrentPrice());
//			}		
//			map.addAttribute("bargain_setting", tdBargainSettingService.findTopBy());
//			map.addAttribute("bargain_record_page", tdBargainRecordService.findByMobileAndcutPriceNotNull(mobile, page, ClientConstant.pageSize));
//			map.addAttribute("totalrecords", tdBargainRecordService.countByMobileAndCutPriceNotNull(mobile));
//			map.addAttribute("openId", res.get("openid"));
			map.addAttribute("participantMobile", mobile);

//			//通过 access_toke 获取微信 jsapi_ticket
			String jsapi_ticket = checkjsapiTicket();
			
			map.addAttribute("jsapi_ticket", jsapi_ticket);
					
			// 获取是时间戳
			String timestamp =Long.toString(System.currentTimeMillis() / 1000);
			map.addAttribute("timestamp", timestamp);
			
			// 随机字符串
			String noncestr = UUID.randomUUID().toString();
			map.addAttribute("noncestr", noncestr);
			
			// 生成签名
			String tempStr = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr +"&timestamp=" 
							+ timestamp + "&url=http://www.xwd33.com/goods/detail"
							+ mobile +  "&state=STATE";

			// sha1加密
			String signature = "";
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
	        crypt.update(tempStr.getBytes("UTF-8"));
	        signature = byteToHex(crypt.digest());

		    map.addAttribute("signature", signature);
		    System.out.println(">>>>>>>>>>>>>>>>>>>"+signature);
			
			return "/client/bargain_friends";


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
        
        res.put("access_token", accessToken);
        res.put("expires_in", expiresIn);
        res.put("refresh_token", refresh_token);
        res.put("openid", openid);
//       System.out.println("accessToken===="+accessToken);  
//        System.out.println("expiresIn==="+expiresIn);  

       // System.out.println("====================获取token结束==============================");  

        is.close();  

    } catch (Exception e) {  

        e.printStackTrace(); 
    }  
	return res;
}

/**
 * @author lc
 * @注释：判断jsapi_ticket是否过期并返回最新jsapi_ticket
 */
public String checkjsapiTicket(){
	//List<TdAccessToken> tdAccessTokenlist = tdAccessTokenService.findAll();
	TdAccessToken tdAccessToken = tdAccessTokenService.findTopBy();
	if (null == tdAccessToken) {
		tdAccessToken = new TdAccessToken();
		String accessToken = getAccess_token();
		while (null == accessToken) {
			accessToken = getAccess_token();
		}
		tdAccessToken.setAccess_token(accessToken);
		tdAccessToken.setAccess_expires_in("7000");
		tdAccessToken.setAccess_updateTime(new Date());
		
		String jsapi_ticket = getTicket(accessToken);
		while (null == jsapi_ticket) {
			jsapi_ticket = getTicket(accessToken);
		}
		tdAccessToken.setJsapi_ticket(jsapi_ticket);
		tdAccessToken.setJsapi_ticket_expires_in("7000");
		tdAccessToken.setJsapi_ticket_updateTime(new Date());
		tdAccessTokenService.save(tdAccessToken);
		return jsapi_ticket;
	}else {
		if (null == tdAccessToken.getJsapi_ticket()) {
			String accessToken = checkAccessToken();
			String jsapi_ticket = getTicket(accessToken);
			while (null == jsapi_ticket) {
				jsapi_ticket = getTicket(accessToken);
			}
			tdAccessToken.setJsapi_ticket(jsapi_ticket);
			tdAccessToken.setJsapi_ticket_expires_in("7000");
			tdAccessToken.setJsapi_ticket_updateTime(new Date());
			tdAccessTokenService.save(tdAccessToken);
			
			return jsapi_ticket;
		}else {	
			Date now = new Date();// new Date()为获取当前系统时间
			if ((tdAccessToken.getJsapi_ticket_updateTime().getTime() + Long.parseLong(tdAccessToken.getJsapi_ticket_expires_in()+"000")) < now.getTime() ) {
				String accessToken = checkAccessToken();
				String jsapi_ticket = getTicket(accessToken);
				while (null == jsapi_ticket) {
					jsapi_ticket = getTicket(accessToken);
				}
				tdAccessToken.setJsapi_ticket(jsapi_ticket);
				tdAccessToken.setJsapi_ticket_updateTime(new Date());
				tdAccessTokenService.save(tdAccessToken);
				return jsapi_ticket;
			}else {
				return tdAccessToken.getJsapi_ticket();
			}
		}
	}		
}

/**
 * @author lc
 * @注释：通过 access_toked 获取微信 jsapi_ticket
 */
public String getTicket(String accessToken){

	String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";
	String jsapi_ticket = null;
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

       // accessToken = demoJson.getString("access_token");  
        jsapi_ticket = demoJson.getString("ticket");  

        System.out.println("jsapi_ticket===="+jsapi_ticket);    

       // System.out.println("====================获取token结束==============================");  

        is.close();  

    } catch (Exception e) {  

        e.printStackTrace(); 
    } 

	return jsapi_ticket;
}

private static String byteToHex(final byte[] hash) {
    Formatter formatter = new Formatter();
    for (byte b : hash)
    {
        formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
}

public String getAccess_token(){
	String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="  
			  
               + Configure.getAppid()+ "&secret=" + Configure.getSecret();
	String accessToken = null;
	String expiresIn = null;
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
        System.out.println("--------------------DEMO-----------"+demoJson);
        accessToken = demoJson.getString("access_token");  
        expiresIn = demoJson.getString("expires_in");  

        System.out.println("accessToken===="+accessToken);  
//        System.out.println("expiresIn==="+expiresIn);  

       // System.out.println("====================获取token结束==============================");  

        is.close();  

    } catch (Exception e) {  

        e.printStackTrace(); 
    }  
	return accessToken;
}

/**
 * @author lc
 * @注释： 判断access_token是否过期并返回最新access_token
 */
public String checkAccessToken(){
	//List<TdAccessToken> tdAccessTokenlist = tdAccessTokenService.findAll();
	TdAccessToken tdAccessToken = tdAccessTokenService.findTopBy();
	if (null == tdAccessToken) {
		tdAccessToken = new TdAccessToken();
		String accessToken = getAccess_token();
		while (null == accessToken) {
			accessToken = getAccess_token();
		}
		tdAccessToken.setAccess_token(accessToken);
		tdAccessToken.setAccess_expires_in("7000");
		tdAccessToken.setAccess_updateTime(new Date());
		tdAccessTokenService.save(tdAccessToken);
		
		return accessToken;
	}else {	
		Date now = new Date();// new Date()为获取当前系统时间
		if ((tdAccessToken.getAccess_updateTime().getTime() + Long.parseLong(tdAccessToken.getAccess_expires_in()+"000")) < now.getTime() ) {
			String accessToken = getAccess_token();
			tdAccessToken.setAccess_token(accessToken);
			tdAccessToken.setAccess_updateTime(new Date());
			tdAccessTokenService.save(tdAccessToken);
			return accessToken;
		}else {
			return tdAccessToken.getAccess_token();
		}
	}
}

}