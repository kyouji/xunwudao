package com.ynyes.xunwudao.controller.front;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tencent.common.Configure;

import net.sf.json.JSONObject;

@Controller
@RequestMapping
public class TdQqController {
	// Step2：通过Authorization Code获取Access Token
	public Map<String, String> getQqAccessToken(String code, String state) throws UnsupportedEncodingException{
		Map<String, String> res = new HashMap<String, String>();
		if(null == state || !state.equals("STATE")){
			return res;
		}
		
		String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="
									+Configure.getClientId()+"&client_secret="+Configure.getAppKey()
									+"&code="+code
									+"&redirect_uri="+URLEncoder.encode("http://www.xwd33.com/qq/return","utf-8");
		
		String access_token = null;
		String expires_in = null;
		String refresh_token = null;

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
//            int size = is.available();  
//            byte[] jsonBytes = new byte[size];  
//            is.read(jsonBytes);  
//            String message = new String(jsonBytes, "UTF-8");  
//            JSONObject demoJson = JSONObject.fromObject(message);  
//            access_token = demoJson.getString("access_token");  
//            expires_in = demoJson.getString("expires_in");  
//            refresh_token = demoJson.getString("refresh_token");
            //改用BufferReader试试
            StringBuffer sb = new StringBuffer();
            InputStreamReader isreader = null;
            try {
                isreader = new InputStreamReader(is, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(isreader);
            String temp = null;
  
                while ((temp = bufferedReader.readLine()) != null) {
                    sb.append(temp);
                }
                bufferedReader.close();
                isreader.close();
                is.close();
                is = null;

            String token =  sb.toString();
            System.out.println("0314---------GET TOKEN :" + token);
            String[] strs = token.split("=");
    	    res.put("access_token", strs[1].substring(0, strs[1].indexOf("&")));
    	    res.put("expires_in", strs[2].substring(0, strs[2].indexOf("&")));
    	    res.put("refresh_token", strs[3]);
        } catch (Exception e) {  
            e.printStackTrace(); 
        }  
		return res;
	}
	
	/*-----------------------测试---------------------------*/
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public Map<String, String> test(){
		Map<String, String>res = new HashMap<String , String>();
		String token =  "access_token=B18F56F135CFA61B7FFAE04C9AA65EC4&expires_in=7776000&refresh_token=CFFB583B1AB0E40935E849DCFD82FBD8";
	    System.out.println("-------------TOKEN:   "+token);
	    String[] strs = token.split("=");
	    res.put("access_token", strs[1].substring(0, strs[1].indexOf("&")));
	    res.put("expires_in", strs[2].substring(0, strs[2].indexOf("&")));
	    res.put("refresh_token", strs[3]);
	    System.out.println("----------access_token:"+strs[1].substring(0, strs[1].indexOf("&")));
	    System.out.println("--------expires_in:"+strs[2].substring(0, strs[2].indexOf("&")));
	    System.out.println("-----------refresh_token:"+strs[3]);
	    return res;
	}
    /*-----------------------测试---------------------------*/
	
	
	//获取用户的openid
	public String getQqOpenid(String access_token) throws UnsupportedEncodingException{
		String client_id = null;
		String qqOpenid = null;
		String url = "https://graph.qq.com/oauth2.0/me?access_token="+access_token;

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
            String json = message.substring(message.indexOf("(")+1, message.indexOf(")"));
            System.out.println("获取openid-----------JSON："+json);
            JSONObject demoJson = JSONObject.fromObject(json);  
            qqOpenid = demoJson.getString("openid");  
            is.close();  

        } catch (Exception e) {  
            e.printStackTrace(); 
        }  
		return qqOpenid;
	}
	
	//获取用户信息
	public Map<String, String> getQqUserInfo(String access_token, String oauth_consumer_key, String openid) throws UnsupportedEncodingException{
		Map<String, String> userInfo = new HashMap<String, String>();
		
		
		String url = "https://graph.qq.com/user/get_user_info?access_token="+access_token+"&oauth_consumer_key="+oauth_consumer_key+"&openid="+openid;
		String msg = null;
		String nickname = null;
		String figureurl_qq_1 = null;
		String gender = null;

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
            System.out.println("0314-------MESSAGE:"+message);
            JSONObject demoJson = JSONObject.fromObject(message);  
            
            msg = demoJson.getString("msg");  
            nickname = demoJson.getString("nickname");  
            figureurl_qq_1 = demoJson.getString("figureurl_qq_1");
            gender = demoJson.getString("gender");
            
            userInfo.put("msg", msg);
            userInfo.put("nickname", nickname);
            userInfo.put("figureurl_qq_1", figureurl_qq_1);
            userInfo.put("gender", gender);
            is.close();  

        } catch (Exception e) {  
            e.printStackTrace(); 
        }  
		return userInfo;
	}
	
}