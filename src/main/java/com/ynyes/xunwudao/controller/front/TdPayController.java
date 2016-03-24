package com.ynyes.xunwudao.controller.front;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
//import com.unionpay.DemoBase;
//import com.unionpay.acp.sdk.SDKConfig;
//import com.unionpay.acp.sdk.SDKConstants;
//import com.unionpay.acp.sdk.SDKUtil;
import com.ynyes.xunwudao.entity.TdOrder;
import com.ynyes.xunwudao.entity.TdOrderGoods;
import com.ynyes.xunwudao.entity.TdSetting;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdOrderGoodsService;
import com.ynyes.xunwudao.service.TdOrderService;
import com.ynyes.xunwudao.service.TdSettingService;
import com.ynyes.xunwudao.service.TdUserService;

@Controller
@RequestMapping(value = "/user/pay")
public class TdPayController {

	@Autowired
	private TdUserService tdUserService;

	@Autowired
	private TdOrderService tdOrderService;

	@Autowired
	private TdSettingService tdSettingService;
	
	@Autowired
	private TdOrderGoodsService tdOrderGoodsService;
	
	@RequestMapping(value = "/free")
	public String payFree(HttpServletRequest req, HttpServletResponse resp,String orderNumber, Double money, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}

			//支付后的处理
			TdOrder tdOrder = tdOrderService.findByOrderNumber(orderNumber);
			if(null != tdOrder.getStatusId() && tdOrder.getStatusId()==2){
				tdOrder.setStatusId(4L);
				tdOrder.setPayTime(new Date());
				if(null != tdOrder.getOrderGoodsList()){
					for(TdOrderGoods item : tdOrder.getOrderGoodsList()){
						item.setTime(new Date());
						item.setUsername(tdOrder.getUsername());
						tdOrderGoodsService.save(item);
					}
				}
				tdOrderService.save(tdOrder);
				
				//分销处理
				if(null != tdOrder.getTotalGoodsPrice() && tdOrder.getTotalGoodsPrice() > 0 && null != tdOrder.getUserId()){
					TdUser user = tdUserService.findOne(tdOrder.getUserId());
					System.out.println("user:"+user.getUsername());
					if(null != user){
						
						//消费总额
						Double spend = 0.00;
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
			

		return "redirect:/user/ordre/list";
	
	}

	@RequestMapping(value = "/alipay/recharge")
	public String aliRecharge(HttpServletRequest req, HttpServletResponse resp,String orderNumber, Double money, ModelMap map) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/login";
		}

		TdUser user = tdUserService.findByUsername(username);

		// -----请求参数-----

		// 支付类型
		String payment_type = "1";

		// 页面跳转同步通知页面路径
		String return_url = "http://www.xwd33.com/user/pay/alipay/recharge/return";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		// 以下代码用于生成充值单编号
		Date date = new Date();
		String sDate = sdf.format(date);
		Random random = new Random();
		Integer suiji = random.nextInt(900) + 100;
//		String orderNum = "XWD" + sDate + suiji;
		String out_trade_no = orderNumber;
		String subject = "循伍道支付";
//		String show_url = "http://www.xwd33.com/user";
		String show_url = "http://www.xwd33.com/user/order/list";
		String total_fee = money + "";

//		TdRechargeLog log = new TdRechargeLog();
//		log.setNum(orderNum);
//		log.setMoney(money);
//		log.setUserId(user.getId());
//		log.setUsername(username);
//		log.setRechargeDate(new Date());
//		log.setStatusId(new Long(-1));
//		tdRechargetLogService.save(log);

		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		map.put("code", sHtmlText);
		return "/client/waiting_pay";
	}

	@RequestMapping(value = "/alipay/recharge/return")
	public String alipayReturn(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//

		String out_trade_no = null;
		String trade_no = null;
		String trade_status = null;
		try {
			// 商户订单号
			out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			//支付后的处理
			TdOrder tdOrder = tdOrderService.findByOrderNumber(out_trade_no);
			if(null != tdOrder.getStatusId() && tdOrder.getStatusId()==2){
				tdOrder.setStatusId(4L);
				tdOrder.setPayTime(new Date());
				if(null != tdOrder.getOrderGoodsList()){
					for(TdOrderGoods item : tdOrder.getOrderGoodsList()){
						item.setTime(new Date());
						item.setUsername(tdOrder.getUsername());
						tdOrderGoodsService.save(item);
					}
				}
				tdOrderService.save(tdOrder);
				
				//分销处理
				if(null != tdOrder.getTotalGoodsPrice() && tdOrder.getTotalGoodsPrice() > 0 && null != tdOrder.getUserId()){
					TdUser user = tdUserService.findOne(tdOrder.getUserId());
					System.out.println("user:"+user.getUsername());
					if(null != user){
						
						//消费总额
						Double spend = 0.00;
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
			else{
				System.out.println("订单有误！！！");
			}
			
			// 支付宝交易号
			trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		verify_result = true;
		if (verify_result) {// 验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
				System.out.println("TRADE_SUCCESS~");
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序
//				TdRechargeLog log = tdRechargetLogService.findByNum(out_trade_no);
//				log.setStatusId(new Long(0));
//				log = tdRechargetLogService.save(log);
//				if (null != log && null != log.getStatusId() && 0 == log.getStatusId()) {
//					TdUser user = tdUserService.findOne(log.getUserId());
//					user.setBalance(user.getBalance() + log.getMoney());
//					tdUserService.save(user);
//				}
			}

		} else {
			// 该页面可做页面美工编辑
		}
		return "/client/pay_success";
	}

	@RequestMapping(value = "/alipay/online")
	public String aliOnline(HttpServletRequest req, HttpServletResponse resp, Double money, ModelMap map, Long id) {
		String username = (String) req.getSession().getAttribute("username");
		if (null == username) {
			return "redirect:/user/center/login";
		}

		TdUser user = tdUserService.findByUsername(username);

		// -----请求参数-----

		// 支付类型
		String payment_type = "1";

		// 页面跳转同步通知页面路径
		String return_url = "http://www.xwd33.com/user/pay/alipay/online/return";

		TdOrder order = tdOrderService.findOne(id);
		String subject = "线上支付";
		String show_url = "http://www.xwd33.com/user/order/list";
		String total_fee = money + "";

		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("out_trade_no", order.getOrderNumber());
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		map.put("code", sHtmlText);
		return "/user/waiting_pay";
	}

	@RequestMapping(value = "/alipay/online/return")
	public String alipayOnlineReturn(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//

		String out_trade_no = null;
		String trade_no = null;
		String trade_status = null;
		try {
			// 商户订单号
			out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		verify_result = true;
		if (verify_result) {// 验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序
				TdOrder order = tdOrderService.findByOrderNumber(out_trade_no);
//				TdDiySite site = tdDiySiteService.findOne(order.getDiyId());
				TdUser user = tdUserService.findByUsername(order.getUsername());
				TdSetting setting = tdSettingService.findOne(1L);
//				if (1L == order.getStatusId()) {
//					// 判断停车场是否还有剩余车位
//					if (site.getParkingNowNumber() > 0) {
//						// 如果还有剩余的车位，就开始判断停车场是否有摄像头
//						if (null != site.getIsCamera() && site.getIsCamera()) {// 有摄像头就自动预约成功
//							if (!(site.getParkingNowNumber() > 0)) {
//								order.setFirstPay(0.00);
//								user.setBalance(user.getBalance() + setting.getFirstPay());
//								order.setStatusId(9L);
//								order.setCancelReason("指定停车场无剩余车位");
//								tdOrderService.save(order);
//								tdUserService.save(user);
//							}
//							order.setStatusId(3L);
//							order.setReserveTime(new Date());
//							tdOrderService.save(order);
//							tdDiySiteService.save(site);
//						} else {// 如果没有摄像头就需要等待泊车员手动确认预约
//							order.setStatusId(2L);
//							tdOrderService.save(order);
//						}
//					} else {// 剩余车位不足即预定失败，订单结束
//						order.setFirstPay(0.00);
//						// 返还定金
//						user.setBalance(user.getBalance() + setting.getFirstPay());
//						// 设置订单状态为交易结束
//						order.setStatusId(9L);
//						// 设置订单取消的原因
//						order.setCancelReason("指定停车场无剩余车位");
//
//						tdOrderService.save(order);
//						// 设置消息提示
//					}
//				} else if (4L == order.getStatusId()) {
//					order.setThePayType(0L);
					order.setStatusId(6L);
					order.setFinishTime(new Date());
//					order.setRemarkInfo("收取了" + setting.getPoundage() * 100 + "%的手续费");
//					site.setAllMoney(site.getAllMoney() + order.getTotalPrice());
					tdOrderService.save(order);
//					tdDiySiteService.save(site);
				}
			}
//		} else {
//			// 该页面可做页面美工编辑
//		}
		return "/user/pay_success";
	}


	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}

	@ModelAttribute
	public void init() {
		/**
		 * 参数初始化 在java main 方式运行时必须每次都执行加载 如果是在web应用开发里,这个方写在可使用监听的方式写入缓存,无须在这出现
		 */
//		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
	}
}
