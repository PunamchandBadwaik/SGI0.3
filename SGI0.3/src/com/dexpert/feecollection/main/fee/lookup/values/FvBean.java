package com.dexpert.feecollection.main.fee.lookup.values;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.parent.ParBean;

@Entity
@Table(name = "fee_values_master")
public class FvBean implements Serializable {

	@GenericGenerator(name = "g10", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g10")
	private Integer feeValueId;
	private String value;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "applicantParamValues")
	Set<AppBean> appBeanParamSet;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parentFeeValueSet")
	Set<ParBean> parentParam;

	// one to one unidirectional mapping with Fee Config

	@OneToOne
	@JoinColumn(name = "fee_config_fk")
	private FcBean fcBean;

	@ManyToOne(targetEntity = LookupBean.class)
	@JoinColumn(name = "FeeLookupId_Fk", referencedColumnName = "lookupId")
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

	public Set<ParBean> getParentParam() {
		return parentParam;
	}

	public void setParentParam(Set<ParBean> parentParam) {
		this.parentParam = parentParam;
	}
	@Override
	public String toString(){
	String value="Value Id="+feeValueId+"Value Name="+this.value;
	return value;
	}

}
