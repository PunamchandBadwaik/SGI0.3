package com.dexpert.feecollection.main.users.affiliated;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.operator.OperatorBean;
import com.dexpert.feecollection.main.users.parent.ParBean;

@Entity
@Table(name = "affiliated_institute_details")
public class AffBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer instId;

	@Column(unique = true)
	private String instName;

	private String contactPerson, place, email, contactNumber, mobileNum;
	private String instAddress;

	// --------------------------------------
	// to upload file
	private File fileUpload;

	private String fileUploadFileName;
	private Integer fileSize;

	@Lob
	@Column(name = "filesByteSize", columnDefinition = "mediumblob")
	byte[] filesByteSize;

	// ------------------------------------

	// one to one bidirectional relationship with login
	// its parent
	@OneToOne(cascade = CascadeType.ALL)
	LoginBean loginBean;

	@ManyToOne(targetEntity = ParBean.class)
	@JoinColumn(name = "University_Id_Fk", referencedColumnName = "parInstId")
	ParBean parBeanAff;

	// one to many relationship with Applicants (Students)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "affBeanStu")
	@Fetch(FetchMode.JOIN)
	@OrderBy(value = "enrollmentNumber")
	Set<AppBean> aplBeanSet;

	// one to many relationship with College Operator (operator)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "affBean")
	@Fetch(FetchMode.JOIN)
	Set<OperatorBean> OptrBeanSet;

	// many to many relationship with FeeDetails)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "affiliatedinstitute_feedetails", joinColumns = @JoinColumn(name = "inst_id"), inverseJoinColumns = @JoinColumn(name = "feeId"))
	@Column(name = "sequenceId")
	@OrderBy(value = "feeId")
	Set<FeeDetailsBean> feeSet;

	/*
	 * // many to many relationship with FeeConfigs)
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, mappedBy="structure_id")
	 * Set<FcBean> configSet;
	 */

	// one to one bidirectional relationship with student and college
	// child
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "affBeanStu")
	private AppBean appBean;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "affiliated_values", joinColumns = @JoinColumn(name = "inst_id"), inverseJoinColumns = @JoinColumn(name = "value_id"))
	Set<FvBean> collegeParamvalues;

	@OneToMany(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	private Set<AffFeePropBean> feeProps;

	@OneToMany(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	private Set<PaymentDuesBean> dueFeesSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "affBean")
	@Fetch(FetchMode.JOIN)
	private Set<CollegeCourses> collegeCourses;

	public Set<CollegeCourses> getCollegeCourses() {
		return collegeCourses;
	}

	public void setCollegeCourses(Set<CollegeCourses> collegeCourses) {
		this.collegeCourses = collegeCourses;
	}

	public Set<AppBean> getAplBeanSet() {
		return aplBeanSet;
	}

	public void setAplBeanSet(Set<AppBean> aplBeanSet) {
		this.aplBeanSet = aplBeanSet;
	}

	/*
	 * public Set<LoginBean> getLoginBeanSet() { return loginBeanSet; }
	 * 
	 * public void setLoginBeanSet(Set<LoginBean> loginBeanSet) {
	 * this.loginBeanSet = loginBeanSet; }
	 */

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getInstAddress() {
		return instAddress;
	}

	public void setInstAddress(String instAddress) {
		this.instAddress = instAddress;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getFilesByteSize() {
		return filesByteSize;
	}

	public void setFilesByteSize(byte[] filesByteSize) {
		this.filesByteSize = filesByteSize;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public AppBean getAppBean() {
		return appBean;
	}

	public void setAppBean(AppBean appBean) {
		this.appBean = appBean;
	}

	public Set<FeeDetailsBean> getFeeSet() {
		return feeSet;
	}

	public void setFeeSet(Set<FeeDetailsBean> feeSet) {
		this.feeSet = feeSet;
	}

	public Set<FvBean> getCollegeParamvalues() {
		return collegeParamvalues;
	}

	public void setCollegeParamvalues(Set<FvBean> collegeParamvalues) {
		this.collegeParamvalues = collegeParamvalues;
	}

	public Set<AffFeePropBean> getFeeProps() {
		return feeProps;
	}

	public void setFeeProps(Set<AffFeePropBean> feeProps) {
		this.feeProps = feeProps;
	}

	public Set<PaymentDuesBean> getDueFeesSet() {
		return dueFeesSet;
	}

	public void setDueFeesSet(Set<PaymentDuesBean> dueFeesSet) {
		this.dueFeesSet = dueFeesSet;
	}

	public Set<OperatorBean> getOptrBeanSet() {
		return OptrBeanSet;
	}

	public void setOptrBeanSet(Set<OperatorBean> optrBeanSet) {
		OptrBeanSet = optrBeanSet;
	}

	public ParBean getParBeanAff() {
		return parBeanAff;
	}

	public void setParBeanAff(ParBean parBeanAff) {
		this.parBeanAff = parBeanAff;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
