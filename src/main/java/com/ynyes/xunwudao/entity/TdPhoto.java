package com.ynyes.xunwudao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 证件照/营业执照
 * @author Zhangji
 * 2016-1-9 12:14:55
 */

@Entity
public class TdPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    //用户id
    @Column
    private Long userId;
    
    // 名称
    @Column
    private String title;
  
    //状态id（进度）【0】单个上传待确认【1】单个上传确认；【2】上传完成；
    @Column
    private Long statusId;
    
    //图片
    @Column
    private String imgUrl;
    
    //上传时间
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    
    // 类型id 【0】企业；【1】会计
    @Column
    private Long phtoType;

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

	public Long getPhtoType() {
		return phtoType;
	}

	public void setPhtoType(Long phtoType) {
		this.phtoType = phtoType;
	}
    

	
}
  