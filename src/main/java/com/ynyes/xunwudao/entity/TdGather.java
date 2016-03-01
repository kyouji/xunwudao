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
public class TdGather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // 排序
    @Column
    private Long  sortId;
    
    //用户id
    @Column
    private Long userId;
    
    // 标题
    @Column
    private String title;
  
    //状态id
    @Column
    private Long statusId;
    
    //填写时间（月份）
    @Column
    @DateTimeFormat(pattern="yyyy-MM")
    private Date time;
    
    /*---------------------------------------------------------
    ===============  收入 =================
    ---------------------------------------------------------*/
    // 【普票】数量
    @Column
    private Double generalAmount;
    
    // 【普票】不含税收入
    @Column
    private Double generalIncome;
    
    // 【普票】销项税
    @Column
    private Double generalTax;
    
    // 【专票】数量
    @Column
    private Double specialAmount;
    
    // 【专票】不含税收入
    @Column
    private Double specialIncome;
    
    // 【专票】销项税
    @Column
    private Double specialTax;
    
    // 【不开票】收入
    @Column
    private Double noTicketIncome;
    
    // 【不开票】销项税
    @Column
    private Double noTicketTax;
    
    // 不含税收入合计
    @Column
    private Double totalIncome;
    
    // 销项税合计
    @Column
    private Double totalTax;
    
    /*---------------------------------------------------------
    ===============  进货 =================
    ---------------------------------------------------------*/
    // 上月留抵税金
    @Column
    private Double taxRetention;
    
    // 【上月进项税额】数量
    @Column
    private Double vatAmount;
    
    // 【上月进项税额】税额
    @Column
    private Double vat;
    
    // 【运费抵扣】数量
    @Column
    private Double transDeductionAmount;
    
    // 【运费抵扣】税额
    @Column
    private Double transDeduction;
    
    // 【增值税抵扣】数量
    @Column
    private Double taxDeductionAmount;
    
    // 【增值税抵扣】税额
    @Column
    private Double taxDeduction;
    
    // 进项税额合计
    @Column
    private Double totalVat;
    
    //小规模纳税人
    // 本月进货发票数量
    @Column
    private Double inBillAmount;
    
    // 本月进货金额
    @Column
    private Double inBill;
    
    /*---------------------------------------------------------
    ===============  应纳税金 ==============
    ---------------------------------------------------------*/

    // 【增值税】金额
    @Column
    private Double taxAdd;
    
    // 【增值税】税负
    @Column
    private Double taxBearing;
    
    // 【所得税】金额
    @Column
    private Double incomeTax;
    
    // 【城建税】金额
    @Column
    private Double urbanTax;
    
    // 【教育费附加】金额
    @Column
    private Double eduAdd;
    
    // 【地方教育费附加】金额
    @Column
    private Double eduAddLocal;
    
    // 【地税合计】金额
    @Column
    private Double landTax;
    
    /*---------------------------------------------------------
    ===============  待抵扣================
    ---------------------------------------------------------*/
    

    
    // 【抵扣联】未收，金额
    @Column
    private Double deTodo;
    
    // 【抵扣联】未收，数量
    @Column
    private Double deTodoAmount;
    
    // 【抵扣联】未收，最早日期
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date deTodoDate;
    
    // 【抵扣联】已收，金额
    @Column
    private Double deDone;
    
    // 【抵扣联】已收，数量
    @Column
    private Double deDoneAmount;
    
    // 【抵扣联】已收，最早日期
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date deDoneDate;
    
    // 【本月应纳所得税】
    @Column
    private Double incomeTaxTodo;
    
    //备注
    @Column
    private String remark;
    
    //补充说明
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getGeneralAmount() {
		return generalAmount;
	}

	public void setGeneralAmount(Double generalAmount) {
		this.generalAmount = generalAmount;
	}

	public Double getGeneralIncome() {
		return generalIncome;
	}

	public void setGeneralIncome(Double generalIncome) {
		this.generalIncome = generalIncome;
	}

	public Double getGeneralTax() {
		return generalTax;
	}

	public void setGeneralTax(Double generalTax) {
		this.generalTax = generalTax;
	}

	public Double getSpecialAmount() {
		return specialAmount;
	}

	public void setSpecialAmount(Double specialAmount) {
		this.specialAmount = specialAmount;
	}

	public Double getSpecialIncome() {
		return specialIncome;
	}

	public void setSpecialIncome(Double specialIncome) {
		this.specialIncome = specialIncome;
	}

	public Double getSpecialTax() {
		return specialTax;
	}

	public void setSpecialTax(Double specialTax) {
		this.specialTax = specialTax;
	}

	public Double getNoTicketIncome() {
		return noTicketIncome;
	}

	public void setNoTicketIncome(Double noTicketIncome) {
		this.noTicketIncome = noTicketIncome;
	}

	public Double getNoTicketTax() {
		return noTicketTax;
	}

	public void setNoTicketTax(Double noTicketTax) {
		this.noTicketTax = noTicketTax;
	}


	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Double getTaxRetention() {
		return taxRetention;
	}

	public void setTaxRetention(Double taxRetention) {
		this.taxRetention = taxRetention;
	}

	public Double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Double getTransDeductionAmount() {
		return transDeductionAmount;
	}

	public void setTransDeductionAmount(Double transDeductionAmount) {
		this.transDeductionAmount = transDeductionAmount;
	}

	public Double getTransDeduction() {
		return transDeduction;
	}

	public void setTransDeduction(Double transDeduction) {
		this.transDeduction = transDeduction;
	}

	public Double getTaxDeductionAmount() {
		return taxDeductionAmount;
	}

	public void setTaxDeductionAmount(Double taxDeductionAmount) {
		this.taxDeductionAmount = taxDeductionAmount;
	}

	public Double getTaxDeduction() {
		return taxDeduction;
	}

	public void setTaxDeduction(Double taxDeduction) {
		this.taxDeduction = taxDeduction;
	}

	public Double getTotalVat() {
		return totalVat;
	}

	public void setTotalVat(Double totalVat) {
		this.totalVat = totalVat;
	}

	public Double getTaxAdd() {
		return taxAdd;
	}

	public void setTaxAdd(Double taxAdd) {
		this.taxAdd = taxAdd;
	}

	public Double getTaxBearing() {
		return taxBearing;
	}

	public void setTaxBearing(Double taxBearing) {
		this.taxBearing = taxBearing;
	}

	public Double getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(Double incomeTax) {
		this.incomeTax = incomeTax;
	}

	public Double getUrbanTax() {
		return urbanTax;
	}

	public void setUrbanTax(Double urbanTax) {
		this.urbanTax = urbanTax;
	}

	public Double getEduAdd() {
		return eduAdd;
	}

	public void setEduAdd(Double eduAdd) {
		this.eduAdd = eduAdd;
	}

	public Double getEduAddLocal() {
		return eduAddLocal;
	}

	public void setEduAddLocal(Double eduAddLocal) {
		this.eduAddLocal = eduAddLocal;
	}

	public Double getLandTax() {
		return landTax;
	}

	public void setLandTax(Double landTax) {
		this.landTax = landTax;
	}

	public Double getDeTodo() {
		return deTodo;
	}

	public void setDeTodo(Double deTodo) {
		this.deTodo = deTodo;
	}

	public Double getDeTodoAmount() {
		return deTodoAmount;
	}

	public void setDeTodoAmount(Double deTodoAmount) {
		this.deTodoAmount = deTodoAmount;
	}

	public Date getDeTodoDate() {
		return deTodoDate;
	}

	public void setDeTodoDate(Date deTodoDate) {
		this.deTodoDate = deTodoDate;
	}

	public Double getDeDone() {
		return deDone;
	}

	public void setDeDone(Double deDone) {
		this.deDone = deDone;
	}

	public Double getDeDoneAmount() {
		return deDoneAmount;
	}

	public void setDeDoneAmount(Double deDoneAmount) {
		this.deDoneAmount = deDoneAmount;
	}

	public Date getDeDoneDate() {
		return deDoneDate;
	}

	public void setDeDoneDate(Date deDoneDate) {
		this.deDoneDate = deDoneDate;
	}

	public Double getIncomeTaxTodo() {
		return incomeTaxTodo;
	}

	public void setIncomeTaxTodo(Double incomeTaxTodo) {
		this.incomeTaxTodo = incomeTaxTodo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getInBillAmount() {
		return inBillAmount;
	}

	public void setInBillAmount(Double inBillAmount) {
		this.inBillAmount = inBillAmount;
	}

	public Double getInBill() {
		return inBill;
	}

	public void setInBill(Double inBill) {
		this.inBill = inBill;
	}

	

}
  