package com.ynyes.xunwudao.controller.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;
import com.ynyes.xunwudao.entity.TdAccessToken;
import com.ynyes.xunwudao.entity.TdOrder;
import com.ynyes.xunwudao.entity.TdOrderGoods;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdAccessTokenService;
import com.ynyes.xunwudao.service.TdOrderGoodsService;
import com.ynyes.xunwudao.service.TdOrderService;
import com.ynyes.xunwudao.service.TdSettingService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.HttpRequest;

import net.sf.json.JSONObject;

@Controller
@RequestMapping
public class TdWeixinController {
	@Autowired
	private TdUserService tdUserService;
	
	@Autowired
	private TdOrderService tdOrderService;
	
	@Autowired
	private TdAccessTokenService tdAccessTokenService;
	
	@Autowired
	private TdOrderGoodsService tdOrderGoodsService;
	
	@Autowired
	private TdSettingService tdSettingService;
	
	
	//网页授权的access_token
	public Map<String, String> getWebAccessToken(String code){
		
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
	
	public Map<String, Object> getWeixinInfo(String access_token, String openid){
		Map<String, Object> userInfo = new HashMap<String, Object>();
		
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		String nickname = null;
		Long sex = 1L;
		String city = null;
		String province = null;
		String headimgurl = null;
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
            System.out.println("0318-------MESSAGE:"+message);
            JSONObject demoJson = JSONObject.fromObject(message);  
            
            nickname = demoJson.getString("nickname");  
            sex = demoJson.getLong("sex");
            city = demoJson.getString("city");
            province = demoJson.getString("province");
            headimgurl = demoJson.getString("headimgurl");
            unionid = demoJson.getString("unionid");
            
            userInfo.put("nickname", nickname);
            if(null != sex && sex == 1){
            	userInfo.put("sex", true);
            }else{
            	userInfo.put("sex", false);
            }
            
            userInfo.put("city", city);
            userInfo.put("province", province);
            userInfo.put("headimgurl", headimgurl);
            userInfo.put("unionid", unionid);

            is.close();  

        } catch (Exception e) {  
            e.printStackTrace(); 
        }  
		return userInfo;
	}
	
	
	/*--------------------------------------------------------
	 * -------------------    支付///////        -------------------
	 -------------------------------------------------------*/
	
	//微信 获取openid
	@RequestMapping(value = "/weixin/pay/getOpen")
	public String weixinPayGetOpen(String orderNumber, HttpServletRequest req, ModelMap map) throws UnsupportedEncodingException{
			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Configure.getAppid()+ 
					 "&redirect_uri=http://www.xwd33.com/weixin/pay?orderNumber="+orderNumber+"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	}
	
	 /**
     * 支付
     * 
     * @param orderId
     * @param map
     * @param req
     * @return
	 * @throws ParseException 
     */
    @RequestMapping(value = "/weixin/pay")
    public String weixinPay(String orderNumber, ModelMap map,HttpServletRequest req,String state,String code) throws ParseException
    {
        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
        	map.addAttribute("msg", "请先登陆！");
        	map.addAttribute("url", "/login");
        	return "/client/error_404";
        }
        if(null == orderNumber || orderNumber.equals("")){
        	map.addAttribute("msg", "订单不存在！");
        	map.addAttribute("url", "/user/order/list/2");
        	return "/client/error_404";
        }
        
        TdOrder order = tdOrderService.findByOrderNumber(orderNumber);
        if(null == order){
        	map.addAttribute("msg", "订单不存在！");
        	map.addAttribute("url", "/user/order/list/2");
        	return "/client/error_404";
        }
        TdUser user = tdUserService.findByUsername(username);

        String openId = "";

        if (user.getOpenid() == null)
        {
            if (code != null)
        	{
        		System.out.println("Madejing: code = " + code);
    			String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Configure.getAppid() +"&secret=" + Configure.getSecret() +"&code=" + code + "&grant_type=authorization_code";
    			System.out.println("Madejing: accessTokenUrl = " + accessTokenUrl);
    			String result = com.ynyes.xunwudao.util.HttpRequest.sendGet(accessTokenUrl, null);
    			System.out.println("madjeing: result =" + result);
    			
    		    openId = JSONObject.fromObject(result).getString("openid");
    		    TdUser user2 = tdUserService.findByOpenid(openId);
    		    if (user2 == null && user.getOpenid() == null)
    		    {
    		    	user.setOpenid(JSONObject.fromObject(result).getString("openid"));
				}
    		    
    		    System.out.println("Madejing: openid = " + openId);
    		}
		}else{
			System.out.println("---------OPENID IS NULL");
		}
 

        // 根据订单类型来判断支付时间是否过期
        // 普通 订单提交后30分钟内
        Date cur = new Date();
        long temp = cur.getTime() - order.getOrderTime().getTime();
        if (temp > 1000 * 3600 / 2)
        {
        	order.setStatusId(7L);
        	tdOrderService.save(order);
        	map.addAttribute("msg", "订单已过期！");
        	map.addAttribute("url", "/user/order/list");
        	return "/client/error_404";
        }

        // 待付款
        if (!order.getStatusId().equals(2L))
        {
            return "/client/error_404";
        }

        String amount = order.getTotalPrice().toString();
        req.setAttribute("totalPrice", amount);

        String payForm = "";
        map.addAttribute("order", order);
        map.addAttribute("order_number", order.getOrderNumber());
		map.addAttribute("total_price", order.getTotalPrice());

		Calendar calExpire = Calendar.getInstance();
		calExpire.setTime(order.getOrderTime());


		//统一支付接口
		String noncestr = RandomStringGenerator.getRandomStringByLength(32);
		ModelMap signMap = new ModelMap();
		signMap.addAttribute("appid", Configure.getAppid());
		signMap.addAttribute("attach", "订单支付");
		signMap.addAttribute("body", "支付订单" + order.getOrderNumber());
		signMap.addAttribute("mch_id", Configure.getMchid());
		signMap.addAttribute("nonce_str",noncestr);
		signMap.addAttribute("out_trade_no", order.getOrderNumber());
		signMap.addAttribute("total_fee", Math.round(order.getTotalPrice() * 100));
		signMap.addAttribute("spbill_create_ip", "116.55.233.157");
		signMap.addAttribute("notify_url", "http://www.xwd33.com/wx_notify");
		signMap.addAttribute("trade_type", "JSAPI");
		signMap.addAttribute("openid", user.getOpenid());
		String mysign = Signature.getSign(signMap);

		String content = "<xml>\n"
				+ "<appid>" + Configure.getAppid() + "</appid>\n"
				+ "<attach>订单支付</attach>\n"
				+ "<body>"+"支付订单"+ order.getOrderNumber() + "</body>\n"
				+ "<mch_id>" + Configure.getMchid() + "</mch_id>\n"
				+ "<nonce_str>" + noncestr + "</nonce_str>\n"
				+ "<notify_url>http://www.xwd33.com/wx_notify</notify_url>\n"
				+ "<out_trade_no>" + order.getOrderNumber()+ "</out_trade_no>\n"
				+ "<spbill_create_ip>116.55.233.157</spbill_create_ip>\n"
				+ "<total_fee>" + Math.round(order.getTotalPrice() * 100) + "</total_fee>\n" 
				+ "<trade_type>JSAPI</trade_type>\n"
				+ "<sign>" + mysign + "</sign>\n"
				+ "<openid>" + user.getOpenid() + "</openid>\n" + "</xml>\n";
		System.out.print("MDJ: xml=" + content + "\n");

		String return_code = null;
		String prepay_id = null;
		String result_code = null;
		String line = null;
		HttpsURLConnection urlCon = null;
		try
		{
			urlCon = (HttpsURLConnection) (new URL("https://api.mch.weixin.qq.com/pay/unifiedorder")).openConnection();
			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Length",String.valueOf(content.getBytes().length));
			urlCon.setUseCaches(false);
			// 设置为gbk可以解决服务器接收时读取的数据中文乱码问题
			urlCon.getOutputStream().write(content.getBytes("utf-8"));
			urlCon.getOutputStream().flush();
			urlCon.getOutputStream().close();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

			while ((line = in.readLine()) != null)
			{
				System.out.println(": rline: " + line);
				if (line.contains("<return_code>"))
				{
					return_code = line.replaceAll(
							"<xml><return_code><\\!\\[CDATA\\[", "")
							.replaceAll("\\]\\]></return_code>", "");
				} 
				else if (line.contains("<prepay_id>")) 
				{
					prepay_id = line.replaceAll("<prepay_id><\\!\\[CDATA\\[",
							"").replaceAll("\\]\\]></prepay_id>", "");
				}
				else if (line.contains("<result_code>"))
				{
					result_code = line.replaceAll(
							"<result_code><\\!\\[CDATA\\[", "").replaceAll(
									"\\]\\]></result_code>", "");
				}
			}

			System.out.println("MDJ: return_code: " + return_code + "\n");
			System.out.println("MDJ: prepay_id: " + prepay_id + "\n");
			System.out.println("MDJ: result_code: " + result_code + "\n");

			if ("SUCCESS".equalsIgnoreCase(return_code)
					&& "SUCCESS".equalsIgnoreCase(result_code)
					&& null != prepay_id)
			{
				noncestr = RandomStringGenerator.getRandomStringByLength(32);

				String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
				String packageString = "prepay_id=" + prepay_id;
				String signType = "MD5";
				ModelMap returnsignmap = new ModelMap();
				returnsignmap.addAttribute("appId", Configure.getAppid());
				returnsignmap.addAttribute("timeStamp", timeStamp);
				returnsignmap.addAttribute("nonceStr", noncestr);
				returnsignmap.addAttribute("package", packageString);
				returnsignmap.addAttribute("signType", signType);

				
				String returnsign = Signature.getSign(returnsignmap);
				content = "<xml>\n" + 
				"<appid>" + Configure.getAppid() + "</appid>\n" + 
				"<timeStamp>" + timeStamp + "</timeStamp>\n" +
				"<nonceStr>" + noncestr + "</nonceStr>\n" + 
				"<package>" + packageString + "</package>\n" + 
				"<signType>" + signType + "</signType>\n" + 
				"<signType>" + returnsign + "</signType>\n" + 
				"</xml>\n";

				System.out.print(": returnPayData xml=" + content);
				map.addAttribute("appId", Configure.getAppid());
				map.addAttribute("timeStamp", timeStamp);
				map.addAttribute("nonceStr", noncestr);
				map.addAttribute("package", packageString);
				map.addAttribute("signType", signType);
				map.addAttribute("paySign", returnsign);
				map.addAttribute("orderId", order.getId());
				
				map.addAttribute("order", order);
				
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

        map.addAttribute("payForm", payForm);

        return "/client/user_order_pay";
    }
	
	
	
    @RequestMapping(value = "/wx_notify")
    public void wx_notify(HttpServletResponse response,HttpServletRequest request) throws IOException
    {
    	System.out.println("MDJ: 回调方法触发！\n");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String line = null;
		String return_code = null;
		String result_code = null;
		String noncestr = null;
		String out_trade_no = null;

		try {
			while ((line = br.readLine()) != null) {
				System.out.print("MDJ: notify" + line + "\n");

				if (line.contains("<return_code>")) {
					return_code = line.replaceAll("<return_code><\\!\\[CDATA\\[", "") .replaceAll("\\]\\]></return_code>", "");
				} else if (line.contains("<out_trade_no>")) {
					out_trade_no = line.replaceAll("<out_trade_no><\\!\\[CDATA\\[", "").replaceAll("\\]\\]></out_trade_no>", "");
				} else if (line.contains("<result_code>")) {
					result_code = line.replaceAll("<result_code><\\!\\[CDATA\\[", "").replaceAll("\\]\\]></result_code>", "");
				}
			}

			System.out.println("MDJ: notify return_code: " + return_code + "\n");
			System.out.println("MDJ: notify out_trade_no: " + out_trade_no + "\n");
			System.out.println("MDJ: notify result_code: " + result_code + "\n");

			if (return_code.contains("SUCCESS") && 
					result_code.contains("SUCCESS") && 
					null != out_trade_no)
			{
				TdOrder tdOrder = tdOrderService.findByOrderNumber(out_trade_no);

				if (null != tdOrder)
				{
					if(null != tdOrder.getStatusId() && tdOrder.getStatusId()==2){
						System.out.println("------------0321、tdOrder statusId！"+tdOrder.getStatusId());
						tdOrder.setStatusId(4L);
						tdOrder.setPayTime(new Date());
						tdOrder.setPayTypeTitle("微信支付");
						if(null != tdOrder.getOrderGoodsList()){
							System.out.println("------------0321、orderGoodsList 不为空！！");
							for(TdOrderGoods item : tdOrder.getOrderGoodsList()){
								System.out.println("------------0321、开始循环orderGoodsList！！");
								item.setTime(new Date());
								item.setUsername(tdOrder.getUsername());
								tdOrderGoodsService.save(item);
							}
						}
						System.out.println("------------0321、保存tdOrder！！");
						tdOrderService.save(tdOrder);
						System.out.println("------------0321、保存完毕！！");
						//分销处理
						System.out.println("------------0321、开始分销处理！");
						System.out.println("------------0321、总价！"+tdOrder.getTotalGoodsPrice());
						System.out.println("------------0321、用户id！"+tdOrder.getUserId());
						if(null != tdOrder.getTotalGoodsPrice() && tdOrder.getTotalGoodsPrice() > 0 && null != tdOrder.getUserId()){
							System.out.println("------------0321、检测用户！！");
							TdUser user = tdUserService.findOne(tdOrder.getUserId());
							System.out.println("user:"+user.getUsername());
							if(null != user){
								System.out.println("------------0321、用户存在！");
								//消费总额
								Double spend = 0.00;
								System.out.println("------------0321、user。getspend："+user.getSpend());
								if(null != user.getSpend()){
									spend=user.getSpend();
								}
								user.setSpend(spend+tdOrder.getTotalPrice());
								tdUserService.save(user);
								
								Long pOne = (long)(tdOrder.getTotalPrice()*100* tdSettingService.findTopBy().getRegisterSuccessPoints()/100); //第一层应得积分 
								Long pTwo = (long)(tdOrder.getTotalGoodsPrice()*100* tdSettingService.findTopBy().getRegisterSharePoints()/100); //第二层应得积分 
								System.out.println("pOne:"+pOne);
								System.out.println("pTwo:"+pTwo);
								//上一级推荐人
								TdUser userOne = tdUserService.findOne(user.getUpUserOne());
								if(null != userOne){
									userOne.setTotalPoints(userOne.getTotalPoints()+pOne);
									tdUserService.save(userOne);
								}
								//二级推荐人
								TdUser userTwo = tdUserService.findOne(user.getUpUserTwo());
								if(null != userTwo){
									userTwo.setTotalPoints(userTwo.getTotalPoints()+pTwo);
									tdUserService.save(userTwo);
								}
							}
						}
					}
				}
				
				String content = "<xml>\n"
						+ "<result_code>SUCCESS</result_code>\n"
						+ "<return_code></return_code>\n"
						+ "</xml>\n";

				System.out.print("MDJ: return xml=" + content + "\n");

				try {
					// 把xml字符串写入响应
					byte[] xmlData = content.getBytes();

					response.setContentType("text/xml");
					response.setContentLength(xmlData.length);

					ServletOutputStream os = response.getOutputStream();
					os.write(xmlData);

					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
//				HttpGet httpGet = new HttpGet("http://kq.hz023.com/DoubleEgg/ApiActivity/BuyReturn?telephone=" + request.getSession().getAttribute("username") + "&status=1&commondityId=" + request.getSession().getAttribute("huodongGoodsId"));
//				if (request.getSession().getAttribute("dianxinhuodong") != null) 
//				{
//					String accessTokenUrl = "http://kq.hz023.com/GxDoubleEgg/ApiActivity/BuyReturn?telephone=" + request.getSession().getAttribute("username") + "&status=1&commondityId=" + request.getSession().getAttribute("huodongGoodsId");
//					System.out.println("Madejing: accessTokenUrl = " + accessTokenUrl);
//					String result = com.ynyes.csb.util.HttpRequest.sendGet(accessTokenUrl, null);
//					System.out.println("Madejing:->dianxinfanhui:" +result);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
    }
	/*--------------------------------------------------------
	 * -------------------    //////支付  -------------------------
	 -------------------------------------------------------*/
    @RequestMapping(value="/getQr")
    public void getQr() throws UnsupportedEncodingException{
    	String ticket = getUrlTicket();
    	String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+URLEncoder.encode(ticket,"utf-8");
    	
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

        } catch (Exception e) {  
            e.printStackTrace(); 
        }  
    }
    
    /*
     * 微信分销，生成二维码
     * 获取ticket
     */
    	public  String getUrlTicket() {
    		Map<String , String> map = new HashMap<String , String>();
    		map.put("expire_seconds", "604800");
    		map.put("action_name","QR_SCENE");
    		map.put("action_info","分享二维码");
    		String json = JSONObject.fromObject(map).toString();
            System.out.println(json);
            
            String ACCESS_TOKEN = getAccess_token();
            map = HttpRequest.connectWeiXinInterface("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+ACCESS_TOKEN, json);
    		String ticket = map.get("ticket");
            return ticket;
    		
    	}
/*-------------------------------------微信js接口-----------------------*/
    
    /**
	 * 获取AccessToken
	 * @return
	 */
	public String getAccessToken()
	{
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Configure.getAppid()+ "&secret=" + Configure.getSecret();
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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}  
		return accessToken;
	}
    
    /**
	 * 返回最新AccessToken
	 * @return
	 */
	public String checkAccessToken()
	{
		//List<TdAccessToken> tdAccessTokenlist = tdAccessTokenService.findAll();
		TdAccessToken tdAccessToken = tdAccessTokenService.findTopBy();
		if (null == tdAccessToken) 
		{
			tdAccessToken = new TdAccessToken();
			String accessToken = getAccessToken();
			while (null == accessToken) 
			{
				accessToken = getAccessToken();
			}
			tdAccessToken.setAccess_token(accessToken);
			tdAccessToken.setAccess_expires_in("7000");
			tdAccessToken.setAccess_updateTime(new Date());
			tdAccessTokenService.save(tdAccessToken);
			
			return accessToken;
		}
		else
		{	
			Date now = new Date();// new Date()为获取当前系统时间
			if ((tdAccessToken.getAccess_updateTime().getTime() + Long.parseLong(tdAccessToken.getAccess_expires_in()+"000")) < now.getTime() )
			{
				String accessToken = getAccessToken();
				tdAccessToken.setAccess_token(accessToken);
				tdAccessToken.setAccess_updateTime(new Date());
				tdAccessTokenService.save(tdAccessToken);
				return accessToken;
			}
			else
			{
				return tdAccessToken.getAccess_token();
			}
		}
	}
    /**
     * 微信js接口获取ticket
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
				if(null == jsapi_ticket)
				{
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
				}else
				{
					return tdAccessToken.getJsapi_ticket();
				}
			}
		}		
	}
    
public String getAccess_token(){
		
		System.out.println("Madejing:进入access_token()");
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Configure.getAppid()+ "&secret=" + Configure.getSecret();
		String accessToken = null;
		String expiresIn = null;
		try {  
			  
            URL urlGet = new URL(url);

            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();

            http.setRequestMethod("GET"); // 必须是get方式请求 

            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            http.setDoOutput(true);

            http.setDoInput(true);

            http.connect();

            InputStream is = http.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            String message = new String(jsonBytes, "UTF-8");
            System.out.println("Madejing:message:" + message);
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

/**
 * @注释：通过 access_token 获取微信 jsapi_ticket
 */
public String getTicket(String accessToken){

	String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";
	String jsapi_ticket = null;
	try {  
		  
        URL urlGet = new URL(url);

        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  

        http.setRequestMethod("GET"); // 必须是get方式请求  

        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        http.setDoOutput(true);  

        http.setDoInput(true);  

        http.connect();  

        InputStream is = http.getInputStream();  

        int size = is.available();  

        byte[] jsonBytes = new byte[size];  

        is.read(jsonBytes);  

        String message = new String(jsonBytes, "UTF-8");  
        
        System.out.println("Madejing:Ticket:message:" + message);

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
    /*------------------------------------微信js接口-------------------------------------*/


/*111111111-TESTING!!!!!!!!!!!!!!!!*/
@RequestMapping(value = "/notify/test")
public void wx_notifyTEST(String orderNumber,HttpServletResponse response,HttpServletRequest request) throws IOException
{
	System.out.println("MDJ: 回调方法触发！\n");
	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	
	String line = null;
	String return_code = null;
	String result_code = null;
	String noncestr = null;
	String out_trade_no = null;

	try {
		orderNumber = "20160321174752252XWD516";
			TdOrder tdOrder = tdOrderService.findByOrderNumber(orderNumber);

			if (null != tdOrder)
			{
				if(null != tdOrder.getStatusId() && tdOrder.getStatusId()==2){
					System.out.println("------------0321、tdOrder statusId！"+tdOrder.getStatusId());
					tdOrder.setStatusId(4L);
					tdOrder.setPayTime(new Date());
					if(null != tdOrder.getOrderGoodsList()){
						System.out.println("------------0321、orderGoodsList 不为空！！");
						for(TdOrderGoods item : tdOrder.getOrderGoodsList()){
							System.out.println("------------0321、循环goods！！");
							item.setTime(new Date());
							item.setUsername(tdOrder.getUsername());
							tdOrderGoodsService.save(item);
						}
					}
					tdOrderService.save(tdOrder);
					
					//分销处理
					System.out.println("------------0321、开始分销处理！");
					System.out.println("------------0321、总价！"+tdOrder.getTotalGoodsPrice());
					System.out.println("------------0321、用户id！"+tdOrder.getUserId());
					if(null != tdOrder.getTotalGoodsPrice() && tdOrder.getTotalGoodsPrice() > 0 && null != tdOrder.getUserId()){
						System.out.println("------------0321、检测用户！！");
						TdUser user = tdUserService.findOne(tdOrder.getUserId());
						System.out.println("user:"+user.getUsername());
						if(null != user){
							System.out.println("------------0321、用户存在！");
							//消费总额
							Double spend = 0.00;
							System.out.println("------------0321、user。getspend！"+user.getSpend());
							if(null != user.getSpend()){
								spend=user.getSpend();
							}
							user.setSpend(spend+tdOrder.getTotalPrice());
							tdUserService.save(user);
							
							Long pOne = (long)(tdOrder.getTotalPrice()*100* tdSettingService.findTopBy().getRegisterSuccessPoints()); //第一层应得积分 
							Long pTwo = (long)(tdOrder.getTotalGoodsPrice()*100* tdSettingService.findTopBy().getRegisterSharePoints()); //第二层应得积分 
							System.out.println("pOne:"+pOne);
							System.out.println("pTwo:"+pTwo);
							//上一级推荐人
							TdUser userOne = tdUserService.findOne(user.getUpUserOne());
							if(null != userOne){
								if(null != userOne.getTotalPoints()){
									userOne.setTotalPoints(userOne.getTotalPoints()+pOne);
								}else{
									userOne.setTotalPoints(pOne);
								}
								
								tdUserService.save(userOne);
							}
							//二级推荐人
							TdUser userTwo = tdUserService.findOne(user.getUpUserTwo());
							if(null != userTwo){
								if(null != userTwo.getTotalPoints()){
									userTwo.setTotalPoints(userTwo.getTotalPoints()+pTwo);
								}else{
									userTwo.setTotalPoints(pTwo);
								}
								
								tdUserService.save(userTwo);
							}
						}
					}
				}
				else{
					System.out.println("订单不存在！！！");
				}
				
			
			
			String content = "<xml>\n"
					+ "<result_code>SUCCESS</result_code>\n"
					+ "<return_code></return_code>\n"
					+ "</xml>\n";

			System.out.print("MDJ: return xml=" + content + "\n");

			try {
				// 把xml字符串写入响应
				byte[] xmlData = content.getBytes();

				response.setContentType("text/xml");
				response.setContentLength(xmlData.length);

				ServletOutputStream os = response.getOutputStream();
				os.write(xmlData);

				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			HttpGet httpGet = new HttpGet("http://kq.hz023.com/DoubleEgg/ApiActivity/BuyReturn?telephone=" + request.getSession().getAttribute("username") + "&status=1&commondityId=" + request.getSession().getAttribute("huodongGoodsId"));
//			if (request.getSession().getAttribute("dianxinhuodong") != null) 
//			{
//				String accessTokenUrl = "http://kq.hz023.com/GxDoubleEgg/ApiActivity/BuyReturn?telephone=" + request.getSession().getAttribute("username") + "&status=1&commondityId=" + request.getSession().getAttribute("huodongGoodsId");
//				System.out.println("Madejing: accessTokenUrl = " + accessTokenUrl);
//				String result = com.ynyes.csb.util.HttpRequest.sendGet(accessTokenUrl, null);
//				System.out.println("Madejing:->dianxinfanhui:" +result);
//			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		br.close();
	}
}
}