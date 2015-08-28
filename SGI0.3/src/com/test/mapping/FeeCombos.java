package com.test.mapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="fee_combos")
public class FeeCombos {

	
	@GenericGenerator(name = "g4", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g4")
	private Integer config_id;
	private Integer commboId,value_id_fk;
	private Double amount;
	
	@ManyToOne
    @JoinColumn(name="feeId")
	private FeeDetails feedata;
	
	
	public Integer getConfig_id() {
		return config_id;
	}
	public void setConfig_id(Integer config_id) {
		this.config_id = config_id;
	}
	
	public Integer getCommboId() {
		return commboId;
	}
	public void setCommboId(Integer commboId) {
		this.commboId = commboId;
	}
	public Integer getValue_id_fk() {
		return value_id_fk;
	}
	public void setValue_id_fk(Integer value_id_fk) {
		this.value_id_fk = value_id_fk;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public FeeDetails getFeedata() {
		return feedata;
	}
	public void setFeedata(FeeDetails feedata) {
		this.feedata = feedata;
	}
	
	
	
}
