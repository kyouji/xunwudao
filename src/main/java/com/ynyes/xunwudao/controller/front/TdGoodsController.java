package com.ynyes.xunwudao.controller.front;




import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tencent.common.Configure;
import com.tencent.common.MD5;
import com.tencent.common.Signature;
import com.ynyes.xunwudao.entity.TdAccessToken;
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdOrderGoods;
import com.ynyes.xunwudao.entity.TdProductCategory;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.entity.TdUserCollect;
import com.ynyes.xunwudao.service.TdAccessTokenService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdOrderGoodsService;
import com.ynyes.xunwudao.service.TdProductCategoryService;
import com.ynyes.xunwudao.service.TdUserCollectService;
import com.ynyes.xunwudao.service.TdUserService;

import net.sf.json.JSONObject;


/**
 * 首页（我的）【需登陆】
 * 角色数：2
 * 会计【唯一界面】、客户。
 *@author Zhangji
 */
@Controller
@RequestMapping("/goods")
public class TdGoodsController {

    
    @Autowired
    private TdCommonService tdCommonService;
    
    @Autowired
    private TdUserService tdUserService;

    @Autowired
    private TdProductCategoryService tdProductCategoryService;
    
    @Autowired
    private TdGoodsService tdGoodsService;
    
    @Autowired
    private TdUserCollectService tdUserCollectService;
    
    @Autowired
    private TdOrderGoodsService tdOrderGoodsService;
    
    @Autowired
    private TdAccessTokenService tdAccessTokenService;
    
    //商城首页
    @RequestMapping("/index")
    public String index(HttpServletRequest req, ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
    	
    	List<TdProductCategory> category = tdProductCategoryService.findAll();
    	if (category.size() > 0)
    	{
    		map.addAttribute("category", category.get(0));
    	}
    	
    	map.addAttribute("category_list", category);
    	map.addAttribute("showIcon", 2);
    	
        return "/client/goods_index";
    }
    
    //列表页
    @RequestMapping("/list")
    public String goodsList(Long catId, HttpServletRequest req,  ModelMap map) {        
    	tdCommonService.setHeader(map, req); 
    	
    	if(null != catId)
    	{
    		TdProductCategory category = tdProductCategoryService.findOne(catId);
    		List<TdGoods> goodsList = tdGoodsService.findByCategoryIdAndIsOnSaleTrue(catId);
    		
        	map.addAttribute("goods_list", goodsList);
    		map.addAttribute("category", category);
        	map.addAttribute("showIcon", 2);
        	map.addAttribute("catId", catId);
    	}
    	
        return "/client/goods_list";
    }
    
    //列表页
    @RequestMapping("/detail")
    public String goodsDetail( Long id, HttpServletRequest req,  ModelMap map) throws NoSuchAlgorithmException, UnsupportedEncodingException {        
    	tdCommonService.setHeader(map, req); 
    	String username = (String) req.getSession().getAttribute("username");
    	if(null != username)
    	{
    		map.addAttribute("user", tdUserService.findByUsername(username));
    		
        	//推荐码
    		TdUser tdUser = tdUserService.findByUsername(username);
        	String userNumber = tdUser.getNumber();
        	Random random = new Random();
    		String randomNumber = random.nextInt(900) + 100 + "";
    		
    		String rfCode = userNumber + randomNumber.toString() + id.toString();
    		map.addAttribute("rfCode", rfCode);
    		
    	}
    	if(null != id)
    	{
    		TdGoods tdGoods = tdGoodsService.findOne(id);
    		if(null != tdGoods)
    		{
    			map.addAttribute("tdGoods", tdGoods);
    			
    			if(null != username){
    				TdUserCollect collect = tdUserCollectService.findByUsernameAndGoodsId(username, tdGoods.getId());
    				if(null != collect){
    					map.addAttribute("collected", 1);
    				}
    				else{
    					map.addAttribute("collected", 0);
    				}
    			}
    			
    			
    		}
    		
    		//购买记录
    		List<TdOrderGoods> orderGoodsList = tdOrderGoodsService.findByGoodsIdOrderByIdDesc(id);
    		if(null != orderGoodsList && orderGoodsList.size() > 0){
    			map.addAttribute("order_goods_list", orderGoodsList);
    		}
    		
    		//微信自定义分享
      		//通过 access_token 获取微信 jsapi_ticket
			String jsapi_ticket = checkjsapiTicket();
			map.addAttribute("jsapi_ticket", jsapi_ticket);
			System.out.println("\n-----------jsapi_ticket:"+jsapi_ticket);
			// 获取是时间戳
			String timestamp =Long.toString(System.currentTimeMillis() / 1000);
			map.addAttribute("timestamp", timestamp);
			System.out.println("timestamp--------------"+timestamp);
			// 随机字符串
			String noncestr = UUID.randomUUID().toString();
			map.addAttribute("noncestr", noncestr);
			System.out.println("noncestr--------------"+noncestr);
			// 生成签名
			String tempStr = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr +"&timestamp=" 
							+ timestamp + "&url=http://www.xwd33.com/goods/detail?id="
							+ id ;
			
			System.out.println("\n-----------tempStr:"+tempStr);
			// sha1加密
			String signature = "";
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
	        crypt.update(tempStr.getBytes("UTF-8"));
	        signature = byteToHex(crypt.digest());
			

	        System.out.println("signature--------------"+signature);
		    map.addAttribute("signature", signature);
    		
        	map.addAttribute("showIcon", 2);
    	}
    	
        return "/client/goods_detail";
    }
    
    
    public static String getSign(Map<String,Object> map) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
//        result += "key=" + Configure.getKey();
        //Util.log("Sign Before MD5:" + result);
//        result = MD5.MD5Encode(result).toUpperCase();
        //Util.log("Sign Result:" + result);
        
		// sha1加密
		String signature = "";
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
        crypt.update(result.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());
        return result;
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
            System.out.println("\n get ticket返回值："+demoJson);
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
//            System.out.println("expiresIn==="+expiresIn);  

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
}
