package com.dexpert.feecollection.challan;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "transaction_detail")
public class TransactionBean {

	@Id
	private String txnId;

	String paymentMode, payeeName, payeeMob, payeeEmail, payeeAdd;
	private Double payeeAmount;
	private Date transDate;
	private String studentEnrollmentNumber;
	private Integer insId;
	private String status;
	private String dueString;
	private Integer bulkPay;
	private String allowPayCode;

	public String getAllowPayCode() {
		return allowPayCode;
	}

	public void setAllowPayCode(String allowPayCode) {
		this.allowPayCode = allowPayCode;
	}

	public String getDueString() {
		return dueString;
	}

	public void setDueString(String dueString) {
		this.dueString = dueString;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Double getPayeeAmount() {
		return payeeAmount;
	}

	public void setPayeeAmount(Double payeeAmount) {
		this.payeeAmount = payeeAmount;
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

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public Integer getBulkPay() {
		return bulkPay;
	}

	public void setBulkPay(Integer bulkPay) {
		this.bulkPay = bulkPay;
	}

}
