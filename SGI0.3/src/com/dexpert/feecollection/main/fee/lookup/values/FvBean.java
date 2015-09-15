package com.dexpert.feecollection.main.fee.lookup.values;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.users.applicant.AppBean;

@Entity
@Table(name = "fee_values_master")
public class FvBean implements Serializable {

	@GenericGenerator(name = "g10", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g10")
	private Integer feeValueId;
	private String value;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "applicantParamValues")
	Set<AppBean> appBeanParamSet;

	// one to one unidirectional mapping with Fee Config

	@OneToOne
	@JoinColumn(name = "fee_config_fk")
	private FcBean fcBean;

	@OneToOne
	private LookupBean lookupname;

	public FcBean getFcBean() {
		return fcBean;
	}

	public void setFcBean(FcBean fcBean) {
		this.fcBean = fcBean;
	}

	public Integer getFeeValueId() {
		return feeValueId;
	}

	public void setFeeValueId(Integer feeValueId) {
		this.feeValueId = feeValueId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LookupBean getLookupname() {
		return lookupname;
	}

	public void setLookupname(LookupBean lookupname) {
		this.lookupname = lookupname;
	}

	public Set<AppBean> getAppBeanParamSet() {
		return appBeanParamSet;
	}

	public void setAppBeanParamSet(Set<AppBean> appBeanParamSet) {
		this.appBeanParamSet = appBeanParamSet;
	}

}
