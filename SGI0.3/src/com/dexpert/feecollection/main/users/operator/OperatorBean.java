package com.dexpert.feecollection.main.users.operator;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffBean;

@Entity
@Table(name = "operator_table")
public class OperatorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne(cascade = CascadeType.ALL)
	LoginBean loginBean;

	@ManyToOne(targetEntity = AffBean.class)
	@JoinColumn(name = "College_Id_Fk", referencedColumnName = "instId")
	AffBean affBean;

	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer operatorId;
	private String operatorName, operatorLstName, operatorAddress, operatorContactSec, operatorEmail, operatorContact;

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorAddress() {
		return operatorAddress;
	}

	public void setOperatorAddress(String operatorAddress) {
		this.operatorAddress = operatorAddress;
	}

	

	public String getOperatorEmail() {
		return operatorEmail;
	}

	public void setOperatorEmail(String operatorEmail) {
		this.operatorEmail = operatorEmail;
	}

	public String getOperatorContact() {
		return operatorContact;
	}

	public void setOperatorContact(String operatorContact) {
		this.operatorContact = operatorContact;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getOperatorLstName() {
		return operatorLstName;
	}

	public void setOperatorLstName(String operatorLstName) {
		this.operatorLstName = operatorLstName;
	}

	public String getOperatorContactSec() {
		return operatorContactSec;
	}

	public void setOperatorContactSec(String operatorContactSec) {
		this.operatorContactSec = operatorContactSec;
	}

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}

}
