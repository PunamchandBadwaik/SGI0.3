package com.dexpert.feecollection.main.fee.config;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.users.superadmin.SaBean;

@Entity
@Table(name = "fee_config_master")
public class FcBean implements Serializable {

	@GenericGenerator(name = "g11", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g11")
	private Integer feeConfigId;
	
	private Integer comboId,valueId;
	private Double amount;
	

	
//	//one to many Unidirectional relationship
//	
//	@OneToMany(cascade=CascadeType.ALL,targetEntity=LookupBean.class)
//	@JoinColumn(name="feeConfig_id_fk",referencedColumnName="feeConfigId")
//	private List<LookupBean> lookupBeanList;

	//you can use Set also in place of List
	
	//One to one unidirectional with feedetails
	@OneToOne(cascade = CascadeType.ALL, targetEntity = FeeDetailsBean.class)
	@JoinColumn(name = "fee_id_fk", referencedColumnName = "feeId")
	private FeeDetailsBean feedetailbean;
	
	

	public Integer getFeeConfigId() {
		return feeConfigId;
	}

	public void setFeeConfigId(Integer feeConfigId) {
		this.feeConfigId = feeConfigId;
	}


	public Integer getComboId() {
		return comboId;
	}

	public void setComboId(Integer comboId) {
		this.comboId = comboId;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	public FeeDetailsBean getFeedetailbean() {
		return feedetailbean;
	}

	public void setFeedetailbean(FeeDetailsBean feedetailbean) {
		this.feedetailbean = feedetailbean;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	

}
