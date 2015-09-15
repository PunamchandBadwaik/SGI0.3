package com.dexpert.feecollection.main.users.applicant;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.payment.transaction.PayBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffBean;

@Entity
@Table(name = "applicant_details")
public class AppBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String enrollmentNumber;
	private String aplFirstName, aplLstName, aplEmail, aplAddress, aplMobilePri, aplMobileSec, gender, category,
			course, year, yearCode;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@ManyToOne(targetEntity = AffBean.class)
	@JoinColumn(name = "College_Id_Fk", referencedColumnName = "instId")
	AffBean affBeanStu;

	// one to one bidirectional relationship with login
	@OneToOne(cascade = CascadeType.ALL)
	LoginBean loginBean;

	// one to many relationship of applicant with Payment
	@OneToMany(cascade = CascadeType.ALL, targetEntity = PayBean.class)
	@JoinColumn(name = "aplicantId_Fk", referencedColumnName = "enrollmentNumber")
	private Set<PayBean> payBeansSet;

	@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name = "applicant_values", joinColumns = @JoinColumn(name = "enrollmentNumber"), inverseJoinColumns = @JoinColumn(name = "value_id"))
	Set<FvBean> applicantParamValues;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appBean")
	@OrderBy(value = "dueId")
	private Set<PaymentDuesBean> paymentDues;

	public Set<PaymentDuesBean> getPaymentDues() {
		return paymentDues;
	}

	public void setPaymentDues(Set<PaymentDuesBean> paymentDues) {
		this.paymentDues = paymentDues;
	}

	public AffBean getAffBeanStu() {
		return affBeanStu;
	}

	public void setAffBeanStu(AffBean affBeanStu) {
		this.affBeanStu = affBeanStu;
	}

	public Set<PayBean> getPayBeansSet() {
		return payBeansSet;
	}

	public void setPayBeansSet(Set<PayBean> payBeansSet) {
		this.payBeansSet = payBeansSet;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getAplLstName() {
		return aplLstName;
	}

	public void setAplLstName(String aplLstName) {
		this.aplLstName = aplLstName;
	}

	public String getAplAddress() {
		return aplAddress;
	}

	public void setAplAddress(String aplAddress) {
		this.aplAddress = aplAddress;
	}

	public String getAplFirstName() {
		return aplFirstName;
	}

	public void setAplFirstName(String aplFirstName) {
		this.aplFirstName = aplFirstName;
	}

	public String getAplEmail() {
		return aplEmail;
	}

	public void setAplEmail(String aplEmail) {
		this.aplEmail = aplEmail;
	}

	public String getAplMobilePri() {
		return aplMobilePri;
	}

	public void setAplMobilePri(String aplMobilePri) {
		this.aplMobilePri = aplMobilePri;
	}

	public String getAplMobileSec() {
		return aplMobileSec;
	}

	public void setAplMobileSec(String aplMobileSec) {
		this.aplMobileSec = aplMobileSec;
	}

	public String getEnrollmentNumber() {
		return enrollmentNumber;
	}

	public void setEnrollmentNumber(String enrollmentNumber) {
		this.enrollmentNumber = enrollmentNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYearCode() {
		return yearCode;
	}

	public void setYearCode(String yearCode) {
		this.yearCode = yearCode;
	}

	public Set<FvBean> getApplicantParamValues() {
		return applicantParamValues;
	}

	public void setApplicantParamValues(Set<FvBean> applicantParamValues) {
		this.applicantParamValues = applicantParamValues;
	}

}
