package com.ynyes.xunwudao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 票据类别
 * @author Zhangji
 */

@Entity
public class TdApplyType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // 排序
    @Column
    private Long  sortId;
    
    // 名称
    @Column
    private String title;
  
    // 状态
    @Column
    private Boolean isEnable;
    
    // 【类型】（会计代理会多一个选项）【0】一般类型；【1】特殊类型（会计），多一个公司类型选项
    @Column
    private Long  spAcc;
    
    // 简介
    @Column
    private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSortId() {
		return sortId;
	}

	public void setSortId(Long sortId) {
		this.sortId = sortId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Long getSpAcc() {
		return spAcc;
	}

	public void setSpAcc(Long spAcc) {
		this.spAcc = spAcc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
 
}
  