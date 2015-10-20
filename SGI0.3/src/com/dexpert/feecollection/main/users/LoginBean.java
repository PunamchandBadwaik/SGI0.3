package com.dexpert.feecollection.main.users;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.operator.OperatorBean;
import com.dexpert.feecollection.main.users.parent.ParBean;
import com.dexpert.feecollection.main.users.superadmin.SaBean;

@Entity
@Table(name = "login_master")
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer loginId;
	private String userName;
	private String password;
	private String profile;

	// one to one bidirectional relationship with super admin
	// child
	@OneToOne(cascade = CascadeType.ALL, targetEntity = OperatorBean.class)
	@JoinColumn(name = "operatorId_Id_Fk", referencedColumnName = "operatorId")
	private OperatorBean operatorBean;

	// one to one bidirectional relationship with super admin
	// child
	@OneToOne(cascade = CascadeType.ALL, targetEntity = SaBean.class)
	@JoinColumn(name = "super_Admin_Id_Fk", referencedColumnName = "saId")
	private SaBean saBean;

	// one to one bidirectional relationship with Applicant (Student)
	@OneToOne(cascade = CascadeType.ALL, targetEntity = AppBean.class)
	@JoinColumn(name = "Applicat_Id_Fk", referencedColumnName = "enrollmentNumber")
	private AppBean appBean;

	// one to one bidirectional relationship with Institute (College)

	@OneToOne(cascade = CascadeType.ALL, targetEntity = AffBean.class)
	@JoinColumn(name = "Aff_Inst_Id_Fk", referencedColumnName = "instId")
	private AffBean affBean;

	// one to one bidirectional relationship with University

	@OneToOne(cascade = CascadeType.ALL, targetEntity = ParBean.class)
	@JoinColumn(name = "Par_Inst_Id_Fk", referencedColumnName = "parInstId")
	private ParBean parBean;

	public SaBean getSaBean() {
		return saBean;
	}

	public void setSaBean(SaBean saBean) {
		this.saBean = saBean;
	}

	public AppBean getAppBean() {
		return appBean;
	}

	public void setAppBean(AppBean appBean) {
		this.appBean = appBean;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}

	public ParBean getParBean() {
		return parBean;
	}

	public void setParBean(ParBean parBean) {
		this.parBean = parBean;
	}

	public OperatorBean getOperatorBean() {
		return operatorBean;
	}

	public void setOperatorBean(OperatorBean operatorBean) {
		this.operatorBean = operatorBean;
	}

}
