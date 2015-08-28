package com.dexpert.feecollection.main.users.affiliated;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.PaymentDuesBean;

@Entity
@Table(name = "institute_fee_properties")
public class AffFeePropBean {

	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer propId;
	private Integer feeId, collPeriodValue,calcFlag;
	private Date startDate,endDate,lateDate;
	private String collPeriodUnit;
	private Double lateAmount;
	private String feeName;
	@OneToOne(cascade = CascadeType.ALL)
	private PaymentDuesBean dueBean;
	
	@OneToMany(cascade= CascadeType.ALL)
	private Set<AffFeeCalcDetail>multipliers;
	
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public Integer getFeeId() {
		return feeId;
	}
	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}
	public Integer getCollPeriodValue() {
		return collPeriodValue;
	}
	public void setCollPeriodValue(Integer collPeriodValue) {
		this.collPeriodValue = collPeriodValue;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getCollPeriodUnit() {
		return collPeriodUnit;
	}
	public void setCollPeriodUnit(String collPeriodUnit) {
		this.collPeriodUnit = collPeriodUnit;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getLateDate() {
		return lateDate;
	}
	public void setLateDate(Date lateDate) {
		this.lateDate = lateDate;
	}
	public Double getLateAmount() {
		return lateAmount;
	}
	public void setLateAmount(Double lateAmount) {
		this.lateAmount = lateAmount;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public PaymentDuesBean getDueBean() {
		return dueBean;
	}
	public void setDueBean(PaymentDuesBean dueBean) {
		this.dueBean = dueBean;
	}
	public Set<AffFeeCalcDetail> getMultipliers() {
		return multipliers;
	}
	public void setMultipliers(Set<AffFeeCalcDetail> multipliers) {
		this.multipliers = multipliers;
	}
	public Integer getCalcFlag() {
		return calcFlag;
	}
	public void setCalcFlag(Integer calcFlag) {
		this.calcFlag = calcFlag;
	}
	
	
	
	

}
