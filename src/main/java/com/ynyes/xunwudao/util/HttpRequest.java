package com.ynyes.xunwudao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tencent.common.Configure;

import net.sf.json.JSONObject;

/**
 * Created by hyoga
 */
public class HttpRequest {
    //get请求
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader bufferedReader = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        //String s = HttpRequest.sendGet("https://www.victoriassecret.com/panties/4-for-29-styles/thong-panty-everyday-perfect", "cm_sp=&ProductID=260572&CatalogueType=OLS");
        String s = HttpRequest.sendPost("https://www.victoriassecret.com/panties/4-for-29-styles/thong-panty-everyday-perfect", "cm_sp=&ProductID=260572&CatalogueType=OLS");
        System.out.println(s);
    }

    //post请求
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out = new PrintWriter(connection.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
	//微信发送POST请求
   public static  Map<String,String> connectWeiXinInterface(String interfaceUrl,String json){
	        URL url;
	        Map<String, String> map = new HashMap<String, String>();
	        OutputStream os = null;
	        InputStream is = null;
	       try {
	           url = new URL(interfaceUrl);
	           HttpURLConnection http = (HttpURLConnection) url.openConnection();
	           http.setRequestMethod("POST");
	           http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	           http.setDoOutput(true);
	           http.setDoInput(true);
	           System.setProperty("sun.net.client.defaultConnectTimeout", "10000");// 连接超时30秒
	           System.setProperty("sun.net.client.defaultReadTimeout", "10000"); // 读取超时30秒
	           http.connect();
	           os = http.getOutputStream();
	           os.write(json.getBytes("UTF-8"));// 传入参数
	           is = http.getInputStream();
	           int size = is.available();
	           byte[] jsonBytes = new byte[size];
	           is.read(jsonBytes);
	           String result = new String(jsonBytes, "UTF-8");
	           
	           JSONObject demoJson = JSONObject.fromObject(result);
	           map.put("ticket", demoJson.getString("ticket"));
	           map.put("expire_seconds", demoJson.getString("expire_seconds"));
	           map.put("url", demoJson.getString("url"));
	           
//	           logger.info("sendTextMessageToUser result:"+result);
	           System.out.println("请求返回结果:"+result);
	           os.flush();
	       } catch (Exception e) {
	           e.printStackTrace();
	       } finally{
		      try {
			      os.close();
			      is.close();
			 } catch (IOException e) {
			 e.printStackTrace();
			}
	       }
	       
	       return map;
    	}
	   
}
