package com.ynyes.xunwudao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单
 *
 * 记录了订单详情
 * 
 * @author Sharon
 *
 */

@Entity
public class TdOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column
    private Long couponid;
    
    //商品描述
    @Column
    private String goodsub;
    
    @Column
    private Long quantity;
    
    //体检人姓名
    @Column
    private String realName;
    
    //体检人性别
    @Column
    private Boolean sex;
    
    //单价
    @Column(scale=2)
    private Double GoodsPrice; 
    
    @Column 
    private String chooseTool;
    
    // 订单号
    @Column(unique=true)
    private String orderNumber;
    
    // 订单商品
    @OneToMany
    @JoinColumn(name="tdOrderId")
    private List<TdOrderGoods> orderGoodsList;
    
    //能否使用优惠劵
    @Column
    private boolean isCupon;
    
    //地区id
    @Column
    private Long areaId;
    
    //地址
    @Column
    private String address;
    
    //套餐图片url
    @Column
    private String ImgUrl;

    //套餐名称
    @Column 
    private String goodsTitle;
    
    //经办人
    @Column
    private String operator;
      
    // 收货电话
    @Column
    private String mobile;

    //身份证
    @Column
    private String idCard;
    
    // 线下同盟店
    @Column
    private Long shopId;
    
    // 用户id
    @Column
    private Long userId;
    
    // 同盟店名称
    @Column
    private String shopTitle;
    
    // 支付方式
    @Column
    private Long payTypeId;
    
    // 支付方式名称
    @Column
    private String payTypeTitle;
    
    // 支付方式手续费
    @Column(scale=2)
    private Double payTypeFee;
    
    // 用户留言备注
    @Column
    private String userRemarkInfo;
    
    // 后台备注
    @Column
    private String remarkInfo;
    
    // 是否索要发票
    @Column
    private Boolean isNeedInvoice;
    
    // 发票抬头
    @Column
    private String invoiceTitle;
    
    // 发票内容
    @Column
    private String invoiceContent;
    
    // 发票类型
    @Column
    private String invoiceType;
    
    // 预约服务时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;
    
    // 下单时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    
    // 取消时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;
    
    // 确认时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    
    // 付款时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    
    // 付尾款时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payLeftTime;
    
    // 配送时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;
    
    // 体检时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date serviceTime;
    
    // 收货时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    
    // 评价时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date commentTime;
    
    // 完成时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    
    // 订单状态 2:待付款 3:待付尾款 4:待服务 5:待评价 6: 已完成 7: 已取消 8: 支付取消(失败) 9: 已删除
    @Column
    private Long statusId;
    
    // 订单类型 1：普通订单 2：组合购买订单 3：抢购订单 4：十人团订单  5：百人团订单
    @Column
    private Long typeId;
    
    // 订单取消原因
    @Column
    private String cancelReason;
    
    // 用户
    @Column
    private String username;
    
    // 发货时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    
    //验证码
    @Column
    private String smscode;
    
    // 商品总金额
    @Column(scale=2)
    private Double totalGoodsPrice;
    
    // 订单总金额
    @Column(scale=2)
    private Double totalPrice;
    
    // 修改总金额备注
    @Column
    private String totalPriceChangeInfo;
    
    // 修改支付手续费备注
    @Column
    private String payTypePriceChangeInfo;
    
    // 修改配送手续费备注
    @Column
    private String deliverTypePriceChangeInfo;
    
    // 订单尾款总金额，有些订单需付尾款
    @Column(scale=2)
    private Double totalLeftPrice;
    
    // 排序号
    @Column
    private Long sortId;
    
    // 使用积分数 
    @Column
    private Long pointUse;
    
    //是否已返粮草
    private Boolean isReturnPoints;
    
    // 可获取积分
    @Column
    private Long points;
    
    // 使用优惠券抵用额度
    @Column
    private Double couponUse;
    
    @Column
    private String couponTitle;
    
    // 是否在线付款
    @Column
    private Boolean isOnlinePay;
    
    // 是否申请售后
    @Column
    private Boolean isReturn;
    
    //商城收入
    @Column
    private Double platformService;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCouponid() {
		return couponid;
	}

	public void setCouponid(Long couponid) {
		this.couponid = couponid;
	}

	public String getGoodsub() {
		return goodsub;
	}

	public void setGoodsub(String goodsub) {
		this.goodsub = goodsub;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Double getGoodsPrice() {
		return GoodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		GoodsPrice = goodsPrice;
	}

	public String getChooseTool() {
		return chooseTool;
	}

	public void setChooseTool(String chooseTool) {
		this.chooseTool = chooseTool;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<TdOrderGoods> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<TdOrderGoods> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	public boolean isCupon() {
		return isCupon;
	}

	public void setCupon(boolean isCupon) {
		this.isCupon = isCupon;
	}

	public String getImgUrl() {
		return ImgUrl;
	}

	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public Long getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Long payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayTypeTitle() {
		return payTypeTitle;
	}

	public void setPayTypeTitle(String payTypeTitle) {
		this.payTypeTitle = payTypeTitle;
	}

	public Double getPayTypeFee() {
		return payTypeFee;
	}

	public void setPayTypeFee(Double payTypeFee) {
		this.payTypeFee = payTypeFee;
	}

	public String getUserRemarkInfo() {
		return userRemarkInfo;
	}

	public void setUserRemarkInfo(String userRemarkInfo) {
		this.userRemarkInfo = userRemarkInfo;
	}

	public String getRemarkInfo() {
		return remarkInfo;
	}

	public void setRemarkInfo(String remarkInfo) {
		this.remarkInfo = remarkInfo;
	}

	public Boolean getIsNeedInvoice() {
		return isNeedInvoice;
	}

	public void setIsNeedInvoice(Boolean isNeedInvoice) {
		this.isNeedInvoice = isNeedInvoice;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Date getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getPayLeftTime() {
		return payLeftTime;
	}

	public void setPayLeftTime(Date payLeftTime) {
		this.payLeftTime = payLeftTime;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSmscode() {
		return smscode;
	}

	public void setSmscode(String smscode) {
		this.smscode = smscode;
	}

	public Double getTotalGoodsPrice() {
		return totalGoodsPrice;
	}

	public void setTotalGoodsPrice(Double totalGoodsPrice) {
		this.totalGoodsPrice = totalGoodsPrice;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getTotalPriceChangeInfo() {
		return totalPriceChangeInfo;
	}

	public void setTotalPriceChangeInfo(String totalPriceChangeInfo) {
		this.totalPriceChangeInfo = totalPriceChangeInfo;
	}

	public String getPayTypePriceChangeInfo() {
		return payTypePriceChangeInfo;
	}

	public void setPayTypePriceChangeInfo(String payTypePriceChangeInfo) {
		this.payTypePriceChangeInfo = payTypePriceChangeInfo;
	}

	public String getDeliverTypePriceChangeInfo() {
		return deliverTypePriceChangeInfo;
	}

	public void setDeliverTypePriceChangeInfo(String deliverTypePriceChangeInfo) {
		this.deliverTypePriceChangeInfo = deliverTypePriceChangeInfo;
	}

	public Double getTotalLeftPrice() {
		return totalLeftPrice;
	}

	public void setTotalLeftPrice(Double totalLeftPrice) {
		this.totalLeftPrice = totalLeftPrice;
	}

	public Long getSortId() {
		return sortId;
	}

	public void setSortId(Long sortId) {
		this.sortId = sortId;
	}

	public Long getPointUse() {
		return pointUse;
	}

	public void setPointUse(Long pointUse) {
		this.pointUse = pointUse;
	}

	public Boolean getIsReturnPoints() {
		return isReturnPoints;
	}

	public void setIsReturnPoints(Boolean isReturnPoints) {
		this.isReturnPoints = isReturnPoints;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public Double getCouponUse() {
		return couponUse;
	}

	public void setCouponUse(Double couponUse) {
		this.couponUse = couponUse;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}

	public Boolean getIsOnlinePay() {
		return isOnlinePay;
	}

	public void setIsOnlinePay(Boolean isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
	}

	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public Double getPlatformService() {
		return platformService;
	}

	public void setPlatformService(Double platformService) {
		this.platformService = platformService;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    
    
   
}
