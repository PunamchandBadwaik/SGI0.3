package com.dexpert.feecollection.main.fee.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "affiliated_institute_structure_mapping")
public class FeeStructureData {

	
	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer id;
	private Integer inst_id;
	private Integer structure_id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getInst_id() {
		return inst_id;
	}
	public void setInst_id(Integer inst_id) {
		this.inst_id = inst_id;
	}
	public Integer getStructure_id() {
		return structure_id;
	}
	public void setStructure_id(Integer structure_id) {
		this.structure_id = structure_id;
	}
	
	
}
