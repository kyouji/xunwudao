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
 * 财务状况
 * @author Zhangji
 */

@Entity
public class TdFinance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // 用户名
    @Column
    private Long  userId;
    
    // 排序
    @Column
    private Long  sortId;
    
	//账面库存
	@OneToMany
	@JoinColumn(name = "masterId")
	private List<TdStock> stockList;
    
    //时间（月份）
    @Column
    @DateTimeFormat(pattern="yyyy-MM")
    private Date time;
    
    //附件
    @Column
    private String fileUrl;
    
    // 补充说明
    @Column
    private String content;
    
    // 备注
    @Column
    private String remark;
    
    // 本月不含税收入
    @Column
    private Double noTax;
    
    //累计收入
    @Column
    private Double totalIncome;
    
    //本月利润
    @Column
    private Double profit;
    
    //累计利润
    @Column
    private Double totalProfit;
    
    //累计毛利率
    @Column
    private Double totalGross;
    
    //本年累计上缴增值税
    @Column
    private Double totalDeduction;
    
    //本年累计上缴所得税
    @Column
    private Double totalIncomeTax;
    
    /*---------------------------------------------------------
    ============== 一般纳税人==============
    ---------------------------------------------------------*/
    
    //本月留抵税金
    @Column
    private Double taxRetention;
    
    //折合为价税合计
    @Column
    private Double valorem;
    
    //税负
    @Column
    private String taxBear;
    
    //本月待抵扣税金
    @Column
    private Double todo;
    
    //折合为价税合计
    @Column
    private Double todoValorem;
    
    //未收抵扣联，数量
    @Column
    private Double todoAmount;
    
    //未收抵扣联，最早日期
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date todoEarlyDate;
    
    //已收抵扣联，数量
    @Column
    private Double doneAmount;
    
    //已收抵扣联，最早日期
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date doneEarlyDate;
    
    
    
    /*---------------------------------------------------------
    ============= 小规模纳税人==============
    ---------------------------------------------------------*/
    
    //最高可开票金额
    @Column
    private Double maxTicket;
    
    //提示小消息
    @Column
    private String tip;

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

	public Long getSortId() {
		return sortId;
	}

	public void setSortId(Long sortId) {
		this.sortId = sortId;
	}

	public List<TdStock> getStockList() {
		return stockList;
	}

	public void setStockList(List<TdStock> stockList) {
		this.stockList = stockList;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getNoTax() {
		return noTax;
	}

	public void setNoTax(Double noTax) {
		this.noTax = noTax;
	}

	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}

	public Double getTotalGross() {
		return totalGross;
	}

	public void setTotalGross(Double totalGross) {
		this.totalGross = totalGross;
	}

	public Double getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(Double totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public Double getTotalIncomeTax() {
		return totalIncomeTax;
	}

	public void setTotalIncomeTax(Double totalIncomeTax) {
		this.totalIncomeTax = totalIncomeTax;
	}

	public Double getTaxRetention() {
		return taxRetention;
	}

	public void setTaxRetention(Double taxRetention) {
		this.taxRetention = taxRetention;
	}

	public Double getValorem() {
		return valorem;
	}

	public void setValorem(Double valorem) {
		this.valorem = valorem;
	}

	public String getTaxBear() {
		return taxBear;
	}

	public void setTaxBear(String taxBear) {
		this.taxBear = taxBear;
	}

	public Double getTodo() {
		return todo;
	}

	public void setTodo(Double todo) {
		this.todo = todo;
	}

	public Double getTodoValorem() {
		return todoValorem;
	}

	public void setTodoValorem(Double todoValorem) {
		this.todoValorem = todoValorem;
	}

	public Double getTodoAmount() {
		return todoAmount;
	}

	public void setTodoAmount(Double todoAmount) {
		this.todoAmount = todoAmount;
	}

	public Date getTodoEarlyDate() {
		return todoEarlyDate;
	}

	public void setTodoEarlyDate(Date todoEarlyDate) {
		this.todoEarlyDate = todoEarlyDate;
	}

	public Double getDoneAmount() {
		return doneAmount;
	}

	public void setDoneAmount(Double doneAmount) {
		this.doneAmount = doneAmount;
	}

	public Date getDoneEarlyDate() {
		return doneEarlyDate;
	}

	public void setDoneEarlyDate(Date doneEarlyDate) {
		this.doneEarlyDate = doneEarlyDate;
	}

	public Double getMaxTicket() {
		return maxTicket;
	}

	public void setMaxTicket(Double maxTicket) {
		this.maxTicket = maxTicket;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	

}
  