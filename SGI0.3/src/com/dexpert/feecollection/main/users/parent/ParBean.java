package com.dexpert.feecollection.main.users.parent;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.applicant.AppBean;

@Entity
@Table(name = "parent_inst_detail")
public class ParBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@GenericGenerator(name = "g15", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g15")
	private Integer parInstId;
	private String parInstName, parInstAddress, parInstContPerson, parInstEmail, parInstContact;
	// --------------------------------------
	// to upload file
	private File fileUpload;

	private String fileUploadFileName;
	private Integer fileSize;

	@Lob
	@Column(name = "filesByteSize", columnDefinition = "mediumblob")
	byte[] filesByteSize;

	// ------------------------------------

	// one to many relation ship with Institutes
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parBeanAff")
	Set<AffBean> affBeanOneToManySet;

	// @OneToOne(cascade = CascadeType.ALL, mappedBy = "parBeanOneToOne")
	// private AffBean affBeanOneToOneBi;

	

	@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "University_values", joinColumns = @JoinColumn(name = "parInstId"), inverseJoinColumns = @JoinColumn(name = "value_id"))
	Set<FvBean> parentFeeValueSet;

	@OneToOne(cascade = CascadeType.ALL)
	LoginBean loginBean;

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public Integer getParInstId() {
		return parInstId;
	}

	public void setParInstId(Integer parInstId) {
		this.parInstId = parInstId;
	}

	public String getParInstName() {
		return parInstName;
	}

	public void setParInstName(String parInstName) {
		this.parInstName = parInstName;
	}

	public String getParInstAddress() {
		return parInstAddress;
	}

	public void setParInstAddress(String parInstAddress) {
		this.parInstAddress = parInstAddress;
	}

	public String getParInstContPerson() {
		return parInstContPerson;
	}

	public void setParInstContPerson(String parInstContPerson) {
		this.parInstContPerson = parInstContPerson;
	}

	public String getParInstEmail() {
		return parInstEmail;
	}

	public void setParInstEmail(String parInstEmail) {
		this.parInstEmail = parInstEmail;
	}

	public String getParInstContact() {
		return parInstContact;
	}

	public void setParInstContact(String parInstContact) {
		this.parInstContact = parInstContact;
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

	public Set<AffBean> getAffBeanOneToManySet() {
		return affBeanOneToManySet;
	}

	public void setAffBeanOneToManySet(Set<AffBean> affBeanOneToManySet) {
		this.affBeanOneToManySet = affBeanOneToManySet;
	}

	public Set<FvBean> getParentFeeValueSet() {
		return parentFeeValueSet;
	}

	public void setParentFeeValueSet(Set<FvBean> parentFeeValueSet) {
		this.parentFeeValueSet = parentFeeValueSet;
	}

}
