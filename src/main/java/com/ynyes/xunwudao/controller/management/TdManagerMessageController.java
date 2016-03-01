package com.ynyes.xunwudao.controller.management;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.params.ConnRoutePNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.qq.connect.utils.http.HttpClient;
import com.tencent.common.Configure;
import com.ynyes.xunwudao.entity.TdApplyType;
import com.ynyes.xunwudao.entity.TdArea;
import com.ynyes.xunwudao.entity.TdBillType;
import com.ynyes.xunwudao.entity.TdEnterType;
import com.ynyes.xunwudao.entity.TdSetting;
import com.ynyes.xunwudao.service.TdApplyTypeService;
import com.ynyes.xunwudao.service.TdAreaService;
import com.ynyes.xunwudao.service.TdBillTypeService;
import com.ynyes.xunwudao.service.TdEnterTypeService;
import com.ynyes.xunwudao.service.TdManagerLogService;
import com.ynyes.xunwudao.service.TdSettingService;
import com.ynyes.xunwudao.util.SiteMagConstant;
import com.ynyes.xunwudao.util.StringUtils;

/**
 * 微信消息推送
 * 
 * @author Zhangji
 */






@Controller
@RequestMapping(value="/Verwalter/setting")
public class TdManagerMessageController {
	   /**

     * 微信公共账号发送给账号

     * @param content 文本内容

     * @param toUser 微信用户  

     * @return

     */

    public  void sendTextMessageToUser(String content,String toUser){

       String json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"text\", \"text\": {\"content\": \""+content+"\"}}";
       
       

       JSONObject first = new JSONObject();
       first.put("value", "收费通知");
       
       JSONObject keyword1 = new JSONObject();
       keyword1.put("value", "111元");
       keyword1.put("color", "#173177");
       
       JSONArray data = new JSONArray();
       data.add(first);
       data.add(keyword1);
       
       JSONObject jsonstr = new JSONObject();
       jsonstr.put("touser", 1);
       jsonstr.put("template_id", "qNAyIle_566VQh5eRZjkBEaCqy0yH0232a9gQ4xzTOs");
       jsonstr.put("url", "http://csb1688.com/");
       jsonstr.put("data", data);
       
       
       
       //获取access_token

//       GetExistAccessToken getExistAccessToken = GetExistAccessToken.getInstance();

       String accessToken = getAccess_token();

       //获取请求路径

       String action = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken;

       System.out.println("json:"+json);

       try {

           connectWeiXinInterface(action,jsonstr.toString());

       } catch (Exception e) {

           e.printStackTrace();

       }

   }



    /**

     * 连接请求微信后台接口

     * @param action 接口url

     * @param json  请求接口传送的json字符串

     */

    public  void connectWeiXinInterface(String action,String json){

        URL url;

       try {

           url = new URL(action);

           HttpURLConnection http = (HttpURLConnection) url.openConnection();

           http.setRequestMethod("POST");

           http.setRequestProperty("Content-Type",

                   "application/x-www-form-urlencoded");

           http.setDoOutput(true);

           http.setDoInput(true);

           System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒

           System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

           http.connect();

           OutputStream os = http.getOutputStream();

           os.write(json.getBytes("UTF-8"));// 传入参数

           InputStream is = http.getInputStream();

           int size = is.available();

           byte[] jsonBytes = new byte[size];

           is.read(jsonBytes);

           String result = new String(jsonBytes, "UTF-8");

           System.out.println("请求返回结果:"+result);

           os.flush();

           os.close();

       } catch (Exception e) {

           e.printStackTrace();

       }

    }
    //获取Access_token
	public String getAccess_token(){
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="  
				  
                   + Configure.getAppid()+ "&secret=" + Configure.getKey();
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

            accessToken = demoJson.getString("access_token");  
            expiresIn = demoJson.getString("expires_in");  

            System.out.println("accessToken===="+accessToken);  
//            System.out.println("expiresIn==="+expiresIn);  
  
           // System.out.println("====================获取token结束==============================");  

            is.close();  

        } catch (Exception e) {  

            e.printStackTrace(); 
        }  
		return accessToken;
	}
}
