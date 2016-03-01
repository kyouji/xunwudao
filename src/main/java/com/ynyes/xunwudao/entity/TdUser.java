package com.ynyes.xunwudao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 用户
 * 
 * 储存所有用户信息
 * 
 * @author Sharon
 *
 */

@Entity
public class TdUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	// 头像
	@Column
	private String headImageUrl;
	
	// 证件照
	@Column
	private String paperImageUri;
	
	// 用户名
	@Column(nullable=false, unique=true)
	private String username;
	
	// 性别
	@Column
	private Boolean sex;
    
	// 状态
    @Column
    private Long statusId;
	
	// 昵称
    @Column
    private String nickname;
	
	// 密码
	@Column(nullable=false)
	private String password;
	
	// 真实姓名
	@Column
	private String realName;
	
	//积分额度
	@Column
	private Long totalPoints;
	
	//地址
	@Column
	private String address;
	
	//企业名称
	@Column
	private String enterName;
	
	//企业类型
	@Column
	private String enterType;
	
	//企业类型id
	@Column
	private Long enterTypeId;

	// 角色 
    @Column
    private Long roleId;
    
    //有无未处理票据 2016-1-14 15:05:13 【0】无；【1】有
    @Column
    private Long isDeal;
    
	// 注册时间
	@Column
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date registerTime;
	
	// 最后登录时间
	@Column
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;
	
	// 手机号码
	@Column
	private String mobile;
	
	// 电子邮箱
	@Column
	private String email;
    
    // 排序号
    @Column
    private Long sortId;
    
    //推荐用户id（上一级）
    @Column
    private Long upUserOne;
    
    //推荐用户id（上两级）
    @Column
    private Long UpUserTwo;
    
    //总消费额  
    @Column
    private Double spend;
    
    //编号
    @Column
    private String number;
    
    //微信标识
    @Column
    private String openid;
   


	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getHeadImageUrl() {
		return headImageUrl;
	}





	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}





	public String getPaperImageUri() {
		return paperImageUri;
	}





	public void setPaperImageUri(String paperImageUri) {
		this.paperImageUri = paperImageUri;
	}





	public String getUsername() {
		return username;
	}





	public void setUsername(String username) {
		this.username = username;
	}





	public Boolean getSex() {
		return sex;
	}





	public void setSex(Boolean sex) {
		this.sex = sex;
	}





	public Long getStatusId() {
		return statusId;
	}





	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}





	public String getNickname() {
		return nickname;
	}





	public void setNickname(String nickname) {
		this.nickname = nickname;
	}





	public String getPassword() {
		return password;
	}





	public void setPassword(String password) {
		this.password = password;
	}





	public String getRealName() {
		return realName;
	}





	public void setRealName(String realName) {
		this.realName = realName;
	}





	public Long getTotalPoints() {
		return totalPoints;
	}





	public void setTotalPoints(Long totalPoints) {
		this.totalPoints = totalPoints;
	}





	public String getAddress() {
		return address;
	}





	public void setAddress(String address) {
		this.address = address;
	}





	public String getEnterName() {
		return enterName;
	}





	public void setEnterName(String enterName) {
		this.enterName = enterName;
	}





	public String getEnterType() {
		return enterType;
	}





	public void setEnterType(String enterType) {
		this.enterType = enterType;
	}





	public Long getEnterTypeId() {
		return enterTypeId;
	}





	public void setEnterTypeId(Long enterTypeId) {
		this.enterTypeId = enterTypeId;
	}





	public Long getRoleId() {
		return roleId;
	}





	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}





	public Long getIsDeal() {
		return isDeal;
	}





	public void setIsDeal(Long isDeal) {
		this.isDeal = isDeal;
	}





	public Date getRegisterTime() {
		return registerTime;
	}





	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}





	public Date getLastLoginTime() {
		return lastLoginTime;
	}





	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}





	public String getMobile() {
		return mobile;
	}





	public void setMobile(String mobile) {
		this.mobile = mobile;
	}





	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public Long getSortId() {
		return sortId;
	}





	public void setSortId(Long sortId) {
		this.sortId = sortId;
	}





	public Long getUpUserOne() {
		return upUserOne;
	}





	public void setUpUserOne(Long upUserOne) {
		this.upUserOne = upUserOne;
	}





	public Long getUpUserTwo() {
		return UpUserTwo;
	}





	public void setUpUserTwo(Long upUserTwo) {
		UpUserTwo = upUserTwo;
	}





	public String getNumber() {
		return number;
	}





	public void setNumber(String number) {
		this.number = number;
	}





	public String getOpenid() {
		return openid;
	}





	public void setOpenid(String openid) {
		this.openid = openid;
	}





	public Double getSpend() {
		return spend;
	}





	public void setSpend(Double spend) {
		this.spend = spend;
	}





	@Override
	public String toString() {
		return "TdUser [id=" + id + ", lastLoginTime=" + lastLoginTime + "]";
	}
}
