package com.ynyes.xunwudao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class TdAccessToken {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column
	private String access_token;
	
	@Column
    private String access_expires_in;

	@Column
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date access_updateTime; 
	
	@Column
	private String jsapi_ticket;
	
	@Column
    private String jsapi_ticket_expires_in;

	@Column
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date jsapi_ticket_updateTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAccess_expires_in() {
		return access_expires_in;
	}

	public void setAccess_expires_in(String access_expires_in) {
		this.access_expires_in = access_expires_in;
	}

	public Date getAccess_updateTime() {
		return access_updateTime;
	}

	public void setAccess_updateTime(Date access_updateTime) {
		this.access_updateTime = access_updateTime;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public String getJsapi_ticket_expires_in() {
		return jsapi_ticket_expires_in;
	}

	public void setJsapi_ticket_expires_in(String jsapi_ticket_expires_in) {
		this.jsapi_ticket_expires_in = jsapi_ticket_expires_in;
	}

	public Date getJsapi_ticket_updateTime() {
		return jsapi_ticket_updateTime;
	}

	public void setJsapi_ticket_updateTime(Date jsapi_ticket_updateTime) {
		this.jsapi_ticket_updateTime = jsapi_ticket_updateTime;
	}

	
}
