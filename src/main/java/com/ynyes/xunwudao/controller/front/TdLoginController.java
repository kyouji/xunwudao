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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.VerifServlet;


/**
 * 登录
 *
 */
@Controller
public class TdLoginController extends HttpServlet {
	@Autowired
	private TdUserService tdUserService;

	@Autowired
	private TdCommonService tdCommonService;
	
	@Autowired
	private TdGoodsService tdGoodsService;
	

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginReturn(Long goodsId, HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");

		String referer = req.getHeader("referer");

		// 基本信息
		tdCommonService.setHeader(map, req);

		if (null != goodsId){
			map.addAttribute("goodsId", goodsId);
		}
		if (null == username) {
			//QQ互联
			String appId = Configure.getAppId();
			map.addAttribute("appId", appId);
			//微信
			String appid = Configure.getAppid();
			map.addAttribute("appid", appid);
			
			appId = null;
			return "/client/login";
		}

		if (null == referer) {
			referer = "/";
		}

		return "redirect:" + referer;
	}

	@RequestMapping(value = "/login/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(String username, String password, 
			 HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();

		res.put("code", 1);
		
		if (password.length() > 16 || password.length() < 6) {
			res.put("msg", "密码长度为6到16位！");
			res.put("username", username);
			return res;
		}

		/**
		 * 按账号查找登录验证 密码验证 修改最后登录时间
		 * 
		 * @author libiao
		 */
		TdUser user = tdUserService.findByUsername(username);

		if (null != user) {
			if (!user.getPassword().equals(password)) {
				res.put("msg", "密码错误，请重新输入。");
				res.put("username", username);
				return res;
			}
			
			if(null != user.getStatusId() && user.getStatusId() != 1){
				res.put("msg", "该用户已被禁用");
				res.put("username", username);
				return res;
			}
			user.setLastLoginTime(new Date());
			user = tdUserService.save(user);

			res.put("code", 0);

			System.err.println(user);
			request.getSession().setMaxInactiveInterval(60 * 60 * 2);

			request.getSession().setAttribute("username", user.getUsername());
			return res;
		}
		/**
		 * 如果账号验证未通过，再进行手机登录验证 密码验证 修改最后登录时间
		 * 
		 * @author libiao
		 */
		user = tdUserService.findByMobileAndIsEnabled(username);
		if (null != user) {
			if (!user.getPassword().equals(password)) {
				res.put("msg", "密码错误，请重新输入。");
				return res;
			}
			user.setLastLoginTime(new Date());

			user = tdUserService.save(user);

			res.put("code", 0);

			request.getSession().setAttribute("username", user.getUsername());
			return res;
		} else { // 账号-手机都未通过验证，则用户不存在
			res.put("msg", "帐号不存在，请重新输入。");
			return res;
		}
	}
	
	//手机登陆
	@RequestMapping(value = "/login/mobile", method = RequestMethod.GET)
	public String loginMobile(Long goodsId, HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");

		String referer = req.getHeader("referer");

		// 基本信息
		tdCommonService.setHeader(map, req);

		if (null != goodsId){
			map.addAttribute("goodsId", goodsId);
		}
		if (null == username) {
			return "/client/login_mobile";
		}

		if (null == referer) {
			referer = "/";
		}

		return "redirect:" + referer;
	}
	
	//手机登陆发送验证码
	@RequestMapping(value = "/login/mobile/smscode", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> smsCode(String mobile, String code, HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<>();
		res.put("status", -1);
		
		String codeBack = (String) request.getSession().getAttribute("RANDOMVALIDATECODEKEY");
		if (!codeBack.equalsIgnoreCase(code)) {
			res.put("msg", "验证码错误");
			res.put("id", "txt_regCode");
			return res;
		}
		
		TdRegController reg = new TdRegController();
		if (!reg.isMobileNO(mobile))
		{
			res.put("msg","手机号不正确");
			return res;
		}
		TdUser user  = tdUserService.findByMobileAndIsEnabled(mobile);
		if(null == user){
			res.put("msg", "用户不存在");
		}
		
		Random random = new Random();
		String smscode = random.nextInt(9000) + 1000 + "";
		HttpSession session = request.getSession();
		session.setAttribute("SMSCODE", smscode);
		session.setAttribute("SMS_MOBILE", mobile);
		String info = "【循伍道助健康】验证码:" + smscode + "，健康交给循伍道，活到100不算老。此验证码三分钟内有效。";
		System.err.println(smscode);
		String content = null;
		try {
			content = URLEncoder.encode(info, "UTF-8");
			System.err.println(content);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("message", "验证码生成失败！");
			return res;
		}
		String url = "http://121.199.1.58:8888/sms.aspx?action=send&userid=622&account=xwdzjk&password=023xwdzjk&mobile=" + mobile
				+ "&content=" + content+"&sendTime=&taskName=&checkcontent=0&mobilenumber=1&countnumber=1&telephonenumber=1";
		
		StringBuffer fanhui = null;
		try {
			URL u = new URL(url);
			URLConnection connection = u.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			httpConn.setRequestProperty("Content-type", "text/html");
			httpConn.setRequestProperty("Accept-Charset", "utf-8");
			httpConn.setRequestProperty("contentType", "utf-8");
			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			StringBuffer resultBuffer = new StringBuffer();
			String tempLine = null;
			System.out.println(httpConn.getResponseCode());
			if (httpConn.getResponseCode() >= 300) {
				res.put("message", "HTTP Request is not success, Response code is " + httpConn.getResponseCode());
				return res;
			}

			try {
				inputStream = httpConn.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);

				while ((tempLine = reader.readLine()) != null) {
					resultBuffer.append(tempLine);
				}

				fanhui = resultBuffer;

			} finally {

				if (reader != null) {
					reader.close();
				}

				if (inputStreamReader != null) {
					inputStreamReader.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			res.put("message", "验证码生成失败！");
			return res;
		}
		res.put("status", 0);
		res.put("message", fanhui);
		return res;
	}
	
	@RequestMapping(value = "/login/mobile/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginMobile(String mobile, String smsCode, 
			 HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();

		res.put("code", 1);
		
	    String SMSCODE = (String) request.getSession().getAttribute("SMSCODE");
	    if(null != SMSCODE){
			if(!SMSCODE.equalsIgnoreCase(smsCode)){
				res.put("msg", "手机验证码错误！");
				res.put("id", "txt_regMcode");
				return res;
			}
	    }
	    String SMS_MOBILE =  (String) request.getSession().getAttribute("SMS_MOBILE");
	    if(null != SMS_MOBILE){
			if(!SMS_MOBILE.equalsIgnoreCase(mobile)){
				res.put("msg", "手机号码错误！");
				res.put("id", "txt_regMobile");
				return res;
			}
	    }

		/**
		 * 按账号查找登录验证 密码验证 修改最后登录时间
		 * 
		 * @author libiao
		 */
		TdUser user = tdUserService.findByMobile(mobile);

		if (null != user) {
			
			if(null != user.getStatusId() && user.getStatusId() != 1){
				res.put("msg", "该用户已被禁用");
				res.put("username", user.getUsername());
				return res;
			}
			user.setLastLoginTime(new Date());
			user = tdUserService.save(user);

			res.put("code", 0);

			System.err.println(user);
			request.getSession().setMaxInactiveInterval(60 * 60 * 2);

			request.getSession().setAttribute("username", user.getUsername());
			return res;
		}
		else { 
			res.put("msg", "帐号不存在，请重新输入。");
			return res;
		}
	}
	

	/**
	 * @author lc
	 * @return
	 * @注释：密码找回
	 */
	@RequestMapping(value = "/login/password_retrieve", method = RequestMethod.GET)
	public String Retrieve(HttpServletRequest req, ModelMap map) {
		tdCommonService.setHeader(map, req);
		return "/client/user_retrieve";
	}

	@RequestMapping(value = "/login/password_retrieve", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> Check(String smsCode,String mobile, HttpServletRequest req) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 1);

//		String codeBack = (String) request.getSession().getAttribute("RANDOMVALIDATECODEKEY");
//		if (!codeBack.equalsIgnoreCase(code)) {
//			res.put("msg", "验证码错误");
//			return res;
//		}
		
		String code = (String)req.getSession().getAttribute("SMSCODE");
		
		if(null !=smsCode){
			if(!smsCode.equalsIgnoreCase(code)){
				res.put("msg","短信验证码错误！");
				return res;
			}
		}

		TdUser user = tdUserService.findByUsernameAndIsEnabled(mobile);
		if (null != user) {

			req.getSession().setAttribute("username", user.getUsername());
			res.put("msg", "修改密码成功！");
			res.put("code", 0);
		} else {
			res.put("msg", "用户不存在");
		}

		return res;
	}

	@RequestMapping(value = "/login/retrieve_step", method = RequestMethod.POST)
	public String Step2(String mobile, HttpServletRequest req,HttpServletResponse resp, ModelMap map) {
		smsCode(mobile, resp, req);
		System.err.println(mobile);
		map.addAttribute("mobile", mobile);
		
		return "/client/user_retrieve_step2";
	}
	
	
	@RequestMapping(value = "/login/retrieve_step2", method = RequestMethod.POST)
	public String Step2(String smsCode,String mobile,HttpServletResponse resp, HttpServletRequest req, ModelMap map) {
		tdCommonService.setHeader(map, req);
		String code = (String)req.getSession().getAttribute("SMSCODE");
		
		map.addAttribute("mobile", mobile);
		if(null !=smsCode){
			if(!smsCode.equalsIgnoreCase(code)){
//				smsCode(mobile, resp, req);
				map.addAttribute("msg","短信验证码错误！");
				return "/client/user_retrieve_step2";
			}
		}
		TdUser user = tdUserService.findByMobile(mobile);
		map.addAttribute("user",user);
		req.getSession().setAttribute("retrieve_username", user.getUsername());
		return "/client/user_retrieve_step3";
	}


	@RequestMapping(value = "/login/retrieve_step3", method = RequestMethod.POST)
	public String Step3(String password, HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("retrieve_username");
		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);
		user.setPassword(password);
		tdUserService.save(user);
//		if (1L == user.getRoleId()) {
//			TdEnterprise enterprise = tdEnterpriseService.findbyUsername(username);
//			enterprise.setPassword(password);
//			tdentErpriseService.save(enterprise);
//		}
//		if (2L == user.getRoleId()) {
//			TdRegionAdmin regionAdmin = tdRegionAdminService.findbyUsername(username);
//			regionAdmin.setPassword(password);
//			tdRegionAdminService.save(regionAdmin);
//		}
//		if (3L == user.getRoleId()) {
//			TdExpert expert = tdExpertService.findbyUsername(username);
//			expert.setPassword(password);
//			tdExpertService.save(expert);
//		}
//		if (4L == user.getRoleId()) {
//			TdActivityAdmin activityAdmin = tdActivityAdminService.findbyUsername(username);
//			activityAdmin.setPassword(password);
//			tdActivityAdminService.save(activityAdmin);
//		}
		return "/client/user_retrieve_step4";
	}

	/**
	 * @author lc
	 * @注释：登录手机验证
	 */
	@RequestMapping(value = "/login/mobile_accredit", method = RequestMethod.POST)
	public String mobileVerification(String username, String mobile, String type, String typeId,
			HttpServletRequest request, ModelMap map) {
		if (null == username) {
			return "client/error_404";
		}
		if (null == mobile) {
			return "client/error_404";
		}
		TdUser user = tdUserService.addNewUser(username, "123456", null, null, null);
		if (null != user) {
			// if("qq".equals(type)){
			// //QQ登录新建账号
			// user.setQqUserId(typeId);
			// }else{
			// //支付宝登录新建账号
			// user.setAlipayUserId(typeId);
			// }
			user.setMobile(mobile);
			user.setLastLoginTime(new Date());
			tdUserService.save(user);
			request.getSession().setAttribute("username", user.getUsername());
			request.getSession().setAttribute("usermobile", user.getMobile());
			return "redirect:/";
		}
		return "client/error_404";
	}

	@RequestMapping("/logout")
	public String logOut(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/login";
	}

	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public void verify(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		VerifServlet randomValidateCode = new VerifServlet();
		randomValidateCode.getRandcode(request, response);
	}
	
	public Map<String, Object> smsCode(String mobile, HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<>();
		res.put("status", -1);
		Random random = new Random();
		String smscode = random.nextInt(9000) + 1000 + "";
		HttpSession session = request.getSession();
		session.setAttribute("SMSCODE", smscode);
		String info = "您的验证码为" + smscode + "，请在页面中输入以完成验证。【科技小巨人】";
		System.err.println(smscode);
		String content = null;
		try {
			content = URLEncoder.encode(info, "GB2312");
			System.err.println(content);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("message", "验证码生成失败！");
			return res;
		}
		String url = "http://www.ht3g.com/htWS/BatchSend.aspx?CorpID=CQDL00059&Pwd=644705&Mobile=" + mobile
				+ "&Content=" + content;
		StringBuffer fanhui = null;
		try {
			URL u = new URL(url);
			URLConnection connection = u.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			httpConn.setRequestProperty("Content-type", "text/html");
			httpConn.setRequestProperty("Accept-Charset", "utf-8");
			httpConn.setRequestProperty("contentType", "utf-8");
			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			StringBuffer resultBuffer = new StringBuffer();
			String tempLine = null;

			if (httpConn.getResponseCode() >= 300) {
				res.put("message", "HTTP Request is not success, Response code is " + httpConn.getResponseCode());
				return res;
			}

			try {
				inputStream = httpConn.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);

				while ((tempLine = reader.readLine()) != null) {
					resultBuffer.append(tempLine);
				}

				fanhui = resultBuffer;

			} finally {

				if (reader != null) {
					reader.close();
				}

				if (inputStreamReader != null) {
					inputStreamReader.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			res.put("message", "验证码生成失败！");
			return res;
		}
		res.put("status", 0);
		res.put("message", fanhui);
		return res;
	}
	
	//微信 获取openid
	@RequestMapping(value = "/getOpen")
	public String getOpenId(Long goodsId, HttpServletRequest req, ModelMap map) throws UnsupportedEncodingException{
		
		if(null != goodsId)
		{
			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Configure.getAppid()+ 
					 "&redirect_uri=http://www.xwd33.com/goods/detail/"+goodsId+"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
		}

		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Configure.getAppid()+ 
				 "&redirect_uri=http://www.xwd33.com/user/center&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	}
	
	
	/*  QQ登陆*/
	/**
	 * 跳转进入互联开放平台进行QQ登录验证
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author libiao
	 */
//	@RequestMapping(value = "/qq/login", method = RequestMethod.GET)
//	public void infoQQLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.setContentType("text/html;charset=utf-8");
//		try {
//			response.sendRedirect(new Oauth().getAuthorizeURL(request));
//		} catch (QQConnectException e) {
//			System.err.println("Connect error");
//			e.printStackTrace();
//		}
//	}
	
	/*
	 * @zhangji
	 * Step1：获取Authorization Code
	 */
	@RequestMapping(value="/qq/login",method = RequestMethod.GET)
	public String infoQQLogin(HttpServletRequest reqest, HttpServletResponse response){
		try {
			return "redirect:https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="
								+Configure.getClientId()+"&redirect_uri="+URLEncoder.encode("http://www.xwd33.com/qq/return","utf-8")
								+"&state=STATE&display=mobile";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "redirect:/login";
		}
	}
	
	/*
	 * @zhangji]
	 * QQ互联回调
	 */
	@RequestMapping(value="/qq/return",method = RequestMethod.GET)
	public String qqGetAccessToken(String code, String state, HttpServletRequest request, HttpServletResponse response){
		Map<String,String> userinfo = null;
		Map<String,String> token = null;
		String qqOpenid = null;
		
		if(null != state && state.equals("STATE")){
			TdQqController qq = new TdQqController();
			try {
				//获取ACCEES_TOKEN
				token = qq.getQqAccessToken(code, state);
				 System.out.println("----------------ACCEES_TOKEN:"+token.get("access_token"));
				 if(null != token){
					 //获取qqOpenid
					 qqOpenid = qq.getQqOpenid(token.get("access_token"));
					 System.out.println("------------------------qqOpenid:"+qqOpenid);
					 
					 //执行用户操作
					String accessToken = token.get("access_token");
					
					userinfo = qq.getQqUserInfo(accessToken, Configure.getClientId(), qqOpenid);

					TdUser user = tdUserService.findByQqOpenid(qqOpenid);
					if(null == user){
						//用户不存在，跳转绑定页面
						TdUser newUser  = new TdUser();
						newUser.setNickname(userinfo.get("nickname"));
						newUser.setQqOpenid(qqOpenid);
						newUser.setRegisterTime(new Date());
						newUser.setStatusId(1L);
						newUser.setTotalPoints(0L);
						newUser.setHeadImageUrl(userinfo.get("figureurl_qq_1"));
						newUser.setPassword("123456"); //password不能为空
						newUser.setUsername(qqOpenid);
						tdUserService.save(newUser);
						
				        Long id = newUser.getId();
				        String number = String.format("%04d", id);
				        newUser.setUsername("qq"+new Random().nextInt(9000)+newUser.getId());
				        newUser.setNumber(number);
				        tdUserService.save(newUser);
				        
						//建立分销关系
				        String rfCode = (String)request.getSession().getAttribute("rfCode");
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
										 url = "/goods/detail/"+goods.getId();
									
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
				        
							request.getSession().setAttribute("username", newUser.getUsername());		
					        
					        if(null != url && !url.equals("")){
					        	return "redirect:"+url;
					        }else{
					        	return "redirect:/user/center?QQ=1";
					        }
				        }
//							return "/client/accredit_login";
						}
					else{
						//用户存在，修改最后登录时间，跳转首页
						user.setLastLoginTime(new Date());
						tdUserService.save(user);
						request.getSession().setAttribute("username", user.getUsername());
						return "redirect:/";
					}
					
					 
				 }
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				System.out.println("-------------CATCH ERROR");
				e.printStackTrace();
			}
		}else{
			System.out.println(" ---------STATE IS WRONG");
			return "redirect:/login";
		}
		return "redirect:/login";
	}
	
	
	/**
	 * QQ登录返回结果处理
	 * @author libiao
	 * @param code
	 * @param state
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/login/qq_login_return", method = RequestMethod.GET)
	public String qqLoginReturn(String code, String state, HttpServletRequest request, HttpServletResponse response,ModelMap map) {

//		tdCommonService.setHeader(map, request);
		response.setContentType("text/html; charset=utf-8");

		try {
			System.err.println("code-------"+code);
			System.err.println("state-------"+state);
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
			String accessToken = null, openID = null;
			long tokenExpireIn = 0L;

			if (accessTokenObj.getAccessToken().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作
				System.err.print("没有获取到响应参数");
			} else {
				accessToken = accessTokenObj.getAccessToken();
				System.err.println("accessToken-------"+accessToken);
				
				tokenExpireIn = accessTokenObj.getExpireIn();

				request.getSession().setAttribute("demo_access_token", accessToken);
				request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

				// 利用获取到的accessToken 去获取当前用的openid -------- start
				OpenID openIDObj = new OpenID(accessToken);
				openID = openIDObj.getUserOpenID();
				System.err.println("openID-----------"+openID);

				//利用获取到的accessToken,openid 去获取用户在Qzone的昵称
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean.getRet() == 0) {
                   map.put("realName",userInfoBean.getNickname());
                }
				
				//根据openID查找用户
				map.put("alipay_user_id", openID);
				map.put("qq", "qq");
				TdUser user = tdUserService.findByQqOpenid(openID);
				if(null == user){
					//用户不存在，跳转绑定页面
					TdUser newUser  = new TdUser();
					newUser.setRealName(userInfoBean.getNickname());
					newUser.setQqOpenid(openID);
					newUser.setRegisterTime(new Date());
					newUser.setStatusId(1L);
					newUser.setHeadImageUrl(userInfoBean.getAvatar().getAvatarURL50());
					tdUserService.save(newUser);
					
			        Long id = newUser.getId();
			        String number = String.format("%04d", id);
			        newUser.setUsername("qq"+new Random().nextInt(9000)+newUser.getId());
			        newUser.setNumber(number);
			        tdUserService.save(newUser);
			        
					//建立分销关系
			        String rfCode = (String)request.getSession().getAttribute("rfCode");
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
									 url = "/goods/detail/"+goods.getId();
								
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
			        
						request.getSession().setAttribute("username", newUser.getUsername());		
				        
				        if(null != url && !url.equals("")){
				        	return "redirect:"+url;
				        }
			        }
//					return "/client/accredit_login";
					}
				else{
					//用户存在，修改最后登录时间，跳转首页
					user.setLastLoginTime(new Date());
					tdUserService.save(user);
					request.getSession().setAttribute("username", user.getUsername());
					return "redirect:/";
				}
			}
		
		} catch (QQConnectException e) {
			System.out.println("-----------------CATCH");
			return "redirect:/login";
		}
		return "redirect:/";
	}
}
