package com.dexpert.feecollection.main.fee.lookup;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.lookup.values.FvBean;

@Entity
@Table(name = "fee_lookup_master")
public class LookupBean implements Serializable {

	@GenericGenerator(name = "g10", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g10")
	private Integer lookupId;
	private String lookupScope, lookupName, lookupType, lookupDesc;

	// one to many unidirectional relationship with fee look up values
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FvBean.class)
	@JoinColumn(name = "FeeLookupId_Fk", referencedColumnName = "lookupId")
	private List<FvBean> fvBeansList;

	public List<FvBean> getFvBeansList() {
		return fvBeansList;
	}

	public void setFvBeansList(List<FvBean> fvBeansList) {
		this.fvBeansList = fvBeansList;
	}

	public Integer getLookupId() {
		return lookupId;
	}

	public void setLookupId(Integer lookupId) {
		this.lookupId = lookupId;
	}

	public String getLookupScope() {
		return lookupScope;
	}

	public void setLookupScope(String lookupScope) {
		this.lookupScope = lookupScope;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getLookupType() {
		return lookupType;
	}

	public void setLookupType(String lookupType) {
		this.lookupType = lookupType;
	}

	public String getLookupDesc() {
		return lookupDesc;
	}

	public void setLookupDesc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
	}

}
