package com.dexpert.feecollection.main.payment.studentPayment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "transaction_bulk_detail")
public class BulkPaymentBean {
	
	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer paymentId;
	private String transId;
	String paymentMode, payeeName, payeeMob, payeeEmail, payeeAdd;
	private Double payeeAmount;
	private Date transDate;
	private String studentEnrollmentNumber;
	private Integer insId;
	private String dueString;
	
	
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeMob() {
		return payeeMob;
	}
	public void setPayeeMob(String payeeMob) {
		this.payeeMob = payeeMob;
	}
	public String getPayeeEmail() {
		return payeeEmail;
	}
	public void setPayeeEmail(String payeeEmail) {
		this.payeeEmail = payeeEmail;
	}
	public String getPayeeAdd() {
		return payeeAdd;
	}
	public void setPayeeAdd(String payeeAdd) {
		this.payeeAdd = payeeAdd;
	}
	public Double getPayeeAmount() {
		return payeeAmount;
	}
	public void setPayeeAmount(Double payeeAmount) {
		this.payeeAmount = payeeAmount;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getStudentEnrollmentNumber() {
		return studentEnrollmentNumber;
	}
	public void setStudentEnrollmentNumber(String studentEnrollmentNumber) {
		this.studentEnrollmentNumber = studentEnrollmentNumber;
	}
	public Integer getInsId() {
		return insId;
	}
	public void setInsId(Integer insId) {
		this.insId = insId;
	}
	public String getDueString() {
		return dueString;
	}
	public void setDueString(String dueString) {
		this.dueString = dueString;
	}

	

}
