package com.ynyes.xunwudao.controller.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.hibernate.engine.spi.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;
import com.ynyes.xunwudao.entity.TdOrder;
import com.ynyes.xunwudao.entity.TdApply;
import com.ynyes.xunwudao.entity.TdApplyType;
import com.ynyes.xunwudao.entity.TdArea;
import com.ynyes.xunwudao.entity.TdBill;
import com.ynyes.xunwudao.entity.TdDemand;
import com.ynyes.xunwudao.entity.TdPhoto;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.entity.TdUserPoint;
import com.ynyes.xunwudao.service.TdApplyService;
import com.ynyes.xunwudao.service.TdApplyTypeService;
import com.ynyes.xunwudao.service.TdAreaService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdDemandService;
import com.ynyes.xunwudao.service.TdEnterTypeService;
import com.ynyes.xunwudao.service.TdOrderService;
import com.ynyes.xunwudao.service.TdPhotoService;
import com.ynyes.xunwudao.service.TdUserPointService;
import com.ynyes.xunwudao.service.TdUserService;
import com.ynyes.xunwudao.util.ClientConstant;

import net.sf.json.JSONObject;

/**
 * 用户中心
 * 
 * @author Sharon
 *
 */
@Controller
public class TdUserController {

	@Autowired
	private TdUserService tdUserService;

	@Autowired
	private TdDemandService tdDemandService;

	@Autowired
	private TdCommonService tdCommonService;
	
	@Autowired
	private TdEnterTypeService tdEnterTypeService;
	
	@Autowired
	private TdAreaService tdAreaService;
	
	@Autowired
	private TdApplyTypeService tdApplyTypeService;
	
	@Autowired
	private TdApplyService tdApplyService;
	
	@Autowired
	private TdPhotoService tdPhotoService;
	
	@Autowired
	private TdUserPointService tdUserPointService;
	
	@Autowired
	private TdOrderService tdOrderService;

	@RequestMapping(value = "/user/center")
	public String user(HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}

		tdCommonService.setHeader(map, req);

		map.addAttribute("server_ip", req.getLocalName());
		map.addAttribute("server_port", req.getLocalPort());

		TdUser tdUser = tdUserService.findByUsername(username);

		if (null == tdUser) {
			return "/client/error_404";
		}

		map.addAttribute("user", tdUser);
		map.addAttribute("showIcon", 4);

		return "/client/user_center";
	}

	@RequestMapping(value = "/demand/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> demandAdd(HttpServletRequest req, String content, String name, String mobile,
			String mail, Long statusId, ModelMap map) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 1);

		TdDemand tdDemand = new TdDemand();

		tdDemand.setContent(content);
		tdDemand.setTime(new Date());
		tdDemand.setName(name);
		tdDemand.setMail(mail);
		tdDemand.setMobile(mobile);
		tdDemand.setStatusId(0L);

		// TdUser user = tdUserService.findByUsernameAndIsEnabled(username);

		tdDemandService.save(tdDemand);

		map.addAttribute("demand_list", tdDemand);

		res.put("code", 0);

		return res;
	}

	@RequestMapping(value = "/user/comment/sec")
	public String commentSec(HttpServletRequest req, Long commentId, ModelMap map) {
		return "/client/comment_sec";
	}



	@RequestMapping("/user/check/oldpassword")
	@ResponseBody
	public Map<String, Object> checkOldPassword(HttpServletRequest req, String param) {
		Map<String, Object> res = new HashMap<>();
		res.put("status", "n");

		String username = (String) req.getSession().getAttribute("username");
		TdUser user = tdUserService.findByUsername(username);
		if (!param.equals(user.getPassword())) {
			res.put("info", "当前密码输入错误！");
			return res;
		}
		res.put("status", "y");
		return res;
	}



	@RequestMapping(value = "/user/info", method = RequestMethod.GET)
	public String userInfo(Long hatu , HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");

		if (null == username) {
			return "redirect:/login";
		}

		tdCommonService.setHeader(map, req);
		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);

		map.addAttribute("user", user);
        map.addAttribute("showIcon", 4);

		return "/client/user_info";
	}

	@RequestMapping(value = "/user/info/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> userInfo(HttpServletRequest req,
													String realName,
													Boolean sex,
//													String headImageUrl,
													String address, 
													String mobile, 
													HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
		
		String usernameSession = (String) req.getSession().getAttribute("username");

		if (null == usernameSession) {
			res.put("msg", "请先登录！");
			res.put("login", 1);
			return res;
		}

		TdUser user = tdUserService.findByUsernameAndIsEnabled(usernameSession);
		
//		if (null == realName ||realName.equals(""))
//		{
//			res.put("msg", "联系人姓名不能为空！");
//			return res;
//		}
//		if (null == username ||username.equals(""))
//		{
//			res.put("msg", "用户名不能为空！");
//			return res;
//		}
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "联系电话不能为空！");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "电话号码格式不对！");
			return res;
		}
//
//		TdUser user1 = tdUserService.findByUsername(username);
//		if (null != user1 && user1.getId() != user.getId()) {
//			res.put("msg", "该用户名已被注册！");
//			return res;
//		}
		TdUser user2 = tdUserService.findByMobile(mobile);
		if (null != user2 && user2.getId() != user.getId()) {
			res.put("msg", "该联系电话已被注册！");
			return res;
		}
		
//		user.setHeadImageUrl(headImageUrl);
		user.setSex(sex);
		user.setAddress(address);
		user.setRealName(realName);
		user.setMobile(mobile);
		user = tdUserService.save(user);
  
	    res.put("code", 0);
	    return res;
	}
	
	//用户积分 zhangji
	@RequestMapping(value = "/user/point")
	public String userPoint(HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}
		
		TdUser tdUser = tdUserService.findByUsername(username);
		if (null == tdUser) {
			return "/client/error_404";
		}
		
    	//推荐码
    	String userNumber = tdUser.getNumber();
    	Random random = new Random();
		String randomNumber = random.nextInt(900) + 100 + "";
		
		String rfCode = userNumber + randomNumber.toString() ;
		map.addAttribute("rfCode", rfCode);
		
		map.addAttribute("user", tdUser);
		map.addAttribute("showIcon", 4);
		 
		return "/client/user_point";
	}
	
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(0|86|17951|[0-9]{3})?([0-9]{8})|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		}

	@RequestMapping(value = "/user/password", method = RequestMethod.GET)
	public String userPassword(HttpServletRequest req, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "/client/login";
		}
		TdUser user = tdUserService.findByUsername(username);

		map.addAttribute("user", user);
		return "/client/password_reset";

	}

	@RequestMapping(value = "/user/password", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userPassword(HttpServletRequest req, String oldPassword, String newPassword,
			ModelMap map) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 1);

		String username = (String) req.getSession().getAttribute("username");

		if (null == username) {
			res.put("msg", "请先登录！");
			return res;
		}

		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);

		if (user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
		}

		map.addAttribute("user", tdUserService.save(user));

		res.put("code", 0);
		return res;
	}

	@RequestMapping("/user/password/save")
	public String savePassword(HttpServletRequest req, String newPassword) {
		String username = (String) req.getSession().getAttribute("username");
		TdUser user = tdUserService.findByUsername(username);
		user.setPassword(newPassword);
		tdUserService.save(user);
		
		return "/client/login";
	}
	
	//改变头像
	@RequestMapping(value = "/user/head")
	public String userHead(ModelMap map,  HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}
		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);
		map.addAttribute("user", user);
		
		return "client/user_head";
	}
	
	//提交表单页面【我要】
	@RequestMapping(value = "/apply")
	public String apply(ModelMap map,  HttpServletRequest req) {
		tdCommonService.setHeader(map, req); 
		String username = (String) req.getSession().getAttribute("username");
//		if (null == username) {
//			return "redirect:/login";
//		}
		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);
		if (null != user)
		{
			map.addAttribute("user", user);
		}
		
		
		//业务类型
		List<TdApplyType> applyTypeList = tdApplyTypeService.findByIsEnableTrueOrderBySortIdAsc();
		map.addAttribute("applyType_list", applyTypeList);
		
		return "client/apply_index";
	}
	
	//填写信息
	@RequestMapping(value = "/apply/edit/{applyTypeId}")
	public String applyMake(@PathVariable Long applyTypeId, ModelMap map,  HttpServletRequest req) {
		tdCommonService.setHeader(map, req); 
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}
		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);
		map.addAttribute("user", user);
		
		if (null != applyTypeId)
		{
			TdApplyType applyType = tdApplyTypeService.findOne(applyTypeId);
			map.addAttribute("typeTitle", applyType.getTitle());
			map.addAttribute("applyTypeId", applyTypeId);
			if(null != applyType.getSpAcc()&&applyType.getSpAcc()==1)
			{
				map.addAttribute("spAcc", applyType.getSpAcc());
				map.addAttribute("enterType_list", tdEnterTypeService.findByIsEnableTrueOrderBySortIdAsc());
			}
			
			//【防止重复提交】检查当前类别是否有未审核的业务
			List<TdApply> tdApplyList = tdApplyService.findByUserIdAndStatusIdAndApplyTypeId(user.getId(), 0L, applyTypeId);
			if(null != tdApplyList && tdApplyList.size() > 0)
			{
				map.addAttribute("aru", 1);
			}
		}
		
		List<TdArea> areaList = tdAreaService.findByIsEnableTrueOrderBySortIdAsc();
		map.addAttribute("area_list", areaList);
		

		return "client/apply_edit";
	}
	
	//提交表单
	@RequestMapping(value = "/apply/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> applySubmit(HttpServletRequest req, 
												String realName, 
												String mobile,
												Long areaId,
												String address,
												String remark,
												Long applyTypeId,
												Long enterTypeId) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 1);

		String username = (String) req.getSession().getAttribute("username");

		if (null == username) {
			res.put("msg", "请先登录！");
			return res;
		}

		TdUser user = tdUserService.findByUsernameAndIsEnabled(username);

		if(null == realName || realName.equals(""))
		{
			res.put("msg", "请输入联系人姓名！");
			return res;
		}
		if(null == mobile || mobile.equals(""))
		{
			res.put("msg", "请输入联系人电话！");
			return res;
		}
		if(null == areaId || areaId.equals(""))
		{
			res.put("msg", "请输入公司所处区域！");
			return res;
		}
		
		TdApply tdApply = new TdApply();
		tdApply.setRealName(realName);
		tdApply.setMobile(mobile);
		tdApply.setAreaId(areaId);
		tdApply.setStatusId(0L);
		tdApply.setSortId(99L);
		tdApply.setTime(new Date());
		tdApply.setUserId(user.getId());
		if(null != remark)
		{
			tdApply.setRemark(remark);
		}
		if(null != address)
		{
			tdApply.setAddress(address);
		}
		if(null != enterTypeId)
		{
			tdApply.setEnterTypeId(enterTypeId);
		}
		if(null != applyTypeId)
		{
			tdApply.setApplyTypeId(applyTypeId);
		}
		tdApplyService.save(tdApply);
		
		res.put("code", 0);
		return res;
	}
	
	//订单列表
	@RequestMapping(value = "/user/order/list/{statusId}")
    public String orderList(@PathVariable Integer statusId, Integer page,
            String keywords, Integer timeId, HttpServletRequest req,
            ModelMap map) {
        String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
            return "redirect:/login";
        }

        tdCommonService.setHeader(map, req);

        if (null == page) {
            page = 0;
        }

        if (null == timeId) {
            timeId = 0;
        }

        if (null == statusId) {
            statusId = 0;
        }

        TdUser tdUser = tdUserService.findByUsernameAndIsEnabled(username);

        map.addAttribute("user", tdUser);
        map.addAttribute("status_id", statusId);
        map.addAttribute("time_id", timeId);
    	List<TdOrder> orderList = tdOrderService.findByUserIdOrderByOrderTimeDesc(tdUser.getId());
    	map.addAttribute("order_list", orderList);
//        Page<TdOrder> orderPage = tdOrderService.findByUsername(username, page, ClientConstant.pageSize);
//        if (timeId.equals(0)) {
//            if (statusId.equals(0)) {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService.findByUsernameAndSearch(
//                            username, keywords, page, ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService.findByUsername(username, page,
//                            ClientConstant.pageSize);
//                }
//            } else {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndSearch(username,
//                                    statusId, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService.findByUsernameAndStatusId(
//                            username, statusId, page, ClientConstant.pageSize);
//                }
//            }
//        } else if (timeId.equals(1)) {
//            Date cur = new Date();
//            Calendar calendar = Calendar.getInstance();// 日历对象
//            calendar.setTime(cur);// 设置当前日期
//            calendar.add(Calendar.MONTH, -1);// 月份减一
//            Date time = calendar.getTime();
//
//            if (statusId.equals(0)) {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndTimeAfterAndSearch(username,
//                                    time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService.findByUsernameAndTimeAfter(
//                            username, time, page, ClientConstant.pageSize);
//                }
//            } else {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfterAndSearch(
//                                    username, statusId, time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfter(username,
//                                    statusId, time, page,
//                                    ClientConstant.pageSize);
//                }
//            }
//        } else if (timeId.equals(3)) {
//            Date cur = new Date();
//            Calendar calendar = Calendar.getInstance();// 日历对象
//            calendar.setTime(cur);// 设置当前日期
//            calendar.add(Calendar.MONTH, -3);// 月份减一
//            Date time = calendar.getTime();
//
//            if (statusId.equals(0)) {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndTimeAfterAndSearch(username,
//                                    time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService.findByUsernameAndTimeAfter(
//                            username, time, page, ClientConstant.pageSize);
//                }
//            } else {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfterAndSearch(
//                                    username, statusId, time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfter(username,
//                                    statusId, time, page,
//                                    ClientConstant.pageSize);
//                }
//            }
//        } else if (timeId.equals(6)) {
//            Date cur = new Date();
//            Calendar calendar = Calendar.getInstance();// 日历对象
//            calendar.setTime(cur);// 设置当前日期
//            calendar.add(Calendar.MONTH, -6);// 月份减一
//            Date time = calendar.getTime();
//
//            if (statusId.equals(0)) {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndTimeAfterAndSearch(username,
//                                    time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService.findByUsernameAndTimeAfter(
//                            username, time, page, ClientConstant.pageSize);
//                }
//            } else {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfterAndSearch(
//                                    username, statusId, time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfter(username,
//                                    statusId, time, page,
//                                    ClientConstant.pageSize);
//                }
//            }
//        } else if (timeId.equals(12)) {
//            Date cur = new Date();
//            Calendar calendar = Calendar.getInstance();// 日历对象
//            calendar.setTime(cur);// 设置当前日期
//            calendar.add(Calendar.YEAR, -1);// 减一
//            Date time = calendar.getTime();
//
//            if (statusId.equals(0)) {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndTimeAfterAndSearch(username,
//                                    time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService.findByUsernameAndTimeAfter(
//                            username, time, page, ClientConstant.pageSize);
//                }
//            } else {
//                if (null != keywords && !keywords.isEmpty()) {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfterAndSearch(
//                                    username, statusId, time, keywords, page,
//                                    ClientConstant.pageSize);
//                } else {
//                    orderPage = tdOrderService
//                            .findByUsernameAndStatusIdAndTimeAfter(username,
//                                    statusId, time, page,
//                                    ClientConstant.pageSize);
//                }
//            }
//        }
//
//        map.addAttribute("order_page", orderPage);

        return "/client/user_order_list";
    }
	
	/*--------------------------------------------------------
	 * -------------------    支付///////        -------------------
	 -------------------------------------------------------*/


	/*--------------------------------------------------------
	 * -------------------    //////支付  -------------------------
	 -------------------------------------------------------*/
	/**
	 * 图片地址字符串整理，多张图片用,隔开
	 * 
	 * @param params
	 * @return
	 */
	private String parsePicUris(String[] uris) {
		if (null == uris || 0 == uris.length) {
			return null;
		}

		String res = "";

		for (String item : uris) {
			String uri = item.substring(item.indexOf("|") + 1, item.indexOf("|", 2));

			if (null != uri) {
				res += uri;
				res += ",";
			}
		}

		return res;
	}
}
