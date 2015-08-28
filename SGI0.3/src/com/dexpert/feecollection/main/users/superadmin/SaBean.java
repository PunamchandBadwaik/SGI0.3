package com.dexpert.feecollection.main.users.superadmin;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.users.LoginBean;

@Entity
@Table(name = "super_admin_master")
public class SaBean implements Serializable{

	@GenericGenerator(name = "g2", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g2")
	private Integer saId;
	private String firstName, midName, lstName, address, gender, mobileNum, emailId;
	private Integer age;

	// one to one bidirectional relationship with login
	
	//parent
	@OneToOne(cascade = CascadeType.ALL)
	LoginBean loginBean;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getSaId() {
		return saId;
	}

	public void setSaId(Integer saId) {
		this.saId = saId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLstName() {
		return lstName;
	}

	public void setLstName(String lstName) {
		this.lstName = lstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
