package com.dexpert.feecollection.main.fee;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.users.applicant.AppBean;
@Entity
@Table(name = "fee_dues_master")
public class PaymentDuesBean implements Serializable {

	@Override
	public String toString() {
	 System.out.println("due Id is"+dueId);	
	 System.out.println("net due"+netDue);	
	 System.out.println("total amount");	
	 System.out.println("payment to date"+payments_to_date);	
	 return "786";
	}
	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer dueId;
	private String payee,feeName;
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	private Integer feeId;
	private Date dueDate,dateCalculated;
	private Double total_fee_amount,payments_to_date,netDue;
	@ManyToOne(targetEntity=AppBean.class)
	@JoinColumn(name = "enrollmentNumber_Fk", referencedColumnName = "enrollmentNumber")
	private AppBean appBean;
	@Transient
	private Integer sequenceId;
	public Integer getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	public AppBean getAppBean() {
		return appBean;
	}
	public void setAppBean(AppBean appBean) {
		this.appBean = appBean;
	}
	public Integer getDueId() {
		return dueId;
	}
	public void setDueId(Integer dueId) {
		this.dueId = dueId;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public Integer getFeeId() {
		return feeId;
	}
	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getDateCalculated() {
		return dateCalculated;
	}
	public void setDateCalculated(Date dateCalculated) {
		this.dateCalculated = dateCalculated;
	}
	public Double getTotal_fee_amount() {
		return total_fee_amount;
	}
	public void setTotal_fee_amount(Double total_fee_amount) {
		this.total_fee_amount = total_fee_amount;
	}
	public Double getPayments_to_date() {
		return payments_to_date;
	}
	public void setPayments_to_date(Double payments_to_date) {
		this.payments_to_date = payments_to_date;
	}
	public Double getNetDue() {
		return netDue;
	}
	public void setNetDue(Double netDue) {
		this.netDue = netDue;
	}
	
	
	
}
