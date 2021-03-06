package com.ynyes.xunwudao.controller.front;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tencent.common.Configure;
import com.ynyes.xunwudao.entity.TdArea;
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdAreaService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdEnterTypeService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdSettingService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.VerifServlet;

import net.sf.json.JSONObject;

/**
 * 注册处理
 * 
 */
@Controller
public class TdRegController {
	@Autowired
	private TdUserService tdUserService;

	@Autowired
	private TdCommonService tdCommonService;
	
	@Autowired
	private TdGoodsService tdGoodsService;
	
	@Autowired
	private TdAreaService tdAreaService;
	
	@RequestMapping("/reg")
	public String reg(/* Integer shareId, */String name, String rfCode, String code,   HttpServletRequest request,
			ModelMap map) {
		String username = (String) request.getSession().getAttribute("username");

//		if (null != shareId) {
//			map.addAttribute("share_id", shareId);
//		}
		// 基本信息
		tdCommonService.setHeader(map, request);

		if (null == username) {
			
			map.addAttribute("username", name);
			if(null != rfCode){
				map.addAttribute("rfCode", rfCode);
			}

			return "/client/reg";
		
		}
		return "redirect:/user/center";
	}

	@RequestMapping(value = "/logutil")
	public String LogUtils() {
		return "/logutil";
	}

	//修改手机号码
	@RequestMapping(value="/user/change/mobile")
	public String userCM(ModelMap map, HttpServletRequest request){
		String username = (String) request.getSession().getAttribute("username");
		if(null == username){
			return "redirect:/login";
		}
		
		return "/client/user_change_mobile";
	}
	
	@RequestMapping(value = "/user/mobile/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userMS(
									String mobile, 
									String smsCode,
									HttpServletRequest request) {
	    Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
	    
		String username = (String) request.getSession().getAttribute("username");
		if(null == username){
			res.put("msg", "请先登陆");
			return res;
		}
	    
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "手机电话不能为空！");
			res.put("id", "txt_regMobile");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "手机号码格式不对！");
			res.put("id", "txt_regMobile");
			return res;
		}
		String CODE_MOBILE = (String) request.getSession().getAttribute("CODE_MOBILE");
		if(null != CODE_MOBILE){
			if(!CODE_MOBILE.equalsIgnoreCase(mobile)){
				res.put("msg", "手机号与短信验证不一致！");
				res.put("id", "txt_regMobile");
				return res;
			}
	    }
		TdUser user2 = tdUserService.findByMobile(mobile);
		if (null != user2) {
			TdUser user  = tdUserService.findByUsername(username);
			if(null == user){
				res.put("msg", "用户不存在或用户名错误");
				res.put("login", 1);
				return res;
			}
//			String qqOpenid = user.getQqOpenid();
//			String openid = user.getOpenid();
//			String qqOpenid2 = user2.getQqOpenid();
//			String openid2 = user2.getOpenid();
//			if(null != qqOpenid && !qqOpenid.equals("") && null != qqOpenid2 && !qqOpenid2.equals("") && !qqOpenid.equals(qqOpenid2)){
//				res.put("msg", "该手机号码已绑定");
//			}
			if(user.getId() != user2.getId()){
				res.put("msg", "该手机号码已绑定其他账号，确认要绑定吗？");
				res.put("addall", mobile);
				res.put("id", "txt_regMobile");
				return res;
			}
		
		}
		
	    String SMSCODE = (String) request.getSession().getAttribute("SMSCODE");
	    if(null != SMSCODE){
			if(!SMSCODE.equalsIgnoreCase(smsCode)){
				res.put("msg", "手机验证码错误！");
				res.put("id", "txt_regMcode");
				return res;
			}
	    }
	
		TdUser user = tdUserService.findByUsername(username);
		user.setMobile(mobile);
		tdUserService.save(user);
		
		request.getSession().setAttribute("username", user.getUsername());
		res.put("msg", "修改成功！");
	    res.put("code", 0);
	    return res;
	}
	
	@RequestMapping(value = "/user/addall", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userAddall(
									String mobile, 
									HttpServletRequest request) {
	    Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
	    
		String username = (String) request.getSession().getAttribute("username");
		if(null == username){
			res.put("msg", "请先登陆");
			res.put("login", 1);
			return res;
		}
	    
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "手机电话不能为空！");
			res.put("id", "txt_regMobile");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "手机号码格式不对！");
			res.put("id", "txt_regMobile");
			return res;
		}
		TdUser user2 = tdUserService.findByMobile(mobile);
		if (null == user2) {
			res.put("msg", "该手机尚未注册！");
			res.put("id", "txt_regMobile");
			return res;
		}
	
		TdUser user = tdUserService.findByUsername(username);
		//合并账号
//		user.setMobile(mobile);
//		user.setPassword(user2.getPassword());
//		if(null == user.getNickname() || user.getNickname().equals("")){
//			user.setNickname(user2.getNickname());
//		}
//		if(null == user.getRealName() || user.getRealName().equals("")){
//			user.setRealName(user2.getRealName());
//		}
//		if(null == user.getAddress() || user.getAddress().equals("")){
//			user.setAddress(user2.getAddress());
//		}
//		user.setTotalPoints(user.getTotalPoints() + user2.getTotalPoints());
//		tdUserService.save(user);
//		
//		request.getSession().setAttribute("username", user.getUsername());
		
		//转移手机号码的绑定
		user2.setMobile("");
		tdUserService.save(user2);
		user.setMobile(mobile);
		tdUserService.save(user);
		res.put("msg", "修改成功！");
	    res.put("code", 0);
	    return res;
	}
	
	/*
	 * 第三方登陆，绑定手机号
	 */
	@RequestMapping(value = "/user/set/mobile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userSetMobile(
									String mobile, 
									String smsCode,
									String rfCode,
									String qqOpenid,
									String openid,
									String unionid,
									String username,
									String password,
									String nickname,
									String headImageUrl,
									String address,
									String province,
									Boolean sex,
									HttpServletRequest request) {
	    Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
	    
	    if(null == openid && null == unionid && null == qqOpenid){
	    	res.put("msg", "数据错误，请退出重新操作！");
			return res;
	    }
	    
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "手机电话不能为空！");
			res.put("id", "txt_regMobile");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "手机号码格式不对！");
			res.put("id", "txt_regMobile");
			return res;
		}
		String CODE_MOBILE = (String) request.getSession().getAttribute("CODE_MOBILE");
		if(null != CODE_MOBILE){
			if(!CODE_MOBILE.equalsIgnoreCase(mobile)){
				res.put("msg", "手机号与短信验证不一致！");
				res.put("id", "txt_regMobile");
				return res;
			}
	    }
		TdUser user2 = tdUserService.findByMobile(mobile);
		if (null != user2) {
			res.put("msg", "该手机号码已注册，确定绑定账号吗？");
			res.put("addall", mobile);
			res.put("id", "txt_regMobile");
			return res;
		}
		
	    String SMSCODE = (String) request.getSession().getAttribute("SMSCODE");
	    if(null != SMSCODE){
			if(!SMSCODE.equalsIgnoreCase(smsCode)){
				res.put("msg", "手机验证码错误！");
				res.put("id", "txt_regMcode");
				return res;
			}
	    }
	
		TdUser newUser = new TdUser();
		if(null == username || username.equals("")){
			Random random = new Random();
			if(null != openid && !openid.equals("")){
				newUser.setUsername("weixin_"+openid.substring(0, 4)+(random.nextInt(9000)+1000));
			}else if(null != qqOpenid && !qqOpenid.equals("")){
				newUser.setUsername("qq_"+qqOpenid.substring(0, 4)+(random.nextInt(9000)+1000));
			}
			
		}
		if(null != province && !province.equals("")){
			List<TdArea> arealist = tdAreaService.findByIsEnableTrueOrderBySortIdAsc();
			for(TdArea item : arealist){
				if (province.equals(item.getTitle())){
					newUser.setAreaId(item.getId());
				}
			}
		}
		
		newUser.setPassword("123456");
		newUser.setAddress(address);
		newUser.setNickname(nickname);
		newUser.setSex(sex);
		newUser.setMobile(mobile);
		newUser.setHeadImageUrl(headImageUrl);
		newUser.setTotalPoints(0L);
		newUser.setStatusId(1L);
		newUser.setRegisterTime(new Date());
		newUser.setLastLoginTime(new Date());
		newUser.setOpenid(openid);
		newUser.setUnionid(unionid);
		newUser.setQqOpenid(qqOpenid);
		tdUserService.save(newUser);
		
		Long id = newUser.getId();
        String number = String.format("%04d", id);
		newUser.setNumber(number);
		tdUserService.save(newUser);
		
		request.getSession().setMaxInactiveInterval(60 * 60 * 2);
		request.getSession().setAttribute("username", newUser.getUsername());
		
		 if(null != rfCode && !rfCode.equals("") && !rfCode.equals("null")){
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
						 res.put("url", url);
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
        }
		
		res.put("msg", "绑定成功！");
	    res.put("code", 0);
	    return res;
	}
	
	@RequestMapping(value = "/user/changeTo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userChangeTo(
									String mobile, 
									String openid,
									String unionid,
									String qqOpenid,
									String username,
									String password,
									HttpServletRequest request) {
	    Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
	    
	    if(null == openid && null == unionid && null == qqOpenid){
	    	res.put("msg", "数据错误，请退出重新操作！");
			return res;
	    }
	    
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "手机电话不能为空！");
			res.put("id", "txt_regMobile");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "手机号码格式不对！");
			res.put("id", "txt_regMobile");
			return res;
		}
		String CODE_MOBILE = (String) request.getSession().getAttribute("CODE_MOBILE");
		if(null != CODE_MOBILE){
			if(!CODE_MOBILE.equalsIgnoreCase(mobile)){
				res.put("msg", "手机号与短信验证不一致！");
				res.put("id", "txt_regMobile");
				return res;
			}
	    }
		TdUser userEver = tdUserService.findByMobile(mobile);
		if (null == userEver) {
			res.put("msg", "账号不存在！");
			res.put("id", "txt_regMobile");
			return res;
		}
	
		System.out.println(">>>>>>>>>>OPENID:"+openid+"\n>>>>>>>>>>>>>qqOPENID:"+qqOpenid);
		
		//绑定第三方到手机所在账号
		if(null != username && !username.equals("")){
			userEver.setUsername(username);
		}
		if(null != password && !password.equals("")){
			userEver.setPassword(password);
		}else{
			userEver.setPassword("123456");
		}
		if(null != openid && !openid.equals("")){
			userEver.setOpenid(openid);
		}
		if(null != unionid && !unionid.equals("")){
			userEver.setUnionid(unionid);
		}
		if(null != qqOpenid && !qqOpenid.equals("")){
			userEver.setQqOpenid(qqOpenid);
		}
		
		
		tdUserService.save(userEver);
		
		request.getSession().setMaxInactiveInterval(60 * 60 * 2);
		request.getSession().setAttribute("username", userEver.getUsername());
		
		res.put("msg", "绑定成功！");
	    res.put("code", 0);
	    return res;
	}
	/**
	 * 
	 * 注册用户保存到数据库<BR>
	 * 方法名：saveUser<BR>
	 * 时间：2015年2月2日-下午1:44:45 <BR>
	 * 
	 * @param user
	 * @param name
	 * @param mobile
	 * @param password
	 * @param newpassword
	 * @return String<BR>
	 * @param [参数1]
	 *            [参数1说明]
	 * @param [参数2]
	 *            [参数2说明]
	 * @exception <BR>
	 * @since 1.0.0
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> reg(
									String mobile, 
									String password, 
									String code,
									String rfCode,
									String smsCode,
									ModelMap map,
									HttpServletRequest request) {
	    Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
	    
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "手机电话不能为空！");
			res.put("id", "txt_regMobile");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "手机号码格式不对！");
			res.put("id", "txt_regMobile");
			return res;
		}
		
		
		
		TdUser user2 = tdUserService.findByMobile(mobile);
		if (null != user2) {
			res.put("msg", "该联系电话已被注册！");
			res.put("id", "txt_regMobile");
			return res;
		}
		
		String CODE_MOBILE = (String) request.getSession().getAttribute("CODE_MOBILE");
		if(null != CODE_MOBILE){
			if(!CODE_MOBILE.equalsIgnoreCase(mobile)){
				res.put("msg", "手机号与短信验证不一致！");
				res.put("id", "txt_regMobile");
				return res;
			}
	    }
		
	    String SMSCODE = (String) request.getSession().getAttribute("SMSCODE");
	    if(null != SMSCODE){
			if(!SMSCODE.equalsIgnoreCase(smsCode)){
				res.put("msg", "手机验证码错误！");
				res.put("id", "txt_regMcode");
				return res;
			}
	    }
		if(null == password || password.equals(""))
		{
			res.put("msg", "密码不能为空！");
			res.put("id", "txt_regPassword");
			return res;
		}
		if( !usernamePattern(password))
		{
			res.put("msg", "密码不能包含特殊字符");
			res.put("id", "txt_regPassword");
			return res;
		}
		if( !lengthPattern(password))
		{
			res.put("msg", "密码必须是6到18位字符");
			res.put("id", "txt_regPassword");
			return res;
		}
//		String codeBack = (String) request.getSession().getAttribute("RANDOMVALIDATECODEKEY");
//		if (!codeBack.equalsIgnoreCase(code)) {
//			res.put("msg", "验证码错误");
//			res.put("id", "txt_regCode");
//			return res;
//		}
		TdUser user = new TdUser();
		//建立分销关系
		if(null != rfCode && !rfCode.equals("")){
			//第一级推荐人id
			Long userId = Long.parseLong(rfCode.substring(0, 4));
			//商品id 
			if(rfCode.length() > 7){
				Long goodsId = Long.parseLong(rfCode.substring(7));
				TdGoods goods = tdGoodsService.findOne(goodsId);
				
				//让新用户登陆后跳转到分享的商品详情页面
				if( null != goods){
					String url = "/goods/detail/"+goods.getId();
					res.put("url", url);
				}
			}
			TdUser userOne = tdUserService.findOne(userId);
			
			
			if(null == userOne){
				res.put("msg","推荐码有误，请重新核对！");
				res.put("id", "txt_regRfcode");
				return res;
			}
			//第一级推荐人
			user.setUpUserOne(userOne.getId());
			//第二级推荐人
			Long userTwoUpId = userOne.getUpUserOne();
			if(null != userTwoUpId){
				TdUser userTwo = tdUserService.findOne(userTwoUpId);
				if(null != userTwo){
					user.setUpUserTwo(userTwo.getId());
				}
			}

		}
		user.setPassword(password);
		user.setUsername(mobile);
		user.setMobile(mobile);
		user.setStatusId(1L);
		user.setTotalPoints(0L);
		user.setRegisterTime(new Date());
		user.setIsDeal(0L);
		user.setLastLoginTime(new Date());
		tdUserService.save(user);
		
        Long id = user.getId();
        String number = String.format("%04d", id);
        user.setNumber(number);
        tdUserService.save(user);
		
		request.getSession().setAttribute("username", user.getUsername());
		res.put("msg", "注册成功，欢迎使用循伍道！");
	    res.put("code", 0);
	    return res;

	}
	
	@RequestMapping(value = "/reg/smscode", method = RequestMethod.GET)
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
		
		if (!isMobileNO(mobile))
		{
			res.put("message","手机号不正确");
			return res;
		}
		TdUser user = tdUserService.findByMobile(mobile);
		if (null != user) {
			res.put("msg", "该手机已经注册");
			return res;
		}
		
		Random random = new Random();
		String smscode = random.nextInt(9000) + 1000 + "";
		HttpSession session = request.getSession();
		session.setAttribute("SMSCODE", smscode);
		session.setAttribute("CODE_MOBILE", mobile);
		String info = "【循伍道助健康】验证码:" + smscode + "，健康交给循伍道，活到100不算老，欢迎你注册循伍道健康管理VIP会员，此验证码三分钟内有效。";
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
		
		//测试
//		String url = "http://121.199.1.58:8888/sms.aspx?action=send&userid=607&account=ssgp&password=0551ssgp&mobile=15086690025&content=【上上果品】顾客您好，您的手机动态验证码为：****** 。该码30分钟内有效，若30分钟内未输入，需重新获取。验证码转发无效。&sendTime=&taskName=&checkcontent=0&mobilenumber=1&countnumber=1&telephonenumber=1";
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
		System.out.println("返回："+fanhui);
		return res;
	}

	@RequestMapping(value = "/retrieve/smscode", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> retrieveSmsCode(String mobile, String code, Long third, HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<>();
		res.put("status", -1);
		
		String codeBack = (String) request.getSession().getAttribute("RANDOMVALIDATECODEKEY");
		if (!codeBack.equalsIgnoreCase(code)) {
			res.put("msg", "验证码错误");
			res.put("id", "txt_regCode");
			return res;
		}
		
		if (!isMobileNO(mobile))
		{
			res.put("message","手机号不正确");
			return res;
		}
		
		if(third == null){
			TdUser user = tdUserService.findByMobile(mobile);
			if (null == user) {
				res.put("msg", "用户不存在！");
				return res;
			}
		}

		
		Random random = new Random();
		String smscode = random.nextInt(9000) + 1000 + "";
		HttpSession session = request.getSession();
		session.setAttribute("SMSCODE", smscode);
		session.setAttribute("CODE_MOBILE", mobile);
		String info = "【循伍道助健康】验证码:" + smscode + "，此验证码三分钟内有效。健康交给循伍道，活到100不算老。";
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
		
		//测试
//		String url = "http://121.199.1.58:8888/sms.aspx?action=send&userid=607&account=ssgp&password=0551ssgp&mobile=15086690025&content=【上上果品】顾客您好，您的手机动态验证码为：****** 。该码30分钟内有效，若30分钟内未输入，需重新获取。验证码转发无效。&sendTime=&taskName=&checkcontent=0&mobilenumber=1&countnumber=1&telephonenumber=1";
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
		System.out.println("返回："+fanhui);
		return res;
	}

	
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(0|86|17951|[0-9]{3})?([0-9]{8})|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		}
	public boolean usernamePattern(String username){
		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher m = p.matcher(username);
		return m.matches();
	}
	public boolean lengthPattern(String username){
		Pattern p = Pattern.compile("^.{6,18}$");
		Matcher m = p.matcher(username);
		return m.matches();
	}

}