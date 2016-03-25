package com.ynyes.xunwudao.controller.front;

import static org.apache.commons.lang3.StringUtils.leftPad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.neo4j.cypher.internal.compiler.v1_9.commands.True;
//import org.neo4j.kernel.api.exceptions.schema.TooManyLabelsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.cytm.payment.alipay.AlipayConfig;
//import com.cytm.payment.alipay.Constants;
//import com.cytm.payment.alipay.PaymentChannelAlipay;
//import com.cytm.payment.alipay.core.AlipayNotify;
//import com.ibm.icu.util.Calendar;
import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;
import com.ynyes.xunwudao.entity.TdCartGoods;
import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.entity.TdOrder;
import com.ynyes.xunwudao.entity.TdOrderGoods;
import com.ynyes.xunwudao.entity.TdPayType;
import com.ynyes.xunwudao.entity.TdUser;
import com.ynyes.xunwudao.service.TdAreaService;
import com.ynyes.xunwudao.service.TdCartGoodsService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.service.TdOrderGoodsService;
import com.ynyes.xunwudao.service.TdOrderService;
import com.ynyes.xunwudao.service.TdUserService;
//import com.ynyes.xunwudao.util.QRCodeUtils;

import net.sf.json.JSONObject;

/**
 * 订单
 *
 */
@Controller
@RequestMapping("/order")
public class TdOrderController extends AbstractPaytypeController {

    private static final String PAYMENT_ALI = "ALI";
    private static final String PAYMENT_WX = "WX";

    @Autowired
    private TdCartGoodsService tdCartGoodsService;

    @Autowired
    private TdUserService tdUserService;

    @Autowired
    private TdGoodsService tdGoodsService;

    @Autowired
    private TdOrderGoodsService tdOrderGoodsService;

    @Autowired
    private TdOrderService tdOrderService;

    @Autowired
    private TdCommonService tdCommonService;

    @Autowired
    private TdAreaService tdAreaService;
    
//    private PaymentChannelAlipay paymentChannelAlipay = new PaymentChannelAlipay();

    
    
    @RequestMapping(value = "/cancel")
    public String orderCancel(String orderNumber, Long state,HttpServletRequest req)
    {
    	String username = (String) req.getSession().getAttribute("username");
    	
    	if (username == null)
    	{
    		return "redirect:/login";
		}
    	TdOrder tdOrder = tdOrderService.findByOrderNumber(orderNumber);
		if (null == tdOrder || !tdOrder.getStatusId().equals(2L))
		{
//			return "redirect:/user/order/list/" + state; 
			return "redirect:/user/order/list"; 
		}
		
		TdUser user = tdUserService.findByUsername(username);
		Long totalPoints = user.getTotalPoints();
		if(null == totalPoints){
			totalPoints = 0L;
		}
		Long pointUse = tdOrder.getPointUse();
		if(null != pointUse && pointUse > 0){
			user.setTotalPoints(totalPoints+pointUse);
			tdUserService.save(user);
		}
		tdOrder.setStatusId(7L);

		tdOrderService.save(tdOrder);
    	
    	
//    	return "redirect:/user/order/list/" + state;
		return "redirect:/user/order/list"; 
    }
    
    //确认服务 已体检
    @RequestMapping(value = "/confirm" , method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> orderconfirm(String orderNumber, Long state,HttpServletRequest req)
    {
    	Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 1);
    	String username = (String) req.getSession().getAttribute("username");
    	
    	if(null == username)
        {
        	res.put("msg", "请先登录！");
        	res.put("login",1);
        	return res;
        }
    	TdOrder tdOrder = tdOrderService.findByOrderNumber(orderNumber);
		if (null == tdOrder || !tdOrder.getStatusId().equals(4L))
		{
			res.put("msg", "订单状态有误或不存在！");
        	return res;
		}
		
		tdOrder.setStatusId(6L);
		tdOrderService.save(tdOrder);
    	
    	res.put("msg", "确认成功");
	    res.put("code", 0);
        return res;
    }
    
    //我的收藏 买买买
    @RequestMapping(value = "/buybuybuy", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> buybuybuy(Long[] goodsIds, ModelMap map,
			 HttpServletRequest req) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 1);
		//必须登陆
        String username = (String) req.getSession().getAttribute("username");
        if(null == username)
        {
        	res.put("msg", "请先登录！");
        	res.put("login",1);
        	return res;
        }
        
        //不允许空订单
        if(null == goodsIds || goodsIds.length == 0)
        {
        	res.put("msg", "请至少选择一件商品！");
        	return res;
        }
        
        List<TdCartGoods> buyGoodsList = new ArrayList<TdCartGoods>();
        
        for(Long id : goodsIds){
        	TdGoods goods = tdGoodsService.findOne(id);
        	
            TdCartGoods buyGoods = new TdCartGoods();
            buyGoods.setGoodsId(goods.getId());
            buyGoods.setGoodsTitle(goods.getTitle());
            buyGoods.setGoodsCoverImageUri(goods.getCoverImageUri());
            buyGoods.setPrice(goods.getSalePrice());
            buyGoods.setQuantity(1L);
            buyGoods.setIsLoggedIn(true);
            buyGoods.setIsSelected(true);
            buyGoods.setUsername(username);
            buyGoods.setIsCollected(true);
            tdCartGoodsService.save(buyGoods);
            
            buyGoodsList.add(buyGoods);
        }
        
        res.put("code", 0);
        return res;
        
	}

    //收藏直接购买专用
    @RequestMapping(value = "/buyC")
    public String buyC(HttpServletRequest req, ModelMap map) {
    	
        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
        	return "redirect:/login";
        }
        TdUser tdUser = tdUserService.findByUsername(username);

        map.addAttribute("user", tdUser);
        
        map.addAttribute("area_list", tdAreaService.findByIsEnableTrueOrderBySortIdAsc());
        
        tdCommonService.setHeader(map, req);
        
        List<TdCartGoods> buyGoodsList = tdCartGoodsService.findByUserIdAndIsCollectedTrue(username);
        map.addAttribute("buy_goods_list", buyGoodsList);
        
        return "/client/order_info";
    }
    
	/**
     * 立即购买
     * 
     * @param type
     *            购买类型 (comb: 组合购买)
     * @param gid
     *            商品ID
     * @param zhid
     *            组合ID，多个组合商品以","分开
     * @param req
     * @param map
     * @return
     */
    @RequestMapping(value = "/buy")
    public String orderBuy(Long gid, Double price, String code, Long quantity,HttpServletRequest req, ModelMap map) {
    	
        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
        	return "redirect:/login?goodsId="+gid;
        }
        TdUser tdUser = tdUserService.findByUsername(username);

        map.addAttribute("user", tdUser);
        
        map.addAttribute("area_list", tdAreaService.findByIsEnableTrueOrderBySortIdAsc());
        if (null == quantity)
        {
			quantity = 1L;
		}

        // 购买商品
        TdGoods goods = tdGoodsService.findOne(gid);

        if (null == goods) {
        	return "/client/error_404";
        }

        // 优惠券
        // map.addAttribute("coupon_list",tdCouponService.findByUsernameAndIsUseable(username));

        TdCartGoods buyGoods = new TdCartGoods();

        buyGoods.setGoodsId(goods.getId());
        buyGoods.setGoodsTitle(goods.getTitle());
        buyGoods.setGoodsCoverImageUri(goods.getCoverImageUri());
        buyGoods.setPrice(goods.getSalePrice());
        buyGoods.setQuantity(1L);
        buyGoods.setIsLoggedIn(true);
        buyGoods.setIsSelected(true);
        buyGoods.setUsername(username);
        tdCartGoodsService.save(buyGoods);
        
        tdCommonService.setHeader(map, req);
//		map.addAttribute("userLike", tdUser.getUserLike());
        map.addAttribute("goods", tdGoodsService.findOne(gid));
        
        
        List<TdCartGoods> buyGoodsList = new ArrayList<TdCartGoods>();
        buyGoodsList.add(buyGoods);
        map.addAttribute("buy_goods_list", buyGoodsList);
        
        
        Date date=new Date();//取时间
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        dateString = dateString.replace(' ', 'T');
        map.addAttribute("next_day_time", dateString);
        System.err.println(dateString);
        return "/client/order_info";
    }

    /**
     * 
     * @param addressId
     * @param shopId
     * @param payTypeId
     * @param deliveryTypeId
     * @param isNeedInvoice
     * @param invoiceTitle
     * @param userMessage
     * @param appointmentTime
     * @param req
     * @param map
     * @return
     */
    @RequestMapping(value = "/buysubmit", method = RequestMethod.POST)
    public String buySubmit(String shippingAddress, // 送货地址
    						String realName, //购买人
    						String shippingPhone, //购买人手机
    						String appointmentTime, //用餐时间    	
    						String userRemarkInfo, //用户留言
    						Long goodId,	//商品id
				            Long quantity, // 购买数量
				            Long taste, //口味
				            String tool, //用餐工具
				            String peopleRange,//使用人数范围
				            HttpServletRequest req,
				            ModelMap map)
    {
    	Double totalLeftPrice = 0.0;
    	shippingAddress = shippingAddress.replace(',', ' ');
        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
            return "redirect:/login?goodId=" + goodId;
        }

        TdUser tdUser = tdUserService.findByUsernameAndIsEnabled(username);

        if (null == tdUser)
        {
            return "redirect:/login?goodId=" + goodId;
        }

        // 订单商品
        List<TdOrderGoods> orderGoodsList = new ArrayList<TdOrderGoods>();
        
        
        // 商品
        TdGoods goods = tdGoodsService.findById(goodId);
        
        if (goods == null)
        {
			return "/client/food";
		}
        
        if (quantity == null)
        {
			quantity = 0L;
		}
        if (taste == null)
        {
			taste = 0L;
		}
//        if (tool == null)
//        {
//        	tool = 0L;
//		}
        
        
        
        
        
        Double totalGoodsPrice = 0.0;

        TdOrderGoods ordergoods = new TdOrderGoods();
        ordergoods.setGoodsId(goods.getId());
        ordergoods.setGoodsTitle(goods.getTitle());
        ordergoods.setGoodsCoverImageUri(goods.getCoverImageUri());
        ordergoods.setQuantity(quantity);
        ordergoods.setIsCommented(false);
        ordergoods.setUsername(username);
        orderGoodsList.add(ordergoods);
        
        Double goodsPrice;
        //电信活动价格设置
        if (req.getSession().getAttribute("dianxinhuodong") != null)
        {
        	Double price = (Double)req.getSession().getAttribute("huodongPrice");
        	ordergoods.setPrice(price);
            totalGoodsPrice = quantity * price;
            goodsPrice = price;
		}
        else
        {
        	ordergoods.setPrice(goods.getSalePrice());
        	totalGoodsPrice = quantity * goods.getSalePrice();
        	goodsPrice = goods.getSalePrice();
		}
        
        //优惠劵
        Long maxCouponId = 0L;
        Double maxPrice=0.0;

        if (null == orderGoodsList || orderGoodsList.size() <= 0)
        {
            return "/client/error_404";
        }


        TdOrder tdOrder = new TdOrder();

        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String curStr = sdf.format(current);
        Random random = new Random();

        // 预约时间
        if (null != appointmentTime && appointmentTime.length() > 0)
        {
        	//上午：2015-11-18T00:59
        	//下午：2015-11-20T12:59
        	appointmentTime = appointmentTime.replace('T', ' ');
        	
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 小写的mm表示的是分钟

            try
            {
                Date appTime = sdf.parse(appointmentTime);
                tdOrder.setAppointmentTime(appTime);
            }
            catch (ParseException e) 
            {
                e.printStackTrace();
            }
        }

        // 基本信息
        tdOrder.setUsername(tdUser.getUsername());
        tdOrder.setOrderTime(current);
        tdOrder.setRealName(realName);//姓名
        tdOrder.setMobile(shippingPhone);//电话
        tdOrder.setImgUrl(goods.getCoverImageUri());//图片
        tdOrder.setGoodsTitle(goods.getTitle());//商品title
        tdOrder.setQuantity(quantity);
        tdOrder.setGoodsub(goods.getSubTitle());
        
        if (maxCouponId != 0)
        {
			tdOrder.setCouponUse(maxPrice);
			tdOrder.setCouponid(maxCouponId);
		}
        
        if (tool != null)
        {
        	tdOrder.setChooseTool(tool);
		}
        
        // 订单号
        tdOrder.setOrderNumber("SX" + curStr + leftPad(Integer.toString(random.nextInt(999)), 3, "0"));

        // 用户留言
        tdOrder.setUserRemarkInfo(userRemarkInfo);

        // 待付款
        tdOrder.setStatusId(2L);

        // 需付尾款额
        tdOrder.setTotalLeftPrice(totalLeftPrice);

        // 订单商品
        tdOrder.setOrderGoodsList(orderGoodsList);
        
        if (totalGoodsPrice < 0)
        {
			totalGoodsPrice = 0.0;
		}
        
        tdOrder.setTotalGoodsPrice(totalGoodsPrice);
        tdOrder.setGoodsPrice(goodsPrice);
        tdOrder.setTotalPrice(totalGoodsPrice);

        // 保存订单商品及订单
        tdOrderGoodsService.save(orderGoodsList);

        tdOrder = tdOrderService.save(tdOrder);        
        
        // if (tdOrder.getIsOnlinePay()) {
        
        if (req.getSession().getAttribute("dianxinhuodong") !=null)
        {
			req.getSession().removeAttribute("dianxinhuodong");
			req.getSession().removeAttribute("huodongPrice");
			req.getSession().removeAttribute("huodongGoodsId");
		}
        
        return "redirect:/user/order";
        // }

        // return "redirect:/order/success?orderId=" + tdOrder.getId();
    }
    
    //zhangji 购物车结算
    @RequestMapping(value = "/editCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editCart(Long[] goodsIds, 
			 HttpServletRequest req) {
		Map<String, Object> res = new HashMap<String, Object>();

		res.put("code", 1);

		//必须登陆
        String username = (String) req.getSession().getAttribute("username");
        if(null == username)
        {
        	res.put("msg", "请先登陆");
        	res.put("login", 1);
        	return res;
        }
        
        //不允许空订单
        if(null == goodsIds || goodsIds.length == 0)
        {
        	res.put("msg", "请至少选择一件商品！");
        	return res;
        }

            //删掉购物车中未选择的
            List<TdCartGoods> selectedGoodsList = tdCartGoodsService
                    .findByUsername(username);
            
            for (TdCartGoods cartGoods : selectedGoodsList) {
            	cartGoods.setIsSelected(false);
            	
            	for (Long cartGoodsId : goodsIds){
            		if(cartGoods.getId() == cartGoodsId)
            		{
                        cartGoods.setIsSelected(true);
                        
            		}
            	}
            	
            	tdCartGoodsService.save(cartGoods);
            	
            	if(null == cartGoods.getIsSelected() || cartGoods.getIsSelected() == false)
            	{
            		tdCartGoodsService.delete(cartGoods);
            	}
            }
     
        	res.put("code", 0);
			return res;
	
	}

    @RequestMapping(value = "/info")
    public String orderInfo(HttpServletRequest req, HttpServletResponse resp,
            ModelMap map) {
        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
            return "redirect:/login";
        }

        // 把所有的购物车项转到该登陆用户下
        String sessionId = req.getSession().getId();

        List<TdCartGoods> cartGoodsList = tdCartGoodsService
                .findByUsername(sessionId);

        if (null != cartGoodsList && cartGoodsList.size() > 0) {
            for (TdCartGoods cartGoods : cartGoodsList) {
                cartGoods.setUsername(username);
                cartGoods.setIsLoggedIn(true);
            }
            tdCartGoodsService.save(cartGoodsList);
        }

        TdUser user = tdUserService.findByUsernameAndIsEnabled(username);

        if (null != user) {

	        user = tdUserService.findByUsernameAndIsEnabled(username);
	        map.addAttribute("user", user);
        }

        
        List<TdCartGoods> selectedGoodsList = tdCartGoodsService
                .findByUsernameAndIsSelectedTrue(username);

        Long totalPointLimited = 0L;// 积分限制综总和
        Double totalPrice = 0.0; // 购物总额

        // 积分限制总和 和 购物总额
        if (null != selectedGoodsList) {
            for (TdCartGoods cg : selectedGoodsList) {
                TdGoods goods = tdGoodsService.findOne(cg.getGoodsId());
                if (null != goods && null != goods.getPointLimited()) {
                    totalPointLimited += goods.getPointLimited()
                            * cg.getQuantity();
                    totalPrice += cg.getPrice() * cg.getQuantity();
                }
            }
        }

        // 查询购物车的所有种类
        List<String> productIds = new ArrayList<>();
        for (TdCartGoods cg : selectedGoodsList) {
            TdGoods goods = tdGoodsService.findOne(cg.getGoodsId());
            if (productIds.isEmpty()) {
                productIds.add(goods.getCategoryIdTree().split(",")[0]);// 根类别
            } else {
                if (!productIds
                        .contains(goods.getCategoryIdTree().split(",")[0])) {
                    productIds.add(goods.getCategoryIdTree().split(",")[0]);
                }
            }
        }
        /**
         * @author lc
         * @注释：优惠券 TODO: 满减券， 单品类券，普通券查找
         */
        // 如果有不同种类的商品则不能使用优惠券

//        // 积分限额
//        if (null != user.getTotalPoints()) {
//            if (totalPointLimited > user.getTotalPoints()) {
//                map.addAttribute("total_point_limit", user.getTotalPoints());
//            } else {
//                map.addAttribute("total_point_limit", totalPointLimited);
//            }
//        }

        // 支付方式列表
        setPayTypes(map, true, false, req);

        // 选中商品
        map.addAttribute("buy_goods_list", selectedGoodsList);
        
        Date curr = new Date();
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(curr);
        cal.add(Calendar.DATE, 1);
        
        map.addAttribute("tomorrow", cal.getTime());
        
        //区域列表
        map.addAttribute("area_list", tdAreaService.findByIsEnableTrueOrderBySortIdAsc());

        tdCommonService.setHeader(map, req);

        return "/client/order_info";
    }
    
    //提交订单 zhangji
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
	public Map<String,Object>  submit(
			String realName,
			Boolean sex,
			Long areaId,
			String address,
			String mobile,
			String idCard,
			String serviceTime,
			String userRemarkInfo,
			Long[] cartGoodsIds,
			Long[] quantities,
			Double pointD,
			String payTypeTitle,
			HttpServletRequest req, ModelMap map) {
	    Map<String, Object> res = new HashMap<String, Object>();
	    res.put("code", 1);
	    
		String username = (String) req.getSession().getAttribute("username");

        if (null == username) {
        	res.put("msg", "请先登录！");
        	res.put("login", 1);
            return res;
        }

        TdUser user = tdUserService.findByUsernameAndIsEnabled(username);

        if (null == user) {
        	res.put("msg", "用户不存在！");
        	res.put("login", 1);
            return res;
        }

        if(null == realName || realName.equals("")){
        	res.put("msg", "姓名不能为空！");
            return res;
        }
        
        if(null == address || address.equals("")){
        	res.put("msg", "地址不能为空！");
            return res;
        }
        
        if(null == serviceTime || serviceTime.equals("")){
        	res.put("msg", "体检时间不能为空！");
            return res;
        }
        
        if(null == cartGoodsIds || null ==quantities){
        	res.put("msg", "商品信息有误！");
            return res;
        }
        
		if (null == mobile || mobile.equals(""))
		{
			res.put("msg", "手机电话不能为空！");
			return res;
		}
		if(!isMobileNO(mobile))
		{
			res.put("msg", "手机号码格式不对！");
			return res;
		}
		
		if (null == idCard || idCard.equals(""))
		{
			res.put("msg", "身份证号不能为空！");
			return res;
		}
		if(!isIdCardNO(idCard))
		{
			res.put("msg", "身份证号格式不对！");
			return res;
		}
        
		double payTypeFee = 0.0;
//		double deliveryTypeFee = 0.0;
		 double pointFee = 0.0;
		// double couponFee = 0.0;

		// 购物车商品
		List<TdCartGoods> cartSelectedGoodsList = new ArrayList<TdCartGoods>();
		for (int i=0;i<cartGoodsIds.length;i++){
			TdCartGoods tdCartGoods = tdCartGoodsService.findOne(cartGoodsIds[i]);
			tdCartGoods.setQuantity(quantities[i]);
			tdCartGoodsService.save(tdCartGoods);
			
			cartSelectedGoodsList.add(tdCartGoods);
		}
			

		List<TdOrderGoods> orderGoodsList = new ArrayList<TdOrderGoods>();

		// 商品总价
		Double totalGoodsPrice = 0.0;

		// 购物车商品
		if (null != cartSelectedGoodsList) {
			for (TdCartGoods cartGoods : cartSelectedGoodsList) {
				if (cartGoods.getIsSelected()) {

					TdGoods goods = tdGoodsService.findOne(cartGoods
							.getGoodsId());

					// 不存在该商品或已下架，则跳过
					if (null == goods || !goods.getIsOnSale()) {
						continue;
					}

					TdOrderGoods orderGoods = new TdOrderGoods();

					// 商品信息
					orderGoods.setGoodsId(goods.getId());
					orderGoods.setGoodsTitle(goods.getTitle());
					orderGoods.setGoodsSubTitle(goods.getSubTitle());
					orderGoods.setGoodsCoverImageUri(goods.getCoverImageUri());

					// 是否已申请退货
					orderGoods.setIsReturnApplied(false);

					// 销售方式
					orderGoods.setGoodsSaleType(0);

					long quantity = 0;

					// 成交价
					orderGoods.setPrice(goods.getSalePrice());

					// 数量
//					quantity = Math.min(cartGoods.getQuantity(),
//							goods.getLeftNumber());
					
					quantity = cartGoods.getQuantity();
					orderGoods.setQuantity(quantity);

					// 获得积分
					// if (null != goods.getReturnPoints()) {
					// totalPointReturn += goods.getReturnPoints() * quantity;
					// orderGoods
					// .setPoints(goods.getReturnPoints() * quantity);
					// }

					// 商品总价
					totalGoodsPrice += cartGoods.getPrice()
							* cartGoods.getQuantity();

					orderGoodsList.add(orderGoods);

					// 更新销量
					Long soldNumber = goods.getSoldNumber();

					if (null == soldNumber) {
						soldNumber = 0L;
					}

					soldNumber += quantity;
					goods.setSoldNumber(soldNumber);

					/**
					 * @author lc
					 * @注释：更新库存
					 */
//					Long leftNumber = goods.getLeftNumber();
//					if (leftNumber >= quantity) {
//						leftNumber = leftNumber - quantity;
//					}
//					goods.setLeftNumber(leftNumber);

					// 保存商品
					tdGoodsService.save(goods);
				}
			}
		}

		if (null == orderGoodsList || orderGoodsList.size() <= 0) {
			res.put("msg", "商品数量不能为空！");
			return res;
		}

		TdOrder tdOrder = new TdOrder();

		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String curStr = sdf.format(current);
		Random random = new Random();
		SimpleDateFormat ssdf = new SimpleDateFormat("yyyy-MM-dd");
		Date serviceDate;
		try {
			serviceDate = ssdf.parse(serviceTime);
			tdOrder.setServiceTime(serviceDate);
		} catch (ParseException e) {
			System.out.println("时间格式不对！");
			e.printStackTrace();
		}
		// 基本信息
		tdOrder.setRealName(realName);
		tdOrder.setUsername(username);
		tdOrder.setUserId(user.getId());
		tdOrder.setOrderTime(current);
		tdOrder.setShopId(0L);
		tdOrder.setSex(sex);
		tdOrder.setAreaId(areaId);
		tdOrder.setAddress(address);
		tdOrder.setMobile(mobile);
		tdOrder.setIdCard(idCard);
		tdOrder.setPayTypeTitle(payTypeTitle);
		
		tdOrder.setUserRemarkInfo(userRemarkInfo);

		// 订单号
		tdOrder.setOrderNumber(curStr+"XWD" 
				+ leftPad(Integer.toString(random.nextInt(999)), 3, "0"));

//		if (null != payTypeId) {
//			TdPayType payType = tdPayTypeService.findOne(payTypeId);
//
//			// 支付类型
//			payTypeFee = payType.getFee();
//			tdOrder.setPayTypeId(payType.getId());
//			tdOrder.setPayTypeTitle(payType.getTitle());
//			tdOrder.setPayTypeFee(payTypeFee);
//			tdOrder.setIsOnlinePay(payType.getIsOnlinePay());
//		}
		//积分兑换
		Long usedPoint = 0L;
		if(null != pointD){
			usedPoint = (long)(pointD*100);
		}
		
		tdOrder.setPointUse(usedPoint);
		
		Long totalPoints = 0L;
		if(null != user.getTotalPoints())
		{
			totalPoints = user.getTotalPoints();
		}
		user.setTotalPoints(totalPoints - usedPoint);
		tdUserService.save(user);
		pointFee = -pointD;

		// 待付款
		tdOrder.setStatusId(2L);

		// 总价
		tdOrder.setTotalPrice(totalGoodsPrice + payTypeFee + pointFee );

		// 需付尾款额
//		tdOrder.setTotalLeftPrice(0.0);

		// 订单商品
		tdOrder.setOrderGoodsList(orderGoodsList);
		tdOrder.setTotalGoodsPrice(totalGoodsPrice);

		// 保存订单商品及订单
		tdOrderGoodsService.save(orderGoodsList);
		tdOrder = tdOrderService.save(tdOrder);

		// 删除已生成订单的购物车项
		tdCartGoodsService.delete(cartSelectedGoodsList);

//		if (tdOrder.getIsOnlinePay()) {
//			        	return "/client/order_status";
//			return "redirect:/order/dopay?orderId=" + tdOrder.getId();
//		}

		//         return "redirect:/order/success?orderId=" + tdOrder.getId();
//		map.addAttribute("order", tdOrder);
		
//		return "/client/order_status";
		
		res.put("orderNumber", tdOrder.getOrderNumber());
		res.put("money", tdOrder.getTotalPrice());
		if(null != payTypeTitle){
			if(payTypeTitle.equals("支付宝")){
				res.put("payType", 0);
			}else if(payTypeTitle.equals("微信")){
				res.put("payType", 1);
			}
		}
		res.put("code", 0);
		return res;
	}


    /**
     * 支付
     * 
     * @param orderId
     * @param map
     * @param req
     * @return
     */
    @RequestMapping(value = "/dopay")
    public String payOrder(Long orderId, ModelMap map,HttpServletRequest req,Long state,String code)
    {
        String username = (String) req.getSession().getAttribute("username");

        if (null == username)
        {
            return "redirect:/login";
        }
        TdUser user = tdUserService.findByUsername(username);

        tdCommonService.setHeader(map, req);

        String openId = "";
        if (null == state) 
        {
            return "/client/error_404";
        }
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
		}


        TdOrder order = tdOrderService.findOne(state);

        if (null == order)
        {
            return "/client/error_404";
        }

        // 根据订单类型来判断支付时间是否过期
        // 普通 订单提交后30分钟内
        Date cur = new Date();
        long temp = cur.getTime() - order.getOrderTime().getTime();
        if (temp > 1000 * 3600 / 2)
        {
        	order.setStatusId(7L);

        	tdOrderService.save(order);
        	return "redirect:/user/order";
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
		signMap.addAttribute("notify_url", "http://chuzi.peoit.com/order/wx_notify");
		signMap.addAttribute("trade_type", "JSAPI");
		signMap.addAttribute("openid", user.getOpenid());

		String mysign = Signature.getSign(signMap);

		String content = "<xml>\n" + "<appid>"
				+ Configure.getAppid()
				+ "</appid>\n"
				+ "<attach>订单支付</attach>\n"
				+ "<body>支付订单"
				+ order.getOrderNumber()
				+ "</body>\n"
				+ "<mch_id>"
				+ Configure.getMchid()
				+ "</mch_id>\n"
				+ "<nonce_str>"
				+ noncestr
				+ "</nonce_str>\n"
				+ "<notify_url>http://chuzi.peoit.com/order/wx_notify</notify_url>\n"
				+ "<out_trade_no>" + order.getOrderNumber() + "</out_trade_no>\n"
				+ "<spbill_create_ip>116.55.233.157</spbill_create_ip>\n"
				+ "<total_fee>" + Math.round(order.getTotalPrice() * 100)
				+ "</total_fee>\n" + "<trade_type>JSAPI</trade_type>\n"
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
				map.addAttribute("orderId", state);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        tdOrderService.save(order);

        map.addAttribute("payForm", payForm);

        return "/client/pay_order";
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
				TdOrder order = tdOrderService.findByOrderNumber(out_trade_no);

				if (null != order)
				{
					afterPaySuccess(order);
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
				if (request.getSession().getAttribute("dianxinhuodong") != null) 
				{
					String accessTokenUrl = "http://kq.hz023.com/GxDoubleEgg/ApiActivity/BuyReturn?telephone=" + request.getSession().getAttribute("username") + "&status=1&commondityId=" + request.getSession().getAttribute("huodongGoodsId");
					System.out.println("Madejing: accessTokenUrl = " + accessTokenUrl);
					String result = com.ynyes.xunwudao.util.HttpRequest.sendGet(accessTokenUrl, null);
					System.out.println("Madejing:->dianxinfanhui:" +result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
    }

    /**
     * 支付尾款
     * 
     * @param orderId
     * @param map
     * @param req
     * @return
     */
//    @RequestMapping(value = "/dopayleft/{orderId}")
//    public String payOrderLeft(@PathVariable Long orderId, ModelMap map,
//            HttpServletRequest req) {
//        String username = (String) req.getSession().getAttribute("username");
//
//        if (null == username) {
//            return "redirect:/login";
//        }
//
//        tdCommonService.setHeader(map, req);
//
//        if (null == orderId) {
//            return "/client/error_404";
//        }
//
//        TdOrder order = tdOrderService.findOne(orderId);
//
//        if (null == order) {
//            return "/client/error_404";
//        }
//        // 根据订单类型来判断支付时间是否过期
//        if (order.getTypeId().equals(3L)) { // 抢拍 订单提交后20分钟内
//            Date cur = new Date();
//            long temp = cur.getTime() - order.getOrderTime().getTime();
//            // System.out.println(temp);
//            if (temp > 1000 * 60 * 20) {
//                return "/client/overtime";
//            }
//        } else if (order.getTypeId().equals(4L) || order.getTypeId().equals(5L)) { // 团购
//        	
//        	TdGoods tdGoods = tdGoodsService.findOne(order.getOrderGoodsList().get(0).getGoodsId());
//        	
//        	long temp = 0L;
//        	Date cur = new Date();
//        	// 预付是订单提交后12小时内，尾款是团购时间结束后24小时
//            if (order.getTypeId().equals(4L) && null != tdGoods.getGroupSaleStopTime()) {
//            	temp =cur.getTime() - tdGoods.getGroupSaleStopTime().getTime() ;
//			}else {
//				if (null != tdGoods.getGroupSaleHundredStopTime()) {
//					temp = cur.getTime() - tdGoods.getGroupSaleHundredStopTime().getTime();
//				}				
//			}            
//            if (temp > 1000 * 3600 * 24) {
//                return "/client/overtime";
//            }
//        } else { // 普通 订单提交后24小时内
//            Date cur = new Date();
//            long temp = cur.getTime() - order.getOrderTime().getTime();
//            if (temp > 1000 * 3600 * 24) {
//                return "/client/overtime";
//            }
//        }
//
//        // 待付尾款
//        if (!order.getStatusId().equals(3L)) {
//            return "/client/error_404";
//        }
//
//        String amount = order.getTotalLeftPrice().toString();
//        req.setAttribute("totalPrice", amount);
//
//        String payForm = "";
//
//        Long payId = order.getPayTypeId();
//        TdPayType payType = tdPayTypeService.findOne(payId);
//        if (payType != null) {
//            TdPayRecord record = new TdPayRecord();
//            record.setCreateTime(new Date());
//            record.setOrderId(order.getId());
//            record.setPayTypeId(payType.getId());
//            record.setStatusCode(1);
//            record.setCreateTime(new Date());
//            record = payRecordService.save(record);
//
//            String payRecordId = record.getId().toString();
//            int recordLength = payRecordId.length();
//            if (recordLength > 6) {
//                payRecordId = payRecordId.substring(recordLength - 6);
//            } else {
//                payRecordId = leftPad(payRecordId, 6, "0");
//            }
//            req.setAttribute("payRecordId", payRecordId);
//
//            req.setAttribute("orderNumber", order.getOrderNumber());
//
//            String payCode = payType.getCode();
//            if (PAYMENT_ALI.equals(payCode)) {
//                payForm = paymentChannelAlipay.getPayFormData(req);
//                map.addAttribute("charset", AlipayConfig.CHARSET);
//            }else if (PAYMENT_WX.equals(payCode)) {
//                map.addAttribute("order_number", order.getOrderNumber());
//                map.addAttribute("total_price", order.getTotalLeftPrice());
//
//                String sa = "appid=" + Configure.getAppid() + "&mch_id="
//                        + Configure.getMchid() + "&nonce_str="
//                        + RandomStringGenerator.getRandomStringByLength(32)
//                        + "&product_id=" + order.getId() + "&time_stamp="
//                        + System.currentTimeMillis() / 1000;
//
//                String sign = MD5.MD5Encode(
//                        sa + "&key=192006250b4c09247ec02edce69f6acy")
//                        .toUpperCase();
//
//                System.out.print("Sharon: weixin://wxpay/bizpayurl?" + sa
//                        + "&sign=" + sign + "\n");
//
//                req.getSession().setAttribute("WXPAYURLSESSEION",
//                        "weixin://wxpay/bizpayurl?" + sa + "&sign=" + sign);
//                // "weixin://wxpay/bizpayurl?appid=wx2421b1c4370ec43b&mch_id=10000100&nonce_str=f6808210402125e30663234f94c87a8c&product_id=1&time_stamp=1415949957&sign=512F68131DD251DA4A45DA79CC7EFE9D");
//                return "/client/order_pay_wx";
//            }
//            else {
//                // 其他目前未实现的支付方式
//                return "/client/error_404";
//            }
//        } else {
//            return "/client/error_404";
//        }
//
//        tdOrderService.save(order);
//
//        map.addAttribute("payForm", payForm);
//
//        return "/client/order_pay_form";
//    }

    @RequestMapping(value = "/pay/success")
    public String paySuccess(ModelMap map, HttpServletRequest req) {
        // String username = (String) req.getSession().getAttribute("username");
        //
        // if (null == username) {
        // return "redirect:/login";
        // }

        tdCommonService.setHeader(map, req);

        return "/client/order_pay_success";
    }

    @RequestMapping(value = "/pay/notify")
    public String payNotify(ModelMap map, HttpServletRequest req) {
        // String username = (String) req.getSession().getAttribute("username");
        //
        // if (null == username) {
        // return "redirect:/login";
        // }

        tdCommonService.setHeader(map, req);

        return "/client/order_pay";
    }

    /*
     * 
     */
//    @RequestMapping(value = "/pay/notify_alipay")
//    public void payNotifyAlipay(ModelMap map, HttpServletRequest req,
//            HttpServletResponse resp) {
//    	paymentChannelAlipay.doResponse(req, resp);
//    }

    /*
     * 
     */
    @RequestMapping(value = "/pay/notify_cebpay")
    public void payNotifyCEBPay(ModelMap map, HttpServletRequest req,
            HttpServletResponse resp) {
    }

    /*
     * 
     */
//    @RequestMapping(value = "/pay/result_alipay")
//    public String payResultAlipay(Device device, ModelMap map, HttpServletRequest req,
//            HttpServletResponse resp) {
//        Map<String, String> params = new HashMap<String, String>();
//        Map<String, String[]> requestParams = req.getParameterMap();
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter
//                .hasNext();) {
//            String name = iter.next();
//            String[] values = requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            try {
//                valueStr = new String(valueStr.getBytes("ISO-8859-1"),
//                        AlipayConfig.CHARSET);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            params.put(name, valueStr);
//        }
//
//        // 获取支付宝的返回参数
//        String orderNo = "";
//        String trade_status = "";
//        try {
//            // 商户订单号
//            orderNo = new String(req.getParameter(Constants.KEY_OUT_TRADE_NO)
//                    .getBytes("ISO-8859-1"), AlipayConfig.CHARSET);
//            // 交易状态
//            trade_status = new String(req.getParameter("trade_status")
//                    .getBytes("ISO-8859-1"), AlipayConfig.CHARSET);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        // 计算得出通知验证结果
//        boolean verify_result = AlipayNotify.verify(params);
//
//        tdCommonService.setHeader(map, req);
//        orderNo = (orderNo == null) ? "" : (orderNo.length() < 6) ? orderNo
//                : orderNo.substring(0, orderNo.length() - 6);
//        TdOrder order = tdOrderService.findByOrderNumber(orderNo);
//        if (order == null) {
//            // 订单不存在
//        	// 触屏
//            if (device.isMobile() || device.isTablet()) {
//                return "/touch/order_pay_failed";
//            }
//            return "/client/order_pay_failed";
//        }
//        map.put("order", order);
//        if (verify_result) {// 验证成功
//            if ("WAIT_SELLER_SEND_GOODS".equals(trade_status)) {
//
//                // 订单支付成功
//                afterPaySuccess(order);
//                // 触屏
//                if (device.isMobile() || device.isTablet()) {
//                    return "/touch/order_pay_success";
//                }
//                return "/client/order_pay_success";
//            }
//        }
//
//        // 验证失败或者支付失败
//        // 触屏
//        if (device.isMobile() || device.isTablet()) {
//            return "/touch/order_pay_failed";
//        }
//        return "/client/order_pay_failed";
//    }

//    @RequestMapping(value = "/pay/result_wxpay")
//    public String payResultWxpay(Device device, String productid, String openid, ModelMap map,
//            HttpServletRequest req, HttpServletResponse resp) {
//
//        Map<String, String> params = new HashMap<String, String>();
//        Map<String, String[]> requestParams = req.getParameterMap();
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter
//                .hasNext();) {
//            String name = iter.next();
//            String[] values = requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            try {
//                valueStr = new String(valueStr.getBytes("ISO-8859-1"),
//                        AlipayConfig.CHARSET);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            params.put(name, valueStr);
//        }
//
//        // 获取支付宝的返回参数
//        String orderNo = "";
//        String trade_status = "";
//        try {
//            // 商户订单号
//            orderNo = new String(req.getParameter(Constants.KEY_OUT_TRADE_NO)
//                    .getBytes("ISO-8859-1"), AlipayConfig.CHARSET);
//            // 交易状态
//            trade_status = new String(req.getParameter("trade_status")
//                    .getBytes("ISO-8859-1"), AlipayConfig.CHARSET);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        // 计算得出通知验证结果
//        boolean verify_result = AlipayNotify.verify(params);
//
//        tdCommonService.setHeader(map, req);
//
//        orderNo = (orderNo == null) ? "" : (orderNo.length() < 6) ? orderNo
//                : orderNo.substring(0, orderNo.length() - 6);
//        TdOrder order = tdOrderService.findByOrderNumber(orderNo);
//        if (order == null) {
//            // 订单不存在
//        	 // 触屏
//            if (device.isMobile() || device.isTablet()) {
//                return "/touch/order_pay_failed";
//            }
//            return "/client/order_pay_failed";
//            
//        }
//
//        map.put("order", order);
//
//        if (verify_result) {// 验证成功
//            if ("WAIT_SELLER_SEND_GOODS".equals(trade_status)) {
//
//                // 订单支付成功
//                afterPaySuccess(order);
//                // 触屏
//                if (device.isMobile() || device.isTablet()) {
//                    return "/touch/order_pay_success";
//                }
//                return "/client/order_pay_success";
//            }
//        }
//
//        // 验证失败或者支付失败
//        // 触屏
//        if (device.isMobile() || device.isTablet()) {
//            return "/touch/order_pay_failed";
//        }
//        return "/client/order_pay_failed";
//    }

    /*
     * 
     */


    /*
     * 
     */
    @RequestMapping(value = "/change_paymethod", method = { RequestMethod.POST })
    public @ResponseBody Map<String, String> changePaymentMethod(Long orderId,
            Long paymentMethodId, ModelMap map, HttpServletRequest req) {
        String username = (String) req.getSession().getAttribute("username");
        Map<String, String> result = new HashMap<String, String>();
        result.put("status", "F");
        if (null == username) {
            result.put("message", "请先登录！");
            return result;
        }

        if (null == orderId) {
            result.put("message", "订单Id非法！");
            return result;
        }

        if (null == paymentMethodId) {
            result.put("message", "支付方式非法！");
            return result;
        }

        TdOrder order = tdOrderService.findOne(orderId);

        if (null == order) {
            result.put("message", "不存在的订单信息！");
            return result;
        }

        TdPayType payType = tdPayTypeService.findOne(paymentMethodId);
        if (null == payType) {
            result.put("message", "不存在的支付方式信息！");
            return result;
        }

        if (!order.getStatusId().equals(2L) && !order.getStatusId().equals(3L)) {
            result.put("message", "订单不能修改支付方式！");
            return result;
        }

        if (payType.getIsEnable()) {
            result.put("message", "所选的支付方式暂不支持，请选择其他支付方式！");
        }

        Double payTypeFee = payType.getFee();
        payTypeFee = payTypeFee == null ? 0.0 : payTypeFee;

        double goodPrice = order.getTotalGoodsPrice();
        /*
         * 订单金额=商品总额+支付手续费+运费-优惠券金额-积分抵扣金额 优惠券金额+积分抵扣金额=商品总额+支付手续费+运费-订单金额
         */
        Double orgPayTypeFee = order.getPayTypeFee();
        orgPayTypeFee = orgPayTypeFee == null ? 0.0 : orgPayTypeFee;
        double couponAndPointsFee = goodPrice + orgPayTypeFee 
                - order.getTotalPrice();

        /*
         * 按百分比收取手续费,手续费重新计算(商品总额*百分比)
         */
        if (payType.getIsFeeCountByPecentage()) {
            payTypeFee = goodPrice * payTypeFee / 100;
        }

        order.setTotalPrice(goodPrice + payTypeFee 
                - couponAndPointsFee);
        order.setPayTypeFee(payTypeFee);
        order.setPayTypeId(payType.getId());
        order.setPayTypeTitle(payType.getTitle());
        order.setIsOnlinePay(payType.getIsOnlinePay());

        tdOrderService.save(order);

        result.put("status", "S");
        result.put("message", "订单支付方式修改成功！");
        return result;
    }
    
    //检验手机号
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(0|86|17951|[0-9]{3})?([0-9]{8})|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		}
	//检验身份证号
	public boolean isIdCardNO(String idCard) {
		Pattern p = Pattern.compile("([0-9]{17}([0-9]|X))|([0-9]{15})");
		Matcher m = p.matcher(idCard);
		return m.matches();
		}
    /**
     * 订单支付成功后步骤
     * 
     * @param tdOrder
     *            订单
     */
    private void afterPaySuccess(TdOrder tdOrder) {
    	if (null == tdOrder) 
    	{
    		return;
    	}

    	// 用户
    	TdUser tdUser = tdUserService.findByUsername(tdOrder.getUsername());

    	if (tdOrder.getStatusId().equals(2L)
    			&& !tdOrder.getTotalLeftPrice().equals(0)) 
    	{
    		// 待付尾款
    		tdOrder.setPayTime(new Date());
    		tdOrder.setStatusId(4L);
    		tdOrder = tdOrderService.save(tdOrder);
    		return;
    	} 
    	else
    	{
    		// 待发货
    		tdOrder.setStatusId(4L);
    		tdOrder = tdOrderService.save(tdOrder);
    	}

    	//    			// 给用户发送短信
    	//    			if (null != tdUser)
    	//    			{
    	//    				SMSUtil.send(
    	//    						tdOrder.getShippingPhone(),
    	//    						"29040",
    	//    						new String[] {
    	//    								tdUser.getUsername(),
    	//    								tdOrder.getOrderGoodsList().get(0).getGoodsTitle(),
    	//    								tdOrder.getOrderNumber().substring(
    	//    										tdOrder.getOrderNumber().length() - 4) });
    	//    				System.out.println("---Sharon---: 向用户" + tdOrder.getShippingPhone()
    	//    				+ "发送短信");
    	//    			}

//    	List<TdOrderGoods> tdOrderGoodsList = tdOrder.getOrderGoodsList();
//
//    	Long totalPoints = 0L;
//    	Double totalCash = 0.0;

    	// 返利总额
//    	if (null != tdOrderGoodsList) {
//    		for (TdOrderGoods tog : tdOrderGoodsList) {
//    			if (0 == tog.getGoodsSaleType()) // 正常销售
//    			{
//    				TdGoods tdGoods = tdGoodsService.findOne(tog.getGoodsId());
//
//    				if (null != tdGoods && null != tdGoods.getReturnPoints()) {
//    					totalPoints += tdGoods.getReturnPoints();
//
//    					if (null != tdGoods.getShopReturnRation()) {
//    						totalCash = tdGoods.getSalePrice() * tdGoods.getShopReturnRation();
//    					}
//    				}
//    			}
//    		}
//    	}
    }
}
