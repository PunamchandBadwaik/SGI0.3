package com.dexpert.feecollection.main.users.affiliated;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dexpert.feecollection.main.fee.lookup.values.FvBean;

@Entity
@Table(name = "institute_feeconfig_params")
public class AffParams {

	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer id;
	
	private Integer value_id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getValue_id() {
		return value_id;
	}

	public void setValue_id(Integer value_id) {
		this.value_id = value_id;
	}

	
	
	
	
	
}

