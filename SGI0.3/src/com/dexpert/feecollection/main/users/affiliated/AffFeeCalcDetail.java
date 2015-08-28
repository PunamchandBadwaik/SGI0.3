package com.dexpert.feecollection.main.users.affiliated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "institute_fee_calculation_parameters")
public class AffFeeCalcDetail {

	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer calcId;
	private Integer comboId;
	private Double multiplier;
	
	
	public Integer getCalcId() {
		return calcId;
	}
	public void setCalcId(Integer calcId) {
		this.calcId = calcId;
	}
	public Integer getComboId() {
		return comboId;
	}
	public void setComboId(Integer comboId) {
		this.comboId = comboId;
	}
	public Double getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}
	
	
}
