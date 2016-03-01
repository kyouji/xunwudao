package com.ynyes.xunwudao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 票据
 * @author Zhangji
 * 2016-1-5 9:48:25
 */

@Entity
public class TdBill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    //用户id
    @Column
    private Long userId;
    
    // 名称
    @Column
    private String title;
  
    //状态id（进度）【0】单个上传待确认【1】单个上传确认；【2】上传完成；【3】票据已下载（处理中）；【4】财务处理；【5】税费扣缴；【6】财务状况表
    @Column
    private Long statusId;
    
    //图片
    @Column
    private String imgUrl;
    
    //上传时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    
    //处理时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    
    // 票据类型id
    @Column
    private Long billTypeId;
    
    // 备注
    @Column
    private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getBillTypeId() {
		return billTypeId;
	}

	public void setBillTypeId(Long billTypeId) {
		this.billTypeId = billTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

}
  